package ImageProcessor.ImagesTransformers;

import Exceptions.XMLExeptions.XMLErrorInModelException;
import ImageProcessor.DesignNode;
import ImageProcessor.ImageTransformer;
import java.awt.image.BufferedImage;
import org.w3c.dom.Element;

/**
 * this transformer do nothing and is just use to create an stop for interfacecreation
 * @author Camille LECOURT
 */
public  class TransformerInert extends ImageTransformer {

        public TransformerInert(DesignNode upperDE, Element elt) throws XMLErrorInModelException {
                super(upperDE, elt);
        }

  

        @Override
        protected void generateFromElement() throws XMLErrorInModelException {
        }
        
        
        @Override
        public void DRY_DRYUpdate(BufferedImage img_in) {
                this.imageOut=img_in;
        }

}
