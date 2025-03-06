package ImageProcessor.ImageGenerators;

import ImageProcessor.ImageGenerator;
import Exceptions.ResourcesFileErrorException;
import Exceptions.XMLExeptions.GetAttributeValueException;
import ImageProcessor.DesignNode;
import ImageProcessor.Layer;
import Layers.Layer_old;
import Layers.SubClasses.QuadrupletFloat;
import ResourcesManager.ResourcesManager;
import imageBuilder.ImageBuilder_old;
import interfaces.Interface;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import org.w3c.dom.Element;
import staticFunctions.StaticImageEditing;

/**
 *
 * @author camil
 */
public class GeneratorFixedImage extends ImageGenerator {

        private String imageName;

       public GeneratorFixedImage(DesignNode upperDE,Element elt ) throws GetAttributeValueException {
                super(upperDE,elt);
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
                        Logger.getLogger(GeneratorFixedImage.class.getName()).log(Level.SEVERE, null, ex);
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
