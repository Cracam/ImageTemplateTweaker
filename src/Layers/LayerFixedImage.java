package Layers;

import Exceptions.ResourcesFileErrorException;
import Layers.SubClasses.QuadrupletFloat;
import ResourcesManager.ResourcesManager;
import ResourcesManager.XmlManager;
import imageBuilder.ImageBuilder;
import interfaces.Interface;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import staticFunctions.StaticImageEditing;

/**
 *
 * @author camil
 */
public class LayerFixedImage extends Layer {

        private String imageName;

        public LayerFixedImage(String layerName, ResourcesManager modelResources, Interface layerInterface, ImageBuilder linkedImageBuilder, QuadrupletFloat posSize) {
                super(layerName, modelResources, layerInterface, linkedImageBuilder, posSize);
        }

     

        @Override
        public void refreshImageGet() {
                try {
                        File imageFile = this.modelResources.get(this.imageName);

                        if (imageFile == null) {
                                throw new ResourcesFileErrorException("This file dont exist : " + this.imageName);
                        }
                         this.image_get= StaticImageEditing.ResizeImage(ImageIO.read(imageFile), this.pixelPosSize.getSize_x(), this.pixelPosSize.getSize_y());
                } catch (IOException | ResourcesFileErrorException ex) {
                        Logger.getLogger(LayerFixedImage.class.getName()).log(Level.SEVERE, null, ex);
                }

        }

       


        @Override
       public  void readNode(Element paramNode) {
                this.imageName = paramNode.getElementsByTagName("Image").item(0).getAttributes().getNamedItem("image_name").getNodeValue();
        }

        @Override
        public void refreshPreview() {
        }

        @Override
        public void DPIChanged() {
        }

     
}
