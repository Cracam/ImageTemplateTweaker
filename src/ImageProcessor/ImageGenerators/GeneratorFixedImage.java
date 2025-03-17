package ImageProcessor.ImageGenerators;

import ImageProcessor.ImageGenerator;
import Exceptions.ResourcesFileErrorException;
import Exceptions.XMLExeptions.XMLErrorInModelException;
import ImageProcessor.DesignNode;
import ImageProcessor.ImageBuilder;
import static ResourcesManager.XmlManager.extractSingleElement;
import static ResourcesManager.XmlManager.getStringAttribute;
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

       public GeneratorFixedImage(DesignNode upperDE,Element elt ) throws XMLErrorInModelException {
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
        protected void generateFromElement() throws XMLErrorInModelException {
            //  this.imageName = elt.getElementsByTagName("Image").item(0).getAttributes().getNamedItem("image_name").getNodeValue();
               Element subElt = extractSingleElement(elt.getElementsByTagName("Image"));
                 this.imageName = getStringAttribute(subElt, "image_name", "ERROR");
        }

    

     
}
