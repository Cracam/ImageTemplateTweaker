/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package staticFunctions;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;

/**
 *
 * @author Camille LECOURT
 */
public class StaticImageEditing {

        public static Image convertToFXImage(BufferedImage bufferedImage) {
                if (bufferedImage == null) {
                        throw new IllegalArgumentException("BufferedImage cannot be null");
                }
                WritableImage writableImage = new WritableImage(bufferedImage.getWidth(), bufferedImage.getHeight());
                return SwingFXUtils.toFXImage(bufferedImage, writableImage);
        }

        /**
         * Creates an ImageView from a BufferedImage.
         *
         * @param bufferedImage the BufferedImage to convert
         * @return the ImageView containing the converted Image
         */
        public static ImageView createImageView(BufferedImage bufferedImage) {
                WritableImage fxImage = SwingFXUtils.toFXImage(bufferedImage, null);
                ImageView imageView = new ImageView(fxImage);
                imageView.setPreserveRatio(true);
                // imageView.setFitHeight(1000);
                //imageView.setFitWidth(1000);
                return imageView;
        }

        /**
         * Creates a BufferedImage with the specified dimensions.
         *
         * @param x the width of the image
         * @param y the height of the image
         * @return the created BufferedImage
         */
        public static BufferedImage createBufferedImage(int x, int y) {
                // Create a BufferedImage with the specified dimensions
                BufferedImage image = new BufferedImage(x, y, BufferedImage.TYPE_INT_ARGB);

                // Get the Graphics2D object to draw on the image
                Graphics2D g2d = image.createGraphics();

                // Set the background color to black with 0 opacity
                Color transparentBlack = new Color(0, 0, 0, 0);
                g2d.setColor(transparentBlack);
                g2d.fillRect(0, 0, x, y);

                // Dispose of the Graphics2D object to free resources
                g2d.dispose();

                return image;
        }

        /**
         * Tiis method will resize the image get to what we nedd
         *
         * @param imageToBeResized
         * @param size_x
         * @param size_y
         * @return
         */
        public static BufferedImage ResizeImage(BufferedImage imageToBeResized, int size_x, int size_y) {
                // Resize image_get to size_x and size_y
                BufferedImage resizedImageGet = new BufferedImage(size_x, size_y, BufferedImage.TYPE_INT_ARGB);
                Graphics2D g2d = resizedImageGet.createGraphics();
                g2d.drawImage(imageToBeResized, 0, 0, size_x, size_y, null);
                g2d.dispose();
                return resizedImageGet;
        }

        /**
         * This function extract the opacity data from the image to transform it
         * into array.
         *
         * @param image
         * @return
         */
        public static int[][] transformToOpacityArray(BufferedImage image) {
                int width = image.getWidth();
                int height = image.getHeight();
                int[][] opacityArray = new int[width][height];

                Raster raster = image.getAlphaRaster();
                if (raster == null) {
                        throw new IllegalArgumentException("The image does not have an alpha channel.");
                }

                for (int y = 0; y < height; y++) {
                        for (int x = 0; x < width; x++) {
                                int alpha = raster.getSample(x, y, 0);
                                opacityArray[x][y] = alpha;
                        }
                }
                return opacityArray;
        }

        /**
         * This convert a hexadacimal color into a java color
         *
         * @param hex
         * @return
         */
        public static Color hexToColor(String hex) {
                // Remove the '#' character if present
                if (hex.startsWith("#")) {
                        hex = hex.substring(1);
                }

                // Parse the hex string
                int r = Integer.parseInt(hex.substring(0, 2), 16);
                int g = Integer.parseInt(hex.substring(2, 4), 16);
                int b = Integer.parseInt(hex.substring(4, 6), 16);

                return new Color(r, g, b);
        }

        /**
         * This convert a color into hexadecimal value (in sting form)
         *
         * @param color
         * @return
         */
        public static String colorToHex(Color color) {
                String hex = String.format("#%02x%02x%02x", color.getRed(), color.getGreen(), color.getBlue());
                return hex;
        }

        public static BufferedImage overlayImages(BufferedImage img1, BufferedImage img2) {
                return overlayImages(img1, img2, 0, 0);
        }

           /**
         * Overlays two images with specified coordinates for the second image.
         *
         * @param img1 The first image (base image).
         * @param img2 The second image to overlay on the first image.
         * @param x The x-coordinate where img2 will be placed on img1.
         * @param y The y-coordinate where img2 will be placed on img1.
         * @return A new combined image.
         */
        public static BufferedImage overlayImages(BufferedImage img1, BufferedImage img2, int x, int y) {
                // Crée une nouvelle image avec les dimensions de la plus grande des deux images

                BufferedImage combined = new BufferedImage(img1.getWidth(), img1.getHeight(), BufferedImage.TYPE_INT_ARGB);

                // Obtient le contexte graphique de la nouvelle image
                Graphics2D g = combined.createGraphics();

                // Dessine la première image sur la nouvelle image
                g.drawImage(img1, 0, 0, null);

                // Dessine la deuxième image par-dessus la première image aux coordonnées spécifiées
                g.drawImage(img2, x, y, null);

                // Libère les ressources graphiques
                g.dispose();

                return combined;
        }
        public static BufferedImage applyMask(BufferedImage imageIn, int[][] mask) {
        // Vérifie que les dimensions du masque correspondent à celles de l'image
        if (mask.length != imageIn.getWidth() || mask[0].length != imageIn.getHeight()) {
            throw new IllegalArgumentException("Les dimensions du masque doivent correspondre à celles de l'image.  "+imageIn.getWidth()+"  "+ imageIn.getHeight()+"   "+mask.length+"   "+mask[0].length);
        }

        // Crée une nouvelle image pour stocker le résultat
        BufferedImage imageOut = new BufferedImage(imageIn.getWidth(), imageIn.getHeight(), BufferedImage.TYPE_INT_ARGB);

        // Parcourt chaque pixel de l'image
        for (int y = 0; y < imageIn.getHeight(); y++) {
            for (int x = 0; x < imageIn.getWidth(); x++) {
                // Récupère la valeur du masque pour le pixel courant
                int maskValue = mask[x][y];

                // Récupère la couleur du pixel courant dans l'image d'entrée
                int rgb = imageIn.getRGB(x, y);

                // Applique le masque en multipliant la couleur par la valeur du masque
                int alpha = ((rgb >> 24) & 0xFF) * maskValue / 255;
                int red = ((rgb >> 16) & 0xFF) * maskValue / 255;
                int green = ((rgb >> 8) & 0xFF) * maskValue / 255;
                int blue = (rgb & 0xFF) * maskValue / 255;

                // Combine les nouvelles valeurs de couleur en une seule valeur ARGB
                int newRgb = (alpha << 24) | (red << 16) | (green << 8) | blue;

                // Définit la nouvelle couleur dans l'image de sortie
                imageOut.setRGB(x, y, newRgb);
            }
        }

        return imageOut;
    }
        
         public static int[][] invertOpacityTable(int[][] table) {
                if (table == null) {
                        throw new IllegalArgumentException("Table cannot be null");
                }

                for (int i = 0; i < table.length; i++) {
                        for (int j = 0; j < table[i].length; j++) {
                                try {
                                        if (table[i][j] < 0 || table[i][j] > 255) {
                                                throw new IllegalArgumentException("Table values must be between 0 and 255");
                                        }
                                        table[i][j] = 255 - table[i][j];
                                } catch (IllegalArgumentException e) {
                                        table[i][j] = 0;
                                }
                        }
                }
                return table;
        }
}
