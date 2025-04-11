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

//        @Override
//        public Node saveInterfaceData(Document doc) {
//                XmlManager xmlManager = new XmlManager(doc);
//
//                XmlChild XmlGradient = new XmlChild("Gradient");
//                XmlGradient.addAttribute("Gradient_Name", this.getGradientPicker().getSelectedGradientName());
//                XmlGradient.addAttribute("Color_1", StaticImageEditing.colorToHex(this.getGradientPicker().getColor1()));
//                XmlGradient.addAttribute("Color_2", StaticImageEditing.colorToHex(this.getGradientPicker().getColor2()));
//                XmlGradient.addAttribute("ColorIntensity", String.valueOf(this.getGradientPicker().getColorIntensity()));
//                XmlGradient.addAttribute("Param_1", String.valueOf(this.getGradientPicker().getParam1()));
//                XmlGradient.addAttribute("Param_2", String.valueOf(this.getGradientPicker().getParam2()));
//
//                XmlChild XmlTextBuilder = new XmlChild("TextBuilder");
//                //XmlTextBuilder.addAttribute("Text", this.getTextGenerator().getTextValue());
//                XmlTextBuilder.addAttribute("Text_Size", String.valueOf(this.getTextGenerator().getTextSizeValue()));
//                XmlTextBuilder.addAttribute("Text_Height", String.valueOf(this.getTextGenerator().getTextHeigthValue()));
//
//                File fontToSave = this.getTextGenerator().getFontFile();
//                if (fontToSave != null) {
//                        String fontName = fontToSave.getName();
//                        this.designBuilder.getDesignResources().set(fontName, fontToSave);
//                        XmlTextBuilder.addAttribute("Font_name", fontName);
//                }
//
//                xmlManager.addChild(XmlGradient);
//                xmlManager.addChild(XmlTextBuilder);
//
//                return xmlManager.createDesignParamElement("DesignParam", "InterfaceName", interfaceName);
//
//        }
//
//        @Override
//        public void loadInterfaceData(Element dataOfTheLayer) {
//
//                String gradientName = dataOfTheLayer.getElementsByTagName("Gradient").item(0).getAttributes().getNamedItem("Gradient_Name").getNodeValue();
//
//                Color color1 = StaticImageEditing.hexToColor(dataOfTheLayer.getElementsByTagName("Gradient").item(0).getAttributes().getNamedItem("Color_1").getNodeValue());
//                Color color2 = StaticImageEditing.hexToColor(dataOfTheLayer.getElementsByTagName("Gradient").item(0).getAttributes().getNamedItem("Color_2").getNodeValue());
//
//                double colorIntensity = Double.parseDouble(dataOfTheLayer.getElementsByTagName("Gradient").item(0).getAttributes().getNamedItem("ColorIntensity").getNodeValue());
//                double param1 = Double.parseDouble(dataOfTheLayer.getElementsByTagName("Gradient").item(0).getAttributes().getNamedItem("Param_1").getNodeValue());
//                double param2 = Double.parseDouble(dataOfTheLayer.getElementsByTagName("Gradient").item(0).getAttributes().getNamedItem("Param_2").getNodeValue());
//                this.getGradientPicker().setInterfaceState(gradientName, color1, color2, colorIntensity, param1, param2);
//
//                Node fontNameNode =  dataOfTheLayer.getElementsByTagName("TextBuilder").item(0).getAttributes().getNamedItem("Font_name");
//                if (fontNameNode != null) {
//                        String fontName = fontNameNode.getNodeValue();
//                        this.getTextGenerator().loadNewFont(this.designBuilder.getDesignResources().get(fontName));
//                }
//
//                //      String text = dataOfTheLayer.getElementsByTagName("TextBuilder").item(0).getAttributes().getNamedItem("Text").getNodeValue();
//                double textSize = Double.parseDouble(dataOfTheLayer.getElementsByTagName("TextBuilder").item(0).getAttributes().getNamedItem("Text_Size").getNodeValue());
//                double textheight = Double.parseDouble(dataOfTheLayer.getElementsByTagName("TextBuilder").item(0).getAttributes().getNamedItem("Text_Height").getNodeValue());
//
//                this.getTextGenerator().loadValues("", textSize, textheight);
//        }
//
//        public BufferedImage getImageOut(int[][] opacityMap) {
//                return this.getGradientPicker().getImageOut(opacityMap);
//        }
//
//        /**
//         * This code return a
//         *
//         * @param text
//         * @param pixelMmFactor
//         * @param textSizeMin
//         * @param textSizeMax
//         * @return
//         */
//        public int[][] refreshOpacityMap(String text, float pixelMmFactor, float textSizeMin, float textSizeMax) {
//
//                BufferedImage resizedImageGetRaw = this.getTextGenerator().getImageOut(text, pixelMmFactor, textSizeMin, textSizeMax);
//                return StaticImageEditing.transformToOpacityArray(resizedImageGetRaw);
//        }

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
