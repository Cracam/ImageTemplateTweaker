package ImageProcessor.ImageGenerators;

import Exceptions.XMLExeptions.XMLErrorInModelException;
import ImageProcessor.DesignNode;
import ImageProcessor.ImageBuilder;
import ImageProcessor.ImageGenerator;
import Layers.SubClasses.QuadrupletFloat;
import Layers.SubClasses.QuadrupletInt;
import ResourcesManager.XmlManager;
import static ResourcesManager.XmlManager.extractSingleElement;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 *
 * @author Camille LECOURT
 */
public class GeneratorRandomImageAllocation extends ImageGenerator {

        private ArrayList<QuadrupletFloat> positions;
        private float maxSizeX;
        private float maxSizeY;

        private ArrayList<QuadrupletInt> pixelPositions;

        private Element interfaceLoaderElement; //the element info to load every 

        public GeneratorRandomImageAllocation(DesignNode upperDE, Element elt) throws XMLErrorInModelException {
                super(upperDE, elt);
        }

        @Override
        protected void generateFromElement() throws XMLErrorInModelException {
                //Loading each positions

                Element subElt = extractSingleElement(elt.getElementsByTagName("PosSizes"));

                NodeList subElements = subElt.getElementsByTagName("PosSize");
                float pos_x, pos_y, size_x, size_y;
                Element subElement;

                for (int i = 0; i < subElements.getLength(); i++) {
                        subElement = (Element) subElements.item(i);
                        pos_x = XmlManager.getFloatAttribute(subElement, "pos_x", 0);
                        pos_y = XmlManager.getFloatAttribute(subElement, "pos_y", 0);
                        size_x = XmlManager.getFloatAttribute(subElement, "size_x", 0);
                        size_y = XmlManager.getFloatAttribute(subElement, "size_y", 0);
                        positions.add(new QuadrupletFloat(pos_x, pos_y, size_x, size_y));

                        if (maxSizeX < size_x) {
                                maxSizeX = size_x;
                        }
                        if (maxSizeY < size_y) {
                                maxSizeY = size_y;
                        }
                        DRYRefreshDPI();
                }
                
                 interfaceLoaderElement = extractSingleElement( subElt.getElementsByTagName("SubLayer"));
                
        }

        @Override
        public void DRYUpdate() {
                throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
        }

        @Override
        public void DRYRefreshDPI() {
                float factor = ((ImageBuilder) getUpperOrHimselfDN(ImageBuilder.class)).getDesignBuilder().getPixelMmFactor();
                System.out.println("taille du facteur" + factor);
                this.x_p_size = (int) (this.x_size * factor);
                this.y_p_size = (int) (this.y_size * factor);
                this.imageOut = new BufferedImage(x_p_size, x_p_size, BufferedImage.TYPE_INT_ARGB);
                pixelPositions.clear();

                for (QuadrupletFloat posSize : positions) {
                        pixelPositions.add(new QuadrupletInt(posSize, factor));
                }

        }
}
