

package interfaces;

import Layers.Layer;
import Layers.LayerCustomColor;
import ResourcesManager.ResourcesManager;
import imageBuilder.ImageBuilder;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 *All those subclasses are made to edit an image with entry from it's interface and layer 
 * 
 * All have a getImageOut methods 
 * 
 * @author Camille LECOURT
 */
public abstract class Interface {
        
        String interfaceName;
        String tabName;
         ResourcesManager designResources;
         // this variable will be use by the Image builder to detect a change and recompute the image accordingly.
        
           ArrayList<ImageBuilder>  linkedImagesBuilders=new ArrayList<>();
           ArrayList<Layer> linkedLayers=new ArrayList<>();
         
        public static final Map<String, Class<? extends Interface>> layersTypesMap = Map.of("Fixed_Image", InterfaceFixedImage.class, "Custom_Image",  InterfaceCustomImage.class,"Custom_Color", InterfaceCustomColor.class);

        
        Interface(String interfaceName,String tabName, ResourcesManager designResources){
                this.interfaceName=interfaceName;
                this.tabName=tabName;
                this.designResources=designResources;
                this.initialiseInterface();
        }
        
        
        
        /**
         * Load the interface
         */
        protected abstract void initialiseInterface();
        
        
        
        /**
         * This function will return a node that will allow to save the data of the interface
         * @return 
         */
        public abstract Node saveInterfaceData();
        
        /**
         * This will load the data from the XML file
         * @param dataOfTheLayer
         */
        public abstract void loadInterfaceData(Element dataOfTheLayer);

        
        
        
        
        public void setDesignResources(ResourcesManager designResources) {
                this.designResources = designResources;
        }
        
        
        
/**
 * This method create an interface with the key (name of the node in the XML)
 * @param key
         * @param name
         * @param tabName
         * @param designResources
 * @return 
 */        
        public static Interface createInterface(String key,String name, String tabName, ResourcesManager designResources)  {

                try {
                        Class<? extends Interface> subclass = layersTypesMap.get(key);
                        Constructor<? extends Interface> constructor = subclass.getConstructor(String.class, String.class, ResourcesManager.class);
                        return constructor.newInstance(name, tabName,  designResources);
                        
                } catch (NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
                        Logger.getLogger(Interface.class.getName()).log(Level.SEVERE, null, ex);
                        return null;
                }
        }
        
        
        
        
        
        
        
        
        
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
         * this method refresh all the layer that are kinked to the interface.
         */
        void refreshLayers() {
                for (int i = 0; i < linkedLayers.size(); i++) {
                        linkedLayers.get(i).refreshImageGet();
                }
        }
        
          /**
         * this method refresh all the layer that are kinked to the interface.
         */
        void refreshImageBuilders() {
                for (int i = 0; i < linkedImagesBuilders.size(); i++) {
                        linkedImagesBuilders.get(i).refresh();
                }
        }
        
}
