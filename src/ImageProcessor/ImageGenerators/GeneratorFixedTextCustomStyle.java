package ImageProcessor.ImageGenerators;

import ImageProcessor.ImageGenerator;
import Exceptions.XMLExeptions.XMLErrorInModelException;
import ImageProcessor.DesignNode;
import ResourcesManager.XmlManager;
import interfaces.InterfaceFixedTextCustomStyleCustomColor;
import org.w3c.dom.Element;

/**
 *
 * @author Camille LECOURT
 */
public class GeneratorFixedTextCustomStyle extends ImageGenerator {
        private int[][] opacityMap;
        private String text;

        public GeneratorFixedTextCustomStyle(DesignNode upperDE, Element elt) throws XMLErrorInModelException {
                super(upperDE, elt);
        }
       
      


    
//   /**
//         * 
//         * @param paramNode
//         */
//        @Override
//        public void readNode(Element paramNode) {
//              Element element=  (Element) paramNode.getElementsByTagName("Text").item(0);
//              
//                this.textHeightFactor=XmlManager.getFloatAttribute(element,"TextHeightFactor",this.textHeightFactor);
//                this.textSizeMin=XmlManager.getFloatAttribute(element,"TextSizeMin",this.textSizeMin);
//                this.textSizeMax=XmlManager.getFloatAttribute(element,"TextSizeMax",this.textSizeMax);
//                 this.text=XmlManager.getStringAttribute(element,"Text",this.text);
//                 ((InterfaceFixedTextCustomStyleCustomColor) (this.linkedInterface)).getTextGenerator().setText(this.text);
//        }         

        @Override
        public void DRYUpdate() {
                throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
        }

        @Override
        public void DRYgenerateFromElement(Element elt) throws XMLErrorInModelException {
                throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
        }

        
}
