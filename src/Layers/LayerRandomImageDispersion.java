/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Layers;

import Exceptions.TheXmlElementIsNotANodeException;
import Exceptions.ThisInterfaceDoesNotExistException;
import Exceptions.ThisLayerDoesNotExistException;
import Layers.SubClasses.QuadrupletFloat;
import ResourcesManager.ResourcesManager;
import ResourcesManager.XmlManager;
import imageBuilder.ImageBuilder;
import interfaces.Interface;
import interfaces.InterfaceRandomImageDispersion;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 *
 * @author Camille LECOURT
 */
public class LayerRandomImageDispersion extends Layer {

        private float maxXSize;
        private float minXSize;
        private float maxYSize;
        private float minYSize;

        private float maxXInterval;
        private float minXInterval;
        private float maxYInterval;
        private float minYInterval;

        private Interface linkedSubInterface;
        private Layer linkedSubLayer;

        public LayerRandomImageDispersion(String layerName, ResourcesManager modelResources, Interface layerInterface, ImageBuilder linkedImageBuilder, QuadrupletFloat posSize) {
                super(layerName, modelResources, layerInterface, linkedImageBuilder, posSize);
        }

        @Override
        void DPIChanged() {
                throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
        }

        @Override
        public void refreshImageGet() {
                throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
        }

        @Override
        public void readNode(Element paramNode) {

                try {
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

                        String key = element.getNodeName(); // key for defining the layer and the Interface
                        String nameElement = element.getAttribute("name");

                        // ----------  ----------  ----------  ----------  ----------  ----------  ----------  ----------  ----------
                        // ----------  ----------  ----------  ----------  ----------  ----------  ----------  ----------  ----------
                        //Create an interface or return it
                        // ----------  ----------  ----------  ----------  ----------  ----------  ----------  ----------  ----------
                        // ----------  ----------  ----------  ----------  ----------  ----------  ----------  ----------  ----------
                        linkedSubInterface = Interface.createInterface(key, nameElement, this.linkedImagesBuilder.getDesignBuilder());
                        ((InterfaceRandomImageDispersion) (this.linkedInterface)).linkInterface(linkedSubInterface);

                        // ----------  ----------  ----------  ----------  ----------  ----------  ----------  ----------  ----------
                        // ----------  ----------  ----------  ----------  ----------  ----------  ----------  ----------  ----------
                        // Create a layer
                        // ----------  ----------  ----------  ----------  ----------  ----------  ----------  ----------  ----------
                        // ----------  ----------  ----------  ----------  ----------  ----------  ----------  ----------  ----------
                        float pos_x = Float.parseFloat(element.getElementsByTagName("pos").item(0).getAttributes().getNamedItem("pos_x").getNodeValue());
                        float pos_y = Float.parseFloat(element.getElementsByTagName("pos").item(0).getAttributes().getNamedItem("pos_y").getNodeValue());
                        float size_x = Float.parseFloat(element.getElementsByTagName("size").item(0).getAttributes().getNamedItem("size_x").getNodeValue());
                        float size_y = Float.parseFloat(element.getElementsByTagName("size").item(0).getAttributes().getNamedItem("size_y").getNodeValue());

                        QuadrupletFloat posSize = new QuadrupletFloat(pos_x, pos_y, size_x, size_y);
                        // System.out.println(key+ nameElement+ this.designBuilder.getModelResources()+ linkedInterface);
                        Layer layerCreated = Layer.createInertLayer(key, nameElement, this.modelResources, linkedInterface, linkedImagesBuilder, posSize);

                        if (!(layerCreated == null)) {
                                //  System.out.println(layerCreated);
                                //This code verify if the <Param> element is really an element
                                Element retElement = (Element) element.getElementsByTagName("Param").item(0);
                                if (retElement.getNodeType() != Node.ELEMENT_NODE) {
                                        throw new TheXmlElementIsNotANodeException("IN Layer(2) " + nameElement);
                                }
                                layerCreated.readNode(retElement); //read the specific parameter

                                ((InterfaceRandomImageDispersion) (this.linkedInterface)).linkLayer(layerCreated);

                        }

                } catch (ThisInterfaceDoesNotExistException | ThisLayerDoesNotExistException | TheXmlElementIsNotANodeException ex) {
                        Logger.getLogger(LayerRandomImageDispersion.class.getName()).log(Level.SEVERE, null, ex);
                }
        }

 

}
