package ImageProcessor;

import Exceptions.XMLExeptions.GetAttributeValueException;
import static ImageProcessor.ImageTransformer.createTransformer;
import ResourcesManager.XmlManager;
import static ResourcesManager.XmlManager.getStringAttribute;
import AppInterface.DesignBuilder;
import AppInterface.DesignInterfaceLinker;
import AppInterface.InterfaceNode;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 *
 * @author Camille LECOURT
 */
public class ImageBuilder extends ImageDimentioner {
        DesignBuilder designBuilder;
        
        
        
        public ImageBuilder(DesignNode upperDE, Element elt,DesignBuilder designBuilder) throws GetAttributeValueException {
                super(upperDE, elt);
                this.designBuilder=designBuilder;
        }

        @Override
        public void DRYUpdate() {
                this.imageOut=this.getLowerDN(Layer.class).getImageOut();
        }

        @Override
        public void DRYgenerateFromElement(Element elt) throws GetAttributeValueException {
                 String key;
                Element subElt,subSubElt;
                DesignNode currentUpperDN;
                
         
                 currentUpperDN=this;
                subElt = (Element) elt.getElementsByTagName("Layers").item(0);
                NodeList nodeTransformersList = subElt.getChildNodes();

                //running in the inverse 
                for (int i = nodeTransformersList.getLength()-1; i >=0; i--) {
                        if (nodeTransformersList.item(i).getNodeType() == Node.ELEMENT_NODE) { //To avoid text node and comment node

                                subSubElt = (Element) nodeTransformersList.item(i);
                                key = subSubElt.getNodeName(); // key for defining the layer and the Interface

                                currentUpperDN = createTransformer(key, currentUpperDN, subSubElt);
                        }
                }
                
                
        }
        
        
        
        public DesignBuilder getDesignBuilder(){
                return this.designBuilder;
        }

     
               @Override
        public String DRYComputeUniqueID() {
                return DesignInterfaceLinker.getIdentifier(this.getClass()) + name;
        }

        @Override
        public InterfaceNode createLinkedInterface(InterfaceNode upperInter) {
                throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
        }
        
}
