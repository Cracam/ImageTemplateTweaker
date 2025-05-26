/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ImageProcessor;

import Exceptions.XMLExeptions.XMLErrorInModelException;
import org.w3c.dom.Element;
import static staticFunctions.StaticImageEditing.createBufferedImage;

/**
 *
 * @author Camille LECOURT
 */
public class VoidImage extends DesignNode{
        int x_p;
        int y_p;
        
        public VoidImage(DesignNode upperDE, Element eltConstruct,int x_p,int y_p) throws XMLErrorInModelException {
                super(upperDE, eltConstruct);
                this.x_p=x_p;
                this.y_p=y_p;
                DRYRefreshDPI();
        }

        @Override
        protected void generateFromElement() throws XMLErrorInModelException {
        }

        @Override
        public void DRYUpdate() {
        }

        @Override
        public void DRYRefreshDPI() {
                 this.imageOut = createBufferedImage(x_p,y_p);
         }

        @Override
        protected String DRYtoString() {
                return "VoidImage";
        }
        
        
}
