/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ImageProcessor;

import Exceptions.DesingNodeLowerNodeIsAnormalyVoidException;
import Exceptions.XMLExeptions.GetAttributeValueException;
import static ImageProcessor.ImageGenerator.createGenerator;
import static ResourcesManager.XmlManager.getStringAttribute;
import java.awt.image.BufferedImage;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import static staticFunctions.StaticImageEditing.overlayImages;

/**
 *
 * @author Camille LECOURT
 */
public class Layer extends DesignNode {

        public Layer(DesignNode upperDE, Element elt) throws GetAttributeValueException {
                super(upperDE, elt);
        }

        @Override
        public void DRYUpdate() {
                try {
                        BufferedImage image_get;

                        // Getting the right imageGet
                        DesignNode lowerDN = this.getLowerDE(ImageTransformer.class);
                        if (lowerDN == null) {
                                lowerDN = this.getLowerDE(ImageGenerator.class);
                        }
                        if (lowerDN == null) {
                                throw new DesingNodeLowerNodeIsAnormalyVoidException("The Layer" + this.name + " does not have the ImageGenerator");
                        }
                        image_get = lowerDN.getImageOut();

                        DesignNode lowerLayer = this.getLowerDE(Layer.class);
                        if (lowerLayer == null) {
                                this.imageOut = image_get;
                        } else {
                                this.imageOut = overlayImages(lowerLayer.getImageOut(), image_get);
                        }

                } catch (DesingNodeLowerNodeIsAnormalyVoidException ex) {
                        Logger.getLogger(Layer.class.getName()).log(Level.SEVERE, null, ex);
                }
        }

        @Override
        void generateFromElement(Element elt) throws GetAttributeValueException {
                this.name = getStringAttribute(elt, "name", "ERROR");
                Element subElt = (Element) elt.getElementsByTagName("Generator").item(0);
                String key = subElt.getNodeName();

                DesignNode currentUpperDN = createGenerator(key, this, subElt);

                subElt = (Element) elt.getElementsByTagName("Transformers").item(0);
                NodeList nodeTransformersList = subElt.getChildNodes();

                for (int i = 0; i < nodeTransformersList.getLength(); i++) {
                        if (nodeTransformersList.item(i).getNodeType() == Node.ELEMENT_NODE) { //To avoid text node and comment node

                                Element subSubElt = (Element) nodeTransformersList.item(i);
                                key = subSubElt.getNodeName(); // key for defining the layer and the Interface

                                currentUpperDN = createTransformator(key, currentUpperDN, subSubElt);
                        }
                }

        }

}
