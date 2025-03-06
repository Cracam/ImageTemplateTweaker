/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ImageProcessor;

import Exceptions.XMLExeptions.GetAttributeValueException;
import static ResourcesManager.XmlManager.getStringAttribute;
import java.awt.image.BufferedImage;
import org.w3c.dom.Element;

/**
 *
 * @author Camille LECOURT
 */
public abstract class ImageTransformer extends DesignNode{

        private BufferedImage image_in;
        
        
        public ImageTransformer( DesignNode upperDE,  Element elt,String name) throws GetAttributeValueException {
                super( upperDE,  elt);
                this.name=name;
        }

        @Override
         void generateFromElement(Element elt) throws GetAttributeValueException  {
                
                 DRYgenerateFromElement(elt);
        }
         
        abstract void DRYgenerateFromElement(Element elt) throws GetAttributeValueException;

        @Override
        public void DRYUpdate() {
                ImageTransformer lowerDE =(ImageTransformer) this.getLowerDE(ImageTransformer.class);
                 if(lowerDE!=null)  DRY_DRYUpdate( lowerDE.getImageOut()); 
        }
        
        public abstract void DRY_DRYUpdate(BufferedImage img_in);
        
        
}
