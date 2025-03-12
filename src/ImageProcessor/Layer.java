/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ImageProcessor;

import Exceptions.DesingNodeLowerNodeIsAnormalyVoidException;
import static ImageProcessor.ImageGenerator.createGenerator;
import static ImageProcessor.ImageTransformer.createTransformer;
import Layers.SubClasses.QuadrupletFloat;
import Layers.SubClasses.QuadrupletInt;
import ResourcesManager.XmlManager;
import static ResourcesManager.XmlManager.getStringAttribute;
import AppInterface.DesignBuilder;
import AppInterface.DesignInterfaceLinker;
import Exceptions.XMLExeptions.XMLErrorInModelException;
import static ResourcesManager.XmlManager.extractSingleElement;
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

        protected QuadrupletFloat posSize;
        protected QuadrupletInt pixelPosSize = new QuadrupletInt(0, 0, 0, 0);
        protected String tabName;
        
        public Layer(DesignNode upperDE, Element elt) throws XMLErrorInModelException {
                super(upperDE, elt);
                generateFromElement();
        }

        @Override
        public void DRYUpdate() {
                try {
                        BufferedImage image_get;

                        // Getting the right imageGet
                        DesignNode lowerDN = this.getLowerDN(ImageTransformer.class);
                        if (lowerDN == null) {
                                lowerDN = this.getLowerDN(ImageGenerator.class);
                        }
                        if (lowerDN == null) {
                                throw new DesingNodeLowerNodeIsAnormalyVoidException("The Layer" + this.name + " does not have the ImageGenerator");
                        }
                        image_get = lowerDN.getImageOut();

                        DesignNode lowerLayer = this.getLowerDN(Layer.class);
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
        protected void generateFromElement() throws XMLErrorInModelException {
                String key;
                Element subElt, subSubElt;
                DesignNode currentUpperDN;

                this.name = getStringAttribute(elt, "name", "ERROR");
                 this.tabName = getStringAttribute(elt, "tab_name", "ERROR");
                
                subElt = extractSingleElement(elt.getElementsByTagName("pos"));
                if (subElt == null) {
                        throw new XMLErrorInModelException("L'élément 'pos' du Layer " + this.name + "est manquant ou invalide.");
                }
                float pos_x = XmlManager.getFloatAttribute(subElt, "pos_x", 0);
                float pos_y = XmlManager.getFloatAttribute(subElt, "pos_y", 0);

                subElt = extractSingleElement(elt.getElementsByTagName("size"));
                if (subElt == null) {
                        throw new XMLErrorInModelException("L'élément 'size du Layer " + this.name + "est manquant ou invalide.");
                }
                float size_x = XmlManager.getFloatAttribute(subElt, "size_x", 0);
                float size_y = XmlManager.getFloatAttribute(subElt, "size_y", 0);

                posSize = new QuadrupletFloat(pos_x, pos_y, size_x, size_y);
                DRYRefreshDPI();

                currentUpperDN = this;
                subElt = extractSingleElement(elt.getElementsByTagName("Transformers"));
                if (subElt != null) {

                        NodeList nodeTransformersList = subElt.getChildNodes();

                        //running in the inverse 
                        for (int i = nodeTransformersList.getLength() - 1; i >= 0; i--) {
                                if (nodeTransformersList.item(i).getNodeType() == Node.ELEMENT_NODE) { //To avoid text node and comment node

                                        subSubElt = (Element) nodeTransformersList.item(i);
                                        key = subSubElt.getNodeName(); // key for defining the layer and the Interface

                                        currentUpperDN = createTransformer(key, currentUpperDN, subSubElt);
                                }
                        }
                }

                ////////
                //creating the last element of the chain if there is generator.
                //if not it will be linked to another layer.
                System.out.println("\n\n####### " + elt.toString() + "\n#########");
                subElt = extractSingleElement(elt.getElementsByTagName("Generator"));

                if (subElt == null) {
                        throw new XMLErrorInModelException("Le Layer " + this.name + "n'a pas de bloc: Generator valide");
                }

                subSubElt = extractSingleElement(subElt.getChildNodes());

                if (subSubElt == null) {
                        throw new XMLErrorInModelException("Le bloc generator du Layer " + this.name + "n'a pas de sous générateur valide\n\n " + subElt.getChildNodes().getLength());
                }

                key = subSubElt.getNodeName();

                currentUpperDN = createGenerator(key, currentUpperDN, subSubElt);//mettre 
                ((ImageGenerator) currentUpperDN).setDim(pixelPosSize.getSize_x(), pixelPosSize.getSize_y());

        }

        @Override
        public void DRYRefreshDPI() {
                System.out.println(this.getUpperDN(ImageBuilder.class));
                DesignBuilder designBuilder = ((ImageBuilder) this.getUpperDN(ImageBuilder.class)).getDesignBuilder();
                pixelPosSize.computePixelPosSize(posSize, designBuilder.getPixelMmFactor());
        }

        @Override
        public String DRYComputeUniqueID() {
                return DesignInterfaceLinker.getIdentifier(this.getClass()) + name;
        }

        public String getTabName() {
                return tabName;
        }

}
