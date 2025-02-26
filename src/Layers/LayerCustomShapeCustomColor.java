/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Layers;

import Layers.SubClasses.QuadrupletFloat;
import ResourcesManager.ResourcesManager;
import imageBuilder.ImageBuilder;
import interfaces.Interface;
import interfaces.InterfaceCustomShapeCustomColor;
import org.w3c.dom.Element;

/**
 *
 * @author Camille LECOURT
 */
public class LayerCustomShapeCustomColor extends Layer {
        
        public LayerCustomShapeCustomColor(String layerName, ResourcesManager modelResources, Interface layerInterface, ImageBuilder linkedImageBuilder, QuadrupletFloat posSize) {
                super(layerName, modelResources, layerInterface, linkedImageBuilder, posSize);
        }

        
        @Override
        public void refreshImageGet() {
               this.image_get= ((InterfaceCustomShapeCustomColor) (this.linkedInterface)).getImageOut( this.pixelPosSize.getSize_x(), this.pixelPosSize.getSize_y());
        }

      


        @Override
        public void readNode(Element paramNode) {
        }

       

        /**
         * The work of our method does not change if the DPI change
         */
        @Override
        public void DPIChanged() {
        }
}
