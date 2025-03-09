package AppInterface;

import Exceptions.XMLExeptions.GetAttributeValueException;
import javafx.scene.layout.VBox;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 *
 * @author Camille LECOURT
 */
public class InterfaceContainer extends InterfaceNode {

        private final String name;

        private final VBox containerVbox;

        public InterfaceContainer(String name, InterfaceNode upperIN, VBox containerVbox) {
                super(upperIN);
                this.name = name;
                this.containerVbox = containerVbox;
        }

        @Override
        protected Element DRYLoadDesign(Element element, int index) throws GetAttributeValueException {
                throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
        }

        @Override
        public void placeInterface(InterfaceNode lowerInerface) {
                this.containerVbox.getChildren().add(lowerInerface);
        }

        @Override
        public String DRYComputeUniqueID() {
                return DesignInterfaceLinker.getIdentifier(this.getClass()) + name;
        }


        @Override
        public Element DRYsaveDesign(Document doc) {
                throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
        }

        @Override
        protected void initialiseInterface() {
                throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
        }

}
