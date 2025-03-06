/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ImageProcessor.ImageGenerators;

import ImageProcessor.ImageGenerator;
import Exceptions.XMLExeptions.GetAttributeValueException;
import ImageProcessor.DesignNode;
import interfaces.InterfaceCustomImage;
import org.w3c.dom.Element;

/**
 *
 * @author Camille LECOURT
 */
public class GeneratorCustomImage extends ImageGenerator {



        public GeneratorCustomImage(DesignNode upperDE,Element elt ) throws GetAttributeValueException {
                super(upperDE,elt);
        }

        
        @Override
        public void refreshImageGet() {
               this.image_get= ((InterfaceCustomImage) (this.linkedInterface)).getImageOut( this.pixelPosSize.getSize_x(), this.pixelPosSize.getSize_y());
        }

      


        @Override
        public void readNode(Element paramNode) {
        }

       

        /**
         * The work of our method does not change if the DPI change
         */
        @Override
        public void DPIChanged() {
        }

        @Override
        public void DRYUpdate() {
                throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
        }

        @Override
        void DRYgenerateFromElement(Element elt) throws GetAttributeValueException {
                throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
        }

      

        

}
