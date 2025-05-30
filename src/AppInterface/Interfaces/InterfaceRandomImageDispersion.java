/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package AppInterface.Interfaces;

import AppInterface.DesignInterfaceLinker;
import AppInterface.InterfaceContainer;
import Exeptions.ResourcesFileErrorException;
import ResourcesManager.XmlChild;
import ResourcesManager.XmlManager;
import AppInterface.InterfaceNode;
import Exceptions.XMLExeptions.XMLErrorInModelException;
import imageloaderinterface.ImageLoaderInterface;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import org.controlsfx.control.RangeSlider;
import org.w3c.dom.Element;

/**
 *
 * @author Camille LECOURT
 */
public class InterfaceRandomImageDispersion extends InterfaceContainer {

        @FXML
        private RangeSlider DS_imageSize;

        @FXML
        private RangeSlider DS_intervalSize;

        @FXML
        private VBox vboxContainer;

        @FXML
        private ImageView prev;

        public InterfaceRandomImageDispersion(InterfaceNode upperIN, String name) {
                super(upperIN, name);

                setContainerVBox(vboxContainer);
                if(upperIN!=null){
                          upperInterface.placeInterface(this);
                }
        }

        @Override
        protected void initialiseInterface() {
                try {
                        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/InterfaceRandomImagedispersion.fxml"));
                        if (fxmlLoader == null) {
                                throw new ResourcesFileErrorException();
                        }
                        fxmlLoader.setRoot(this);
                        fxmlLoader.setController(this);

                        fxmlLoader.load();

                        DS_intervalSize.setMin(0);
                        DS_intervalSize.setMax(1);
                        DS_intervalSize.setLowValue(0.25);
                        DS_intervalSize.setHighValue(0.75);

                        DS_imageSize.setMin(0);
                        DS_imageSize.setMax(1);
                        DS_imageSize.setLowValue(0.25);
                        DS_imageSize.setHighValue(0.75);

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
        public float getLowIntervalSize() {
                return (float) this.DS_intervalSize.getLowValue();
        }

        public float getHighIntervalSize() {
                return (float) this.DS_intervalSize.getHighValue();
        }

        public float getLowImageSize() {
                return (float) this.DS_imageSize.getLowValue();
        }

        public float getHighImageSize() {
                return (float) this.DS_imageSize.getHighValue();
        }

        @Override
        protected void DRYLoadDesign(Element element) throws XMLErrorInModelException {
                DS_intervalSize.setLowValue(XmlManager.getDoubleAttribute(element, "Min_Interval", 1));
                DS_intervalSize.setHighValue(XmlManager.getDoubleAttribute(element, "Max_Interval", 2));
                DS_imageSize.setLowValue(XmlManager.getDoubleAttribute(element, "Min_Size", 1));
                DS_imageSize.setHighValue(XmlManager.getDoubleAttribute(element, "Max_Size", 2));
        }

        @Override
        public XmlChild DRYsaveDesign() {

                XmlChild XMLRet = new XmlChild(DesignInterfaceLinker.getIdentifier(this.getClass()));
                XMLRet.addAttribute("Min_Interval", String.valueOf(DS_intervalSize.getLowValue()));
                XMLRet.addAttribute("Max_Interval", String.valueOf(DS_intervalSize.getHighValue()));
                XMLRet.addAttribute("Min_Size", String.valueOf(DS_imageSize.getLowValue()));
                XMLRet.addAttribute("Max_Size", String.valueOf(DS_imageSize.getHighValue()));

                return XMLRet;
        }

}
