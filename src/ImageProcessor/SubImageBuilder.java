package ImageProcessor;

import Exceptions.XMLExeptions.XMLErrorInModelException;
import static ImageProcessor.ImageTransformer.createTransformer;
import ResourcesManager.XmlManager;
import static ResourcesManager.XmlManager.getStringAttribute;
import AppInterface.DesignBuilder;
import AppInterface.DesignInterfaceLinker;
import AppInterface.InterfaceNode;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 *
 * @author Camille LECOURT
 */
public class SubImageBuilder extends ImageDimentioner {
        
        
        
        public SubImageBuilder(DesignNode upperDE, Element elt ) throws XMLErrorInModelException {
                super(upperDE, elt);
                generateFromElement();
        }

        

        @Override
        public void DRYUpdate() {
                this.imageOut=this.getLowerDN(Layer.class).getImageOut();
        }

        @Override
        protected void generateFromElement() throws XMLErrorInModelException {
                 String key;
                Element subElt,subSubElt;
                DesignNode currentUpperDN;
                
         
                 currentUpperDN=this;
        //        subElt = (Element) elt.getElementsByTagName("Layers").item(0);
                NodeList nodeTransformersList = elt.getChildNodes();

                //running in the inverse 
                for (int i = nodeTransformersList.getLength()-1; i >=0; i--) {
                        if (nodeTransformersList.item(i).getNodeType() == Node.ELEMENT_NODE) { //To avoid text node and comment node

                                subSubElt = (Element) nodeTransformersList.item(i);
                                key = subSubElt.getNodeName(); // key for defining the layer and the Interface
                                try{
                                   currentUpperDN = createTransformer(key, currentUpperDN, subSubElt);
                                }catch(XMLErrorInModelException ex){
                                            Logger.getLogger(DesignBuilder.class.getName()).log(Level.SEVERE, null, ex);
                                }
                                
                        }
                }
                
                
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
