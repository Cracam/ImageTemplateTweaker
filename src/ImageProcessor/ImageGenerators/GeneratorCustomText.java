/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ImageProcessor.ImageGenerators;

import AppInterface.Interfaces.InterfaceCustomText;
import ImageProcessor.ImageGenerator;
import Exceptions.XMLExeptions.XMLErrorInModelException;
import ImageProcessor.DesignNode;
import ImageProcessor.ImageBuilder;
import ImageProcessor.Layer;
import Layers.SubClasses.PosFloat;
import static ResourcesManager.XmlManager.extractSingleElement;
import static ResourcesManager.XmlManager.getFloatAttribute;
import org.w3c.dom.Element;
import textinimagegenerator.TextInImageGenerator;

/**
 *
 * @author Camille LECOURT
 */
public class GeneratorCustomText extends ImageGenerator {

        boolean textChanged = true; //use only the the sub classe

        float textHeightFactor; //in mm
        float textSizeMin; //in mm
        float textSizeMax;//in mm

        public GeneratorCustomText(DesignNode upperDE, Element elt) throws XMLErrorInModelException {
                super(upperDE, elt);
        }

//
//        @Override
//        public void refreshImageGet() {
//                this.image_get = ((InterfaceCustomText) (this.linkedInterface)).getImageOut( this.linkedImagesBuilder.getPixelMmFactor(),textSizeMin,textSizeMax);
//        }
//         /**
//         * Compute the image out using Image_in and image_get
//         * (this overrride is for use cenring position)
//         * @param name
//         */
//        @Override
//        public void computeImage_Out(String name) {
//             // Create a new BufferedImage for the output
//                        BufferedImage outputImage = new BufferedImage(image_in.getWidth(), image_in.getHeight(), BufferedImage.TYPE_INT_ARGB);
//                        Graphics2D outputG2d = outputImage.createGraphics();
//
//                        // Draw image_out onto the output image
//                        outputG2d.drawImage(image_in, 0, 0, null);
//                     
//                        //use this if not resize 
//                        
//                        int y=this.pixelPosSize.getPos_y();
//                        y=y - image_get.getHeight()/2;
//                        // on the line below we use minus to change the interface slidebar direction
//                        y=y-(int ) (((InterfaceCustomText) this.linkedInterface).getTextGenerator().getTextHeigthValue()*this.textHeightFactor*this.linkedImagesBuilder.get_pixel_mm_Factor());
//                        
//                        outputG2d.drawImage(image_get,this.pixelPosSize.getPos_x()-image_get.getWidth()/2, y,  null);
//
//                        // Dispose of the Graphics2D object
//                        outputG2d.dispose();
//
//                        // Update image_out with the new image
//                        this.image_out = outputImage;
//                         this.refreshPreview();
//        }
        public void setTextChanged(boolean textChanged) {
                this.textChanged = textChanged;
        }

        @Override
        public void DRYUpdate() {
                float pixelMillimeterFactor =this.getUpperDN(ImageBuilder.class).getDesignBuilder().getPixelMmFactor();
                this.imageOut = ((InterfaceCustomText) (this.getLinkedinterface())).getImageOut(pixelMillimeterFactor, textSizeMin, textSizeMax);
                PosFloat offset = new PosFloat(0, 0);

                float x = -this.imageOut.getWidth() / 2/pixelMillimeterFactor;
                offset.setPos_x(x);

                TextInImageGenerator textGen = ((InterfaceCustomText) this.getLinkedinterface()).getTextGenerator();
                float y = (float) (textSizeMin + (textSizeMax - textSizeMin) * textGen.getTextSizeValue());
                y = (float) (y - textGen.getTextHeigthValue() * this.textHeightFactor);
                offset.setPos_y(y);

                this.getUpperDN(Layer.class).setAnOffset(name, offset);
        }

        @Override
        protected void generateFromElement() throws XMLErrorInModelException {
                Element subElt = extractSingleElement(elt.getElementsByTagName("Text"));
                this.textHeightFactor = getFloatAttribute(subElt, "TextHeightFactor", (float) 1.0);
                this.textSizeMin = getFloatAttribute(subElt, "TextSizeMin", (float) 2.0);
                this.textSizeMax = getFloatAttribute(subElt, "TextSizeMax", (float) 7.0);
        }
}
