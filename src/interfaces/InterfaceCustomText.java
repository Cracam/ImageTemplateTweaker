/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package interfaces;

import Exeptions.ResourcesFileErrorException;
import GradientCreatorInterface.GradientCreatorInterface;
import Layers.LayerCustomText;
import ResourcesManager.XmlChild;
import ResourcesManager.XmlManager;
import designBuilder.DesignBuilder;
import imageloaderinterface.ImageLoaderInterface;
import static interfaces.Interface.refreshPreviewIntermediate;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
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
import textinimagegenerator.TextInImageGenerator;

/**
 * This class is a interface of a custom img Image loaded by the user
 *
 * @author Camille LECOURT
 */
public class InterfaceCustomText extends Interface {

        @FXML
        private PreviewImageBox Preview;

        @FXML
        private TextInImageGenerator TextGenerator;

        @FXML
        private GradientCreatorInterface gradientPicker;

        @FXML
        private TitledPane CustomImageTiledPane;

        private int[][] opacityMap = new int[1][1];
        boolean textChanged = true;

        //The five different boolean to mdify the text bahavior
        boolean canChangeText;
        boolean canChangeTextSize;
        boolean canChangeTextHeight;
        boolean canChangeFont;
        boolean canChangeColor;

        public InterfaceCustomText(String interfaceName, DesignBuilder designBuilder) {

                super(interfaceName, designBuilder);
                canChangeText = true;
                canChangeTextSize = true;
                canChangeTextHeight = true;
                canChangeFont = true;
                canChangeColor = true;
                checkInterfaceHidding();
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

                        this.Preview.toggleFixedSize();

                        this.CustomImageTiledPane.setText(interfaceName);
                        // Add a listener to the changed property
                        gradientPicker.isChanged().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
                                if (newValue) {
                                        //    System.out.println("trigered");
                                        gradientPicker.setChanged(false);
                                        //       System.out.println("(1) CHANGGE DEECTED");
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
                                        //System.out.println("(2) CHANGGE DEECTED");
                                        this.refreshLayers();
                                        this.refreshImageBuilders();

                                        for (int i = 0; i < linkedLayers.size(); i++) {
                                                ((LayerCustomText) (linkedLayers.get(i))).setTextChanged(true);

                                        }

                                }
                        });

                } catch (IOException | ResourcesFileErrorException | IllegalArgumentException ex) {
                        Logger.getLogger(ImageLoaderInterface.class.getName()).log(Level.SEVERE, null, ex);
                }
        }

        /**
         * refresh the visibility of the different interface
         */
        public void checkInterfaceHidding() {
                //Check the visibility of the color interface 
                if (!this.canChangeColor) {
                        gradientPicker.setVisible(false);
                        gradientPicker.setManaged(false);
                }

                if (!this.canChangeText) {
                        TextGenerator.desactivateTextField();
                }

                if (!canChangeTextSize) {
                        TextGenerator.desactivateTextSizeSlideBar();
                }

                if (!canChangeFont) {
                        TextGenerator.desactivateFontCharger();
                }

                if (!canChangeTextHeight) {
                        TextGenerator.desactivateTextHeighSlideBar();
                }
        }

        @Override
        public Node saveInterfaceData(Document doc) {

                XmlManager xmlManager = new XmlManager(doc);

                XmlChild XmlGradient = new XmlChild("Gradient");
                XmlGradient.addAttribute("Gradient_Name", gradientPicker.getSelectedGradientName());
                XmlGradient.addAttribute("Color_1", StaticImageEditing.colorToHex(gradientPicker.getColor1()));
                XmlGradient.addAttribute("Color_2", StaticImageEditing.colorToHex(gradientPicker.getColor2()));
                XmlGradient.addAttribute("ColorIntensity", String.valueOf(gradientPicker.getColorIntensity()));
                XmlGradient.addAttribute("Param_1", String.valueOf(gradientPicker.getParam1()));
                XmlGradient.addAttribute("Param_2", String.valueOf(gradientPicker.getParam2()));

                XmlChild XmlTextBuilder = new XmlChild("TextBuilder");
                XmlTextBuilder.addAttribute("Text", this.getTextGenerator().getTextValue());
                XmlTextBuilder.addAttribute("Text_Size", String.valueOf(this.getTextGenerator().getTextSizeValue()));
                XmlTextBuilder.addAttribute("Text_Height", String.valueOf(this.getTextGenerator().getTextHeigthValue()));

                File fontToSave = TextGenerator.getFontFile();
                if (fontToSave != null) {
                        String fontName = fontToSave.getName();
                        this.designBuilder.getDesignResources().set(fontName, fontToSave);
                        XmlTextBuilder.addAttribute("Font_name", fontName);
                }

                xmlManager.addChild(XmlGradient);
                xmlManager.addChild(XmlTextBuilder);

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
                gradientPicker.setInterfaceState(gradientName, color1, color2, colorIntensity, param1, param2);

                //String textBuilderName = dataOfTheLayer.getElementsByTagName("TextBuilder").item(0).getAttributes().getNamedItem("Text_Name").getNodeValue();
                Node fontNameNode =  dataOfTheLayer.getElementsByTagName("TextBuilder").item(0).getAttributes().getNamedItem("Font_name");
                if (fontNameNode != null) {
                        String fontName = fontNameNode.getNodeValue();
                        this.TextGenerator.loadNewFont(this.designBuilder.getDesignResources().get(fontName));
                }

                String text = dataOfTheLayer.getElementsByTagName("TextBuilder").item(0).getAttributes().getNamedItem("Text").getNodeValue();
                double textSize = Double.parseDouble(dataOfTheLayer.getElementsByTagName("TextBuilder").item(0).getAttributes().getNamedItem("Text_Size").getNodeValue());
                double textheight = Double.parseDouble(dataOfTheLayer.getElementsByTagName("TextBuilder").item(0).getAttributes().getNamedItem("Text_Height").getNodeValue());

                TextGenerator.loadValues(text, textSize, textheight);
        }

        /**
         * Return the out of this interface with the good size
         *
         * @param pixelMmFactor
         * @param textSizeMin
         * @param textSizeMax
         * @return
         */
        public BufferedImage getImageOut(float pixelMmFactor, float textSizeMin, float textSizeMax) {
                if (textChanged) {
                        //   refreshBlendTable();
                        BufferedImage resizedImageGetRaw = this.TextGenerator.getImageOut(pixelMmFactor, textSizeMin, textSizeMax); //72/25.4 convert milimeter into pt 
                        this.opacityMap = StaticImageEditing.transformToOpacityArray(resizedImageGetRaw);
                        textChanged = false;
                }
                return gradientPicker.getImageOut(opacityMap);
        }

        /**
         * launch this program if the text is changed
         */
        private void textChanged() {
                textChanged = true;
        }

        @Override
        public void refreshPreview(String imageBuilderName, ImageView previewImage) {
                refreshPreviewIntermediate(imageBuilderName, previewImage, Preview);
        }

        public boolean isCanChangeText() {
                return canChangeText;
        }

        public void setCanChangeText(boolean canChangeText) {
                this.canChangeText = canChangeText;
        }

        public boolean isCanChangeColor() {
                return canChangeColor;
        }

        public void setCanChangeColor(boolean canChangeColor) {
                this.canChangeColor = canChangeColor;
        }

        public boolean isCanChangeTextSize() {
                return canChangeTextSize;
        }

        public void setCanChangeTextSize(boolean canChangeTextSize) {
                this.canChangeTextSize = canChangeTextSize;
        }

        public boolean isCanChangeTextHeight() {
                return canChangeTextHeight;
        }

        public void setCanChangeTextHeight(boolean canChangeTextHeight) {
                this.canChangeTextHeight = canChangeTextHeight;
        }

        public boolean isCanChangeFont() {
                return canChangeFont;
        }

        public void setCanChangeFont(boolean canChangeFont) {
                this.canChangeFont = canChangeFont;
        }

        public TextInImageGenerator getTextGenerator() {
                return TextGenerator;
        }

        public PreviewImageBox getPreview() {
                return Preview;
        }

        public GradientCreatorInterface getGradientPicker() {
                return gradientPicker;
        }

        public TitledPane getCustomImageTiledPane() {
                return CustomImageTiledPane;
        }

}
