

package interfaces;

import Layers.LayerCustomColor;
import Layers.LayerCustomImage;
import Layers.LayerFixedImage;
import ResourcesManager.ResourcesManager;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
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
         
        public static final Map<String, Class<? extends Interface>> layersTypesMap = Map.of("Fixed_Image", LayerFixedImage.class, "Custom_Image", LayerCustomImage.class,"Custom_Color", LayerCustomColor.class);

        
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
         */
        public abstract void loadInterfaceData();

        
        
        
        
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

        
}
