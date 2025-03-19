package ImageProcessor.ImageGenerators;

import Exceptions.XMLExeptions.XMLErrorInModelException;
import ImageProcessor.DesignNode;
import ImageProcessor.ImageGenerator;
import AppInterface.Interfaces.InterfaceRandomImageDispersion;
import Exceptions.DesingNodeLowerNodeIsAnormalyVoidException;
import ImageProcessor.ImageBuilder;
import static ImageProcessor.ImageGenerator.createGenerator;
import ImageProcessor.ImageTransformer;
import static ImageProcessor.ImageTransformer.createTransformer;
import ImageProcessor.Layer;
import static ResourcesManager.XmlManager.extractSingleElement;
import static ResourcesManager.XmlManager.getFloatAttribute;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import staticFunctions.ObjectDispersion;

/**
 *
 * @author Camille LECOURT
 */
public class GeneratorRandomImageDispersion extends ImageGenerator {

        private float maxXSize;
        private float minXSize;
        private float maxYSize;
        private float minYSize;

        private float maxInterval;
        private float minInterval;

        private static float LOCAL_UPSCALE = 2;

        public GeneratorRandomImageDispersion(DesignNode upperDE, Element elt) throws XMLErrorInModelException {
                super(upperDE, elt);
        }

        @Override
        protected void generateFromElement() throws XMLErrorInModelException {
                String key;
                Element subElt = extractSingleElement(elt.getElementsByTagName("Size"));
                this.maxXSize = getFloatAttribute(subElt, "maxXSize", (float) 1.0);
                this.minXSize = getFloatAttribute(subElt, "minXSize", (float) 2.0);
                this.maxYSize = getFloatAttribute(subElt, "maxYSize", (float) 7.0);
                this.minYSize = getFloatAttribute(subElt, "minYSize", (float) 7.0);

                subElt = extractSingleElement(elt.getElementsByTagName("Interval"));
                this.maxInterval = getFloatAttribute(subElt, "maxInterval", (float) 1.0);
                this.minInterval = getFloatAttribute(subElt, "minInterval", (float) 2.0);

                subElt = extractSingleElement(elt.getElementsByTagName("SubLayer"));

                Element subSubSubElt;
                DesignNode currentUpperDN = this;
                Element subSubElt = extractSingleElement(subElt.getElementsByTagName("Transformers"));
                if (subSubElt != null) {
                        NodeList nodeTransformersList = subSubElt.getChildNodes();

                        //running in the inverse 
                        for (int i = nodeTransformersList.getLength() - 1; i >= 0; i--) {
                                if (nodeTransformersList.item(i).getNodeType() == Node.ELEMENT_NODE) { //To avoid text node and comment node

                                        subSubSubElt = (Element) nodeTransformersList.item(i);
                                        key = subSubSubElt.getNodeName(); // key for defining the layer and the Interface
                                        currentUpperDN = createTransformer(key, currentUpperDN, subSubSubElt);
                                }
                        }
                }

                ////////
                //creating the last element of the chain if there is generator.
                //if not it will be linked to another layer.
                subSubElt = extractSingleElement(subElt.getElementsByTagName("Generator"));

                if (subSubElt == null) {
                        throw new XMLErrorInModelException("Le Layer " + this.name + "n'a pas de bloc: Generator valide");
                }

                subSubSubElt = extractSingleElement(subSubElt.getChildNodes());

                if (subSubSubElt == null) {
                        throw new XMLErrorInModelException("Le bloc generator du Layer " + this.name + "n'a pas de sous générateur valide\n\n " + subElt.getChildNodes().getLength());
                }

                key = subSubSubElt.getNodeName();

                currentUpperDN = createGenerator(key, currentUpperDN, subSubSubElt);//mettre 
                ((ImageGenerator) currentUpperDN).setDim(LOCAL_UPSCALE * maxXSize, LOCAL_UPSCALE * maxYSize);
        }

        
        
        @Override
        public void DRYUpdate() {
                float pixelMillimeterFactor = this.getUpperDN(ImageBuilder.class).getDesignBuilder().getPixelMmFactor();
                // Getting the right imageGet
                try {
                        DesignNode lowerDN = this.getLowerDNForChilds(ImageTransformer.class);
                        if (lowerDN == null) {
                                lowerDN = this.getLowerDNForChilds(ImageGenerator.class);
                        }
                        if (lowerDN == null) {
                                throw new DesingNodeLowerNodeIsAnormalyVoidException("The Layer : " + this.name + " does not have the ImageGenerator");
                        }
                        BufferedImage image_get = lowerDN.getImageOut();

                        // Créer une instance de ObjectDispersion
                        InterfaceRandomImageDispersion inter = ((InterfaceRandomImageDispersion) (this.getLinkedinterface()));
                        int lowIntervalSize = (int) ((this.minInterval + inter.getLowIntervalSize() * (maxInterval - minInterval)) * pixelMillimeterFactor);
                        int highIntervalSize = (int) ((this.minInterval + inter.getHighIntervalSize() * (maxInterval - minInterval)) * pixelMillimeterFactor);

                        int lowImageXSize = (int) ((this.minXSize + inter.getLowImageSize() * (maxXSize - minXSize)) * pixelMillimeterFactor);
                        int highImageXSize = (int) ((this.minXSize + inter.getHighImageSize() * (maxXSize - minXSize)) * pixelMillimeterFactor);

                        ObjectDispersion disperser = new ObjectDispersion(image_get, lowIntervalSize, highIntervalSize, lowImageXSize, highImageXSize, this.x_p_size, this.y_p_size);

                        // Disperser les objets
                        List<ObjectDispersion.ObjectPosition> positions = disperser.disperseObjects();

                        this.imageOut = new BufferedImage(this.x_p_size, this.y_p_size, BufferedImage.TYPE_INT_ARGB);
                        Graphics2D g2d = this.imageOut.createGraphics();
                        // Activer l'anticrénelage
                        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
                        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                        // Dessiner chaque image à la position, taille et angle spécifiés
                        for (ObjectDispersion.ObjectPosition position : positions) {
                                AffineTransform transform = new AffineTransform();
                                transform.translate(position.x, position.y);
                                transform.rotate(Math.toRadians(position.angle), position.width / 2, position.height / 2);
                                transform.scale(position.width / (double) image_get.getWidth(), position.height / (double) image_get.getHeight());
                                g2d.drawImage(image_get, transform, null);
                        }

                        g2d.dispose();

                } catch (DesingNodeLowerNodeIsAnormalyVoidException ex) {
                        Logger.getLogger(Layer.class.getName()).log(Level.SEVERE, null, ex);
                }
        }
}
