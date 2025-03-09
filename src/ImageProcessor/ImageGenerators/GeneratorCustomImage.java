/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ImageProcessor.ImageGenerators;

import ImageProcessor.ImageGenerator;
import Exceptions.XMLExeptions.GetAttributeValueException;
import ImageProcessor.DesignNode;
import AppInterface.Interfaces.InterfaceCustomImage;
import ResourcesManager.XmlManager;
import org.w3c.dom.Element;

/**
 *
 * @author Camille LECOURT
 */
public class GeneratorCustomImage extends ImageGenerator {

        public GeneratorCustomImage(DesignNode upperDE, Element elt) throws GetAttributeValueException {
                super(upperDE, elt);
        }

        @Override
        public void DRYUpdate() {
                this.imageOut = ((InterfaceCustomImage) (this.linkedinterface)).getImageOut(this.x_p_size, this.y_p_size);
        }

        @Override
        public void DRYgenerateFromElement(Element elt) throws GetAttributeValueException {
        }

       

}
