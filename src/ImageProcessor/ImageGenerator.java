
package ImageProcessor;

import Exceptions.XMLExeptions.GetAttributeValueException;
import Layers.SubClasses.QuadrupletFloat;
import ResourcesManager.XmlManager;
import static ResourcesManager.XmlManager.getStringAttribute;
import org.w3c.dom.Element;

/**
 *
 * @author Camille LECOURT
 */
public abstract class ImageGenerator extends DesignNode {

        public ImageGenerator(DesignNode upperDE,Element elt) throws GetAttributeValueException {
                super( upperDE, elt);
        }

        @Override
        void generateFromElement(Element elt) throws GetAttributeValueException  {
            this.name  =getStringAttribute(elt,"name","ERROR");
                
                //Get dim and size of the layer
                Element subElt =(Element) elt.getElementsByTagName("pos").item(0);
                float pos_x =XmlManager.getFloatAttribute(subElt, "pos_x", 0);
                float pos_y = XmlManager.getFloatAttribute(subElt, "pos_y", 0);
                
                subElt =(Element) elt.getElementsByTagName("size").item(0);
                float size_x = XmlManager.getFloatAttribute(subElt, "size_x", 0);
                float size_y =XmlManager.getFloatAttribute(subElt, "size_y", 0);

                QuadrupletFloat posSize = new QuadrupletFloat(pos_x, pos_y, size_x, size_y);
                
                DRYgenerateFromElement(elt);
        }

        @Override
        public abstract void DRYUpdate();
        
         abstract void DRYgenerateFromElement(Element elt) throws GetAttributeValueException;
}
