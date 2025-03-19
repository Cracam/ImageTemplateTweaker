package ImageProcessor.ImageGenerators;

import AppInterface.Interfaces.InterfaceCustomText;
import AppInterface.Interfaces.InterfaceFixedTextCustomStyle;
import Exceptions.XMLExeptions.XMLErrorInModelException;
import ImageProcessor.DesignNode;
import ImageProcessor.ImageBuilder;
import ImageProcessor.ImageGenerator;
import ImageProcessor.Layer;
import Layers.SubClasses.PosFloat;
import static ResourcesManager.XmlManager.extractSingleElement;
import static ResourcesManager.XmlManager.getFloatAttribute;
import static ResourcesManager.XmlManager.getStringAttribute;
import org.w3c.dom.Element;
import textinimagegenerator.TextInImageGenerator;

/**
 *
 * @author Camille LECOURT
 */
public class GeneratorFixedTextCustomStyle extends ImageGenerator {

        private int[][] opacityMap;
        private String text;
        
           protected float textHeightFactor; //in mm
        protected float textSizeMin; //in mm
        protected float textSizeMax;//in mm


        public GeneratorFixedTextCustomStyle(DesignNode upperDE, Element elt) throws XMLErrorInModelException {
                super(upperDE, elt);
                 
        }

        @Override
        protected void generateFromElement() throws XMLErrorInModelException {
                Element subElt = extractSingleElement(elt.getElementsByTagName("Text"));
                this.textHeightFactor = getFloatAttribute(subElt, "TextHeightFactor", (float) 1.0);
                this.textSizeMin = getFloatAttribute(subElt, "TextSizeMin", (float) 2.0);
                this.textSizeMax = getFloatAttribute(subElt, "TextSizeMax", (float) 7.0);
                this.text = getStringAttribute(subElt, "Text", "Pas de Texte définit");

             
        }

        public String getText() {
                return text;
        }
        
              @Override
        public void DRYUpdate() {
                float pixelMillimeterFactor = this.getUpperDN(ImageBuilder.class).getDesignBuilder().getPixelMmFactor();
                this.imageOut = ((InterfaceFixedTextCustomStyle) (this.getLinkedinterface())).getImageOut(pixelMillimeterFactor, textSizeMin, textSizeMax);
                PosFloat offset = new PosFloat(0, 0);

                float x = -this.imageOut.getWidth() / 2 / pixelMillimeterFactor;
                offset.setPos_x(x);

                TextInImageGenerator textGen = ((InterfaceFixedTextCustomStyle) this.getLinkedinterface()).getTextGenerator();
                float y = (float)-  (textSizeMin + (textSizeMax - textSizeMin) * textGen.getTextSizeValue());
                y = (float) (y - textGen.getTextHeigthValue() * this.textHeightFactor);
                offset.setPos_y(y);

                this.getUpperDN(Layer.class).setAnOffset(name, offset);
        }

}
