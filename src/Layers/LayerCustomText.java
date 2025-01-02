/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Layers;

import Layers.SubClasses.QuadrupletFloat;
import ResourcesManager.ResourcesManager;
import imageBuilder.ImageBuilder;
import interfaces.Interface;
import interfaces.InterfaceCustomText;
import org.w3c.dom.Element;

/**
 *
 * @author Camille LECOURT
 */
public class LayerCustomText extends Layer {

        private String imageName;


        public LayerCustomText(String layerName, ResourcesManager modelResources, Interface layerInterface, ImageBuilder linkedImageBuilder, QuadrupletFloat posSize) {
                super(layerName, modelResources, layerInterface, linkedImageBuilder, posSize);
        }

        @Override
        public void refreshImageGet() {
                this.image_get = ((InterfaceCustomText) (this.linkedInterface)).getImageOut( this.linkedImagesBuilder.getPixelMmFactor());
        }

        @Override
        void DPIChanged() {
                //noting to do the text compare the PixelMilimeter factor automaticaly
        }

        /**
         * to modify of problem
         *
         * @param paramNode
         */
        @Override
        public void readNode(Element paramNode) {
                // nothing to load if custon (everything will be changed iun the layer) -- This program wil be modified for other text generator
        }

        @Override
        public String toString() {
                return "LayerCustomColor{" + "imageName=" + imageName + '}';
        }

}
