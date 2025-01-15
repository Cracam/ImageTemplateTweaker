/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Layers;

import Layers.SubClasses.QuadrupletFloat;
import ResourcesManager.ResourcesManager;
import ResourcesManager.XmlManager;
import imageBuilder.ImageBuilder;
import interfaces.Interface;
import interfaces.InterfaceCustomText;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 *
 * @author Camille LECOURT
 */
public class LayerCustomText extends Layer {
        boolean textChanged=true; //use only the the sub classe

        private String imageName;
        float textHeightFactor=(float) 1.0; //in mm
        float textSizeMin=(float) 5.0; //in mm
        float textSizeMax =(float) 25.0;//in mm

        public LayerCustomText(String layerName, ResourcesManager modelResources, Interface layerInterface, ImageBuilder linkedImageBuilder, QuadrupletFloat posSize) {
                super(layerName, modelResources, layerInterface, linkedImageBuilder, posSize);
        }
        

        @Override
        public void refreshImageGet() {
                this.image_get = ((InterfaceCustomText) (this.linkedInterface)).getImageOut( this.linkedImagesBuilder.getPixelMmFactor(),textSizeMin,textSizeMax);
        }
        

        @Override
        void DPIChanged() {
                //noting to do the text compare the PixelMilimeter factor automaticaly
        }
        

        /**
         * 
         * @param paramNode
         */
        @Override
        public void readNode(Element paramNode) {
              Element element=  (Element) paramNode.getElementsByTagName("Text").item(0);

                this.textHeightFactor=XmlManager.getFloatAttribute(element,"TextHeightFactor",this.textHeightFactor);
                this.textSizeMin=XmlManager.getFloatAttribute(element,"TextSizeMin",this.textSizeMin);
                this.textSizeMax=XmlManager.getFloatAttribute(element,"TextSizeMax",this.textSizeMax);
        }

        @Override
        public String toString() {
                return "LayerCustomColor{" + "imageName=" + imageName + '}';
        }
        
        
        
         /**
         * Compute the image out using Image_in and image_get
         * (this overrride is for use cenring position)
         * @param name
         */
        @Override
        public void computeImage_Out(String name) {
          
             // Create a new BufferedImage for the output
                        BufferedImage outputImage = new BufferedImage(image_in.getWidth(), image_in.getHeight(), BufferedImage.TYPE_INT_ARGB);
                        Graphics2D outputG2d = outputImage.createGraphics();

                        // Draw image_out onto the output image
                        outputG2d.drawImage(image_in, 0, 0, null);
                     
                        //use this if not resize 
                        
                        int y=this.pixelPosSize.getPos_y();
                        y=y - image_get.getHeight()/2;
                        // on the line below we use minus to change the interface slidebar direction
                        y=y-(int ) (((InterfaceCustomText) this.linkedInterface).getTextGenerator().getTextHeigthValue()*this.textHeightFactor*this.linkedImagesBuilder.get_pixel_mm_Factor());
                        
                        outputG2d.drawImage(image_get,this.pixelPosSize.getPos_x()-image_get.getWidth()/2, y,  null);

                        // Dispose of the Graphics2D object
                        outputG2d.dispose();

                        // Update image_out with the new image
                        this.image_out = outputImage;
                         this.refreshPreview();
        }
        
               public void setTextChanged(boolean textChanged) {
                this.textChanged = textChanged;
        }
}
