/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package AppInterface;

import AppInterface.Interfaces.VoidInterface;
import Exceptions.XMLExeptions.XMLErrorInModelException;
import ImageProcessor.DesignNode;
import ImageProcessor.ImageBuilder;
import ResourcesManager.ResourcesManager;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.layout.VBox;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 *
 * @author Camille LECOURT
 */
public abstract class InterfaceNode extends VBox {

        private final String name;

        protected InterfaceNode upperInterface;
        private final ArrayList<InterfaceNode> lowerInterfaces = new ArrayList<>();
        protected final ArrayList<DesignNode> linkedDesignNodes = new ArrayList<>();

        // Constructeur prenant un seul upperDN
        public InterfaceNode(InterfaceNode upperIN, String name) {
                this.name = name;
                this.upperInterface = upperIN;

                initialiseInterface();
                if (upperInterface != null) {
                        upperInterface.addLowerIN(this);

                }

        }

        public String ComputeUniqueID() {
                String ret = "";
                this.DRYComputeUniqueID();
                for (InterfaceNode lInter : lowerInterfaces) {
                        ret = ret + lInter.ComputeUniqueID();
                }
                return ret;
        }

        public String DRYComputeUniqueID() {
                return DesignInterfaceLinker.getIdentifier(this.getClass());
        }

        /**
         * Schearch the next nearest interface container in the tree
         *
         * @param lowerInerface
         */
        public void placeInterface(InterfaceNode lowerInerface) {
                if (upperInterface != null) {
                        upperInterface.placeInterface(lowerInerface);
                }
        }

        public void addLowerIN(InterfaceNode lowerIN) {
                lowerInterfaces.add(lowerIN);
        }

        protected abstract Element DRYLoadDesign(Element element, int index) throws XMLErrorInModelException;

        public void loadDesign(Element element, int index) throws XMLErrorInModelException {
                Element subelt = DRYLoadDesign(element, index);
                index++;
                if (subelt != element) {
                        index = 0;
                }
                try {
                        for (InterfaceNode lInter : lowerInterfaces) {
                                lInter.DRYLoadDesign(subelt, index);
                        }
                } catch (XMLErrorInModelException ex) {
                        Logger.getLogger(DesignBuilder.class.getName()).log(Level.SEVERE, null, ex);
                }

        }

        public void updateLinkedDesignNodes() {
                for (DesignNode DN : linkedDesignNodes) {
                        DN.update();
                }
        }

        public void addDesignNode(DesignNode DN) {
                linkedDesignNodes.add(DN);
                DN.setLinkedInterface(this);
        }

        public ArrayList<InterfaceNode> getLowerInterfaces() {
                return lowerInterfaces;
        }

        public NodeList saveDesign(NodeList nodelist) {

                throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody

        }

        public abstract Element DRYsaveDesign(Document doc);

        protected abstract void initialiseInterface();

        public InterfaceNode getUpperDN(Class<?> nodeClass) {
                if (upperInterface == null) {
                        return null;
                }

                if (upperInterface.getClass() == nodeClass) {
                        return upperInterface;
                } else {
                        InterfaceNode result = upperInterface.getUpperDN(nodeClass);
                        if (result != null) {
                                return result;
                        }
                }

                return null;
        }

        public ResourcesManager getDesignRessources() {
                if (!linkedDesignNodes.isEmpty()) {
                        return ((ImageBuilder) linkedDesignNodes.get(0).getUpperDN(ImageBuilder.class)).getDesignBuilder().getDesignResources();
                } else {
                        return null;
                }

        }

        public ArrayList<DesignNode> getLinkedDesignNodes() {
                return linkedDesignNodes;
        }

        public String getName() {
                return name;
        }

      
        /**
         * this return all the class that are sub classes of the entry
         *
         * @return
         */
        public boolean interfaceBranchContainOnlyVoidInterfaces() {
              

                for (InterfaceNode lInter : lowerInterfaces) {
                         if (lInter.getClass() != VoidInterface.class) {
                                return false;
                        }
                         
                        if (lInter.interfaceBranchContainOnlyVoidInterfaces() == false) {
                                return false;
                        }
                }
                
                return true; // Retourne null si aucun élément correspondant n'est trouvé
        }
}
