/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Layers;

import imageBuilder.ImageBuilder;
import Exceptions.TheXmlElementIsNotANodeException;
import Exceptions.ThisImageBuilderNotExistInThisLayer;
import Exceptions.ThisLayerDoesNotExistException;
import Layers.SubClasses.QuadrupletFloat;
import Layers.SubClasses.QuadrupletInt;
import ResourcesManager.ResourcesManager;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.control.TitledPane;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import previewimagebox.PreviewImageBox;

/**
 *
 * @author LECOURT Camille
 */
public abstract class Layer extends TitledPane {

        // Variable of Interface management in the app 
        private final String layerName;
        private static HashMap<String, Layer> createdLayers = new HashMap<>();  //HashMap of all the created Layer (the key will be the name of the DesignBuider class +"_"+ name of the layer

        private List<ImageBuilder> linkedImagesBuilders = new ArrayList<>();

        final ResourcesManager modelResources;  // model conrrespont to the sceletton of the images we return
        final ResourcesManager designResources; // design correspontd to how the user create something from the model
        
        private final String tabName;
        // hashmap containing all the images of used by the layer. 
        // I use Hashmap in order ot habe allow multiple image in/out for having one interface that have multiple in and out 
        // List of the image (all this image have the 
        private final HashMap<String, BufferedImage> image_out = new HashMap<>(); // compilation of image in and the layer below
        private final HashMap<String, BufferedImage> image_in = new HashMap<>(); // compliation of all the lay below
        private final HashMap<String, BufferedImage> image_get = new HashMap<>(); //the image that will containn the processing data 

        //Get Image parameter (real positions and size in milimeter
        private final HashMap<String, QuadrupletFloat> posSize = new HashMap<>();

        //The Image size and parameter in pixel (adaptable to the image definition)
        private final HashMap<String, QuadrupletInt> pixelPosSize = new HashMap<>();

        
        public static final Map<String, Class<? extends Layer>> layersTypesMap = Map.of("Fixed_Image", LayerFixedImage.class, "Custom_Image", LayerCustomImage.class,"Custom_Color", LayerCustomColor.class);

        private boolean haveTiltlePane= true;
        
        // this variable will be use by the Image builder to detect a change and recompute the image accordingly.
        private boolean changed=false;
        
        /**
         * the basic contructor
         *
         * @param layerName
         * @param tabName
         * @param modelResources
         * @param designResources
         */
        public Layer(String layerName, String tabName, ResourcesManager modelResources, ResourcesManager designResources) {
                this.layerName = layerName;
                this.tabName=tabName;
                this.setText(layerName);
                this.modelResources = modelResources;
                this.designResources = designResources;
                Layer.createdLayers.put(this.layerName,this);
                initialiseInterface();
        }

        /**
         * This function initialise an element in each HashMap for the future
         * image buider return
         *
         * @param anotherIamgeBuilder
         */
        private void linkToAnotherImageBuilder(ImageBuilder anotherImageBuilder, float pos_x, float pos_y, float size_x, float size_y) {
                this.linkedImagesBuilders.add(anotherImageBuilder);
                String name = anotherImageBuilder.getName();

                posSize.put(name, new QuadrupletFloat(pos_x, pos_y, size_x, size_y));
                pixelPosSize.put(name, new QuadrupletInt(0,0,0,0));
                reComputePixelSizeAndPos(anotherImageBuilder.get_pixel_mm_Factor());

                int pixelSize_x = pixelPosSize.get(name).getSize_x();
                int pixelSize_y = pixelPosSize.get(name).getSize_y();
               
                System.out.println("Pos Image builder : "+anotherImageBuilder.getX_p_size()+"   "+anotherImageBuilder.getY_p_size());
                this.image_get.put(name, new BufferedImage(pixelSize_x, pixelSize_y, BufferedImage.TYPE_INT_ARGB));
                this.image_in.put(name, new BufferedImage(anotherImageBuilder.getX_p_size(), anotherImageBuilder.getY_p_size(), BufferedImage.TYPE_INT_ARGB));
                this.image_out.put(name, new BufferedImage(anotherImageBuilder.getX_p_size(), anotherImageBuilder.getY_p_size(), BufferedImage.TYPE_INT_ARGB));

        }

