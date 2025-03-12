/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ImageProcessor;

import Exceptions.DesingNodeLowerNodeIsAnormalyVoidException;
import Exceptions.XMLExeptions.XMLErrorInModelException;
import ImageProcessor.ImagesTransformers.TransformerCustomColor;
import ImageProcessor.ImagesTransformers.TransformerMovableFixedImage;
import ImageProcessor.ImagesTransformers.TransformerRandomImageAllocation;
import ImageProcessor.ImagesTransformers.TransformerRandomImageDispersion;
import AppInterface.DesignInterfaceLinker;
import interfaces.Interface;
import java.awt.image.BufferedImage;
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
public abstract class ImageTransformer extends DesignNode{

        
        
        public ImageTransformer( DesignNode upperDE,  Element elt) throws XMLErrorInModelException {
                super( upperDE,  elt);
                generateFromElement();
        }

      

        @Override
        public void DRYUpdate() {              
                try {
                 DesignNode lowerDN = this.getLowerDN(ImageTransformer.class);
                        if (lowerDN == null) {
                                lowerDN = this.getLowerDN(ImageGenerator.class);
                        }
                        if (lowerDN == null) {
                                 throw new DesingNodeLowerNodeIsAnormalyVoidException("The Layer" + this.name + " does not have the ImageGenerator");
                        }
                        DRY_DRYUpdate( lowerDN.getImageOut()); 
                        
                } catch (DesingNodeLowerNodeIsAnormalyVoidException ex) {
                                 Logger.getLogger(ImageTransformer.class.getName()).log(Level.SEVERE, null, ex);
                         }
        }
        
        public abstract void DRY_DRYUpdate(BufferedImage img_in);
        
        
        
         @Override
        public void DRYRefreshDPI() {
             //no update to any sort of size in Imaes transformers
        }
        
        
        
        
        
        
          ////////////////////////////////////////////
                   public static final Map<String, Class<? extends ImageTransformer>> transformersTypesMap = Map.of(
                        "Custom_Color", TransformerCustomColor.class, 
                        "Mouvable_Fixed_Image", TransformerMovableFixedImage.class,
                           "Random_Image_Dispersion",TransformerRandomImageDispersion.class,
                            "Random_Image_Allocations",TransformerRandomImageAllocation.class
        );
         
          /**
         * This code will load the layer using a Strin identifier to get the type of the Layer
         * @param type
         * @param uppersDE
         * @param elt
         * @return
         * @throws XMLErrorInModelException 
         */
          public static ImageTransformer createTransformer(String type,  DesignNode uppersDE,Element elt ) throws XMLErrorInModelException {
                Class classBuilder = DesignInterfaceLinker.getDesignNode(type);
                
                if (classBuilder==null || classBuilder.isAssignableFrom(ImageTransformer.class)) {
                        throw new XMLErrorInModelException("This Transformer type does not exist : " + type);
                } 
                
                
                try {
                        Class<? extends ImageTransformer> subclass = classBuilder;
                        Constructor<? extends ImageTransformer> constructor = subclass.getConstructor(DesignNode.class, Element.class );
                        
                        return constructor.newInstance( uppersDE, elt  );

                } catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException ex) {
                        Logger.getLogger(Interface.class.getName()).log(Level.WARNING, null, ex);
                            ex.printStackTrace(); // Print the stack trace
                                 throw new XMLErrorInModelException("Error in the creation of this Transformer :  " + type);
                }
}
          
          
          
}
