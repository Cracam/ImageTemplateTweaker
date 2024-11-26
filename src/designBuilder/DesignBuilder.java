package designBuilder;

import imageBuilder.ImageBuilder;
import Exceptions.ResourcesFileErrorException;
import ResourcesManager.ResourcesManager;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

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

         private float size_x;
         private float size_y;
         
        
         private Scene scene;
         
         private int DPI;

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
                         for (int i = 1; i < outputNodes.getLength(); i++) {//we begin by one to avoid the description node
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

                         

                  } catch (ResourcesFileErrorException e) {

                  }
         }
         
         
         
         
         
         

         @Override
         public String toString() {
                  return "Batcher_Foyer{ \n\n name=" + name + ", size_x=" + size_x + ", size_y=" + size_y + '}';
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
         




        public float get_pixel_mm_Factor() {
                return (float) (this.DPI / 24.5);
        }

        public ResourcesManager getTemplateResources() {
                return modelResources;
        }

        private void setTemplateResources(ResourcesManager templateResources) {
                this.modelResources = templateResources;
        }

        public ResourcesManager getDesignResources() {
                return designResources;
        }

        private void setDesignResources(ResourcesManager designResources) {
                this.designResources = designResources;
        }

   
        
        
        
        

}
