/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Layers;

import javafx.scene.image.Image;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.control.TitledPane;
/**
 *
 * @author LECOURT Camille
 */
public abstract class Layer {
         
         //Tilled Pane parameter
         private String layerName;
         private TitledPane layer;
         
         //In Image Parameter
         private Boolean ResourcesInCustom; // true if user can select for each model -- false if the image in is get inside the zip folder of model
         private String image_InPath;
         
         //Out Image parameter
         float pos_x;
         float pos_y;
         float size_x;
         float size_y;
         
         // List of the image 
         private BufferedImage image_out;
         private BufferedImage image_in;
         private BufferedImage image_get;
         
         
        
         

         public Layer(String layerName, float pos_x, float pos_y, float size_x, float size_y) {
                  this.layerName = layerName;
                  this.pos_x = pos_x;
                  this.pos_y = pos_y;
                  this.size_x = size_x;
                  this.size_y = size_y;
         }



         public void loadImageIn() {
                  try {
                           System.out.println("Paths " +this.image_InPath);
                           this.image_in = ImageIO.read(new File(this.image_InPath));
                  } catch (IOException e) {
                           System.err.println("Error loading image: " + e.getMessage());
                  }
         }


    

       
         
      
          
           protected abstract void compImagePath();
      

         public BufferedImage getBufferedImage() {
                 return this.image_in;
         }
         public Image getImage() {
                  //return  Toolkit.getDefaultToolkit().createImage( this.image_in.getSource());
                    return SwingFXUtils.toFXImage(this.image_in, null);

         }
         
}
