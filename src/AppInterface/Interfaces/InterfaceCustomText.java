package AppInterface.Interfaces;

import AppInterface.InterfaceNode;
import Exceptions.XMLExeptions.XMLErrorInModelException;
import Exeptions.ResourcesFileErrorException;
import ResourcesManager.XmlChild;
import ResourcesManager.XmlManager;
import static ResourcesManager.XmlManager.getStringAttribute;
import imageloaderinterface.ImageLoaderInterface;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import org.w3c.dom.Element;
import textinimagegenerator.TextInImageGenerator;

/**
 *
 *
 * @author Camille LECOURT
 */
public class InterfaceCustomText extends InterfaceNode {

        @FXML
        private TextInImageGenerator TextGenerator;

        boolean textChanged = true;

        //The five different boolean to mdify the text bahavior
        boolean canChangeText;
        boolean canChangeTextSize;
        boolean canChangeTextHeight;
        boolean canChangeFont;
        boolean canChangeColor;

        public InterfaceCustomText(InterfaceNode upperIN, String name) {
                super(upperIN, name);
                upperInterface.placeInterface(this);
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
                        TextGenerator.isChanged().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
                                if (newValue) {
                                        //    System.out.println("trigered");
                                        this.textChanged();
                                        TextGenerator.setChanged(false);
                                        this.updateLinkedDesignNodes();
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

        /**
         * Return the out of this interface with the good size
         *
         * @param pixelMmFactor
         * @param textSizeMin
         * @param textSizeMax
         * @return
         */
        public BufferedImage getImageOut(float pixelMmFactor, float textSizeMin, float textSizeMax) {
                return this.TextGenerator.getImageOut(pixelMmFactor, textSizeMin, textSizeMax);
        }

        /**
         * launch this program if the text is changed
         */
        private void textChanged() {
                textChanged = true;
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

        @Override
        protected void DRYLoadDesign(Element element) throws XMLErrorInModelException {

                String fontName =   getStringAttribute(element,"Font_name","");
                if ("".equals(fontName)) {
                        this.TextGenerator.loadNewFont(this.getDesignRessources().get(fontName));
                }

                String text = getStringAttribute(element, "Text","PAS DE TEXTE");
                double textSize =XmlManager.getDoubleAttribute(element, "Text_Size", 5);
                double textheight = XmlManager.getDoubleAttribute(element, "Text_Height", 0); 
                TextGenerator.loadValues(text, textSize, textheight);
        }

        
        
        @Override
        public XmlChild DRYsaveDesign() {

                XmlChild XmlTextBuilder = new XmlChild("TextBuilder");
                XmlTextBuilder.addAttribute("Text", this.getTextGenerator().getTextValue());
                XmlTextBuilder.addAttribute("Text_Size", String.valueOf(this.getTextGenerator().getTextSizeValue()));
                XmlTextBuilder.addAttribute("Text_Height", String.valueOf(this.getTextGenerator().getTextHeigthValue()));

                File fontToSave = TextGenerator.getFontFile();
                if (fontToSave != null) {
                        String fontName = fontToSave.getName();
                        this.getDesignRessources().set(fontName, fontToSave);
                        XmlTextBuilder.addAttribute("Font_name", fontName);
                }
                return XmlTextBuilder;
        }

}
