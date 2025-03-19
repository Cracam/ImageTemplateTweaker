/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ImageProcessor.ImageGenerators;

import Exceptions.XMLExeptions.XMLErrorInModelException;
import ImageProcessor.DesignNode;
import ImageProcessor.ImageTransformer;
import Layers.SubClasses.QuadrupletFloat;
import Layers.SubClasses.QuadrupletInt;
import interfaces.SubInterfaceRandomImageAllocation;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import org.w3c.dom.Element;

/**
 *
 * @author Camille LECOURT
 */
public class GeneratorRandomImageAllocation extends ImageTransformer {

        private ArrayList<QuadrupletFloat> positions;
        private ArrayList<QuadrupletInt> pixelPositions;

        private Element interfaceLoaderElement;
        
        private ArrayList<SubInterfaceRandomImageAllocation> lowerInterfaces;

          private float maxXSize = 0;
          private      float maxYSize = 0;

        public GeneratorRandomImageAllocation(DesignNode upperDE, Element elt ) throws XMLErrorInModelException {
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
                throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
        }

        @Override
        public void DRY_DRYUpdate(BufferedImage img_in) {
                throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
        }
}
