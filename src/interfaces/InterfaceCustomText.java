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
import static interfaces.Interface.refreshPreviewIntermediate;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.image.ImageView;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import previewimagebox.PreviewImageBox;
import staticFunctions.StaticImageEditing;
import textinimagegenerator.TextInImageGenerator;

/**
 *This class is a interface of a custom img Image loaded by the user
 * @author Camille LECOURT
 */
public class InterfaceCustomText extends Interface {

        
        @FXML
        private PreviewImageBox Preview;

        @FXML
        private TextInImageGenerator TextGenerator;

        @FXML
        private GradientCreatorInterface gradientPicker;

       private int[][] opacityMap=new int[1][1];
       private boolean textChanged=true;

        public InterfaceCustomText(String interfaceName, ResourcesManager designResources) {
                super(interfaceName, designResources);
        }
        
        
        
        @Override
        protected void initialiseInterface() {
                try {
                        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/InterfaceCustomText.fxml"));
                        if (fxmlLoader == null) {
                                throw new ResourcesFileErrorException();
                        }
                        fxmlLoader.setRoot(this);
                        fxmlLoader.setController(this);

                        fxmlLoader.load();

                        // Add a listener to the changed property
                        gradientPicker.isChanged().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
                                if (newValue) {
                                        //    System.out.println("trigered");
                                        gradientPicker.setChanged(false);
                                      System.out.println("(1) CHANGGE DEECTED");
                                        this.refreshLayers();
                                        this.refreshImageBuilders();
                                }
                        });
                        
                        // Add a listener to the changed property
                        TextGenerator.isChanged().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
                                if (newValue) {
                                        //    System.out.println("trigered");
                                        this.textChanged();
                                        TextGenerator.setChanged(false);
                                      System.out.println("(2) CHANGGE DEECTED");
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
                String imageName = "Image_" + interfaceName + ".png";

                XmlManager xmlManager = new XmlManager();
                xmlManager.addChild("Image", Map.of("image_name", imageName));

                //save the image into the Design zip
          //      this.designResources.setBufferedImage(interfaceName, imageName, LoaderInterface.getImage_out());

                
         
                return xmlManager.createDesignParamElement("DesignParam", "LayerName", interfaceName);
        }

        
        
        @Override
        public void loadInterfaceData(Element dataOfTheLayer ) {              
                
                String gradientName = dataOfTheLayer.getElementsByTagName("Gradient").item(0).getAttributes().getNamedItem("Gradient_Name").getNodeValue();

                Color color1 = StaticImageEditing.hexToColor(dataOfTheLayer.getElementsByTagName("Gradient").item(0).getAttributes().getNamedItem("Color_1").getNodeValue());
                Color color2 = StaticImageEditing.hexToColor(dataOfTheLayer.getElementsByTagName("Gradient").item(0).getAttributes().getNamedItem("Color_2").getNodeValue());

                double colorIntensity = Double.parseDouble(dataOfTheLayer.getElementsByTagName("Gradient").item(0).getAttributes().getNamedItem("ColorIntensity").getNodeValue());
                double param1 = Double.parseDouble(dataOfTheLayer.getElementsByTagName("Gradient").item(0).getAttributes().getNamedItem("Param_1").getNodeValue());
                double param2 = Double.parseDouble(dataOfTheLayer.getElementsByTagName("Gradient").item(0).getAttributes().getNamedItem("Param_2").getNodeValue());
                gradientPicker.setInterfaceState(gradientName, color1, color2, colorIntensity, param1, param2);
                
                //String textBuilderName = dataOfTheLayer.getElementsByTagName("TextBuilder").item(0).getAttributes().getNamedItem("Text_Name").getNodeValue();
                String fontName = dataOfTheLayer.getElementsByTagName("TextBuilder").item(0).getAttributes().getNamedItem("Font_name").getNodeValue();
                this.TextGenerator.setFont(this.designResources.get(fontName));
                
                String text = dataOfTheLayer.getElementsByTagName("TextBuilder").item(0).getAttributes().getNamedItem("Text").getNodeValue();
                double textSize = Double.parseDouble(dataOfTheLayer.getElementsByTagName("TextBuilder").item(0).getAttributes().getNamedItem("Text_Size").getNodeValue());
                double textheight = Double.parseDouble(dataOfTheLayer.getElementsByTagName("TextBuilder").item(0).getAttributes().getNamedItem("Text_Height").getNodeValue());
                   
                TextGenerator.loadValues(text,textSize,textheight);
        }
        
        
        
        /**
         * Return the out of this interface with the good size
         * @param pixelMmFactor
         * @return 
         */
        public BufferedImage getImageOut(float pixelMmFactor){
                        if (textChanged){
                             //   refreshBlendTable();
                                 BufferedImage resizedImageGetRaw = this.TextGenerator.getImageOut(pixelMmFactor);
                                 this.opacityMap =StaticImageEditing.transformToOpacityArray(resizedImageGetRaw);
                                 textChanged=false;
                        }
                    return gradientPicker.getImageOut(opacityMap);
        }
        
        /**
         * launch this program if the text is changed
         */
        private void textChanged(){
                textChanged=true;
        }
        
        
         @Override
        public void refreshPreview(String imageBuilderName, ImageView previewImage){
              refreshPreviewIntermediate(imageBuilderName,previewImage,Preview);
        }
        
}