        /**
         * Get the output of the Layer
         *
         * @param name
         * @return
         */
        public BufferedImage getImage_out(String name) {
                try {
                        if (!this.image_out.containsKey(name)) {
                                throw new ThisImageBuilderNotExistInThisLayer("the key is not valid - It may  an error in image buider ");
                        }
                        return this.image_out.get(name);

                } catch (ThisImageBuilderNotExistInThisLayer ex) {
                        Logger.getLogger(Layer.class.getName()).log(Level.SEVERE, null, ex);
                        return null;
                }
        }

        /**
         * Set the imput of the layer
         *
         * @param name
         * @param image_in
         */
        public void setImage_in(String name, BufferedImage image_in) {
                try {
                        if (!this.image_in.containsKey(name)) {
                                throw new ThisImageBuilderNotExistInThisLayer("the key is not valid - It may  an error in image buider ");

                        }
                        this.image_in.put(name, image_in);
                } catch (ThisImageBuilderNotExistInThisLayer ex) {
                        Logger.getLogger(Layer.class.getName()).log(Level.SEVERE, null, ex);
                }
        }

        /**
         * This method is use to recalculate the position and the size of
         * image_get It's used for ajusting the quality
         *
         * @param pixelPerMilimeterFactor
         */
        public void reComputePixelSizeAndPos(float pixelPerMilimeterFactor) {
                this.pixelPosSize.forEach((key, value) -> {
                        System.out.println("clef :  "+key);
                        pixelPosSize.get(key).computePixelPosSize(posSize.get(key), pixelPerMilimeterFactor);
                });

                computeAllImageGet();// for automaticaly 
        }

        // ----------------------------
        // End of public methods
        // the image computation methods
        private void computeImageGet(String name) {
                try {
                        if (!this.image_get.containsKey(name)) {
                                throw new ThisImageBuilderNotExistInThisLayer("the key is not valid - It may  an error in image buider ");
                        }

                        this.image_get.put(name, generateImageget(name));

                } catch (ThisImageBuilderNotExistInThisLayer ex) {
                        Logger.getLogger(Layer.class.getName()).log(Level.SEVERE, null, ex);
                }
        }

        /**
         * this compute all the image get
         */
         void computeAllImageGet() {
                this.image_get.forEach((key, value) -> {
                        computeImageGet(key);
                });
        }
         
         
       void refreshPreview(PreviewImageBox box){
               box.clearAllImagesViews();
                this.image_out.forEach((key, value) -> {
                        box.addImageView(createImageView(image_out.get(key)));
                });
        }

        /**
         * this programm will retunr the image get (ready to be resized to be
         * paste on the image_in to get image_out) depending o fthe layer type
         * it can be : - an image loaded by user - a locked image(get in the
         * resources of the model) - a gradient generated form user's color
         * choice - a gradient generated from user's shape choice - .....
         *
         * @return
         */
        abstract BufferedImage generateImageget(String key);

        /**
         * Tiis method will resize the image get to what we nedd
         */
        static BufferedImage ResizeImage(BufferedImage imageToBeResized, int size_x, int size_y) {
                // Resize image_get to size_x and size_y
                BufferedImage resizedImageGet = new BufferedImage(size_x, size_y, BufferedImage.TYPE_INT_ARGB);
                Graphics2D g2d = resizedImageGet.createGraphics();
                g2d.drawImage(imageToBeResized, 0, 0, size_x, size_y, null);
                g2d.dispose();
                return resizedImageGet;
        }

