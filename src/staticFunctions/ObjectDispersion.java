/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package staticFunctions;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ObjectDispersion {

    private BufferedImage image;
    private int minDistance;
    private int maxDistance;
    private int minHeight;
    private int maxHeight;
    private int areaWidth;
    private int areaHeight;
    private Random random;
    private static final int MAX_ATTEMPTS = 10;
    private double aspectRatio;

    public ObjectDispersion(BufferedImage image, int minDistance, int maxDistance, int minHeight, int maxHeight, int areaWidth, int areaHeight) {
        this.image = image;
        this.minDistance = minDistance;
        this.maxDistance = maxDistance;
        this.minHeight = minHeight;
        this.maxHeight = maxHeight;
        this.areaWidth = areaWidth;
        this.areaHeight = areaHeight;
        this.random = new Random();
        this.aspectRatio = (double) image.getWidth() / image.getHeight();
    }

    public List<ObjectPosition> disperseObjects() {
        List<ObjectPosition> positions = new ArrayList<>();
        int attempts = 0;

        while (true) {
            boolean validPosition = false;
            int attemptCount = 0;

            while (!validPosition && attemptCount < MAX_ATTEMPTS) {
                int x = random.nextInt(areaWidth);
                int y = random.nextInt(areaHeight);
                int height = minHeight + random.nextInt(maxHeight - minHeight + 1);
                int width = (int) (height * aspectRatio);
                double angle = random.nextDouble() * 360; // Angle aléatoire entre 0 et 360 degrés

                ObjectPosition newPosition = new ObjectPosition(x, y, width, height, angle);

                if (isValidPosition(newPosition, positions)) {
                    positions.add(newPosition);
                    validPosition = true;
                }

                attemptCount++;
            }

            if (!validPosition) {
                attempts++;
                if (attempts >= MAX_ATTEMPTS) {
                    break;
                }
            } else {
                attempts = 0; // Réinitialiser les tentatives si une position valide est trouvée
            }
        }

        return positions;
    }

    private boolean isValidPosition(ObjectPosition newPosition, List<ObjectPosition> existingPositions) {
        Rectangle2D newRect = getBoundingBox(newPosition);

        for (ObjectPosition existingPosition : existingPositions) {
            Rectangle2D existingRect = getBoundingBox(existingPosition);
            if (newRect.intersects(existingRect)) {
                return false;
            }
        }

        return true;
    }

    private Rectangle2D getBoundingBox(ObjectPosition position) {
        double angleRad = Math.toRadians(position.angle);
        double cos = Math.cos(angleRad);
        double sin = Math.sin(angleRad);

        double halfWidth = position.width / 2.0;
        double halfHeight = position.height / 2.0;

        double[] xPoints = {
            position.x + halfWidth * cos - halfHeight * sin,
            position.x - halfWidth * cos - halfHeight * sin,
            position.x - halfWidth * cos + halfHeight * sin,
            position.x + halfWidth * cos + halfHeight * sin
        };

        double[] yPoints = {
            position.y + halfWidth * sin + halfHeight * cos,
            position.y - halfWidth * sin + halfHeight * cos,
            position.y - halfWidth * sin - halfHeight * cos,
            position.y + halfWidth * sin - halfHeight * cos
        };

        double minX = Double.MAX_VALUE;
        double minY = Double.MAX_VALUE;
        double maxX = Double.MIN_VALUE;
        double maxY = Double.MIN_VALUE;

        for (double x : xPoints) {
            if (x < minX) minX = x;
            if (x > maxX) maxX = x;
        }

        for (double y : yPoints) {
            if (y < minY) minY = y;
            if (y > maxY) maxY = y;
        }

        return new Rectangle2D.Double(minX, minY, maxX - minX, maxY - minY);
    }

    public static class ObjectPosition {
        public int x;
        public int y;
        public int width;
        public int height;
        public double angle;

        public ObjectPosition(int x, int y, int width, int height, double angle) {
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
            this.angle = angle;
        }
    }
}





