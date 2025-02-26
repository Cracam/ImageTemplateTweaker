/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package interfaces;

import Exeptions.ResourcesFileErrorException;
import GradientCreatorInterface.GradientCreatorInterface;
import ResourcesManager.XmlManager;
import designBuilder.DesignBuilder;
import imageloaderinterface.ImageLoaderInterface;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TitledPane;
import javafx.scene.image.ImageView;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import previewimagebox.PreviewImageBox;
import staticFunctions.StaticImageEditing;

/**
 *
 * @author Camille LECOURT
 */
public class InterfaceCustomColor extends Interface {

        @FXML
        private PreviewImageBox Preview;

        @FXML
        private GradientCreatorInterface gradientPicker;

        @FXML
        private TitledPane CustomImageTiledPane;


        
        public InterfaceCustomColor(String interfaceName, DesignBuilder designBuilder) {
                super(interfaceName, designBuilder);
        }

        @Override
        protected void initialiseInterface() {
                try {
                        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/InterfaceCustomColor.fxml"));
                        if (fxmlLoader == null) {
                                throw new ResourcesFileErrorException();
                        }
                        fxmlLoader.setRoot(this);
                        fxmlLoader.setController(this);

                        fxmlLoader.load();

                    //    System.out.println("gradientPicker initialized: " + (gradientPicker != null) + " value : " + gradientPicker.isChanged());
                        this.CustomImageTiledPane.setText(interfaceName);
                        // Add a listener to the changed property
                        gradientPicker.isChanged().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
                                if (newValue) {
                                        // System.out.println("trigered");
                                        gradientPicker.setChanged(false);
                                        
                                        this.refreshLayers();
                                        this.refreshImageBuilders();
                                 //       System.out.println("(1) CHANGGE DEECTED " + linkedImagesBuilders.size());
                                }
                        });

                } catch (IOException | ResourcesFileErrorException | IllegalArgumentException ex) {
                        Logger.getLogger(ImageLoaderInterface.class.getName()).log(Level.SEVERE, null, ex);
                }
                
        }

        
        
        
        @Override
        public Node saveInterfaceData(Document doc) {
                  XmlManager xmlManager = new XmlManager(doc);
                xmlManager.addChild("Gradient", Map.of("Gradient_Name", gradientPicker.getSelectedGradientName(), "Color_1", StaticImageEditing.colorToHex(gradientPicker.getColor1()), "Color_2", StaticImageEditing.colorToHex(gradientPicker.getColor2()), "ColorIntensity", String.valueOf(gradientPicker.getColorIntensity()), "Param_1", String.valueOf(gradientPicker.getParam1()), "Param_2", String.valueOf(gradientPicker.getParam2())));
                return xmlManager.createDesignParamElement("DesignParam", "InterfaceName", interfaceName);
        }

        
        
        @Override
        public void loadInterfaceData(Element dataOfTheLayer) {
               String gradientName = dataOfTheLayer.getElementsByTagName("Gradient").item(0).getAttributes().getNamedItem("Gradient_Name").getNodeValue();

                Color color1 = StaticImageEditing.hexToColor(dataOfTheLayer.getElementsByTagName("Gradient").item(0).getAttributes().getNamedItem("Color_1").getNodeValue());
                Color color2 = StaticImageEditing.hexToColor(dataOfTheLayer.getElementsByTagName("Gradient").item(0).getAttributes().getNamedItem("Color_2").getNodeValue());

                double colorIntensity = Double.parseDouble(dataOfTheLayer.getElementsByTagName("Gradient").item(0).getAttributes().getNamedItem("ColorIntensity").getNodeValue());
                double param1 = Double.parseDouble(dataOfTheLayer.getElementsByTagName("Gradient").item(0).getAttributes().getNamedItem("Param_1").getNodeValue());
                double param2 = Double.parseDouble(dataOfTheLayer.getElementsByTagName("Gradient").item(0).getAttributes().getNamedItem("Param_2").getNodeValue());
                
                System.out.println(" gradient  "+gradientName+"  "+ color1+"  "+ color2+"  "+colorIntensity+"  "+ param1+"  "+ param2);
                gradientPicker.setInterfaceState(gradientName, color1, color2, colorIntensity, param1, param2);
        }
        
        
        
        
        public BufferedImage getImageOut(int x,int y, int[][] opacityMap){
                return this.gradientPicker.getImageOut(opacityMap);
        }
        
        @Override
        public void refreshPreview(String imageBuilderName, ImageView previewImage){
              refreshPreviewIntermediate(imageBuilderName,previewImage,Preview);
        }
        

        

}
