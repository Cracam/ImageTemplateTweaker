/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ImageProcessor;

import Exceptions.DesingNodeLowerNodeIsAnormalyVoidException;
import Exceptions.XMLExeptions.GetAttributeValueException;
import java.awt.image.BufferedImage;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.w3c.dom.Element;

/**
 *
 * @author Camille LECOURT
 */
public  class Layer extends DesignNode {
        
        
        public Layer( DesignNode upperDE, Element elt) throws GetAttributeValueException {
                super( upperDE, elt);
        }

        @Override
        public void DRYUpdate() {
                try {
                        BufferedImage image_get;
                        
                        // Getting the right imageGet
                        DesignNode lowerDN = this.getLowerDE(ImageTransformer.class);
                        if(lowerDN==null){
                                lowerDN = this.getLowerDE(ImageGenerator.class);
                        }
                        if(lowerDN==null)throw new DesingNodeLowerNodeIsAnormalyVoidException("The Layer"+ this.name +" does not have the ImageGenerator");
                         image_get=  lowerDN.getImageOut();
                        
                         
                         
                        
                        
                        
                        this.imageOut=this.getLowerDE().getImageOut();
                } catch (DesingNodeLowerNodeIsAnormalyVoidException ex) {
                        Logger.getLogger(Layer.class.getName()).log(Level.SEVERE, null, ex);
                }

        }

     

        @Override
        void generateFromElement(Element elt) throws GetAttributeValueException {
                throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
        }

        @Override
        public void addLowerDN(DesignNode lowersDE) {
                throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
        }

        public BufferedImage getImage_get() {
                return image_get;
        }

        public void setImage_get(BufferedImage image_get) {
                this.image_get = image_get;
        }
        
        
        
        
}
