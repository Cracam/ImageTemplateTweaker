

package interfaces;

import Exceptions.ThisInterfaceDoesNotExistException;
import Layers.Layer;
import ResourcesManager.ResourcesManager;
import designBuilder.DesignBuilder;
import imageBuilder.ImageBuilder;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.control.TitledPane;
import javafx.scene.image.ImageView;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import previewimagebox.PreviewImageBox;

/**
 *All those subclasses are made to edit an image with entry from it's interface and layer 
 * 
 * All have a getImageOut methods 
 * 
 * @author Camille LECOURT
 */
public abstract class Interface extends TitledPane{
        
        String interfaceName;
         DesignBuilder designBuilder;
         // this variable will be use by the Image builder to detect a change and recompute the image accordingly.
        
           ArrayList<ImageBuilder>  linkedImagesBuilders=new ArrayList<>();
           ArrayList<Layer> linkedLayers=new ArrayList<>();
         
        //Contain all the key to the identify an interface
        public static final Map<String, Class<? extends Interface>> interfacesTypesMap = Map.of(
                "Fixed_Image", InterfaceFixedImage.class, 
                "Custom_Image",  InterfaceCustomImage.class,
                "Custom_Color", InterfaceCustomColor.class,
                "Custom_Text",InterfaceCustomText.class,
                "Fixed_Text_Custom_Color_Custom_Style", InterfaceFixedTextCustomStyleCustomColor.class,
                "Mouvable_Fixed_Image", InterfaceMouvableFixedImage.class
        );
        
        boolean haveGraphicInterface=true ;

        
        Interface(String interfaceName, DesignBuilder designBuilder){
                this.interfaceName=interfaceName;
                this.designBuilder=designBuilder;
                this.initialiseInterface();
        }
        
        
        
        /**
         * Load the interface
         */
        protected abstract void initialiseInterface();
        
        
        
        /**
         * This function will return a node that will allow to save the data of the interface
         * @param doc
         * @return 
         */
        public abstract Node saveInterfaceData(Document doc);
        
        /**
         * This will load the data from the XML file
         * @param dataOfTheLayer
         */
        public abstract void loadInterfaceData(Element dataOfTheLayer);

        
        
        
        
      
        
        /**
         * Refresh the Preview using the image builder name as a identifier
         * @param imageBuilderName 
         * @param previewImage 
         */
        public abstract void refreshPreview(String imageBuilderName, ImageView previewImage);
        
       /**
        * Intermediate for the preview refresh
        * @param imageBuilderName
        * @param previewImage
        * @param previewImageBox 
        */
        static void refreshPreviewIntermediate(String imageBuilderName, ImageView previewImage, PreviewImageBox previewImageBox){
                previewImageBox.setImageView(imageBuilderName, previewImage);
        }
        
        

        
        /**
         * this method refresh all the layer that are kinked to the interface.
         */
        void refreshLayers() {
                for (int i = 0; i < linkedLayers.size(); i++) {
                        linkedLayers.get(i).refreshImageGet();
                        linkedLayers.get(i).refreshPreview();
                        linkedLayers.get(i).setChanged(true);
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
         * @param designBuilder
         * @return
         * @throws ThisInterfaceDoesNotExistException 
         */
        public static Interface createInterface(String interfaceType, String interfaceName, DesignBuilder designBuilder) throws ThisInterfaceDoesNotExistException {

                if (!interfacesTypesMap.containsKey(interfaceType)) {
                        throw new ThisInterfaceDoesNotExistException("This interface type does not exist : " + interfaceType);
                }
                try {

                        Class<? extends Interface> subclass = interfacesTypesMap.get(interfaceType);
                        Constructor<? extends Interface> constructor = subclass.getConstructor(String.class, DesignBuilder.class);

                        return constructor.newInstance(interfaceName, designBuilder);

                } catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException ex) {
                        Logger.getLogger(Interface.class.getName()).log(Level.SEVERE, null, ex);
                }

                return null;
        }

        public String getInterfaceName() {
                return interfaceName;
        }

        public void linkNewLayer(Layer layer){
                this.linkedLayers.add(layer);
        }
        
        public void linkNewImageBuilder(ImageBuilder imageBuilder) {
                // Check if the imageBuilder already exists in the linkedImagesBuilders list
                if (!this.linkedImagesBuilders.contains(imageBuilder)) {
                        // If it does not exist, add it to the list
                        this.linkedImagesBuilders.add(imageBuilder);
                }
        }

        public boolean isHaveGraphicInterface() {
                return haveGraphicInterface;
        }

        
        
        
}
