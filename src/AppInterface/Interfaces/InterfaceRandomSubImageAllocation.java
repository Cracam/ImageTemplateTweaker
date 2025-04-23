package AppInterface.Interfaces;

import AppInterface.DesignInterfaceLinker;
import AppInterface.InterfaceContainer;
import Exeptions.ResourcesFileErrorException;
import AppInterface.InterfaceNode;
import Exceptions.XMLExeptions.XMLErrorInModelException;
import ImageProcessor.DesignNode;
import ImageProcessor.ImageGenerators.GeneratorRandomImageAllocation;
import ResourcesManager.XmlChild;
import ResourcesManager.XmlManager;
import imageloaderinterface.ImageLoaderInterface;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.layout.VBox;
import org.w3c.dom.Element;

/**
 *
 * @author Camille LECOURT
 */
public class InterfaceRandomSubImageAllocation extends InterfaceContainer {

        @FXML
        private VBox SubInterfaceContainer;

        @FXML
        private Spinner<Integer> NumberSelector;
        

        
        public InterfaceRandomSubImageAllocation(InterfaceNode upperIN, String name) {
                super(upperIN, name);
       
                
                setContainerVBox(SubInterfaceContainer);
                 if(upperIN!=null){
                          upperInterface.placeInterface(this);
                }
        }

        @Override
        protected void initialiseInterface() {
                try {
                        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/SubInterfaceRandomAllocation.fxml"));
                        if (fxmlLoader == null) {
                                throw new ResourcesFileErrorException();
                        }
                        fxmlLoader.setRoot(this);
                        fxmlLoader.setController(this);

                        fxmlLoader.load();

                        SpinnerValueFactory<Integer> valueFactory = ((GeneratorRandomImageAllocation) this.getUpperIN(InterfaceRandomImageAllocation.class).getLinkedDesignNodes().get(0)).getSpinnerFactory();
                        NumberSelector.setValueFactory(valueFactory);

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
//        @Override
//        public void loadInterfaceData(Element dataOfTheLayer) {
//                DS_intervalSize.setLowValue(Double.parseDouble(dataOfTheLayer.getElementsByTagName("Interval").item(0).getAttributes().getNamedItem("Min_Interval").getNodeValue()));
//                DS_intervalSize.setHighValue(Double.parseDouble(dataOfTheLayer.getElementsByTagName("Interval").item(0).getAttributes().getNamedItem("Max_Interval").getNodeValue()));
//
//                DS_imageSize.setLowValue(Double.parseDouble(dataOfTheLayer.getElementsByTagName("Size").item(0).getAttributes().getNamedItem("Min_Size").getNodeValue()));
//                DS_imageSize.setHighValue(Double.parseDouble(dataOfTheLayer.getElementsByTagName("Size").item(0).getAttributes().getNamedItem("Max_Size").getNodeValue()));
//
//        }
        @FXML
         void deleteThis() {
                for (DesignNode DN : linkedDesignNodes) {
                        this.NumberSelector.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 1));
                        this.NumberSelector.getValueFactory().setValue(0);
                       // System.out.println("FACTVAL : " + this.NumberSelector.getValueFactory().getValue());
                        DN.update();
                        DN.destroyItSelf();
                }
                this.destroyItSelf();
                if (this.getLinkedDesignNodes().isEmpty()) {
                        this.getLinkedDesignNodes().get(0).getUpperDN(GeneratorRandomImageAllocation.class).updateLower();
                }
        }

        @Override
        protected void DRYLoadDesign(Element element) throws XMLErrorInModelException {
                this.NumberSelector.getValueFactory().setValue(XmlManager.getIntAttribute(element, "MaxNumberOfAllocation", 2));
        }

        @Override
        public XmlChild DRYsaveDesign() {
                XmlChild XMLAllocator = new XmlChild(DesignInterfaceLinker.getIdentifier(this.getClass()));
                XMLAllocator.addAttribute("MaxNumberOfAllocation", String.valueOf(NumberSelector.getValue()));
                return XMLAllocator;
        }

        public int getNumberSelectorValue() {
                return NumberSelector.getValue();
        }

      

        
}
