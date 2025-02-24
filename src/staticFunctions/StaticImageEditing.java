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
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;

/**
 *
 * @author Camille LECOURT
 */
public class StaticImageEditing {
       
        
        
        
                /**
     * Creates an ImageView from a BufferedImage.
     *
     * @param bufferedImage the BufferedImage to convert
     * @return the ImageView containing the converted Image
     */
    public static ImageView createImageView(BufferedImage bufferedImage) {
        WritableImage fxImage =SwingFXUtils.toFXImage(bufferedImage, null);
      ImageView imageView =new ImageView(fxImage);
        imageView.setPreserveRatio(true);
       // imageView.setFitHeight(1000);
         //imageView.setFitWidth(1000);
        return imageView;
    }
        
        
        
        
        
          /**
         * Tiis method will resize the image get to what we nedd
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
                int r = Integer.parseInt(hex.substring(0, 2),16);
                int g = Integer.parseInt(hex.substring(2, 4),16);
                int b = Integer.parseInt(hex.substring(4, 6),16);

                return new Color(r, g, b);
        }
        
        /**
         * This convert a color into hexadecimal value (in sting form)
         * @param color
         * @return 
         */
          public static String colorToHex(Color color) {
                String hex = String.format("#%02x%02x%02x", color.getRed(), color.getGreen(), color.getBlue());
                return hex;
        }
}
