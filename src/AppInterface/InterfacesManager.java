/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package AppInterface;

import Exceptions.XMLExeptions.GetAttributeValueException;
import ResourcesManager.XmlManager;
import interfaces.Interface;
import static interfaces.Interface.interfacesTypesMap;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.control.TabPane;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import taboftiltedpane.TabOfTiltedPane;

/**
 *
 * @author Camille LECOURT
 */
public class InterfacesManager {

        private final ArrayList<InterfaceContainer> interfaces = new ArrayList<>();
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

                for (InterfaceContainer myInterface : this.interfaces) {
                        if (targetName.equals(myInterface.ComputeUniqueID())) {
                                return myInterface;
                        }
                }

                return null; // Retourne null si aucune interface correspondante n'est trouvée
        }

//        public void saveInterfaces(Element interfacesElement, Document document) {
//
//                for (InterfaceContainer myInterface : this.interfaces) {
//                        Node interfaceElement = myInterface.saveInterfaceData(document);
//                        if (interfaceElement != null) {
//                                interfacesElement.appendChild(interfaceElement);
//                        }
//                }
//        }

        /**
         * Add an interface in the arrayList of the Desing builder
         *
         * @param interf
         */
        public void addInterface(InterfaceContainer interf) {
                interfaces.add(interf);
        }

        /**
         * return an interface identifie by is type and it's name
         *
         * @param type
         * @param name
         * @return
         */
        public InterfaceContainer getInterface(String type, String name) {
                // Check if the type exists in the map
                if (interfacesTypesMap.containsKey(type)) {
                        // Get the class of the interface
                        Class<? extends Interface> interfaceClass = interfacesTypesMap.get(type);

                        // Iterate through the list of interfaces
                        for (InterfaceContainer interf : interfaces) {
                                // Check if the interface is of the correct type
                                if (interfaceClass.isInstance(interf)) {
                                        // Get the name of the interface
                                        String interfaceName = interf.ComputeUniqueID();

                                        // Compare the name with the provided name
                                        if (interfaceName.equals(name)) {
                                                return interf;
                                        }
                                }
                        }
                }
                // Return null if the interface does not exist
                return null;
        }

        /**
         * This function assign a layer to a tab (in main interface) It can
         * create one if the tab does not exist yet
         *
         * @param inter
         * @param tabName
         */
        public void assignInterfaceToTab(String tabName, Interface inter) {
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
                        Node interfaceNode = interfacesNodes.item(i);
                        Element interfaceElt = (Element) interfaceNode;

                        try {
                                tempInterface = findInterfaceByUniqueID(XmlManager.getStringAttribute(interfaceElt, "name", ""));
                        } catch (GetAttributeValueException ex) {
                                Logger.getLogger(DesignBuilder.class.getName()).log(Level.SEVERE, null, ex);
                                tempInterface = null;
                        }
                        if (tempInterface != null) {
                                try {
                                        tempInterface.loadDesign(interfaceElt, 0);
                                } catch (GetAttributeValueException ex) {
                                        Logger.getLogger(DesignBuilder.class.getName()).log(Level.SEVERE, null, ex);
                                        tempInterface = null;
                                }
                                //     System.out.println("InterfacesNodes: " + interfaceElt.getAttribute("InterfaceName"));

                        } else {
                                System.out.println("Interface Loading failed " + interfaceElt.getAttribute("InterfaceName"));
                        }
                }
        }

}
