package ImageProcessor.ImageGenerators;

import ImageProcessor.ImageGenerator;
import Exceptions.ResourcesFileErrorException;
import Exceptions.XMLExeptions.GetAttributeValueException;
import ImageProcessor.DesignNode;
import ImageProcessor.ImageBuilder;
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
        public void DRYUpdate() {
               try {
                        File imageFile = getModelRessources().get(this.imageName);

                        if (imageFile == null) {
                                throw new ResourcesFileErrorException("This file dont exist : " + this.imageName);
                        }
                         this.imageOut= StaticImageEditing.ResizeImage(ImageIO.read(imageFile), this.x_p_size, this.y_p_size);
                } catch (IOException | ResourcesFileErrorException ex) {
                        Logger.getLogger(GeneratorFixedImage.class.getName()).log(Level.SEVERE, null, ex);
                }
        }

        @Override
        public void DRYgenerateFromElement(Element elt) throws GetAttributeValueException {
              this.imageName = elt.getElementsByTagName("Image").item(0).getAttributes().getNamedItem("image_name").getNodeValue();
        }

    

     
}
