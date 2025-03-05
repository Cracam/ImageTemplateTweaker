
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
                DRYgenerateFromElement(elt);
        }

        @Override
        public abstract void DRYUpdate();
        
         abstract void DRYgenerateFromElement(Element elt) throws GetAttributeValueException;
}
