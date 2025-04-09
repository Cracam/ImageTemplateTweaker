/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package AppInterface.Interfaces;

import AppInterface.DesignInterfaceLinker;
import Exeptions.ResourcesFileErrorException;
import GradientCreatorInterface.GradientCreatorInterface;
import AppInterface.InterfaceNode;
import ImageProcessor.ImageGenerators.GeneratorFixedImage;
import ResourcesManager.XmlChild;
import ResourcesManager.XmlManager;
import static ResourcesManager.XmlManager.extractSingleElement;
import static ResourcesManager.XmlManager.getStringAttribute;
import imageloaderinterface.ImageLoaderInterface;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javax.imageio.ImageIO;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import staticFunctions.StaticImageEditing;

/**
 *
 * @author Camille LECOURT
 */
public class InterfaceCustomColor extends InterfaceNode {

        @FXML
        private GradientCreatorInterface gradientPicker;

        public InterfaceCustomColor(InterfaceNode upperIN, String name) {
                super(upperIN, name);
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
                                }
                        });

                } catch (IOException | ResourcesFileErrorException | IllegalArgumentException ex) {
                        Logger.getLogger(ImageLoaderInterface.class.getName()).log(Level.SEVERE, null, ex);
                }

        }

        @Override
        public XmlChild DRYsaveDesign() {

                XmlChild XmlGradient = new XmlChild(DesignInterfaceLinker.getIdentifier(this.getClass()));
                XmlGradient.addAttribute("Gradient_Name", gradientPicker.getSelectedGradientName());
                XmlGradient.addAttribute("Color_1", StaticImageEditing.colorToHex(gradientPicker.getColor1()));
                XmlGradient.addAttribute("Color_2", StaticImageEditing.colorToHex(this.gradientPicker.getColor2()));
                XmlGradient.addAttribute("ColorIntensity", String.valueOf(this.gradientPicker.getColorIntensity()));
                XmlGradient.addAttribute("Param_1", String.valueOf(this.gradientPicker.getParam1()));
                XmlGradient.addAttribute("Param_2", String.valueOf(this.gradientPicker.getParam2()));

                return XmlGradient;
        }

        @Override
        protected void DRYLoadDesign(Element dataOfTheLayer) {
                
                String gradientName =  getStringAttribute(dataOfTheLayer, "Gradient_Name", "Pas de Texte définit"); 
                
                Color color1 = StaticImageEditing.hexToColor(getStringAttribute(dataOfTheLayer, "Color_1", "#FFFFFF")); 
                Color color2 = StaticImageEditing.hexToColor(getStringAttribute(dataOfTheLayer, "Color_2", "#FFFFFF")); 

                double colorIntensity =XmlManager.getDoubleAttribute(dataOfTheLayer, "ColorIntensity", 0.0);
                double param1 = XmlManager.getDoubleAttribute(dataOfTheLayer, "Param_1", 0.0); 
                double param2 = XmlManager.getDoubleAttribute(dataOfTheLayer, "Param_2", 0.0); 
                
              //  System.out.println(" gradient  " + gradientName + "  " + color1 + "  " + color2 + "  " + colorIntensity + "  " + param1 + "  " + param2);
                gradientPicker.setInterfaceState(gradientName, color1, color2, colorIntensity, param1, param2);
        }

        public BufferedImage getImageOut(int[][] opacityMap) {
                return this.gradientPicker.getImageOut(opacityMap);
        }

}
