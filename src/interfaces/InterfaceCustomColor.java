/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package interfaces;

import Exeptions.ResourcesFileErrorException;
import GradientCreatorInterface.GradientCreatorInterface;
import ResourcesManager.ResourcesManager;
import ResourcesManager.XmlManager;
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
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import previewimagebox.PreviewImageBox;

/**
 *
 * @author Camille LECOURT
 */
public class InterfaceCustomColor extends Interface {

        private String imageName;

        @FXML
        private PreviewImageBox Preview;

        @FXML
        private GradientCreatorInterface gradientPicker;

        @FXML
        private TitledPane CustomImageTiledPane;

        private int[][] opacityMap;
        private BufferedImage image_getRaw;
        
        
        
        public InterfaceCustomColor(String interfaceName, String tabName, ResourcesManager designResources) {
                super(interfaceName, tabName, designResources);
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

                        System.out.println("gradientPicker initialized: " + (gradientPicker != null) + " value : " + gradientPicker.isChanged());
                        // Add a listener to the changed property
                        gradientPicker.isChanged().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
                                if (newValue) {
                                        // System.out.println("trigered");
                                        gradientPicker.setChanged(false);
                                        
                                        this.refreshLayers();
                                        this.refreshImageBuilders();
                                }
                        });

                } catch (IOException | ResourcesFileErrorException | IllegalArgumentException ex) {
                        Logger.getLogger(ImageLoaderInterface.class.getName()).log(Level.SEVERE, null, ex);
                }
                
        }

        
        
        
        @Override
        public Node saveInterfaceData() {
                  XmlManager xmlManager = new XmlManager();
                xmlManager.addChild("Gradient", Map.of("Gradient_Name", gradientPicker.getSelectedGradientName(), "Color_1", colorToHex(gradientPicker.getColor1()), "Color_2", colorToHex(gradientPicker.getColor2()), "ColorIntensity", String.valueOf(gradientPicker.getColorIntensity()), "Param_1", String.valueOf(gradientPicker.getParam1()), "Param_2", String.valueOf(gradientPicker.getParam2())));
                return xmlManager.createDesignParamElement("DesignParam", "interfaceName", interfaceName);
        }

        
        
        @Override
        public void loadInterfaceData(Element dataOfTheLayer) {
               String gradientName = dataOfTheLayer.getElementsByTagName("Gradient").item(0).getAttributes().getNamedItem("Gradient_Name").getNodeValue();

                Color color1 = hexToColor(dataOfTheLayer.getElementsByTagName("Gradient").item(0).getAttributes().getNamedItem("Color_1").getNodeValue());
                Color color2 = hexToColor(dataOfTheLayer.getElementsByTagName("Gradient").item(0).getAttributes().getNamedItem("Color_2").getNodeValue());

                double colorIntensity = Double.parseDouble(dataOfTheLayer.getElementsByTagName("Gradient").item(0).getAttributes().getNamedItem("ColorIntensity").getNodeValue());
                double param1 = Double.parseDouble(dataOfTheLayer.getElementsByTagName("Gradient").item(0).getAttributes().getNamedItem("Param_1").getNodeValue());
                double param2 = Double.parseDouble(dataOfTheLayer.getElementsByTagName("Gradient").item(0).getAttributes().getNamedItem("Param_2").getNodeValue());

                gradientPicker.setInterfaceState(gradientName, color1, color2, colorIntensity, param1, param2);
        }
        
        
        
        
        public BufferedImage getImageOut(int x,int y, int[][] opacityMap){
                return this.gradientPicker.getImageOut(opacityMap);
        }
        
        
        
        
        
        
        
        //////////////////////////////////////////////:
        // Intermediate functions :
        
        public static Color hexToColor(String hex) {
                // Remove the '#' character if present
                if (hex.startsWith("#")) {
                        hex = hex.substring(1);
                }

                // Parse the hex string
                int r = Integer.parseInt(hex.substring(0, 2));
                int g = Integer.parseInt(hex.substring(2, 4));
                int b = Integer.parseInt(hex.substring(4, 6));

                return new Color(r, g, b);
        }
        
          public static String colorToHex(Color color) {
                String hex = String.format("#%02x%02x%02x", color.getRed(), color.getGreen(), color.getBlue());
                return hex;
        }
        
        
}