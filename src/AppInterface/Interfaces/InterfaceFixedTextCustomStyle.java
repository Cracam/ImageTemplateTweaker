package AppInterface.Interfaces;

import AppInterface.DesignInterfaceLinker;
import AppInterface.InterfaceNode;
import Exceptions.XMLExeptions.XMLErrorInModelException;
import ImageProcessor.DesignNode;
import ImageProcessor.ImageGenerators.GeneratorFixedTextCustomStyle;
import ResourcesManager.XmlChild;
import ResourcesManager.XmlManager;
import static ResourcesManager.XmlManager.getStringAttribute;
import java.awt.image.BufferedImage;
import java.io.File;
import org.w3c.dom.Element;

/**
 *
 * @author Camille LECOURT
 */
public class InterfaceFixedTextCustomStyle extends InterfaceCustomText {

        public InterfaceFixedTextCustomStyle(InterfaceNode upperIN, String name) {
                super(upperIN, name);
                canChangeText = false;
                canChangeTextSize = true;
                canChangeTextHeight = true;
                canChangeFont = true;
                canChangeColor = true;
                checkInterfaceHidding();
        }


        @Override
          public void addDesignNode(DesignNode DN) {
                linkedDesignNodes.add(DN);
                DN.setLinkedInterface(this);
                        this.getTextGenerator().setText(((GeneratorFixedTextCustomStyle)DN).getText());
        }
          
          
              /**
         * Return the out of this interface with the good size
         *
         * @param text
         * @param pixelMmFactor
         * @param textSizeMin
         * @param textSizeMax
         * @return
         */
        public BufferedImage getImageOut(String text, float pixelMmFactor, float textSizeMin, float textSizeMax) {
                         this.getTextGenerator().setText(text);
                        return this.getTextGenerator().getImageOut(pixelMmFactor, textSizeMin, textSizeMax);
        }

             @Override
        public XmlChild DRYsaveDesign() {

                XmlChild XmlTextBuilder = new XmlChild(DesignInterfaceLinker.getIdentifier(this.getClass()));
                XmlTextBuilder.addAttribute("Text_Size", String.valueOf(this.getTextGenerator().getTextSizeValue()));
                XmlTextBuilder.addAttribute("Text_Height", String.valueOf(this.getTextGenerator().getTextHeigthValue()));

                File fontToSave = this.getTextGenerator().getFontFile();
                if (fontToSave != null) {
                        String fontName = fontToSave.getName();
                        this.getDesignRessources().set(fontName, fontToSave);
                        XmlTextBuilder.addAttribute("Font_name", fontName);
                }
                return XmlTextBuilder;
        }
        
        @Override
        protected void DRYLoadDesign(Element element) throws XMLErrorInModelException {
                 String fontName =   getStringAttribute(element,"Font_name","");
                if (!("".equals(fontName))) {
                        this.getTextGenerator().loadNewFont(this.getDesignRessources().get(fontName));
                }

                double textSize =XmlManager.getDoubleAttribute(element, "Text_Size", 5);
                double textheight = XmlManager.getDoubleAttribute(element, "Text_Height", 0); 
                this.getTextGenerator().loadValues("", textSize, textheight);
        }
}
