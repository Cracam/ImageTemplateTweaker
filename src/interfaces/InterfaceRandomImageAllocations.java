/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package interfaces;

import Exeptions.ResourcesFileErrorException;
import ImageProcessor.Layers.LayerRandomImageAllocation;
import designBuilder.DesignBuilder;
import imageloaderinterface.ImageLoaderInterface;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.VBox;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import previewimagebox.PreviewImageBox;

/**
 *
 * @author Camille LECOURT
 */
public class InterfaceRandomImageAllocations extends Interface {

        @FXML
        private PreviewImageBox Preview;

        @FXML
        private VBox vboxInterface;

        @FXML
        private ImageLoaderInterface loader;

        private ArrayList<SubInterfaceRandomImageAllocation> lowerInterfaces;

        public InterfaceRandomImageAllocations(String interfaceName, DesignBuilder designBuilder) {
                super(interfaceName, designBuilder);
                //  link( null, vboxInterface);
        }

        @Override
        protected void initialiseInterface() {
                try {
                        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/InterfaceRandomImageAllocation.fxml"));
                        if (fxmlLoader == null) {
                                throw new ResourcesFileErrorException();
                        }
                        fxmlLoader.setRoot(this);
                        fxmlLoader.setController(this);

                        fxmlLoader.load();

                        setPreview(Preview);
                } catch (IOException | ResourcesFileErrorException | IllegalArgumentException ex) {
                        Logger.getLogger(ImageLoaderInterface.class.getName()).log(Level.SEVERE, null, ex);
                }
        }

        @FXML
        public void uptadeInterface() {
                this.refreshLayers();
                this.refreshImageBuilders();
        }

        @FXML
        public void createNewImgBuilder() {
                   SubInterfaceRandomImageAllocation subInter= ((LayerRandomImageAllocation)  this.linkedLayers.get(0)).createNewImgBuilder();
                lowerInterfaces.add(subInter);
        }

        @Override
        public Node saveInterfaceData(Document doc) {
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

//                return xmlManager.createDesignParamElement("DesignParam", "InterfaceName", interfaceName);
                return null;
        }

        @Override
        public void loadInterfaceData(Element dataOfTheLayer) {
//                DS_intervalSize.setLowValue(Double.parseDouble(dataOfTheLayer.getElementsByTagName("Interval").item(0).getAttributes().getNamedItem("Min_Interval").getNodeValue()));
//                DS_intervalSize.setHighValue(Double.parseDouble(dataOfTheLayer.getElementsByTagName("Interval").item(0).getAttributes().getNamedItem("Max_Interval").getNodeValue()));
//
//                DS_imageSize.setLowValue(Double.parseDouble(dataOfTheLayer.getElementsByTagName("Size").item(0).getAttributes().getNamedItem("Min_Size").getNodeValue()));
//                DS_imageSize.setHighValue(Double.parseDouble(dataOfTheLayer.getElementsByTagName("Size").item(0).getAttributes().getNamedItem("Max_Size").getNodeValue()));

        }

}
