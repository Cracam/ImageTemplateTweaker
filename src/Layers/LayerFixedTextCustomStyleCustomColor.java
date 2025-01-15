/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Layers;

import Layers.SubClasses.QuadrupletFloat;
import ResourcesManager.ResourcesManager;
import ResourcesManager.XmlManager;
import imageBuilder.ImageBuilder;
import interfaces.Interface;
import interfaces.InterfaceCustomText;
import interfaces.InterfaceFixedTextCustomStyleCustomColor;
import java.awt.image.BufferedImage;
import org.w3c.dom.Element;
import staticFunctions.StaticImageEditing;

/**
 *
 * @author Camille LECOURT
 */
public class LayerFixedTextCustomStyleCustomColor extends LayerCustomText {
        private int[][] opacityMap;
        private String text;
        
        public LayerFixedTextCustomStyleCustomColor(String layerName, ResourcesManager modelResources, Interface layerInterface, ImageBuilder linkedImageBuilder, QuadrupletFloat posSize) {
                super(layerName, modelResources, layerInterface, linkedImageBuilder, posSize);
                
                
        }

        public void setOpacityMap(int[][] opacityMap) {
                this.opacityMap = opacityMap;
        }

        public int[][] getOpacityMap() {
                return opacityMap;
        }
        

                        
        @Override
        public void refreshImageGet() {
                if (textChanged) {
                        this.opacityMap = ((InterfaceFixedTextCustomStyleCustomColor) (this.linkedInterface)).refreshOpacityMap(text, this.linkedImagesBuilder.get_pixel_mm_Factor(), textSizeMin, textSizeMax);
                        textChanged = false;
                        System.out.println("################################"+text);
                }
                this.image_get = ((InterfaceFixedTextCustomStyleCustomColor) (this.linkedInterface)).getImageOut(opacityMap);
        }

        
        
 
        
          /**
         * 
         * @param paramNode
         */
        @Override
        public void readNode(Element paramNode) {
              Element element=  (Element) paramNode.getElementsByTagName("Text").item(0);
              
                this.textHeightFactor=XmlManager.getFloatAttribute(element,"TextHeightFactor",this.textHeightFactor);
                this.textSizeMin=XmlManager.getFloatAttribute(element,"TextSizeMin",this.textSizeMin);
                this.textSizeMax=XmlManager.getFloatAttribute(element,"TextSizeMax",this.textSizeMax);
                 this.text=XmlManager.getStringAttribute(element,"Text",this.text);
        }

        
}
