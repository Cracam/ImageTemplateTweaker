/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Layers;

import Layers.SubClasses.QuadrupletFloat;
import ResourcesManager.ResourcesManager;
import ResourcesManager.XmlManager;
import imageBuilder.ImageBuilder;
import imageBuilder.SubImageBuilder;
import interfaces.Interface;
import interfaces.InterfaceRandomImageDispersion;
import org.w3c.dom.Element;

/**
 *
 * @author Camille LECOURT
 */
public class LayerRandomImageDispersion extends Layer   {

        private float maxXSize;
        private float minXSize;
        private float maxYSize;
        private float minYSize;

        private float maxXInterval;
        private float minXInterval;
        private float maxYInterval;
        private float minYInterval;
        
        
       private SubImageBuilder imageBuilder;
       
        public LayerRandomImageDispersion(String layerName, ResourcesManager modelResources, Interface layerInterface, ImageBuilder linkedImageBuilder, QuadrupletFloat posSize) {
                super(layerName, modelResources, layerInterface, linkedImageBuilder, posSize);
        }
        
        /**
         * nedd to change the layer result
         */
        @Override
        void DPIChanged() {
        }

        @Override
        public void refreshImageGet() {
        }
        
        

        @Override
        public void readNode(Element paramNode) {

                                               

                        // Supposons que XmlManager a une méthode getIntAttribute similaire à getFloatAttribute
                        Element element = (Element) paramNode.getElementsByTagName("Size").item(0);
                        this.maxXSize = XmlManager.getFloatAttribute(element, "maxXSize", this.maxXSize);
                        this.minXSize = XmlManager.getFloatAttribute(element, "minXSize", this.minXSize);
                        this.maxYSize = XmlManager.getFloatAttribute(element, "maxYSize", this.maxYSize);
                        this.minYSize = XmlManager.getFloatAttribute(element, "minYSize", this.minYSize);

                        element = (Element) paramNode.getElementsByTagName("Interval").item(0);
                        this.maxXInterval = XmlManager.getFloatAttribute(element, "maxXInterval", this.maxXInterval);
                        this.minXInterval = XmlManager.getFloatAttribute(element, "minXInterval", this.minXInterval);
                        this.maxYInterval = XmlManager.getFloatAttribute(element, "maxYInterval", this.maxYInterval);
                        this.minYInterval = XmlManager.getFloatAttribute(element, "minYInterval", this.minYInterval);
                        
                        
                        element = (Element) paramNode.getElementsByTagName("Interface").item(0);

                                imageBuilder=new SubImageBuilder(this.linkedImagesBuilder.getDesignBuilder(), element,maxXSize,maxYSize,this);

                                 ((InterfaceRandomImageDispersion) (this.linkedInterface)).setLowerImageBuilder(imageBuilder);
                        
        }
        
        
}
