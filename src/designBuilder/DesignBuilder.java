package designBuilder;

import imageBuilder.ImageBuilder;
import Exceptions.ACardModelIsLackingException;
import Exceptions.ResourcesFileErrorException;
import Exceptions.TheXmlElementIsNotANodeException;
import Layers.Layer;
import ResourcesManager.ResourcesManager;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
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
         

         
         private  ArrayList<ImageBuilder> imageBuilders = new ArrayList<>();

         private String name;
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

         
         private void loadNewModel(String filepath){
                 this.imageBuilders.clear();
                 this.modelResources=new ResourcesManager(filepath);
                 //gérer xml opening
                 Node loaderNode=   ;
                  try {
                        Element element = (Element) loaderNode;
                        if (loaderNode.getNodeType() != Node.ELEMENT_NODE) {
                                throw new TheXmlElementIsNotANodeException(loaderNode.getNodeName());
                        }
                        this.name =element.getAttribute("name");
                        
                        this.description
                           this.name
                                   this.defaultDesignName

                } catch (TheXmlElementIsNotANodeException ex) {
                        Logger.getLogger(ImageBuilder.class.getName()).log(Level.SEVERE, null, ex);

                }
                 
                 
                 
                 this.modelResources.add();
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

                          loadCardModel();

                           primarystage.setTitle("Batcher FOYER");
                           scene = new Scene(root);
                           primarystage.setScene(scene);
                           primarystage.show();

                           // Add a listener to the scene to listen for changes in the window size
                           scene.widthProperty().addListener((observable, oldValue, newValue) -> {
                                    adjustElementSize(newValue.doubleValue(), scene.getHeight());
                           });
                           scene.heightProperty().addListener((observable, oldValue, newValue) -> {
                                    adjustElementSize(scene.getWidth(), newValue.doubleValue());
                           });

                           System.out.println("imageViewVerso :" + imageViewVerso.toString());
                  } catch (ResourcesFileErrorException e) {

                  }
         }
         
         
         
         
         
         

         @Override
         public String toString() {
                  return "Batcher_Foyer{\nCardRecto=" + CardRecto.toString() + "\n\n CardVerso=" + CardVerso.toString() + "\n\n name=" + name + ", size_x=" + size_x + ", size_y=" + size_y + '}';
         }

         private void adjustElementSize(double width, double height) {
                  // Calculate the scaling factor based on the current screen definition (1920x1080)
                  double scaleX = width / 1920;
                  double scaleY = height / 1080;

                  // Adjust the size of the image views based on the scaling factor
                  this.imageViewRecto.setFitWidth(this.size_x * scaleX);
                  this.imageViewRecto.setFitHeight(this.size_y * scaleY);
                  this.imageViewVerso.setFitWidth(this.size_x * scaleX);
                  this.imageViewVerso.setFitHeight(this.size_y * scaleY);
         }

        public int getId() {
                return id;
        }

         
         

         /**
          * This function will load the data from the XML file and will pass it
          * to the 2 new cardBuilder object
          */
         private void loadCardModel() {
                  try {
                           File file = new File(getResourcesPath("ParamCartesFoyer.xml"));
                           DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
                           DocumentBuilder db = dbf.newDocumentBuilder();
                           Document doc = db.parse(file);
                           doc.getDocumentElement().normalize();

                           this.name = doc.getDocumentElement().getAttribute("name");
                           this.size_x = Float.parseFloat(doc.getDocumentElement().getAttribute("size_x"));
                           this.size_y = Float.parseFloat(doc.getDocumentElement().getAttribute("size_y"));

                           this.CardRecto = null;
                           this.CardVerso = null;

                           NodeList versoElements = doc.getElementsByTagName("Verso").item(0).getChildNodes();
                           NodeList rectoElements = doc.getElementsByTagName("Recto").item(0).getChildNodes();

                           List<Node> versoElementList = new ArrayList<>();
                           List<Node> rectoElementList = new ArrayList<>();

                           for (int i = 0; i < versoElements.getLength(); i++) {
                                    Node node = versoElements.item(i);
                                    if (node.getNodeType() == Node.ELEMENT_NODE) {
                                             versoElementList.add(node);
                                    }
                           }

                           for (int i = 0; i < rectoElements.getLength(); i++) {
                                    Node node = rectoElements.item(i);
                                    if (node.getNodeType() == Node.ELEMENT_NODE) {
                                             rectoElementList.add(node);
                                    }
                           }

                           if (rectoElementList.size() <= 0 | versoElementList.size() <= 0) {
                                    throw new ACardModelIsLackingException("Il n'y a pas les deux modèles de carte comme attendu");
                           }

//                           this.CardRecto = new ImageBuilder("Recto", this.size_x, this.size_y);
//                           this.CardRecto.setLayers(rectoElementList);
//                           this.CardRecto.setImageView(this.imageViewRecto);
//
//                           this.CardVerso = new ImageBuilder("Verso", this.size_x, this.size_y);
//                           this.CardVerso.setLayers(versoElementList);
//                           this.CardVerso.setImageView(this.imageViewVerso);

                           System.out.println(toString());
                           // CardRecto et CardVerso à remplir

                  } catch (IOException | ParserConfigurationException | SAXException e) {
                           System.out.println(e.getMessage());
                  } catch (ACardModelIsLackingException ex) {
                           Logger.getLogger(DesignBuilder.class.getName()).log(Level.SEVERE, null, ex);
                  }

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
