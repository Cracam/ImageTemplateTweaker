package AppInterface.Interfaces;

import AppInterface.InterfaceContainer;
import Exeptions.ResourcesFileErrorException;
import AppInterface.InterfaceNode;
import Exceptions.InvalidLinkbetweenNode;
import Exceptions.XMLExeptions.XMLErrorInModelException;
import ImageProcessor.DesignNode;
import ImageProcessor.ImageGenerators.GeneratorRandomImageAllocation;
import ImageProcessor.ImageGenerators.GeneratorRandomSubImageAllocation;
import ImageProcessor.ImagesTransformers.TransformerInert;
import ResourcesManager.XmlChild;
import imageloaderinterface.ImageLoaderInterface;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 *
 * @author Camille LECOURT
 */
public class InterfaceRandomImageAllocation extends InterfaceContainer {

        @FXML
        private Label MinimumNumberLabel;

        @FXML
        private VBox SubInterfaceContainer;
        
        @FXML
        private VBox CommonContainer;

        public InterfaceRandomImageAllocation(InterfaceNode upperIN, String name) {
                super(upperIN, name);

                setContainerVBox(SubInterfaceContainer);
                upperInterface.placeInterface(this);
        }

        @Override
        protected void initialiseInterface() {
                try {
                        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/InterfaceRandomAllocation.fxml"));
                        if (fxmlLoader == null) {
                                throw new ResourcesFileErrorException();
                        }
                        fxmlLoader.setRoot(this);
                        fxmlLoader.setController(this);

                        fxmlLoader.load();

                } catch (IOException | ResourcesFileErrorException | IllegalArgumentException ex) {
                        Logger.getLogger(ImageLoaderInterface.class.getName()).log(Level.SEVERE, null, ex);
                }
        }

//        public void setLowerImageBuilder(SubImageBuilder lowerImageBuilder) {
//
//                this.lowerImageBuilder = lowerImageBuilder;
//
//                //intall trigger
//                lowerImageBuilder.isChanged().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
//                        if (newValue) {
//                                //    System.out.println("trigered");
//                                lowerImageBuilder.setChanged(false);
//                                //       System.out.println("(1) CHANGGE DEECTED");
//                                refreshSubInterface();
//                                this.refreshLayers();
//                                this.refreshImageBuilders();
//                        }
//                });
//                //    lowerImageBuilder.setChanged(true);
//                lowerImageBuilder.refreshAll();
//                refreshSubInterface();
//               vboxInterface.getChildren().add( this.lowerImageBuilder.getSubInterface());
////                ArrayList<Interface> interfaces = lowerImageBuilder.getInterfaces();
////                for (Interface iface : interfaces) {
////                        vboxInterface.getChildren().add(iface);
////                }
//
//        }
//        public void linkInterface(Interface inter) {
//                vboxInterface.getChildren().add(inter);
//                inter.refreshLayers();
//                inter.refreshImageBuilders();
//        }
        @FXML
        public void uptadeInterface() {
                this.updateLinkedDesignNodes();
        }

//        public void refreshSubInterface() {
//                this.prev.setImage(convertToFXImage(this.lowerImageBuilder.getImageOut()));
//                prev.setStyle("-fx-border-color: blue; -fx-border-width: 2px; -fx-border-style: solid;");
//                //System.out.println("REEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE");
//        }
//        public Node saveInterfaceData(Document doc) {
//                XmlManager xmlManager = new XmlManager(doc);
//
//                XmlChild Xmlinterval = new XmlChild("Interval");
//                Xmlinterval.addAttribute("Min_Interval", String.valueOf(DS_intervalSize.getLowValue()));
//                Xmlinterval.addAttribute("Max_Interval", String.valueOf(DS_intervalSize.getHighValue()));
//                xmlManager.addChild(Xmlinterval);
//
//                XmlChild XmlSize = new XmlChild("Size");
//                XmlSize.addAttribute("Min_Size", String.valueOf(DS_imageSize.getLowValue()));
//                XmlSize.addAttribute("Max_Size", String.valueOf(DS_imageSize.getHighValue()));
//                xmlManager.addChild(XmlSize);
//
//                return xmlManager.createDesignParamElement("DesignParam", "InterfaceName", interfaceName);
//        }
//        @Override
//        public void loadInterfaceData(Element dataOfTheLayer) {
//                DS_intervalSize.setLowValue(Double.parseDouble(dataOfTheLayer.getElementsByTagName("Interval").item(0).getAttributes().getNamedItem("Min_Interval").getNodeValue()));
//                DS_intervalSize.setHighValue(Double.parseDouble(dataOfTheLayer.getElementsByTagName("Interval").item(0).getAttributes().getNamedItem("Max_Interval").getNodeValue()));
//
//                DS_imageSize.setLowValue(Double.parseDouble(dataOfTheLayer.getElementsByTagName("Size").item(0).getAttributes().getNamedItem("Min_Size").getNodeValue()));
//                DS_imageSize.setHighValue(Double.parseDouble(dataOfTheLayer.getElementsByTagName("Size").item(0).getAttributes().getNamedItem("Max_Size").getNodeValue()));
//
//        }
        /**
         * this function create a new subimageBuilder
         */
        @FXML
        private void createNewSubImgBuilder() {

                // Exécuter le code sur le premier élément
                DesignNode DN = linkedDesignNodes.get(0);
                GeneratorRandomImageAllocation imgAll = (GeneratorRandomImageAllocation) DN;
                GeneratorRandomSubImageAllocation subDN = imgAll.createSubImageAllocationBuilder();
                
                InterfaceRandomSubImageAllocation newInter = (InterfaceRandomSubImageAllocation) subDN.createInterfaceTreeFromNodeTree(this,TransformerInert.class);

                for (int i = 1; i < linkedDesignNodes.size(); i++) {
                        try {
                                DN = linkedDesignNodes.get(i);
                                imgAll = (GeneratorRandomImageAllocation) DN;
                                subDN = imgAll.createSubImageAllocationBuilder();
                                subDN.linkDesignNodeToInterfaceNodes(newInter);
                        } catch (InvalidLinkbetweenNode ex) {
                                Logger.getLogger(InterfaceRandomImageAllocation.class.getName()).log(Level.SEVERE, null, ex);
                        }
                }
                //  this.updateLinkedDesignNodes();
                DN.updateLower();
                uptadeInterface();
        }
        
        
        public void createCommomInterface(DesignNode commonInterface){
                this.setContainerVBox(CommonContainer);
                InterfaceNode interNode = commonInterface.createInterfaceTreeFromNodeTree(this);
               this.setContainerVBox(SubInterfaceContainer);  
               // CommonContainer.getChildren().add(interNode);
        }

        @Override
        protected void DRYLoadDesign(Element element) throws XMLErrorInModelException {
                //nothing to load
        }

        @Override
        public XmlChild DRYsaveDesign() {
                return null;
        }

}
