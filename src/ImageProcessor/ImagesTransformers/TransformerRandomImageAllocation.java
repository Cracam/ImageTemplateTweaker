/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ImageProcessor.ImagesTransformers;

import ImageProcessor.ImageTransformer;
import Layers.SubClasses.QuadrupletFloat;
import Layers.SubClasses.QuadrupletInt;
import ResourcesManager.ResourcesManager;
import ResourcesManager.XmlManager;
import imageBuilder.ImageBuilder;
import imageBuilder.SubImageBuilder;
import interfaces.Interface;
import interfaces.InterfaceRandomImageAllocations;
import interfaces.SubInterfaceRandomImageAllocation;
import java.util.ArrayList;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 *
 * @author Camille LECOURT
 */
public class TransformerRandomImageAllocation extends ImageTransformer {

        private ArrayList<QuadrupletFloat> positions;
        private ArrayList<QuadrupletInt> pixelPositions;
        private ArrayList<SubImageBuilder> subImageBuilders;

        private SubImageBuilder imagesAssembler;
        private Element interfaceLoaderElement;
        
          private ArrayList<SubImageBuilder> lowerImageBuilders;
        private ArrayList<SubInterfaceRandomImageAllocation> lowerInterfaces;

          private float maxXSize = 0;
          private      float maxYSize = 0;
                
        public TransformerRandomImageAllocation(String layerName, ResourcesManager modelResources, Interface layerInterface, ImageBuilder linkedImageBuilder, QuadrupletFloat posSize) {
                super(layerName, modelResources, layerInterface, linkedImageBuilder, posSize);
        }

        /**
         * nedd to change the layer result
         */
        @Override
        void DPIChanged() {

        }

        @Override
        public void readNode(Element paramNode) {

                //Loading each positions
                //    Element element = (Element) 
                Element element = (Element) paramNode.getElementsByTagName("SubImagesData").item(0);
                NodeList subElements = element.getElementsByTagName("SubImagesData");
                float pos_x, pos_y, size_x, size_y;
                Element subElement;

              

                for (int i = 0; i < subElements.getLength(); i++) {
                        subElement = (Element) subElements.item(i);
                        pos_x = XmlManager.getFloatAttribute(subElement, "pos_x", 0);
                        pos_y = XmlManager.getFloatAttribute(subElement, "pos_y", 0);
                        size_x = XmlManager.getFloatAttribute(subElement, "size_x", 0);
                        size_y = XmlManager.getFloatAttribute(subElement, "size_y", 0);
                        positions.add(new QuadrupletFloat(pos_x, pos_y, size_x, size_y));
                        pixelPositions.add(new QuadrupletInt(0, 0, 0, 0));
                        pixelPositions.get(i).computePixelPosSize(positions.get(i), linkedImagesBuilder.getPixelMmFactor());

                        if (maxXSize < size_x) {
                                maxXSize = size_x;
                        }
                        if (maxYSize < size_y) {
                                maxYSize = size_y;
                        }
                }
                interfaceLoaderElement = (Element) paramNode.getElementsByTagName("Interface").item(0);

                subImageBuilders.add(new SubImageBuilder(this.linkedImagesBuilder.getDesignBuilder(), interfaceLoaderElement, maxXSize, maxYSize, this));
        }

        
          
        public SubInterfaceRandomImageAllocation createNewImgBuilder() {
                lowerImageBuilders.add(new SubImageBuilder(this.linkedImagesBuilder.getDesignBuilder(), interfaceLoaderElement,  maxXSize, maxYSize, this));
             
                SubInterfaceRandomImageAllocation subInter=new SubInterfaceRandomImageAllocation("Item de"+ this.layerName+" : " + lowerInterfaces.size(), (InterfaceRandomImageAllocations) this.getLinkedInterface(),lowerImageBuilders.get(lowerImageBuilders.size()-1));
                lowerInterfaces.add(subInter);

                return subInter;
        }
        
        @Override
        public void refreshImageGet() {

        }
}
