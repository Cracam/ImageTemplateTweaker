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
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.control.TitledPane;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

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
        }

 

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

        


         
         //je doit changer les prewie avant
       void refreshPreview(){
               box.clearAllImagesViews();
                this.image_out.forEach((key, value) -> {
                        box.addImageView(createImageView(image_out.get(key)));
                });
        }

       
        /**
         * this programm will retunr the image get
         * 
         * depending o fthe layer type
         * it can be : - an image loaded by user - a locked image(get in the
         * resources of the model) - a gradient generated form user's color
         * choice - a gradient generated from user's shape choice - .....
         *
         * @return
         */
        abstract BufferedImage generateImageget();

        
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

        
        
        
       // end image computation
        //-----------------------------------------------------------------------------------------------------------
        // Interface management
        


        // END interface mangement
//---------------------------------------------------------------------------------------------
        // Resources Management


        

 
        

        
        
        
        /**
         * This method create a Layer of the good type it use a static Map of
         * layer class linked to a string (the string that the user will define
         * in the XML file model)
         *
         * @param imageBuilder
         * @param layerNode
         * @param modelResources
         * @return
         * @throws Exceptions.ThisLayerDoesNotExistException
         */
        public static Layer loadLayer(ImageBuilder imageBuilder, Node layerNode, ResourcesManager modelResources) throws ThisLayerDoesNotExistException {
                try {
                        if (layerNode.getNodeType() != Node.ELEMENT_NODE) {
                                throw new TheXmlElementIsNotANodeException(layerNode.getNodeType()+  "   IN Layer (1) "+layerNode.getNodeName());
                        }
                        
                        Element element = (Element) layerNode;
                        String key = element.getNodeName();

                        if (!Layer.layersTypesMap.containsKey(key)) {
                                throw new ThisLayerDoesNotExistException(layerNode.getNodeName());
                        }

                        Layer layerToReturn;

                        String name = element.getAttribute("name");
                        String tabname = element.getAttribute("tab_name");
                        
                        
                                if (Layer.createdLayers.containsKey(name) ) System.out.println("memem ELT ");
                        
                       if (Layer.createdLayers.containsKey(name) && (layersTypesMap.get(key)==Layer.createdLayers.get(name).getClass()) && tabname.equals(Layer.createdLayers.get(name).getTabName())) { // in this case we will just give back the layer (we will make a test to ensure that the layers are from the smae type)
                                layerToReturn = Layer.createdLayers.get(name);

                        } else { // in this case we will create the good layer

                                Class<? extends Layer> subclass = layersTypesMap.get(key);
                                Constructor<? extends Layer> constructor = subclass.getConstructor(String.class,String.class, ResourcesManager.class, ResourcesManager.class);

                                layerToReturn = constructor.newInstance(name, tabname,modelResources, designResources);

                               

                                if (tabname == null | "".equals(tabname)) {
                                        imageBuilder.assignLayerToTab(layerToReturn, "Non attribués");
                                } else {
                                        imageBuilder.assignLayerToTab(layerToReturn, tabname);
                                }

                        }
                        
                        float pos_x = Float.parseFloat(element.getElementsByTagName("pos").item(0).getAttributes().getNamedItem("pos_x").getNodeValue());
                        float pos_y = Float.parseFloat(element.getElementsByTagName("pos").item(0).getAttributes().getNamedItem("pos_y").getNodeValue());
                        float size_x = Float.parseFloat(element.getElementsByTagName("size").item(0).getAttributes().getNamedItem("size_x").getNodeValue());
                        float size_y = Float.parseFloat(element.getElementsByTagName("size").item(0).getAttributes().getNamedItem("size_y").getNodeValue());
                        //System.out.println(pos_x+" "+pos_y+" "+size_x+" "+size_y);

                        layerToReturn.linkToAnotherImageBuilder(imageBuilder,pos_x, pos_y, size_x, size_y); //this line it to inform the Layer of if master
                        
                    
                       
                        //This code verify if the <Param> element is really an element
                        Element retElement=(Element) element.getElementsByTagName("Param").item(0);
                         if (retElement.getNodeType() != Node.ELEMENT_NODE) {
                                throw new TheXmlElementIsNotANodeException("IN Layer(2) "+layerNode.getNodeName());
                        }
                        layerToReturn.readNode(retElement,imageBuilder); //read the specific parameter

                        return layerToReturn;

                } catch (NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException  | TheXmlElementIsNotANodeException ex) {
                        Logger.getLogger(Layer.class.getName()).log(Level.SEVERE, null, ex);
                        return null;
                }
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
          public static Layer createOrReturnLayer(String layerType, String layerName,  ResourcesManager modelResources, Interface layerInterface, ImageBuilder linkedImageBuilder, QuadrupletFloat posSize) throws ThisLayerDoesNotExistException {

                if (!layersTypesMap.containsKey(layerType)) {
                        throw new ThisLayerDoesNotExistException("This interface type does not exist : " + layerType);
                }
                try {

                        Class<? extends Layer> subclass = layersTypesMap.get(layerType);
                        Constructor<? extends Layer> constructor = subclass.getConstructor(String.class, ResourcesManager.class,  Interface.class , ImageBuilder.class , QuadrupletFloat.class );

                        return constructor.newInstance(layerName, modelResources,layerInterface,linkedImageBuilder,posSize );

                } catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException ex) {
                        Logger.getLogger(Interface.class.getName()).log(Level.SEVERE, null, ex);
                }

                return null;
        }
        
        
        
        
        
        
        
        
        
        

        /**
         * This method will read and load the parameter from the XML File that
         * are specific to the layer
         *
         * @param layerNode
         */
        abstract void readNode(Element paramNode, ImageBuilder imageBuilder);

        //implement save return (for design)
        
        
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
