package ImageProcessor;

import Exceptions.DesingNodeLowerNodeIsAnormalyVoidException;
import static ImageProcessor.ImageGenerator.createGenerator;
import static ImageProcessor.ImageTransformer.createTransformer;
import ImageProcessor.SubClasses.QuadrupletFloat;
import ImageProcessor.SubClasses.QuadrupletInt;
import ResourcesManager.XmlManager;
import static ResourcesManager.XmlManager.getStringAttribute;
import AppInterface.DesignBuilder;
import AppInterface.DesignInterfaceLinker;
import AppInterface.InterfaceNode;
import AppInterface.LayersContainer;
import Exceptions.XMLExeptions.XMLErrorInModelException;
import ImageProcessor.SubClasses.PosFloat;
import ImageProcessor.SubClasses.PosInt;
import static ResourcesManager.XmlManager.extractSingleElement;
import static ResourcesManager.XmlManager.getDirectChildByTagName;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;
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

        protected Map<String, PosFloat> offsets = new HashMap<>();

        public Layer(DesignNode upperDE, Element elt) throws XMLErrorInModelException {
                super(upperDE, elt);
                generateFromElement();
                DRYRefreshDPI();

        }

        @Override
        public void DRYUpdate() {
                try {
                        BufferedImage image_get;

                        // Getting the right imageGet
                        DesignNode lowerDN = this.getLowerDNForChilds(ImageTransformer.class);
                        if (lowerDN == null) {
                                lowerDN = this.getLowerDNForChilds(ImageGenerator.class);
                        }
                        if (lowerDN == null) {
                                throw new DesingNodeLowerNodeIsAnormalyVoidException("The Layer : " + this.name + " does not have the ImageGenerator");
                        }
                        image_get = lowerDN.getImageOut();

                        DesignNode lowerLayer = this.getLowerDN(Layer.class);
                        if (lowerLayer == null || lowerLayer.getImageOut() == null) {
                                this.imageOut = image_get;
                        } else {
                                PosInt posOffset = getFinalOffset();
                                this.imageOut = overlayImages(lowerLayer.getImageOut(), image_get, this.pixelPosSize.getPos_x() + posOffset.getPos_x(), this.pixelPosSize.getPos_y() + posOffset.getPos_y());
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
                
                subElt = getDirectChildByTagName(elt, "Transformers");
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
                subElt = getDirectChildByTagName(elt, "Generator");

                if (subElt == null) {
                        throw new XMLErrorInModelException("Le Layer " + this.name + "n'a pas de bloc: Generator valide");
                }

                subSubElt = extractSingleElement(subElt.getChildNodes());

                if (subSubElt == null) {
                        throw new XMLErrorInModelException("Le bloc generator du Layer " + this.name + "n'a pas de sous générateur valide\n\n " + subElt.getChildNodes().getLength());
                }

                key = subSubElt.getNodeName();

                currentUpperDN = createGenerator(key, currentUpperDN, subSubElt);//mettre 
                ((ImageGenerator) currentUpperDN).setDim(posSize.getSize_x(), posSize.getSize_y());

        }

        @Override
        public void DRYRefreshDPI() {
                //  System.out.println(this.getUpperDN(ImageBuilder.class));
                DesignBuilder designBuilder = ((ImageBuilder) this.getUpperDN(ImageBuilder.class)).getDesignBuilder();
                pixelPosSize.computePixelPosSize(posSize, designBuilder.getPixelMmFactor());
                
        }

        /**
         * TESTED
         *
         * @return
         */
        @Override
        public String DRYComputeUniqueID() {
                return DesignInterfaceLinker.getIdentifier(this.getClass()) + name + tabName;
        }

        public String getTabName() {
                return tabName;
        }

        @Override
        public InterfaceNode createLinkedInterface(InterfaceNode upperInter) {
                InterfaceNode inter = new LayersContainer(upperInter, name);
                inter.addDesignNode(this);
                return inter;
        }

        @Override
        protected String DRYtoString() {
                return "\n of name : " + this.name + "\n";
        }

        public QuadrupletFloat getPosSize() {
                return posSize;
        }

        public void setPosSize(QuadrupletFloat posSize) {
                this.posSize = posSize;
        }

        //Management of the pos offset dictionary
        public void setAnOffset(String id, PosFloat offset) {
                this.offsets.put(id, offset);
        }

        private PosInt getFinalOffset() {
                float pixelMilimeterFactor = this.getUpperDN(ImageBuilder.class).getDesignBuilder().getPixelMmFactor();
                PosFloat preRet = new PosFloat(0, 0);

                for (Map.Entry<String, PosFloat> offset : offsets.entrySet()) {
                        preRet.add(offset.getValue());
                }
                return new PosInt(preRet, pixelMilimeterFactor);
        }

}
