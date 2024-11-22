package Layers;

import ResourcesManager.ResourcesManager;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 *
 * @author camil
 */
public class LayerFixedImage extends Layer {
        private String imageName;
        
        public LayerFixedImage(String layerName, ResourcesManager modelResources, ResourcesManager designResources) {
                super(layerName, modelResources, designResources);
        }

        
        @Override
        BufferedImage generateImageget() {
                try {
                        File imageFile = this.modelResources.get(this.imageName);

                        return ImageIO.read(imageFile);
                } catch (IOException ex) {
                        Logger.getLogger(LayerFixedImage.class.getName()).log(Level.SEVERE, null, ex);
                        return null;
                }
        }
        
        
        /**
         * No interface nothing to set
         */
        @Override
        void initialiseInterface() {
        }

                        

        /**
         * this layer don't hava any parameter defined by the user (image getraw is already save in Layer.
         * @return 
         */
        @Override
        Node getLayerParameter() {
                return null;
        }

        /**
         * Fixed image does not need any parameter different from pos and sise so this function nned tio be keep void
         * @param layerNode 
         */
        @Override
        void readNode(Element paramNode) {
                this.imageName =  paramNode.getElementsByTagName("Image").item(0).getAttributes().getNamedItem("image_name").getNodeValue();
        }


         
}
