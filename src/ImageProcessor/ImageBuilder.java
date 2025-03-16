package ImageProcessor;

import Exceptions.XMLExeptions.XMLErrorInModelException;
import AppInterface.DesignBuilder;
import AppInterface.DesignInterfaceLinker;
import AppInterface.InterfaceNode;
import static ResourcesManager.XmlManager.getFloatAttribute;
import static ResourcesManager.XmlManager.getStringAttribute;
import interfaces.Interface;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 *
 * @author Camille LECOURT
 */
public class ImageBuilder extends ImageDimentioner {

        private final DesignBuilder designBuilder;

        public ImageBuilder(DesignNode upperDE, Element elt, DesignBuilder designBuilder) throws XMLErrorInModelException {
                //  this.designBuilder=designBuilder;
                super(upperDE, elt);
                this.designBuilder = designBuilder;
                generateFromElement();
                 DRYRefreshDPI();

        }

        @Override
        public void DRYUpdate() {
                this.imageOut = this.getLowerDN(Layer.class).getImageOut();
                this.designBuilder.refreshPreviewImageBox(this);
        }

        @Override
        protected void generateFromElement() throws XMLErrorInModelException {
                String key;
                Element subElt;
                DesignNode currentUpperDN;
                
                
                this.name = getStringAttribute(elt, "name", "ERROR");
                  x_size = getFloatAttribute(elt, "size_x", 0);
                y_size= getFloatAttribute(elt, "size_y", 0);

                currentUpperDN = this;
                //        subElt = (Element) elt.getElementsByTagName("Layers").item(0);
                NodeList nodeTransformersList = elt.getChildNodes();

                //running in the inverse 
                for (int i = nodeTransformersList.getLength() - 1; i >= 0; i--) {
                        if (nodeTransformersList.item(i).getNodeType() == Node.ELEMENT_NODE) { //To avoid text node and comment node
                                if (!"Layer".equals(nodeTransformersList.item(i).getNodeName())) {
                                        try {
                                                throw new XMLErrorInModelException("A XmlBloc named " + nodeTransformersList.item(i).getNodeName() + " in the imageBuilder : " + this.name + " xas detected ignoringit");
                                        } catch (XMLErrorInModelException ex) {
                                                Logger.getLogger(Interface.class.getName()).log(Level.WARNING , null, ex);
                                                //ex.printStackTrace(); // Print the stack trace
                                        }

                                } else {
                                        subElt = (Element) nodeTransformersList.item(i);
                                        //  key = subSubElt.getNodeName(); // key for defining the layer and the Interface
                                        try {
                                                currentUpperDN = new Layer(currentUpperDN, subElt);
                                                System.out.println( currentUpperDN.toString());
                                        } catch (XMLErrorInModelException ex) {
                                                Logger.getLogger(DesignBuilder.class.getName()).log(Level.WARNING , null, ex);
                                        }
                                }
                        }
                }

        }

        public DesignBuilder getDesignBuilder() {
         //       System.out.println("Design builder dammanded");
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
