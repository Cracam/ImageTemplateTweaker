package AppInterface;

import Exceptions.InvalidLinkbetweenNode;
import Exceptions.XMLExeptions.XMLErrorInModelException;
import ImageProcessor.DesignNode;
import ImageProcessor.ImageBuilder;
import ImageProcessor.ImagesTransformers.TransformerInert;
import ImageProcessor.Layer;
import ResourcesManager.XmlManager;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.control.TabPane;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import taboftiltedpane.TabOfTiltedPane;

/**
 *
 * @author Camille LECOURT
 */
public class InterfacesManager {

        private final ArrayList<LayersContainer> interfaces = new ArrayList<>();
        private final ArrayList<TabOfTiltedPane> tabs = new ArrayList<>();
        private TabPane tabPane;

        public InterfacesManager(TabPane tabPane) {
                this.tabPane = tabPane;
        }

        /**
         * This method shearch in the intefaces array to know if this interfac
         * exist
         *
         * @param targetName
         * @return
         */
        private InterfaceContainer findInterfaceByUniqueID(String targetName) {
                if (targetName == null) {
                        return null;
                }

                for (LayersContainer myInterface : this.interfaces) {
                        //     System.out.println("target Name : "+targetName+"  ID tested : "+myInterface.ComputeUniqueID());
                        if (targetName.equals(myInterface.ComputeUniqueID())) {
                                //    System.out.println("VALID --- target Name : "+targetName+"  ID tested : "+myInterface.ComputeUniqueID());
                                return myInterface;
                        }
                }
                return null; // Retourne null si aucune interface correspondante n'est trouvée
        }

        public void createInterfaceFromImageBuilderList(ArrayList<ImageBuilder> imageBuilders) {
                //We want to rassemble all the layer into only one lis aviding the duplicates
                ArrayList<DesignNode> layersList = new ArrayList<>();

                for (int i = 0; i < imageBuilders.size(); i++) {
                        Set<DesignNode> combinedSet = new HashSet<>(imageBuilders.get(i).getAllLowerDNOff(Layer.class));
                        combinedSet.addAll(layersList);
                        layersList = new ArrayList<>(combinedSet);
                }

                ArrayList<Layer> linkedLayers = new ArrayList<>();

                //now wae want to create them
                boolean alreadyCreated = false;
                Layer alredyCreatedLayer = null;
                String ID;
                for (DesignNode layer : layersList) {
                        if (layer.getClass() == Layer.class) {
                                ID = layer.ComputeUniqueID(Layer.class);
                                //    System.out.println("--------------------------------------------------------------------------------------------------------------------------");
                                for (Layer linkedLayer : linkedLayers) {
                                        //                  System.out.println(linkedLayer.ComputeUniqueID(Layer.class)+"--------------"+ID);
                                        //                 System.out.println("\\\\\\\\\\\\\\\\\\"+linkedLayer.toString());
                                        if (linkedLayer.ComputeUniqueID(Layer.class).equals(ID)) {
                                                //                        System.out.println("===============================================");
                                                alreadyCreated = true;
                                                alredyCreatedLayer = linkedLayer;
                                                break;
                                        }
                                }

                                if (alreadyCreated) {
                                        try {
                                                //      System.out.println("########### unique ID to link : "+layer.ComputeUniqueID(Layer.class));
                                                layer.linkDesignNodeToInterfaceNodes(alredyCreatedLayer.getLinkedinterface());
                                                alreadyCreated = false;
                                        } catch (InvalidLinkbetweenNode ex) {
                                                Logger.getLogger(InterfacesManager.class.getName()).log(Level.SEVERE, null, ex);
                                        }
                                } else {
                                        layer.createInterfaceTreeFromNodeTree(null, Arrays.asList(Layer.class, TransformerInert.class));
                                        //   System.out.println("ONLY VOID ? : "+layer.getLinkedinterface().interfaceBranchContainOnlyVoidInterfaces());
                                        if (!layer.getLinkedinterface().interfaceBranchContainOnlyVoidInterfaces()) {
                                                addInterface((LayersContainer) layer.getLinkedinterface());
                                                //   System.out.println("Interface qe want to add"+layer.getLinkedinterface());
                                                assignInterfaceToTab(((Layer) layer).getTabName(), (LayersContainer) layer.getLinkedinterface());
                                                linkedLayers.add((Layer) layer);
                                        }
                                }
                        }
                }

        }

        /**
         * Add an interface in the arrayList of the Desing builder
         *
         * @param interf
         */
        public void addInterface(LayersContainer interf) {
                interfaces.add(interf);
        }

        /**
         * This function assign a layer to a tab (in main interface) It can
         * create one if the tab does not exist yet
         *
         * @param inter
         * @param tabName
         */
        public void assignInterfaceToTab(String tabName, LayersContainer inter) {
                inter.setTabName(tabName);//set value of the tab name (use for Compute unique ID of layer container)
                for (TabOfTiltedPane tab : tabs) {
                        if (tab.getText().equals(tabName)) {
                                tab.addNodeToVBox(inter);
                                return;
                        }
                }
                //if the tab does not exist yet we create one and add the layer in it
                TabOfTiltedPane tab = new TabOfTiltedPane(tabName);
                tabPane.getTabs().add(tab);
                tab.addNodeToVBox(inter);
                tabs.add(tab);
        }

        public void loadInterfaces(NodeList interfacesNodes) {
                InterfaceContainer tempInterface;
                // Print the names of all "Output" nodes
                for (int i = 0; i < interfacesNodes.getLength(); i++) {//we begin by one to avoid the description node
                        Element interfaceElt = (Element) interfacesNodes.item(i);

                        String name = interfaceElt.getFirstChild().getNodeName();
                        String XMLID = "";
                        try {
                                XMLID = InterfaceNode.concatenateSubElementNames(DesignInterfaceLinker.getInterface(name), interfaceElt);
                                tempInterface = findInterfaceByUniqueID(DesignInterfaceLinker.getIdentifier(LayersContainer.class) + interfaceElt.getAttribute("ID") + XMLID);
                        } catch (NoSuchMethodException ex) {
                                Logger.getLogger(InterfacesManager.class.getName()).log(Level.SEVERE, null, ex);
                                tempInterface = null;
                        }

                        if (tempInterface != null) {
                                try {
                                        tempInterface.loadDesign(interfaceElt, -1);
                                } catch (XMLErrorInModelException ex) {
                                        Logger.getLogger(DesignBuilder.class.getName()).log(Level.SEVERE, null, ex);
                                        tempInterface = null;
                                }

                                //     System.out.println("InterfacesNodes: " + interfaceElt.getAttribute("InterfaceName"));
                        } else {
                                System.out.println("Interface Loading failed " + "   ____  " + DesignInterfaceLinker.getIdentifier(LayersContainer.class) + XMLID);
                        }
                }
        }

        public void closeAllInterfaces() {
                for (LayersContainer myInterface : this.interfaces) {

                        myInterface.destroyItSelf();
                }
        }

        public Element saveInterfaces(Element eltIn, Document doc) {
                XmlManager manager = new XmlManager(doc);
                for (LayersContainer interfaceRoot : interfaces) {
                        interfaceRoot.saveDesign(manager);
                        eltIn.appendChild(manager.createDesignParamElement("Interfaces", "ID", interfaceRoot.getName() + interfaceRoot.getTabName()));//mettre id A NOM + TAB
                }
                return eltIn;

        }

        public ArrayList<LayersContainer> getInterfaces() {
                return interfaces;
        }
        
        
        
}
