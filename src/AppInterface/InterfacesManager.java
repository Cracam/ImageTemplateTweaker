/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package AppInterface;

import interfaces.Interface;
import static interfaces.Interface.interfacesTypesMap;
import java.util.ArrayList;
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
                private final ArrayList<SubInterfaceContainer> interfaces = new ArrayList<>();
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
        private SubInterfaceContainer findInterfaceByName(String targetName) {
                if (targetName == null) {
                        return null;
                }

                for (SubInterfaceContainer myInterface : this.interfaces) {
                        if (targetName.equals(myInterface.getUniqueID())) {
                                return myInterface;
                        }
                }

                return null; // Retourne null si aucune interface correspondante n'est trouvée
        }

        
        
        public void saveInterfaces(Element interfacesElement,   Document document) {

                for (SubInterfaceContainer myInterface : this.interfaces) {
                        Node interfaceElement = myInterface.saveInterfaceData(document);
                        if (interfaceElement != null) {
                                interfacesElement.appendChild(interfaceElement);
                        }
                }
        }
        
        
        
        
           /**
         * Add an interface in the arrayList of the Desing builder
         *
         * @param interf
         */
        public void addInterface(SubInterfaceContainer interf) {
                interfaces.add(interf);
        }

        /**
         * return an interface identifie by is type and it's name
         *
         * @param type
         * @param name
         * @return
         */
        public SubInterfaceContainer getInterface(String type, String name) {
                // Check if the type exists in the map
                if (interfacesTypesMap.containsKey(type)) {
                        // Get the class of the interface
                        Class<? extends Interface> interfaceClass = interfacesTypesMap.get(type);

                        // Iterate through the list of interfaces
                        for (SubInterfaceContainer interf : interfaces) {
                                // Check if the interface is of the correct type
                                if (interfaceClass.isInstance(interf)) {
                                        // Get the name of the interface
                                        String interfaceName = interf.getUniqueID();

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
        
        
               
                  public void loadInterfaces(NodeList interfacesNodes){
                        SubInterfaceContainer tempInterface;
                        // Print the names of all "Output" nodes
                        for (int i = 0; i < interfacesNodes.getLength(); i++) {//we begin by one to avoid the description node
                                Node interfaceNode = interfacesNodes.item(i);
                                Element interfaceElt = (Element) interfaceNode;

                                tempInterface = findInterfaceByName(concatenateNames(interfaceElt));
                                
                                if (tempInterface != null) {
                                        tempInterface.loadInterfaceData(interfaceElt);
                                  //     System.out.println("InterfacesNodes: " + interfaceElt.getAttribute("InterfaceName"));

                                }else{
                                        System.out.println("Interface Loading failed " + interfaceElt.getAttribute("InterfaceName"));
                                }
                        }
                  }
                  
                  
                  
                  
                  
                  
                  
                  
                  
                  
                  
                  
                  
                  
        /**
         * Concatenate the unique ID friom the XML model loader
         * @param layerElement
         * @return 
         */
        public static String concatenateNames(Element layerElement) {
                String interfaceName = layerElement.getAttribute("name");
                String tabName = layerElement.getAttribute("tab_name");

                StringBuilder result = new StringBuilder(interfaceName + tabName);

                NodeList generatorNodes = layerElement.getElementsByTagName("Generator").item(0).getChildNodes();
                for (int i = 0; i < generatorNodes.getLength(); i++) {
                        Node node = generatorNodes.item(i);
                        if (node.getNodeType() == Node.ELEMENT_NODE) {
                                result.append("_-_").append(node.getNodeName());
                                break;
                        }
                }

                NodeList transformerNodes = layerElement.getElementsByTagName("Transformers").item(0).getChildNodes();
                for (int i = 0; i < transformerNodes.getLength(); i++) {
                        Node node = transformerNodes.item(i);
                        if (node.getNodeType() == Node.ELEMENT_NODE) {
                                result.append("_-_").append(node.getNodeName());
                        }
                }

                return result.toString();
        }
        
}
