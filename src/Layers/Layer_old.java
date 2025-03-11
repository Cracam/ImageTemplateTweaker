/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Layers;


import imageBuilder.ImageBuilder_old;
import Exceptions.XMLExeptions.XMLErrorInModelException;
import Layers.SubClasses.QuadrupletFloat;
import Layers.SubClasses.QuadrupletInt;
import ResourcesManager.ResourcesManager;
import interfaces.Interface;
import interfaces.InterfaceRandomImageAllocations;
import interfaces.InterfaceRandomImageDispersion;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.control.TitledPane;
import org.w3c.dom.Element;
import staticFunctions.StaticImageEditing;

/**
 *
 * @author LECOURT Camille
 */
public abstract class Layer_old {

        // Variable of Interface management in the app 
         final String layerName;
         final ImageBuilder_old linkedImagesBuilder;
         final Interface linkedInterface;

        final ResourcesManager modelResources;  // model conrrespont to the sceletton of the images we return
                
         BufferedImage image_out; // compilation of image in and the layer below
         BufferedImage image_in; // compliation of all the lay below
         BufferedImage image_get; //the image that will containn the processing data 

        //Get Image parameter (real positions and size in milimeter
          QuadrupletFloat posSize;

        //Te Image size and parameter in pixel (adaptable to the image definition)
          QuadrupletInt pixelPosSize;
        
//       
//        public static final Map<String, Class<? extends Layer_old>> layersTypesMap = Map.of("Fixed_Image", LayerFixedImage.class, 
//                "Custom_Image", LayerCustomImage.class,
//                "Custom_Color", TransformerCustomColor.class, 
//                "Custom_Text",LayerCustomText.class,
//                "Fixed_Text_Custom_Color_Custom_Style", LayerFixedTextCustomStyleCustomColor.class,
//                "Mouvable_Fixed_Image", TransformerMovableFixedImage.class,
//                  "Custom_Shape_Custom_Color" ,LayerCustomShapeCustomColor.class,
//                   "Random_Image_Dispersion",TransformerRandomImageDispersion.class,
//                    "Random_Image_Allocations",TransformerRandomImageAllocation.class
//        );

        
        // this variable will be use by the Image builder to detect a change and recompute the image accordingly.
        private boolean changed=false;
        
      
        /**
         * the basic contructor
         *
         * @param layerName
         * @param modelResources
         * @param layerInterface
         * @param linkedImageBuilder
         * @param posSize
         */
        public Layer_old(String layerName,  ResourcesManager modelResources, Interface layerInterface, ImageBuilder_old linkedImageBuilder, QuadrupletFloat posSize) {
                this.layerName = layerName;
                this.modelResources = modelResources;
                this.linkedInterface=layerInterface;
                this.linkedImagesBuilder=linkedImageBuilder;
                this.posSize=posSize;
                this.pixelPosSize=new QuadrupletInt(0, 0, 0, 0);
                refreshDPI();
        }
        
        
        /**
         * This code refresh the size of the 3 image used for this abstract
         * class
         */
        public void refreshDPI() {
//                pixelPosSize.computePixelPosSize(posSize, linkedImagesBuilder.getPixelMmFactor());
//                //   System.out.println("Pos Image builder : "+anotherImageBuilder.getX_p_size()+"   "+anotherImageBuilder.getY_p_size());
//                this.image_get = new BufferedImage(pixelPosSize.getSize_x(), pixelPosSize.getSize_y(), BufferedImage.TYPE_INT_ARGB);
//                this.image_in = new BufferedImage(linkedImagesBuilder.getX_p_size(), linkedImagesBuilder.getY_p_size(), BufferedImage.TYPE_INT_ARGB);
//                this.image_out = new BufferedImage(linkedImagesBuilder.getX_p_size(), linkedImagesBuilder.getY_p_size(), BufferedImage.TYPE_INT_ARGB);
//                DPIChanged();
        }
        
        /**
         * run the code specific to the layer in case of DPI change.
         * just the data stored to create the image get : not the image get size itself
         */
        abstract void DPIChanged();
 

        /**
         * Get the output of the Layer
         *
         * @return
         */
        public BufferedImage getImage_out() {
           return this.image_out;
        }

