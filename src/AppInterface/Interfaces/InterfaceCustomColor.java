/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package AppInterface.Interfaces;

import Exeptions.ResourcesFileErrorException;
import GradientCreatorInterface.GradientCreatorInterface;
import AppInterface.InterfaceNode;
import imageloaderinterface.ImageLoaderInterface;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import previewimagebox.PreviewImageBox;
import staticFunctions.StaticImageEditing;

/**
 *
 * @author Camille LECOURT
 */
public class InterfaceCustomColor extends InterfaceNode {

        

        @FXML
        private GradientCreatorInterface gradientPicker;

        
        public InterfaceCustomColor(InterfaceNode upperIN) {
                super(upperIN);
                upperInterface.placeInterface(this);
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
                        // Add a listener to the changed property
                        gradientPicker.isChanged().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
                                if (newValue) {
                                        // System.out.println("trigered");
                                        gradientPicker.setChanged(false);
                                          this.updateLinkedDesignNodes();

                                    
                                 //       System.out.println("(1) CHANGGE DEECTED " + linkedImagesBuilders.size());
                                }
                        });
                                
                } catch (IOException | ResourcesFileErrorException | IllegalArgumentException ex) {
                        Logger.getLogger(ImageLoaderInterface.class.getName()).log(Level.SEVERE, null, ex);
                }
                
        }

        
        
        
        @Override
            public Element DRYsaveDesign(Document doc) {
//                  XmlManager xmlManager = new XmlManager(doc);
//                xmlManager.addChild("Gradient", Map.of("Gradient_Name", gradientPicker.getSelectedGradientName(), "Color_1", StaticImageEditing.colorToHex(gradientPicker.getColor1()), "Color_2", StaticImageEditing.colorToHex(gradientPicker.getColor2()), "ColorIntensity", String.valueOf(gradientPicker.getColorIntensity()), "Param_1", String.valueOf(gradientPicker.getParam1()), "Param_2", String.valueOf(gradientPicker.getParam2())));
//                return xmlManager.createDesignParamElement("DesignParam", "InterfaceName", interfaceName);
return null;
        }

        
        
        @Override
        protected Element  DRYLoadDesign(Element dataOfTheLayer,int index) {
               String gradientName = dataOfTheLayer.getElementsByTagName("Gradient").item(0).getAttributes().getNamedItem("Gradient_Name").getNodeValue();

                Color color1 = StaticImageEditing.hexToColor(dataOfTheLayer.getElementsByTagName("Gradient").item(0).getAttributes().getNamedItem("Color_1").getNodeValue());
                Color color2 = StaticImageEditing.hexToColor(dataOfTheLayer.getElementsByTagName("Gradient").item(0).getAttributes().getNamedItem("Color_2").getNodeValue());

                double colorIntensity = Double.parseDouble(dataOfTheLayer.getElementsByTagName("Gradient").item(0).getAttributes().getNamedItem("ColorIntensity").getNodeValue());
                double param1 = Double.parseDouble(dataOfTheLayer.getElementsByTagName("Gradient").item(0).getAttributes().getNamedItem("Param_1").getNodeValue());
                double param2 = Double.parseDouble(dataOfTheLayer.getElementsByTagName("Gradient").item(0).getAttributes().getNamedItem("Param_2").getNodeValue());
                
                System.out.println(" gradient  "+gradientName+"  "+ color1+"  "+ color2+"  "+colorIntensity+"  "+ param1+"  "+ param2);
                gradientPicker.setInterfaceState(gradientName, color1, color2, colorIntensity, param1, param2);
                return null;
        }
        
        
        
        
        public BufferedImage getImageOut(int[][] opacityMap){
                return this.gradientPicker.getImageOut(opacityMap);
        }

       

       


        

}
