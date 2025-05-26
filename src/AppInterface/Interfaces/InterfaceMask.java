package AppInterface.Interfaces;

import AppInterface.DesignInterfaceLinker;
import AppInterface.InterfaceNode;
import ResourcesManager.XmlChild;

/**
 *
 * @author Camille LECOURT
 */
public class InterfaceMask extends VoidInterface {

        public InterfaceMask(InterfaceNode upperIN, String name) {
                super(upperIN, name);
        }

        @Override
        public XmlChild DRYsaveDesign() {
                XmlChild Xmloffset = new XmlChild(DesignInterfaceLinker.getIdentifier(this.getClass()));
                return Xmloffset;
        }
}
