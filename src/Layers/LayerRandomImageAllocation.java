/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Layers;

import Layers.SubClasses.QuadrupletFloat;
import Layers.SubClasses.QuadrupletInt;
import ResourcesManager.ResourcesManager;
import ResourcesManager.XmlManager;
import imageBuilder.ImageBuilder;
import imageBuilder.SubImageBuilder;
import interfaces.Interface;
import java.util.ArrayList;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 *
 * @author Camille LECOURT
 */
public class LayerRandomImageAllocation extends Layer {

        private ArrayList<QuadrupletFloat> positions;
        private ArrayList<QuadrupletInt> pixelPositions;
        private ArrayList<SubImageBuilder> subImageBuilders;
        
        private SubImageBuilder imagesAssembler;
        private Element interfaceLoaderElement;

        public LayerRandomImageAllocation(String layerName, ResourcesManager modelResources, Interface layerInterface, ImageBuilder linkedImageBuilder, QuadrupletFloat posSize) {
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

                              float maxXSize=0;
                  float maxYSize=0;
                          
                for (int i = 0; i < subElements.getLength(); i++) {
                        subElement = (Element) subElements.item(i);
                        pos_x = XmlManager.getFloatAttribute(subElement, "pos_x", 0);
                        pos_y = XmlManager.getFloatAttribute(subElement, "pos_y", 0);
                        size_x = XmlManager.getFloatAttribute(subElement, "size_x", 0);
                        size_y = XmlManager.getFloatAttribute(subElement, "size_y", 0);
                        positions.add(new QuadrupletFloat(pos_x, pos_y, size_x, size_y));
                        pixelPositions.add(new QuadrupletInt(0, 0, 0, 0));
                        pixelPositions.get(i).computePixelPosSize(positions.get(i), linkedImagesBuilder.getPixelMmFactor());
                        
                        if(maxXSize<size_x) maxXSize=size_x;
                        if(maxYSize<size_y) maxYSize=size_y;
                }
                  interfaceLoaderElement = (Element) paramNode.getElementsByTagName("interfaceLoaderElement").item(0);
    
                  subImageBuilders.add(new SubImageBuilder(this.linkedImagesBuilder.getDesignBuilder(), interfaceLoaderElement, maxXSize, maxYSize, this, 2));
                  
        }

        @Override
        public void refreshImageGet() {


        }
}
