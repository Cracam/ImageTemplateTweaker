package ImageProcessor.ImagesTransformers;

import AppInterface.Interfaces.InterfaceCustomColor;
import Exceptions.XMLExeptions.XMLErrorInModelException;
import ImageProcessor.DesignNode;
import ImageProcessor.ImageTransformer;
import java.awt.image.BufferedImage;
import org.w3c.dom.Element;

/**
 *
 * @author Camille LECOURT
 */
public  class TransformerFixedColor extends ImageTransformer {


        private int[][]opacityMap;
        private BufferedImage image_getRaw;

        public TransformerFixedColor(DesignNode upperDE, Element elt) throws XMLErrorInModelException {
                super(upperDE, elt);
        }

  

        @Override
        protected void generateFromElement() throws XMLErrorInModelException {
        }
        
        
        @Override
        public void DRY_DRYUpdate(BufferedImage img_in) {
                if(image_getRaw!=img_in){
                        image_getRaw=img_in;
                        opacityMap=staticFunctions.StaticImageEditing.transformToOpacityArray(img_in);
                }
                this.imageOut=((InterfaceCustomColor) (this.linkedinterface)).getImageOut(opacityMap);
        }




   

}
