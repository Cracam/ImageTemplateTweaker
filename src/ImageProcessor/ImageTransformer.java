/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ImageProcessor;

import Exceptions.XMLExeptions.GetAttributeValueException;
import static ImageProcessor.ImageGenerator.generatorTypesMap;
import ImageProcessor.ImagesTransformers.TransformerCustomColor;
import ImageProcessor.ImagesTransformers.TransformerMovableFixedImage;
import ImageProcessor.ImagesTransformers.TransformerRandomImageAllocation;
import ImageProcessor.ImagesTransformers.TransformerRandomImageDispersion;
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

        private BufferedImage image_in;
        
        
        public ImageTransformer( DesignNode upperDE,  Element elt,String name) throws GetAttributeValueException {
                super( upperDE,  elt);
                this.name=name;
        }

        @Override
         void generateFromElement(Element elt) throws GetAttributeValueException  {
                
                 DRYgenerateFromElement(elt);
        }
         
        abstract void DRYgenerateFromElement(Element elt) throws GetAttributeValueException;

        @Override
        public void DRYUpdate() {
                ImageTransformer lowerDE =(ImageTransformer) this.getLowerDN(ImageTransformer.class);
                 if(lowerDE!=null)  DRY_DRYUpdate( lowerDE.getImageOut()); 
        }
        
        public abstract void DRY_DRYUpdate(BufferedImage img_in);
        
        
        
        
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
         * @throws GetAttributeValueException 
         */
          public static ImageTransformer createTransformer(String type,  ArrayList<DesignNode> uppersDE,Element elt ) throws GetAttributeValueException {

                if (!generatorTypesMap.containsKey(type)) {
                        throw new GetAttributeValueException("This generator type does not exist : " + type);
                }
                
                try {

                        Class<? extends ImageTransformer> subclass = transformersTypesMap.get(type);
                        Constructor<? extends ImageTransformer> constructor = subclass.getConstructor(ArrayList.class, Element.class );
                        
                        return constructor.newInstance( uppersDE, elt  );

                } catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException ex) {
                        Logger.getLogger(Interface.class.getName()).log(Level.SEVERE, null, ex);
                            ex.printStackTrace(); // Print the stack trace

                        return null;
                }
}
          
          
          
             /**
         * This code will load the layer using a Strin identifier to get the type of the Layer
         * @param type
         * @param upperDE
         * @param elt
         * @return
         * @throws GetAttributeValueException 
         */
          public static ImageTransformer createTransformer(String type,  DesignNode upperDE,Element elt ) throws GetAttributeValueException {
                  ArrayList<DesignNode> arr = new ArrayList<>();
                          arr.add(upperDE);
                  return createTransformer(type,arr,elt);
          }
}
