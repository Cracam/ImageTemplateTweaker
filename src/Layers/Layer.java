/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Layers;

import Exceptions.TheXmlElementIsNotANodeException;
import Exceptions.ThisLayerDoesNotExistException;
import ImageBuilder.ImageBuilder;
import ResourcesManager.ResourcesManager;
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
/**
 *
 * @author LECOURT Camille
 */
public abstract class Layer extends TitledPane{
         
         //Tilled Pane parameter
         private final String layerName;
         
         private final ResourcesManager modelResources;  // model conrrespont to the sceletton of the images we return
         private final ResourcesManager designResources; // design correspontd to how the user create something from the model
         
         
         // List of the image (all this image have the 
         private BufferedImage image_out; // compilation of image in and the layer below
         private BufferedImage image_in; // compliation of all the lay below
         
         
         //Get Image parameter (real positions and size in milimeter
         private final float pos_x;
         private final float  pos_y;
         private final float  size_x;
         private final float size_y;
         
         //The Image size and parameter in pixel (adaptable to the image definition)
         private int pixelPos_x;
         private int pixelPos_y;
         private int pixelSize_x;
         private int pixelSize_y;


         private BufferedImage image_getRaw;
         private BufferedImage image_get; //the image that will containn the processing data 
         
         
         
        private static final Map<String, Class<? extends Layer>> layersTypesMap = Map.of("Fixed_Image", LayerFixedImage.class, "Custom_Image", LayerCustomImage.class);

 
        
        
        
         /**
          * the basic contructor
          * @param layerName
          * @param modelResources
          * @param designResources
          * @param pos_x
          * @param pos_y
          * @param size_x
          * @param size_y 
          */
         public Layer(String layerName,ResourcesManager modelResources,ResourcesManager designResources, float pos_x, float pos_y, float size_x, float size_y) {
                  this.layerName = layerName;
                  this.modelResources=modelResources;
                  this.designResources=designResources;
                  this.pos_x = pos_x;
                  this.pos_y = pos_y;
                  this.size_x = size_x;
                  this.size_y = size_y;
                  initialiseInterface();
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
          * This method is use to recalculate the position and the size of  image_get 
          * It's used for ajusting the quality
          * @param pixelPerMilimeterFactor
          */
         public void reComputeSizeAndPos(float pixelPerMilimeterFactor){
                 this.pixelPos_x=(int) (pos_x*pixelPerMilimeterFactor);
                  this.pixelPos_y=(int) (pos_y*pixelPerMilimeterFactor);
                  this.pixelSize_x=(int) (size_x*pixelPerMilimeterFactor);
                  this.pixelSize_y=(int) (size_y*pixelPerMilimeterFactor);
                 computeImageGet();// for automaticaly 
         }
         
         
         
 // ----------------------------
         // End of public methods
         
         // the image computation methods
  

         

         private void computeImageGet(){
                this.image_get=ResizeImage(generateImageget(),this.pixelSize_x,this.pixelSize_y);
         }
         
         
         /**
          * this programm will retunr the image get (ready to be resized to be paste on the image_in to get image_out)
          * depending o fthe layer type it can be : 
          * - an image loaded by user 
          * - a locked image(get in the resources of the model)
          * - a gradient generated form user's color choice
          * - a gradient generated from user's shape choice 
          *  - .....
          * @return 
          */
      abstract  BufferedImage generateImageget();
         
      
      /**
       * Tiis method will resize the image get to what we nedd
       */
      static BufferedImage ResizeImage(BufferedImage imageToBeResized,int size_x,int size_y){
                  // Resize image_get to size_x and size_y
                  BufferedImage resizedImageGet = new BufferedImage(size_x, size_y, BufferedImage.TYPE_INT_ARGB);
                  Graphics2D g2d = resizedImageGet.createGraphics();
                  g2d.drawImage(imageToBeResized, 0, 0, size_x, size_y, null);
                  g2d.dispose();
                 return resizedImageGet;
      }
   
     
         /**
          * Compute the image out using Image_in and image_get
          */
         public void computeImage_Out() {
                  // Create a new BufferedImage for the output
                  BufferedImage outputImage = new BufferedImage(image_in.getWidth(), image_in.getHeight(), BufferedImage.TYPE_INT_ARGB);
                  Graphics2D outputG2d = outputImage.createGraphics();

                  // Draw image_out onto the output image
                  outputG2d.drawImage(image_in, 0, 0, null);

                  // Draw the resized image_get onto the output image at the specified position
                  outputG2d.drawImage(image_get, pixelPos_x, pixelPos_y, null);

                  // Dispose of the Graphics2D object
                  outputG2d.dispose();

                  // Update image_out with the new image
                  this.image_out = outputImage;
         }

         
         // END image computation
 //-----------------------------------------------------------------------------------------------------------
         // Interface management
         
         abstract void initialiseInterface();
      
          
      

         // END interface mangement
//---------------------------------------------------------------------------------------------
         // Resources Management
         
         /**
          * This function will return every parameter of the layer in the form of a node (in order to save it)
          * @return 
          */
         abstract Node getLayerParameter();
         
         /**
          * This method create a Layer of the good type
          * it use a static Map of layer class linked to a string (the string that the user will define in the XML file model)
          * 
          * @param layerNode 
          * @param templateResources 
          * @param designResources 
          * @return 
          */
         public static Layer loadLayer(Node layerNode,ResourcesManager templateResources,ResourcesManager designResources) {
                  try {
                           if (layerNode.getNodeType() != Node.ELEMENT_NODE) {
                                    throw new TheXmlElementIsNotANodeException(layerNode.getNodeName());
                           }
                           Element element = (Element) layerNode;
                           String key = element.getNodeName();
                           
                           if (!Layer.layersTypesMap.containsKey(key)) {
                                    throw new ThisLayerDoesNotExistException(layerNode.getNodeName());
                           }
                           
                           
                           Class<? extends Layer> subclass =layersTypesMap.get(key);

                           Constructor<? extends Layer> constructor = subclass.getConstructor(String.class, ResourcesManager.class, ResourcesManager.class,float.class, float.class, float.class, float.class);

                           String name = element.getAttribute("name"); 
                           float pos_x = Float.parseFloat(element.getElementsByTagName("pos").item(0).getAttributes().getNamedItem("pos_x").getNodeValue());
                           float pos_y = Float.parseFloat(element.getElementsByTagName("pos").item(0).getAttributes().getNamedItem("pos_y").getNodeValue());
                           float size_x = Float.parseFloat(element.getElementsByTagName("size").item(0).getAttributes().getNamedItem("size_x").getNodeValue());
                           float size_y = Float.parseFloat(element.getElementsByTagName("size").item(0).getAttributes().getNamedItem("size_y").getNodeValue());
                           
                           Layer layerToReturn=constructor.newInstance(name, templateResources,designResources, pos_x, pos_y, size_x, size_y);
                           
                           layerToReturn.readNode(layerNode);
                           return layerToReturn;

                  } catch (NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | ThisLayerDoesNotExistException | TheXmlElementIsNotANodeException ex) {
                           Logger.getLogger(Layer.class.getName()).log(Level.SEVERE, null, ex);
                           return null;
                  }
         }

                           

         
         /**
          * This method will read and load the parameter from the XML File that are spesitic to the layer 
          * @param layerNode 
          */
         abstract void readNode(Node layerNode);
      
         //implement save return (for design)
         
         
}
