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
        
        protected void giveVBox(VBox containerVbox){
                this.containerVbox = containerVbox;
        }

        @Override
        protected Element DRYLoadDesign(Element element, int index) throws XMLErrorInModelException {
                return null;
        }

        @Override
        public void placeInterface(InterfaceNode lowerInerface) {
                        this.containerVbox.getChildren().add(0, lowerInerface);
        }

        @Override
        public String DRYComputeUniqueID() {
                return DesignInterfaceLinker.getIdentifier(this.getClass()) + this.getName();
        }

    
}
