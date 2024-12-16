package designBuilder;

import imageBuilder.ImageBuilder;
import Exceptions.ResourcesFileErrorException;
import Layers.Layer;
import ResourcesManager.ResourcesManager;
import interfaces.Interface;
import static interfaces.Interface.interfacesTypesMap;
import interfaces.InterfaceCustomColor;
import interfaces.InterfaceCustomImage;
import interfaces.InterfaceFixedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TabPane;
import javafx.stage.Stage;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import previewimagebox.PreviewImageBox;
import taboftiltedpane.TabOfTiltedPane;

/**
 *
 * @author LECOURT Camille
 */
public class DesignBuilder extends Application {
         private static int index;
         private  int id;
         
         private ResourcesManager modelResources;
                 private ResourcesManager  designResources;
                 
         //Information on the model
         private String name; // the model Name
         private String description; // The description of themodel
         private String defaultDesignName; // the default design name (inside the zip of the model) it's the file we will copy if the user use a model a reference for it's new Design.
         

         
         private final  ArrayList<ImageBuilder> imageBuilders = new ArrayList<>();
         private final  ArrayList<TabOfTiltedPane> tabs = new ArrayList<>();
         private final ArrayList<Interface> interfaces=new ArrayList<>();
                  
    
        
         private Scene scene;
         
         private int DPI=150;

         
         @FXML
         private TabPane tabPane;
         
         @FXML
         private PreviewImageBox preview;
         
         
         /**
          * @param args the command line arguments
          */
         public static void main(String[] args) {
                  launch(args);
         }

        /**
         * This program will be used to create a new model it will set a resource Manager element, (it will not save the design until the first save)
         * 
         * It will pen an xml file in order to
         * @param filepath 
         */
        private void loadNewModel(String filepath) {
                 try {
                         this.imageBuilders.clear();
                         this.modelResources = new ResourcesManager(filepath);
                         //gérer xml opening
                         // Create a DocumentBuilderFactory
                         DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                         // Create a DocumentBuilder
                         DocumentBuilder builder = factory.newDocumentBuilder();
                         // Parse the XML file and return a DOM Document object
                         Document document = builder.parse(this.modelResources.get("Model_Param.xml"));
                         // Get the root element
                         Element rootElement = document.getDocumentElement();
                         
                         this.name = rootElement.getAttribute("name");
                         
                         Element informationsNode = (Element) rootElement.getElementsByTagName("Informations").item(0);
                         this.description = informationsNode.getElementsByTagName("DefaultDesign").item(0).getAttributes().getNamedItem("name").getNodeValue();
                         this.defaultDesignName = informationsNode.getElementsByTagName("Description").item(0).getAttributes().getNamedItem("Description").getNodeValue();
                         
                         // Get all "Output" nodes
                         NodeList outputNodes = rootElement.getElementsByTagName("Output");

                         // Print the names of all "Output" nodes
                         for (int i = 0; i < outputNodes.getLength(); i++) {//we begin by one to avoid the description node
                                 Node outputNode = outputNodes.item(i);
                                 
                                 System.out.println("Output Node: " + outputNode.getNodeName());
                                 imageBuilders.add(new ImageBuilder(this, outputNode));
                                 System.out.println(this.toString());
                         }
                         
                 } catch (ParserConfigurationException | SAXException | IOException ex) {
                         Logger.getLogger(DesignBuilder.class.getName()).log(Level.SEVERE, null, ex);
                 }
        }

         
         
         @Override
         public void start(Stage primarystage) throws Exception {
                  try {
                          this.id=DesignBuilder.index ;
                          DesignBuilder.index++;
                           
                           URL[] urlList = new URL[1];
                           URL inter_principalle = getClass().getClassLoader().getResource("InterfaceBatcher.fxml");
                           urlList[0] = inter_principalle;
//                           System.out.println(inter_principalle);

                           if (CheckArrayHaveNull(urlList)) {
                                    throw new ResourcesFileErrorException("One or more files are missing in the ressources files");
                           }

                           FXMLLoader loader = new FXMLLoader(inter_principalle);
                           loader.setController(this);
                           Parent root = loader.load();
                          loadNewModel("C:\\BACKUP\\ENSE3\\Foyer\\Programme_Java\\Batcher_Foyer\\test_data\\modelTest.zip");

                           primarystage.setTitle("Batcher FOYER");
                           scene = new Scene(root);
                           primarystage.setScene(scene);
                           primarystage.show();

                         //refresh all the images
                         for (ImageBuilder imageBuilder : imageBuilders) {
                                         imageBuilder.refreshAll();
                         }

                  } catch (ResourcesFileErrorException e) {

                  }
         }

        @Override
        public String toString() {
                return "DesignBuilder{" + "id=" + id + ", modelResources=" + modelResources + ", designResources=" + designResources + ", name=" + name + ", description=" + description + ", defaultDesignName=" + defaultDesignName + ", imageBuilders=" + imageBuilders + ", tabs=" + tabs + ", DPI=" + DPI + ", tabPane=" + tabPane + ", preview=" + preview + '}';
        }
         


        public int getId() {
                return id;
        }

         
         

        

         /**
          * This function retunr true if one or more null element is detected in
          * the @array
          *
          * @param Array
          * @return
          */
         private static boolean CheckArrayHaveNull(Object[] Array) {
                  // Check if array contains null elements
                  boolean containsNull = false;
                  for (Object obj : Array) {
                           if (obj == null) {
                                    containsNull = true;
                                    break;
                           }
                  }
                  return containsNull;
         }

         /**
          *
          * @param fileName
          * @return Return the absoute address of the fileName
          */
         public static String getResourcesPath(String fileName) {
                  // Obtenir le répertoire courant du projet
                  File currentDir = new File("");

                  // Obtenir le chemin d'accès absolu du répertoire courant
                  return currentDir.getAbsolutePath() + "/resources/" + fileName;
         }
         
        /**
         * This function assign a layer to a tab (in main interface)
         * It can create one if the tab does not exist yet
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
         
         




        public float getPixelMmFactor() {
                return (float) (this.DPI / 24.5);
        }

        public ResourcesManager getModelResources() {
                return modelResources;
        }

        private void setModelResources(ResourcesManager templateResources) {
                this.modelResources = templateResources;
        }

        public ResourcesManager getDesignResources() {
                return designResources;
        }

        private void setDesignResources(ResourcesManager designResources) {
                this.designResources = designResources;
        }

        /**
         * This method will refresh the main preview
         */
        public void refreshPreview(){
                this.preview.clearAllImagesViews();
                for(ImageBuilder imageBuilder : imageBuilders){
                       imageBuilder.refreshPreview(this.preview);
                }
        }
        
        
                /**
         * Add an interface in the arrayList of the Desing builder
         *
         * @param interf
         */
        public void addInterface(Interface interf) {
                interfaces.add(interf);
        }

        /**
         * return an interface identifie by is type and it's name
         *
         * @param type
         * @param name
         * @return
         */
        public Interface getInterface(String type, String name) {
                // Check if the type exists in the map
                if (interfacesTypesMap.containsKey(type)) {
                        // Get the class of the interface
                        Class<? extends Interface> interfaceClass = interfacesTypesMap.get(type);

                        // Iterate through the list of interfaces
                        for (Interface interf : interfaces) {
                                // Check if the interface is of the correct type
                                if (interfaceClass.isInstance(interf)) {
                                        // Get the name of the interface
                                        String interfaceName = interf.getInterfaceName();

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
        
}
