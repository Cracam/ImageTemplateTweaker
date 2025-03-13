
package ImageProcessor;

import Exceptions.XMLExeptions.XMLErrorInModelException;
import ResourcesManager.XmlManager;
import static ResourcesManager.XmlManager.getStringAttribute;

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

 public ImageDimentioner(DesignNode upperDE,Element elt) throws XMLErrorInModelException {
                super( upperDE, elt);
        }
        
        
 public void setDim(float  x_size,float y_size){
         this.x_size=x_size;
         this.y_size=y_size;
         DRYRefreshDPI();
         
 }
       

        
        @Override
          protected String DRYtoString(){
                  return " of size : "+this.x_p_size+"  ,  "+this.y_p_size;
          }
      
        
        
        @Override
        public abstract void DRYUpdate();
        
         
         

            @Override
        public void DRYRefreshDPI() {
               float factor =( (ImageBuilder )getUpperOrHimselfDN(ImageBuilder.class)).getDesignBuilder().getPixelMmFactor();
                System.out.println("taille du facteur"+factor);
                this.x_p_size = (int) (this.x_size * factor);
                this.y_p_size = (int) (this.y_size * factor);
        }
        
}
