package ImageProcessor.ImageGenerators;

import ImageProcessor.ImageGenerator;
import Exceptions.XMLExeptions.GetAttributeValueException;
import ResourcesManager.XmlManager;
import interfaces.InterfaceFixedTextCustomStyleCustomColor;
import org.w3c.dom.Element;

/**
 *
 * @author Camille LECOURT
 */
public class GeneratorFixedTextCustomStyleCustomColor extends ImageGenerator {
        private int[][] opacityMap;
        private String text;
       
          public GeneratorFixedTextCustomStyleCustomColorrDE,Element elt) throws GetAttributeValueException {
                super(upperDE,elt);
        }


    
   /**
         * 
         * @param paramNode
         */
        @Override
        public void readNode(Element paramNode) {
              Element element=  (Element) paramNode.getElementsByTagName("Text").item(0);
              
                this.textHeightFactor=XmlManager.getFloatAttribute(element,"TextHeightFactor",this.textHeightFactor);
                this.textSizeMin=XmlManager.getFloatAttribute(element,"TextSizeMin",this.textSizeMin);
                this.textSizeMax=XmlManager.getFloatAttribute(element,"TextSizeMax",this.textSizeMax);
                 this.text=XmlManager.getStringAttribute(element,"Text",this.text);
                 ((InterfaceFixedTextCustomStyleCustomColor) (this.linkedInterface)).getTextGenerator().setText(this.text);
        }         

        
}
