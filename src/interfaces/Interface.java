

package interfaces;

import Exceptions.ThisInterfaceDoesNotExistException;
import Layers.Layer;
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
         ResourcesManager designResources;
         // this variable will be use by the Image builder to detect a change and recompute the image accordingly.
        
           ArrayList<ImageBuilder>  linkedImagesBuilders=new ArrayList<>();
           ArrayList<Layer> linkedLayers=new ArrayList<>();
         
        //Contain all the key to the identify an interface
        public static final Map<String, Class<? extends Interface>> interfacesTypesMap = Map.of(
                "Fixed_Image", InterfaceFixedImage.class, 
                "Custom_Image",  InterfaceCustomImage.class,
                "Custom_Color", InterfaceCustomColor.class
        );

        
        Interface(String interfaceName, ResourcesManager designResources){
                this.interfaceName=interfaceName;
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
        
        
        
        
        
        
        
        /**
         * This code generate an interface using a String kay as an identifier
         * @param interfaceType
         * @param interfaceName
         * @param designResources
         * @return
         * @throws ThisInterfaceDoesNotExistException 
         */
        public static Interface createOrReturnInterface(String interfaceType, String interfaceName, ResourcesManager designResources) throws ThisInterfaceDoesNotExistException {

                if (!interfacesTypesMap.containsKey(interfaceType)) {
                        throw new ThisInterfaceDoesNotExistException("This interface type does not exist : " + interfaceType);
                }
                try {

                        Class<? extends Interface> subclass = interfacesTypesMap.get(interfaceType);
                        Constructor<? extends Interface> constructor = subclass.getConstructor(String.class, ResourcesManager.class);

                        return constructor.newInstance(interfaceName, designResources);

                } catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException ex) {
                        Logger.getLogger(Interface.class.getName()).log(Level.SEVERE, null, ex);
                }

                return null;
        }

}
