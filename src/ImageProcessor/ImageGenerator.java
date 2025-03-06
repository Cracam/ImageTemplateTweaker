
package ImageProcessor;

import Exceptions.XMLExeptions.GetAttributeValueException;

import ImageProcessor.ImageGenerators.GeneratorCustomImage;
import ImageProcessor.ImageGenerators.GeneratorCustomText;
import ImageProcessor.ImageGenerators.GeneratorFixedImage;
import ImageProcessor.ImageGenerators.GeneratorFixedTextCustomStyleCustomColor;

import Layers.SubClasses.QuadrupletFloat;
import Layers.SubClasses.QuadrupletInt;
import ResourcesManager.XmlManager;
import interfaces.Interface;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.w3c.dom.Element;

/**
 *
 * @author Camille LECOURT
 */
public abstract class ImageGenerator extends DesignNode {
        protected QuadrupletFloat posSize;
        protected QuadrupletInt pixelPosSize;

 public ImageGenerator(DesignNode upperDE,Element elt) throws GetAttributeValueException {
                super( upperDE, elt);
        }
        
        
       

        @Override
      public  void generateFromElement(Element elt) throws GetAttributeValueException  {
             Element subElt = (Element) elt.getElementsByTagName("pos").item(0);
                float pos_x = XmlManager.getFloatAttribute(subElt, "pos_x", 0);
                float pos_y = XmlManager.getFloatAttribute(subElt, "pos_y", 0);

                subElt = (Element) elt.getElementsByTagName("size").item(0);
                float size_x = XmlManager.getFloatAttribute(subElt, "size_x", 0);
                float size_y = XmlManager.getFloatAttribute(subElt, "size_y", 0);

                 posSize = new QuadrupletFloat(pos_x, pos_y, size_x, size_y);
                DRYgenerateFromElement(elt);
        }

        @Override
        public abstract void DRYUpdate();
        
         abstract void DRYgenerateFromElement(Element elt) throws GetAttributeValueException;
         
         
         
         
         
         
         
         ////////////////////////////////////////////
              public static final Map<String, Class<? extends ImageGenerator>> generatorTypesMap = Map.of(
                "G_Fixed_Image", GeneratorFixedImage.class, 
                "G_Custom_Image", GeneratorCustomImage.class,
                "G_Custom_Text",GeneratorCustomText.class,
                "G_Fixed_Text_Custom_Color_Custom_Style",GeneratorFixedTextCustomStyleCustomColor.class
                 );
         
          /**
         * This code will load the layer using a Strin identifier to get the type of the Layer
         * @param type
         * @param upperDE
         * @param elt
         * @return
         * @throws GetAttributeValueException 
         */
          public static ImageGenerator createGenerator(String type, DesignNode upperDE,Element elt ) throws GetAttributeValueException {

                if (!generatorTypesMap.containsKey(type)) {
                        throw new GetAttributeValueException("This generator type does not exist : " + type);
                }
                
                try {

                        Class<? extends ImageGenerator> subclass = generatorTypesMap.get(type);
                        Constructor<? extends ImageGenerator> constructor = subclass.getConstructor(DesignNode.class, Element.class );
                        
                        return constructor.newInstance( upperDE, elt  );

                } catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException ex) {
                        Logger.getLogger(Interface.class.getName()).log(Level.SEVERE, null, ex);
                            ex.printStackTrace(); // Print the stack trace

                        return null;
                }
        }
}
