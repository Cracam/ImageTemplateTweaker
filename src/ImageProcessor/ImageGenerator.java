
package ImageProcessor;

import Exceptions.XMLExeptions.GetAttributeValueException;

import ImageProcessor.ImageGenerators.GeneratorCustomImage;
import ImageProcessor.ImageGenerators.GeneratorCustomText;
import ImageProcessor.ImageGenerators.GeneratorFixedImage;
import ImageProcessor.ImageGenerators.GeneratorFixedTextCustomStyle;
import static ImageProcessor.ImageTransformer.createTransformer;

import Layers.SubClasses.QuadrupletFloat;
import Layers.SubClasses.QuadrupletInt;
import ResourcesManager.XmlManager;
import interfaces.Interface;
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
public abstract class ImageGenerator extends DesignNode {
       protected int x_size;
       protected int y_size;

 public ImageGenerator(DesignNode upperDE,Element elt) throws GetAttributeValueException {
                super( upperDE, elt);
        }
        
        
 public void setDim(int x_size,int y_size){
         this.x_size=x_size;
         this.y_size=y_size;
         
 }
       

        @Override
      public  void generateFromElement(Element elt) throws GetAttributeValueException  {
            
                DRYgenerateFromElement(elt);
        }

        @Override
        public abstract void DRYUpdate();
        
         abstract void DRYgenerateFromElement(Element elt) throws GetAttributeValueException;
         
         
         
         
         
         
         
         ////////////////////////////////////////////
              public static final Map<String, Class<? extends ImageGenerator>> generatorTypesMap = Map.of("G_Fixed_Image", GeneratorFixedImage.class, 
                "G_Custom_Image", GeneratorCustomImage.class,
                "G_Custom_Text",GeneratorCustomText.class,
                "G_Fixed_Text_Custom_Color_Custom_Style",GeneratorFixedTextCustomStyle.class
                 );
         
          /**
         * This code will load the layer using a Strin identifier to get the type of the Layer
         * @param type
         * @param uppersDE
         * @param elt
         * @return
         * @throws GetAttributeValueException 
         */
          public static ImageGenerator createGenerator(String type, ArrayList<DesignNode> uppersDE,Element elt ) throws GetAttributeValueException {

                if (!generatorTypesMap.containsKey(type)) {
                        throw new GetAttributeValueException("This generator type does not exist : " + type);
                }
                
                try {

                        Class<? extends ImageGenerator> subclass = generatorTypesMap.get(type);
                        Constructor<? extends ImageGenerator> constructor = subclass.getConstructor(ArrayList.class, Element.class );
                        
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
          public static ImageGenerator createGenerator(String type,  DesignNode upperDE,Element elt ) throws GetAttributeValueException {
                  ArrayList<DesignNode> arr = new ArrayList<>();
                          arr.add(upperDE);
                  return createGenerator(type,arr,elt);
          }
}
