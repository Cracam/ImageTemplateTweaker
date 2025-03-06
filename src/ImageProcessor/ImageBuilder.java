/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ImageProcessor;

import Exceptions.XMLExeptions.GetAttributeValueException;
import Layers.SubClasses.QuadrupletFloat;
import ResourcesManager.XmlManager;
import static ResourcesManager.XmlManager.getStringAttribute;
import designBuilder.DesignBuilder;
import org.w3c.dom.Element;

/**
 *
 * @author Camille LECOURT
 */
public class ImageBuilder extends ImageDimentioner {
        DesignBuilder designBuilder;
        
        
        
        public ImageBuilder(DesignNode upperDE, Element elt,DesignBuilder designBuilder) throws GetAttributeValueException {
                super(upperDE, elt);
                this.designBuilder=designBuilder;
        }

        @Override
        public void DRYUpdate() {
                this.imageOut=this.getLowerDN(Layer.class).getImageOut();
        }

        @Override
        public void generateFromElement(Element elt) throws GetAttributeValueException {
                 String key;
                Element subElt,subSubElt;
                DesignNode currentUpperDN;
                
                this.name = getStringAttribute(elt, "name", "ERROR");
                
                 subElt = (Element) elt.getElementsByTagName("size").item(0);
                x_size = XmlManager.getFloatAttribute(subElt, "size_x", 0);
                y_size= XmlManager.getFloatAttribute(subElt, "size_y", 0);

                
        }
        
        
        
        public DesignBuilder getDesignBuilder(){
                return this.designBuilder;
        }
        
        
}
