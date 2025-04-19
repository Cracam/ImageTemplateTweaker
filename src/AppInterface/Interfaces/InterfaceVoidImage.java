package AppInterface.Interfaces;

import AppInterface.DesignInterfaceLinker;
import AppInterface.InterfaceNode;
import ResourcesManager.XmlChild;

/**
 *
 * @author Camille LECOURT
 */
public class InterfaceVoidImage extends VoidInterface {

        public InterfaceVoidImage(InterfaceNode upperIN, String name) {
                super(upperIN, name);
        }

        @Override
        public XmlChild DRYsaveDesign() {
                XmlChild Xmloffset = new XmlChild(DesignInterfaceLinker.getIdentifier(this.getClass()));
                return Xmloffset;
        }

}
