/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ImageProcessor.ImagesTransformers;

import AppInterface.Interfaces.InterfaceCustomColor;
import Exceptions.XMLExeptions.XMLErrorInModelException;
import ImageProcessor.DesignNode;
import ImageProcessor.ImageTransformer;
import java.awt.image.BufferedImage;
import org.w3c.dom.Element;

/**
 *
 * @author Camille LECOURT
 */
public  class TransformerCustomColor extends ImageTransformer {


        private int[][]opacityMap;
        private BufferedImage image_getRaw;

        public TransformerCustomColor(DesignNode upperDE, Element elt) throws XMLErrorInModelException {
                super(upperDE, elt);
        }

  

        @Override
        protected void generateFromElement() throws XMLErrorInModelException {
        }
        
        
        @Override
        public void DRY_DRYUpdate(BufferedImage img_in) {
                if(image_getRaw!=img_in){
                        image_getRaw=img_in;
                        opacityMap=staticFunctions.StaticImageEditing.transformToOpacityArray(img_in);
                }
                this.imageOut=((InterfaceCustomColor) (this.linkedinterface)).getImageOut(opacityMap);
        }




   

}
