package ImageProcessor;

import AppInterface.DesignInterfaceLinker;
import Exceptions.XMLExeptions.XMLErrorInModelException;

import interfaces.Interface;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.w3c.dom.Element;

/**
 *
 * @author Camille LECOURT
 */
public abstract class ImageGenerator extends ImageDimentioner {

        public ImageGenerator(DesignNode upperDE, Element elt) throws XMLErrorInModelException {
                super(upperDE, elt);
                generateFromElement();
        }

       

        @Override
        public abstract void DRYUpdate();

        ////////////////////////////////////////////
 

        /**
         * This code will load the layer using a Strin identifier to get the
         * type of the Layer
         *
         * @param type
         * @param upperDE
         * @param elt
         * @return
         * @throws XMLErrorInModelException
         */
        public static ImageGenerator createGenerator(String type, DesignNode upperDE, Element elt) throws XMLErrorInModelException {

              Class classBuilder = DesignInterfaceLinker.getDesignNode(type);
                
                if (classBuilder==null || classBuilder.isAssignableFrom(ImageGenerator.class)) {
                        throw new XMLErrorInModelException("This generator type does not exist : " + type);
                } 
                

                try {

                        Class<? extends ImageGenerator> subclass = classBuilder;
                        Constructor<? extends ImageGenerator> constructor = subclass.getConstructor(DesignNode.class, Element.class);

                        return constructor.newInstance(upperDE, elt);

                } catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException ex) {
                        Logger.getLogger(Interface.class.getName()).log(Level.SEVERE, null, ex);
                        ex.printStackTrace(); // Print the stack trace
                         throw new XMLErrorInModelException("Error in the creation of this Generator :  " + type);
                         
                }
        }

        
          

}
