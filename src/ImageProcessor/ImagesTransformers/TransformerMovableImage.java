package ImageProcessor.ImagesTransformers;

import AppInterface.Interfaces.InterfaceMouvableImage;
import Exceptions.XMLExeptions.XMLErrorInModelException;
import ImageProcessor.DesignNode;
import ImageProcessor.ImageTransformer;
import ImageProcessor.Layer;
import ImageProcessor.SubClasses.PosFloat;
import ImageProcessor.SubClasses.QuadrupletFloat;
import static ResourcesManager.XmlManager.extractSingleElement;
import static ResourcesManager.XmlManager.getFloatAttribute;
import java.awt.image.BufferedImage;
import org.w3c.dom.Element;

/**
 *
 * @author camil
 */
public class TransformerMovableImage extends ImageTransformer {

        private float X_MaxOffset;
        private float X_MinOffset ;
        private float Y_MaxOffset;
        private float Y_MinOffset ;

        private  float posRefX;
        private  float posRefY;

        public TransformerMovableImage(DesignNode upperDE, Element elt) throws XMLErrorInModelException {
                super(upperDE, elt);
               DRYRefreshDPI();
        }


        
        @Override
        protected void generateFromElement() throws XMLErrorInModelException {

                Element subElt = extractSingleElement(elt.getElementsByTagName("OffSet"));
                this.X_MaxOffset = getFloatAttribute(subElt, "X_MaxOffset", (float) 1.0);
                this.X_MinOffset = getFloatAttribute(subElt, "X_MinOffset", (float) -1.0);
                this.Y_MaxOffset = getFloatAttribute(subElt, "Y_MaxOffset", (float) 1.0);
                this.Y_MinOffset = getFloatAttribute(subElt, "Y_MinOffset", (float) -1.0);
        }

        @Override
        public void DRY_DRYUpdate(BufferedImage img_in) {
                this.imageOut=img_in;
               
                 
                Layer layer = this.getUpperDN(Layer.class);

                float offsetX;
                float rawOffestX = ((InterfaceMouvableImage) this.linkedinterface).getSliderXValue();
                float rawOffestY = ((InterfaceMouvableImage) this.linkedinterface).getSliderYValue();

                if (rawOffestX > 0) {
                        offsetX = this.X_MaxOffset * rawOffestX;
                } else {
                        offsetX = -this.X_MinOffset * rawOffestX;
                }

                float offsetY;
                if (rawOffestY > 0) {
                        offsetY = this.Y_MaxOffset * rawOffestY;
                } else {
                        offsetY = -this.Y_MinOffset * rawOffestY;
                }
                
          //      QuadrupletFloat posSize = new QuadrupletFloat(posRefX + offsetX, posRefY + offsetY, layer.getPosSize().getSize_x(), layer.getPosSize().getSize_y());
//                layer.setPosSize(posSize);
//                layer.DRYRefreshDPI();
                     //System.out.println("gggggg : "+offsetX+"       "+offsetY);

                PosFloat pos = new PosFloat(offsetX, offsetY);
    layer.setAnOffset("MouvableTransformer",pos);
           //     layer.update();
                
        }
        
         @Override
        public void DRYRefreshDPI() {
                  Layer layer = this.getUpperDN(Layer.class);
               posRefX = layer.getPosSize().getPos_x();
                posRefY = layer.getPosSize().getPos_y();
        }
}
