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
import ResourcesManager.XmlChild;
import ResourcesManager.XmlManager;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.layout.VBox;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 *
 * @author Camille LECOURT
 */
public abstract class InterfaceNode extends VBox {

        private final String name;

        protected InterfaceNode upperInterface;
        final ArrayList<InterfaceNode> lowerInterfaces = new ArrayList<>();
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

        protected abstract void DRYLoadDesign(Element element) throws XMLErrorInModelException;

        public int loadDesign(Element element, int index) throws XMLErrorInModelException {
                this.DRYLoadDesign((Element) element.getChildNodes().item(index));
                index = index + 1;
                System.out.println(this.getClass().getName() + "   index : " + index);
                try {
                        for (InterfaceNode lInter : lowerInterfaces) {
                                index = lInter.loadDesign(element, index);
                        }
                } catch (XMLErrorInModelException ex) {
                        Logger.getLogger(DesignBuilder.class.getName()).log(Level.SEVERE, null, ex);
                }
                return index;
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

        /**
         * this program save the design info of the current interface and order
         * to do it on the subinterface
         *
         * the fisrt will be launch in the Interface manager for each
         * layersContainer
         *
         * @param managerIn
         * @return
         */
        public XmlManager saveDesign(XmlManager managerIn) {
                XmlChild child = this.DRYsaveDesign();
                if (child != null) {
                        managerIn.addChild(child);
                }

                for (int i = 0; i < lowerInterfaces.size(); i++) {
                        lowerInterfaces.get(i).saveDesign(managerIn);
                }
                return managerIn;
        }

        public abstract XmlChild DRYsaveDesign();

        protected abstract void initialiseInterface();

        public InterfaceNode getUpperIN() {
                return getUpperIN(null);
        }

        public InterfaceNode getUpperIN(Class<?> nodeClass) {
                if (upperInterface == null) {
                        return null;
                }

                if (upperInterface.getClass() == nodeClass) {
                        return upperInterface;
                } else {
                        InterfaceNode result = upperInterface.getUpperIN(nodeClass);
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
                        return this.getUpperIN().getDesignRessources();
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

        public void delete(InterfaceNode InterfaceNodeToDelete) {
                if (lowerInterfaces.contains(InterfaceNodeToDelete)) {
                        lowerInterfaces.remove(InterfaceNodeToDelete);
                        //     System.out.println("DesignNodeToDelete a été supprimé de lowersDN.");
                } else {
                        System.out.println("DesignNodeToDelete n'est pas présent dans lowersDN.");
                }
        }

        public void destroyItSelf() {
                upperInterface.delete(this);
        }

        public String ComputeUniqueID() {
                String ret = this.DRYComputeUniqueID();
                for (InterfaceNode lInter : lowerInterfaces) {
                        ret = ret + lInter.ComputeUniqueID();
                }
                return ret;
        }

        public String DRYComputeUniqueID() {
                String ret;
                if (linkedDesignNodes.size() > 0) {
                        ret = DesignInterfaceLinker.getIdentifier(linkedDesignNodes.get(0).getClass()); //better to use the linked DN 
                } else {
                        ret = DesignInterfaceLinker.getIdentifier(this.getClass()); //in case of node
                        System.out.println("Error no linked DN" + DesignInterfaceLinker.getIdentifier(this.getClass()));
                }
                if (ret == null) {
                        return "";
                }
                return ret;
        }



        
        /**
         * This static function is intermediate to lauch the simili static function of "DRYConcatenateSubElementNames" that is intancied and ovverridable
         *  (use in Random Allocator
         * @param interClass
         * @param rootElement
         * @return
         * @throws NoSuchMethodException 
         */
        public static String concatenateSubElementNames(Class<? extends InterfaceNode> interClass,Element rootElement) throws NoSuchMethodException{
                try {
                        Constructor<?> constructor = interClass.getConstructor(InterfaceNode.class, String.class);
                        // Créez une nouvelle instance en utilisant ce constructeur
                        return ((InterfaceNode)constructor.newInstance(null,"")).DRYConcatenateSubElementNames(rootElement);
                } catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
                        Logger.getLogger(InterfaceNode.class.getName()).log(Level.SEVERE, null, ex);
                }
                return null;
        }
        
        
                public String DRYConcatenateSubElementNames(Element rootElement) {
                // Concaténer les noms des sous-éléments
                StringBuilder concatenatedNames = new StringBuilder();
                NodeList childNodes = rootElement.getChildNodes();
                for (int i = 0; i < childNodes.getLength(); i++) {
                        Node childNode = childNodes.item(i);
                        if (childNode.getNodeType() == Node.ELEMENT_NODE) {
                                concatenatedNames.append(childNode.getNodeName());
                        }
                }
                return concatenatedNames.toString();
        }
}
