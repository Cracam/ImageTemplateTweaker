/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Layers;

import javafx.scene.image.Image;
import java.awt.image.BufferedImage;
import javafx.scene.control.TitledPane;
/**
 *
 * @author LECOURT Camille
 */
public abstract class Layer {
         
         //Tilled Pane parameter
         private final String layerName;
         private final TitledPane layerInterface;
         
         //In Image Parameter
         private final Boolean resourcesInCustom; // true if user can select for each model -- false if the image in is get inside the zip folder of model
         
         
         
         // List of the image (all this image have the 
         private BufferedImage image_out; // compilation of image in and the layer below
         private BufferedImage image_in; // compliation of all the lay below
         
         
         //Get Image parameter
         int pos_x;
         int pos_y;
         int size_x;
         int size_y;
         private BufferedImage image_get; //the image that will containn the processing data 
         
         
        
         
         /**
          * the basic contructor
          * @param layerName
          * @param resourcesInCustom
          * @param pos_x
          * @param pos_y
          * @param size_x
          * @param size_y 
          * @param layerInterface 
          */
         public Layer(String layerName,boolean resourcesInCustom, int pos_x, int pos_y, int size_x, int size_y, TitledPane layerInterface) {
                  this.layerName = layerName;
                  this.resourcesInCustom=resourcesInCustom;
                  this.pos_x = pos_x;
                  this.pos_y = pos_y;
                  this.size_x = size_x;
                  this.size_y = size_y;
                  this.layerInterface=layerInterface;
                  
         }
         
         /**
          * Get the output of the Layer
          * @return 
          */
         public BufferedImage getImage_out(){
                  return this.image_out;
         }
         
         /**
          * Set the imput of the layer
          * @param image_in 
          */
         public void setImage_in(BufferedImage image_in){
                  this.image_in=image_in;
         }
         
         /**
          * This method is use to resize the image out 
          * It's used for ajusting the quality
          * @param size_x
          * @param size_y
          * @param pos_x
          * @param pos_y 
          */
         public void setSizeAndPos(int size_x, int size_y,int pos_x,int pos_y){
                  this.size_x=size_x;
                  this.size_y=size_y;
                  this.pos_x=pos_x;
                  this.pos_y=pos_y;
         }
         
         
         
 // ----------------------------
         // End of public methods
         
         
  

         

         /**
          * this programm will retunr the image get (ready to be paste on the image_in to get image_out)
          * depending o fthe layer type it can be : 
          * - an image loaded by user 
          * - a locked image(get in the resources of the model)
          * - a gradient generated form user's color choice
          * - a gradient generated from user's shape choice 
          *  - .....
          * @return 
          */
         private abstract  BufferedImage getImage_get();
         
         
        
         private computeImage_Out(){
                  this.image_in;
                  this.image_out;
                  this.image_get;
                  
                   this.size_x=size_x;
                  this.size_y=size_y;
                  this.pos_x=pos_x;
                  this.pos_y=pos_y;
                  
                  
                
         }
         
     
         
         
    

       private computeImage_Out(){
                  this.image_in;
                  this.image_out;
                  this.image_get;
                  
                   this.size_x;
                  this.size_y;
                  this.pos_x;
                  this.pos_y;
                  
                  
                
         }
         write the method that will 
make the imageGet to size_x and  size_y

paste the image get with position ( pos_x and pos_y ) on image out 

keep opacity
         
      
          
      


      
         
}
