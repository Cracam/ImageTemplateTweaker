package Layers;

import Layers.SubClasses.QuadrupletFloat;
import Layers.SubClasses.QuadrupletInt;
import ResourcesManager.ResourcesManager;
import ResourcesManager.XmlManager;
import imageBuilder.ImageBuilder;
import interfaces.Interface;
import interfaces.InterfaceFixedTextCustomStyleCustomColor;
import org.w3c.dom.Element;

/**
 *
 * @author Camille LECOURT
 */
public class LayerFixedTextCustomStyleCustomColor extends LayerCustomText {
        private int[][] opacityMap;
        private String text;
       
        
        public LayerFixedTextCustomStyleCustomColor(String layerName, ResourcesManager modelResources, Interface layerInterface, ImageBuilder linkedImageBuilder, QuadrupletFloat posSize) {
                super(layerName, modelResources, layerInterface, linkedImageBuilder, posSize);
        }


        

                        
        @Override
        public void refreshImageGet() {
               
                
                
        }
        
    
   /**
         * 
         * @param paramNode
         */
        @Override
        public void readNode(Element paramNode) {
              Element element=  (Element) paramNode.getElementsByTagName("Text").item(0);
              
                this.textHeightFactor=XmlManager.getFloatAttribute(element,"TextHeightFactor",this.textHeightFactor);
                this.textSizeMin=XmlManager.getFloatAttribute(element,"TextSizeMin",this.textSizeMin);
                this.textSizeMax=XmlManager.getFloatAttribute(element,"TextSizeMax",this.textSizeMax);
                 this.text=XmlManager.getStringAttribute(element,"Text",this.text);
        }         

        
}
