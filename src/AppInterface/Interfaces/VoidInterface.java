package AppInterface.Interfaces;

import AppInterface.DesignBuilder;
import AppInterface.InterfaceNode;
import Exceptions.XMLExeptions.XMLErrorInModelException;
import ResourcesManager.XmlChild;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.w3c.dom.Element;

/**
 *
 * @author Camille LECOURT
 */
public abstract class VoidInterface extends InterfaceNode {

        public VoidInterface(InterfaceNode upperIN, String name) {
                super(upperIN, name);
                //      upperInterface.placeInterface(this);
        }

        @Override
        protected void DRYLoadDesign(Element element) throws XMLErrorInModelException {//no interface
        }

        @Override
        public int loadDesign(Element element, int index) throws XMLErrorInModelException {
//                this.DRYLoadDesign((Element) element.getChildNodes().item(index));
               index=index+1;

                try {
                        for (InterfaceNode lInter : this.getLowerInterfaces()) {
                                index = lInter.loadDesign(element, index);
                        }
                } catch (XMLErrorInModelException ex) {
                        Logger.getLogger(DesignBuilder.class.getName()).log(Level.SEVERE, null, ex);
                }
                return index;
        }

             public abstract XmlChild DRYsaveDesign();
                
       
        @Override
        protected void initialiseInterface() {//no interface
        }

}
