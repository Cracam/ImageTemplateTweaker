/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ImageProcessor;

import Exceptions.XMLExeptions.GetAttributeValueException;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import org.w3c.dom.Element;

/**
 *
 * @author Camille LECOURT
 */
public abstract class DesignNode {
        private final DesignNode upperDE;
          protected  String name;
        private ArrayList<DesignNode> lowersDN;
        BufferedImage imageOut;
            
        public DesignNode(DesignNode upperDE, Element elt) throws GetAttributeValueException{
                this.upperDE = upperDE;
                upperDE.addLowerDN(this);
                generateFromElement(elt);
        }

               abstract void generateFromElement(Element elt) throws GetAttributeValueException;
        
         /**
         * Thes update program consserning the element
         */
        public abstract void DRYUpdate();
        
        
           /**
        * This update all the lower elements
        */
        public void update( ){
                DRYUpdate();
                upperDE.update();
        }
            
             
             
             public DesignNode getUpperDE() {
                return upperDE;
        }
             
       
        public void updateLower() {
               for(DesignNode designNode : lowersDN){
                       designNode.updateLower();
               }
        }
        
         public void addLowerDN(DesignNode lowerDN) {
            this.lowersDN.add(lowerDN);
        }
         
         
         
        public BufferedImage getImageOut() {
                return imageOut;
        }

        public void setImageOut(BufferedImage imageOut) {
                this.imageOut = imageOut;
        }
        
        /**
         * Return the first class of the 
         * @param nodeClass
         * @return 
         */
        public DesignNode getLowerDE(Class<?> nodeClass) {
                for (DesignNode designNode : lowersDN) {
                        if (designNode.getClass() == nodeClass) {
                                return designNode;
                        }
                }
                return null; // Retourne null si aucun élément correspondant n'est trouvé
        }
            
        
        
        
        
   
}
