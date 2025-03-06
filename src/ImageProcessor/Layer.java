/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ImageProcessor;

import Exceptions.DesingNodeLowerNodeIsAnormalyVoidException;
import Exceptions.XMLExeptions.GetAttributeValueException;
import static ImageProcessor.ImageGenerator.createGenerator;
import static ImageProcessor.ImageTransformer.createTransformer;
import Layers.SubClasses.QuadrupletFloat;
import Layers.SubClasses.QuadrupletInt;
import ResourcesManager.XmlManager;
import static ResourcesManager.XmlManager.getStringAttribute;
import designBuilder.DesignBuilder;
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
public abstract class Layer extends DesignNode {
 protected QuadrupletFloat posSize;
        protected QuadrupletInt pixelPosSize;
        
        public Layer(DesignNode upperDE, Element elt) throws GetAttributeValueException {
                super(upperDE, elt);
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

                }catch (DesingNodeLowerNodeIsAnormalyVoidException ex) {
                        Logger.getLogger(Layer.class.getName()).log(Level.SEVERE, null, ex);
                }
        }

        
        @Override
        void generateFromElement(Element elt) throws GetAttributeValueException {
                String key;
                Element subElt,subSubElt;
                DesignNode currentUpperDN;
                
                this.name = getStringAttribute(elt, "name", "ERROR");
                
                 subElt = (Element) elt.getElementsByTagName("pos").item(0);
                 float pos_x = XmlManager.getFloatAttribute(subElt, "pos_x", 0);
                 float pos_y = XmlManager.getFloatAttribute(subElt, "pos_y", 0);

                 subElt = (Element) elt.getElementsByTagName("size").item(0);
                 float size_x = XmlManager.getFloatAttribute(subElt, "size_x", 0);
                 float size_y = XmlManager.getFloatAttribute(subElt, "size_y", 0);

                 posSize = new QuadrupletFloat(pos_x, pos_y, size_x, size_y);
                 DRYRefreshDPI();
                
                
                
               

                currentUpperDN=this;
                subElt = (Element) elt.getElementsByTagName("Transformers").item(0);
                NodeList nodeTransformersList = subElt.getChildNodes();

                //running in the inverse 
                for (int i = nodeTransformersList.getLength()-1; i >=0; i--) {
                        if (nodeTransformersList.item(i).getNodeType() == Node.ELEMENT_NODE) { //To avoid text node and comment node

                                subSubElt = (Element) nodeTransformersList.item(i);
                                key = subSubElt.getNodeName(); // key for defining the layer and the Interface

                                currentUpperDN = createTransformer(key, currentUpperDN, subSubElt);
                        }
                }
                
                
                
                ////////
                //creating the last element of the chain if there is generator.
                //if not it will be linked to another layer.
                 subElt = (Element) elt.getElementsByTagName("Generator").item(0);
                 if(subElt!=null){
                        key = subElt.getNodeName();

                        currentUpperDN = createGenerator(key, currentUpperDN, subElt);
                       ((ImageGenerator)currentUpperDN).setDim(pixelPosSize.getSize_x(),pixelPosSize.getSize_y());
                }
        }
        

        @Override
        public void DRYRefreshDPI() {
                DesignBuilder designBuilder = ((ImageBuilder) this.getUpperDN(ImageBuilder.class)).getDesignBuilder();
                pixelPosSize.computePixelPosSize(posSize, designBuilder.getPixelMmFactor());
        }

 
        
}
