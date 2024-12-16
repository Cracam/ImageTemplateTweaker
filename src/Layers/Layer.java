/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Layers;

import imageBuilder.ImageBuilder;
import Exceptions.TheXmlElementIsNotANodeException;
import Exceptions.ThisLayerDoesNotExistException;
import Layers.SubClasses.QuadrupletFloat;
import Layers.SubClasses.QuadrupletInt;
import ResourcesManager.ResourcesManager;
import interfaces.Interface;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.control.TitledPane;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import staticFunctions.StaticImageEditing;

/**
 *
 * @author LECOURT Camille
 */
public abstract class Layer extends TitledPane {

        // Variable of Interface management in the app 
         final String layerName;
         final ImageBuilder linkedImagesBuilder;
         final Interface linkedInterface;

        final ResourcesManager modelResources;  // model conrrespont to the sceletton of the images we return
                
         BufferedImage image_out; // compilation of image in and the layer below
         BufferedImage image_in; // compliation of all the lay below
         BufferedImage image_get; //the image that will containn the processing data 

        //Get Image parameter (real positions and size in milimeter
          QuadrupletFloat posSize;

        //Te Image size and parameter in pixel (adaptable to the image definition)
          QuadrupletInt pixelPosSize;
        
        public static final Map<String, Class<? extends Layer>> layersTypesMap = Map.of("Fixed_Image", LayerFixedImage.class, "Custom_Image", LayerCustomImage.class,"Custom_Color", LayerCustomColor.class);

        
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
        public Layer(String layerName,  ResourcesManager modelResources, Interface layerInterface, ImageBuilder linkedImageBuilder, QuadrupletFloat posSize) {
                this.layerName = layerName;
                this.setText(layerName);
                this.modelResources = modelResources;
                this.linkedInterface=layerInterface;
                this.linkedImagesBuilder=linkedImageBuilder;
                this.posSize=posSize;
                refreshDPI();
        }
        
        
        /**
         * This code refresh the size of the 3 image used for this abstract
         * class
         */
        public void refreshDPI() {
                pixelPosSize.computePixelPosSize(posSize, linkedImagesBuilder.getPixelMmFactor());
                //   System.out.println("Pos Image builder : "+anotherImageBuilder.getX_p_size()+"   "+anotherImageBuilder.getY_p_size());
                this.image_get = new BufferedImage(pixelPosSize.getSize_x(), pixelPosSize.getSize_y(), BufferedImage.TYPE_INT_ARGB);
                this.image_in = new BufferedImage(linkedImagesBuilder.getX_p_size(), linkedImagesBuilder.getY_p_size(), BufferedImage.TYPE_INT_ARGB);
                this.image_out = new BufferedImage(linkedImagesBuilder.getX_p_size(), linkedImagesBuilder.getY_p_size(), BufferedImage.TYPE_INT_ARGB);
                DPIChanged();
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
       public abstract void refreshPreview();
          
       
       void refreshPreview(previewimagebox.PreviewImageBox box){
               box.setImageView(this.layerName,StaticImageEditing.createImageView(image_out));
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

                        // Dispose of the Graphics2D object
                        outputG2d.dispose();

                        // Update image_out with the new image
                        this.image_out = outputImage;
                         this.refreshPreview();

        }

        
       
        
                
        /**
         * This code will load the layer using a Strin identifier to get the type of the Layer
         * @param layerType
         * @param layerName
         * @param modelResources
         * @param layerInterface
         * @param linkedImageBuilder
         * @param posSize
         * @return
         * @throws ThisLayerDoesNotExistException 
         */
          public static Layer createLayer(String layerType, String layerName,  ResourcesManager modelResources, Interface layerInterface, ImageBuilder linkedImageBuilder, QuadrupletFloat posSize) throws ThisLayerDoesNotExistException {

                if (!layersTypesMap.containsKey(layerType)) {
                        throw new ThisLayerDoesNotExistException("This interface type does not exist : " + layerType);
                }
                try {

                        Class<? extends Layer> subclass = layersTypesMap.get(layerType);
                        Constructor<? extends Layer> constructor = subclass.getConstructor(String.class, ResourcesManager.class,  Interface.class , ImageBuilder.class , QuadrupletFloat.class );

                        return constructor.newInstance(layerName, modelResources,layerInterface,linkedImageBuilder,posSize );

                } catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException ex) {
                        Logger.getLogger(Interface.class.getName()).log(Level.SEVERE, null, ex);
                        return null;
                }
        }
        
        
        
        
        
        
        
        
        
        

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
