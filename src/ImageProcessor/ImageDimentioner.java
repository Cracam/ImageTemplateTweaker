
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
        
        
 public void setDim(int x_size,int y_size){
         this.x_size=x_size;
         this.y_size=y_size;
 }
       

        
      public abstract void DRYgenerateFromElement(Element elt) throws XMLErrorInModelException;
      
       @Override
        public void generateFromElement() throws XMLErrorInModelException {
                Element subElt;
                
                this.name = getStringAttribute(elt, "name", "ERROR");
                
                 subElt = (Element) elt.getElementsByTagName("size").item(0);
                x_size = XmlManager.getFloatAttribute(subElt, "size_x", 0);
                y_size= XmlManager.getFloatAttribute(subElt, "size_y", 0);
                
                DRYgenerateFromElement(elt);
        }
      
        
        
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
         
            @Override
        public void DRYRefreshDPI() {
              computeXY_p_size();
        }
        
}
