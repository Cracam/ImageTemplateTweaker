package ImageProcessor.ImagesTransformers;

import AppInterface.Interfaces.InterfaceCustomColor;
import Exceptions.XMLExeptions.XMLErrorInModelException;
import GradientCreatorInterface.GradientCreatorInterface;
import ImageProcessor.DesignNode;
import ImageProcessor.ImageTransformer;
import static ResourcesManager.XmlManager.extractSingleElement;
import static ResourcesManager.XmlManager.getDoubleAttribute;
import static ResourcesManager.XmlManager.getStringAttribute;
import java.awt.Color;
import java.awt.image.BufferedImage;
import org.w3c.dom.Element;
import staticFunctions.StaticImageEditing;

/**
 *
 * @author Camille LECOURT
 */
public class TransformerFixedColor extends ImageTransformer {

        private GradientCreatorInterface gradientPicker;
        private String gradientName;
        private Color color1;
        private Color color2;
        private double colorIntensity;
        private double param1;
        private double param2;

        private int[][] opacityMap;
        private BufferedImage image_getRaw;

        public TransformerFixedColor(DesignNode upperDE, Element elt) throws XMLErrorInModelException {
                super(upperDE, elt);
        }

        @Override
        protected void generateFromElement() throws XMLErrorInModelException {

                   Element subElt = extractSingleElement(elt.getElementsByTagName("Gradient"));
                   gradientPicker=new GradientCreatorInterface();
                gradientName = getStringAttribute(subElt, "gradient_Name", "ERROR");
                 color1 = StaticImageEditing.hexToColor( getStringAttribute(subElt, "color_1", "ERROR"));
                 color2 = StaticImageEditing.hexToColor(getStringAttribute(subElt, "color_2", "ERROR"));
                 colorIntensity = getDoubleAttribute(subElt, "colorIntensity", 0.0);
                 param1 = getDoubleAttribute(subElt, "param_1", 0.0);
                 param2 = getDoubleAttribute(subElt, "param_2", 0.0);
                                
                gradientPicker.setInterfaceState(gradientName, color1, color2, colorIntensity, param1, param2);

        }

        @Override
        public void DRY_DRYUpdate(BufferedImage img_in) {
                if (image_getRaw != img_in) {
                        image_getRaw = img_in;
                        opacityMap = staticFunctions.StaticImageEditing.transformToOpacityArray(img_in);
                }
                this.imageOut = this.gradientPicker.getImageOut(opacityMap);  
        }

}
