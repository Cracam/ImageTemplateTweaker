package AppInterface;

import Exceptions.XMLExeptions.XMLErrorInModelException;
import javafx.scene.layout.VBox;
import org.w3c.dom.Element;

/**
 *
 * @author Camille LECOURT
 */
public abstract class InterfaceContainer extends InterfaceNode {

     

        private  VBox containerVbox;

        public InterfaceContainer( InterfaceNode upperIN,String name) {
                super(upperIN,name);
        }
        
        protected void setContainerVBox(VBox containerVbox){
                this.containerVbox = containerVbox;
        }

        @Override
        protected void DRYLoadDesign(Element element) throws XMLErrorInModelException {
        }

        @Override
        public void placeInterface(InterfaceNode lowerInerface) {
                        this.containerVbox.getChildren().add(0, lowerInerface);
        }



        public void delete(InterfaceNode InterfaceNodeToDelete) {
                if (lowerInterfaces.contains(InterfaceNodeToDelete)) {
                        lowerInterfaces.remove(InterfaceNodeToDelete);
                                containerVbox.getChildren().remove(InterfaceNodeToDelete);

                        System.out.println("DesignNodeToDelete a été supprimé de lowersDN.");
                } else {
                        System.out.println("DesignNodeToDelete n'est pas présent dans lowersDN.");
                }
        }
    
}
