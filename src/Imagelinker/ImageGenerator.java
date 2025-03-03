

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Imagelinker;

import java.awt.image.BufferedImage;

/**
 *
 * @author Camille LECOURT
 */
public abstract class ImageGenerator {
        
        private final ImageTransformer out;

        public ImageGenerator(ImageTransformer out) {
                this.out = out;
        }
        
        abstract BufferedImage getImageOut();
        
        
}