        public Interface getLinkedInterface() {
                return linkedInterface;
        }
        


        /**
         * Set the imput of the layer
         *
         * @param image_in
         */
        public void setImage_in(BufferedImage image_in) {
                           this.image_in=image_in;
        }

        


         
       /**
        * Change the preview (it will just enter the preview Image box object)
        */
        public void refreshPreview() {
//              this.linkedInterface.refreshPreview(this.linkedImagesBuilder.getName(),StaticImageEditing.createImageView(this.image_out));
                //   System.out.println(toString());
        }
          
      
       

       
        /**
         * this programm will refresh the image get
         * 
         * depending o fthe layer type
         * it can be : - an image loaded by user - a locked image(get in the
         * resources of the model) - a gradient generated form user's color
         * choice - a gradient generated from user's shape choice - .....
         *
         */
        public abstract void refreshImageGet();

        
        /**
         * Compute the image out using Image_in and image_get
         * @param name
         */
        public void computeImage_Out(String name) {
           

                        // Create a new BufferedImage for the output
                        BufferedImage outputImage = new BufferedImage(image_in.getWidth(), image_in.getHeight(), BufferedImage.TYPE_INT_ARGB);
                        Graphics2D outputG2d = outputImage.createGraphics();

                        // Draw image_out onto the output image
                        outputG2d.drawImage(image_in, 0, 0, null);

                        // Draw the resized image_get onto the output image at the specified position
                        outputG2d.drawImage(image_get,this.pixelPosSize.getPos_x(), this.pixelPosSize.getPos_y(), this.pixelPosSize.getSize_x(), this.pixelPosSize.getSize_y(), null);
                        
                        //use this if not resize 
                        //outputG2d.drawImage(image_get,this.pixelPosSize.getPos_x(), this.pixelPosSize.getPos_y(),  null);

                        // Dispose of the Graphics2D object
                        outputG2d.dispose();

                        // Update image_out with the new image
                        this.image_out = outputImage;
                         this.refreshPreview();

        }

        
       
        
                
//        /**
//         * This code will load the layer using a Strin identifier to get the type of the Layer
//         * @param layerType
//         * @param layerName
//         * @param modelResources
//         * @param layerInterface
//         * @param linkedImageBuilder
//         * @param posSize
//         * @return
//         * @throws GetAttributeValueException 
//         */
//          public static Layer_old createLayer(String layerType, String layerName,  ResourcesManager modelResources, Interface layerInterface, ImageBuilder_old linkedImageBuilder, QuadrupletFloat posSize) throws GetAttributeValueException {
//
//                if (!layersTypesMap.containsKey(layerType)) {
//                        throw new GetAttributeValueException("This interface type does not exist : " + layerType);
//                }
//                
//                try {
//
//                        Class<? extends Layer_old> subclass = layersTypesMap.get(layerType);
//                        Constructor<? extends Layer_old> constructor = subclass.getConstructor(String.class, ResourcesManager.class,  Interface.class , ImageBuilder_old.class , QuadrupletFloat.class );
//                        
//                        return constructor.newInstance(layerName, modelResources,layerInterface,linkedImageBuilder,posSize );
//
//                } catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException ex) {
//                        Logger.getLogger(Interface.class.getName()).log(Level.SEVERE, null, ex);
//                            ex.printStackTrace(); // Print the stack trace
//
//                        return null;
//                }
//        }
//        
        
        


        
        
        
        
        
        

        /**
         * This method will read and load the parameter from the XML File that
         * are specific to the layer
         *
         * @param paramNode
         */
        public abstract void readNode(Element paramNode);

        //implement save return (for design)
        
        


        public boolean isChanged() {
                return changed;
        }
        
        /**
         * we also set the value of the pper layer if there is one
         * @param changed 
         */
        public void setChanged(boolean changed) {
                        this.changed = changed;
                      
                
        }
        
    

   

        @Override
        public String toString() {
                return "Layer{" + "layerName=" + layerName + ", image_out=" + image_out + ", image_in=" + image_in + ", image_get=" + image_get + ", posSize=" + posSize + ", pixelPosSize=" + pixelPosSize + '}';
        }
    
    


        public BufferedImage getImage_get() {
                return image_get;
        }
        

       
}
