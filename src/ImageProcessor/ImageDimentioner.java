
package ImageProcessor;

import Exceptions.XMLExeptions.GetAttributeValueException;

import ImageProcessor.ImageGenerators.GeneratorCustomImage;
import ImageProcessor.ImageGenerators.GeneratorCustomText;
import ImageProcessor.ImageGenerators.GeneratorFixedImage;
import ImageProcessor.ImageGenerators.GeneratorFixedTextCustomStyle;

import interfaces.Interface;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.w3c.dom.Element;

/**
 *
 * @author Camille LECOURT
 */
public abstract class ImageDimentioner extends DesignNode {
     protected float x_size;
      protected   float y_size;

    protected     int x_p_size;
     protected    int y_p_size;

 public ImageDimentioner(DesignNode upperDE,Element elt) throws GetAttributeValueException {
                super( upperDE, elt);
        }
        
        
 public void setDim(int x_size,int y_size){
         this.x_size=x_size;
         this.y_size=y_size;
 }
       

        @Override
      public abstract void generateFromElement(Element elt) throws GetAttributeValueException;
      
        @Override
        public abstract void DRYUpdate();
        
         
         
         
           /**
         * this function is use to compute the size of the image in pixel
         */
        private void computeXY_p_size() {
                float factor =( (ImageBuilder )getUpperOrHimselfDN(ImageBuilder.class)).getDesignBuilder().getPixelMmFactor();
                this.x_p_size = (int) (this.x_size * factor);
                this.y_p_size = (int) (this.y_size * factor);
        }
         
         
        
}