        /**
         * Compute the image out using Image_in and image_get
         * @param name
         */
        public void computeImage_Out(String name) {
                try {
                        if (!this.image_get.containsKey(name) | !this.image_in.containsKey(name) | !this.image_out.containsKey(name)) {
                                throw new ThisImageBuilderNotExistInThisLayer("the key is not valid - It may  an error in image buider ");
                        }

                        // Create a new BufferedImage for the output
                        BufferedImage outputImage = new BufferedImage(image_in.get(name).getWidth(), image_in.get(name).getHeight(), BufferedImage.TYPE_INT_ARGB);
                        Graphics2D outputG2d = outputImage.createGraphics();

                        // Draw image_out onto the output image
                        outputG2d.drawImage(image_in.get(name), 0, 0, null);

                        // Draw the resized image_get onto the output image at the specified position
                        outputG2d.drawImage(image_get.get(name),this.pixelPosSize.get(name).getPos_x(), this.pixelPosSize.get(name).getPos_y(), this.pixelPosSize.get(name).getSize_x(), this.pixelPosSize.get(name).getSize_y(), null);

                        // Dispose of the Graphics2D object
                        outputG2d.dispose();

                        // Update image_out with the new image
                        this.image_out.put(name, outputImage);
                         this.refreshPreview();
                                 
                } catch (ThisImageBuilderNotExistInThisLayer ex) {
                        Logger.getLogger(Layer.class.getName()).log(Level.SEVERE, null, ex);
                }
        }

        
        
       public abstract void refreshPreview();
        
       // end image computation
        //-----------------------------------------------------------------------------------------------------------
        // Interface management
        
        /**
         * this methods initialise the interface off the layer.
         */
        abstract void initialiseInterface();

        // END interface mangement
//---------------------------------------------------------------------------------------------
        // Resources Management
        
        /**
         * This function will return every parameter of the layer in the form of
         * a node (in order to save it)
         *It also save the image that have been imported on the design
         * @return
         */
        abstract Node saveLayerData();

        
 
        

        
        
        
        /**
         * This method create a Layer of the good type it use a static Map of
         * layer class linked to a string (the string that the user will define
         * in the XML file model)
         *
         * @param imageBuilder
         * @param layerNode
         * @param templateResources
         * @param designResources
         * @return
         * @throws Exceptions.ThisLayerDoesNotExistException
         */
        public static Layer loadLayer(ImageBuilder imageBuilder, Node layerNode, ResourcesManager templateResources, ResourcesManager designResources) throws ThisLayerDoesNotExistException {
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

                                layerToReturn = constructor.newInstance(name, tabname,templateResources, designResources);

                               

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
        imageView.setFitHeight(1000);
         imageView.setFitWidth(1000);
        return imageView;
    }

        public boolean isChanged() {
                return changed;
        }

        public void setChanged(boolean changed) {
                this.changed = changed;
        }

        public boolean isHaveTiltlePane() {
                return haveTiltlePane;
        }

        public void setHaveTiltlePane(boolean haveTiltlePane) {
                this.haveTiltlePane = haveTiltlePane;
        }

        public List<ImageBuilder> getLinkedImagesBuilders() {
                return linkedImagesBuilders;
        }

        public String getTabName() {
                return tabName;
        }

        @Override
        public String toString() {
                return "Layer{" + "layerName=" + layerName + ", tabName=" + tabName + ", image_out=" + image_out + ", image_in=" + image_in + ", image_get=" + image_get + ", posSize=" + posSize + ", pixelPosSize=" + pixelPosSize + '}';
        }
    
    


        public HashMap<String, BufferedImage> getImage_get() {
                return image_get;
        }
        
        
    
        public abstract void DPIChanged();
    
       public QuadrupletInt getPixelPosSize(String name){
               return pixelPosSize.get(name);
       }
       
       
       public int imageGetPixelSizeX(String key){
               return this.pixelPosSize.get(key).getSize_x();
       }
        public int imageGetPixelSizeY(String key){
               return this.pixelPosSize.get(key).getSize_y();
       }
       
}
