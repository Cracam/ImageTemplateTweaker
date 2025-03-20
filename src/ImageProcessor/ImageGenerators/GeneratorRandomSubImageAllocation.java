/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ImageProcessor.ImageGenerators;

import Exceptions.XMLExeptions.XMLErrorInModelException;
import ImageProcessor.DesignNode;
import ImageProcessor.ImageGenerator;
import static ImageProcessor.ImageGenerator.createGenerator;
import static ImageProcessor.ImageTransformer.createTransformer;
import ResourcesManager.XmlManager;
import static ResourcesManager.XmlManager.extractSingleElement;
import static ResourcesManager.XmlManager.getDirectChildByTagName;
import static ResourcesManager.XmlManager.getStringAttribute;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 *
 * @author Camille LECOURT
 */
public class GeneratorRandomSubImageAllocation extends ImageGenerator {

        public GeneratorRandomSubImageAllocation(DesignNode upperDE, Element elt) throws XMLErrorInModelException {
                super(upperDE, elt);
        }

    
                


//
//        @Override
//        public void readNode(Element paramNode) {
//
//                //Loading each positions
//                //    Element element = (Element) 
//                Element element = (Element) paramNode.getElementsByTagName("SubImagesData").item(0);
//                NodeList subElements = element.getElementsByTagName("SubImagesData");
//                float pos_x, pos_y, size_x, size_y;
//                Element subElement;
//
//              
//
//                for (int i = 0; i < subElements.getLength(); i++) {
//                        subElement = (Element) subElements.item(i);
//                        pos_x = XmlManager.getFloatAttribute(subElement, "pos_x", 0);
//                        pos_y = XmlManager.getFloatAttribute(subElement, "pos_y", 0);
//                        size_x = XmlManager.getFloatAttribute(subElement, "size_x", 0);
//                        size_y = XmlManager.getFloatAttribute(subElement, "size_y", 0);
//                        positions.add(new QuadrupletFloat(pos_x, pos_y, size_x, size_y));
//                        pixelPositions.add(new QuadrupletInt(0, 0, 0, 0));
//                        pixelPositions.get(i).computePixelPosSize(positions.get(i), linkedImagesBuilder.getPixelMmFactor());
//
//                        if (maxXSize < size_x) {
//                                maxXSize = size_x;
//                        }
//                        if (maxYSize < size_y) {
//                                maxYSize = size_y;
//                        }
//                }
//                interfaceLoaderElement = (Element) paramNode.getElementsByTagName("Interface").item(0);
//
//                subImageBuilders.add(new SubImageBuilder(this.linkedImagesBuilder.getDesignBuilder(), interfaceLoaderElement, maxXSize, maxYSize, this));
//        }
//
//        
//          
//        public SubInterfaceRandomImageAllocation createNewImgBuilder() {
//                lowerImageBuilders.add(new SubImageBuilder(this.linkedImagesBuilder.getDesignBuilder(), interfaceLoaderElement,  maxXSize, maxYSize, this));
//             
//                SubInterfaceRandomImageAllocation subInter=new SubInterfaceRandomImageAllocation("Item de"+ this.layerName+" : " + lowerInterfaces.size(), (InterfaceRandomImageAllocations) this.getLinkedInterface(),lowerImageBuilders.get(lowerImageBuilders.size()-1));
//                lowerInterfaces.add(subInter);
//
//                return subInter;
//        }
        

        @Override
        protected void generateFromElement() throws XMLErrorInModelException {
                               String key;
                Element subElt, subSubElt;
                DesignNode currentUpperDN;

                this.name = getStringAttribute(elt, "name", "ERROR");

                

                subElt = extractSingleElement(elt.getElementsByTagName("size"));
                if (subElt == null) {
                        throw new XMLErrorInModelException("L'élément 'size du Layer " + this.name + "est manquant ou invalide.");
                }
                float size_x = XmlManager.getFloatAttribute(subElt, "size_x", 0);
                float size_y = XmlManager.getFloatAttribute(subElt, "size_y", 0);
                setDim(size_x,size_y);
                
                
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
                ((ImageGenerator) currentUpperDN).setDim(this.x_size,this.y_size);

        }


        @Override
        public void DRYUpdate() {
                throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
        }
}
