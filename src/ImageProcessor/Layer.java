/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ImageProcessor;

import Exceptions.DesingNodeLowerNodeIsAnormalyVoidException;
import Exceptions.XMLExeptions.GetAttributeValueException;
import ImageProcessor.Layers.LayerCustomColor;
import ImageProcessor.Layers.LayerCustomImage;
import ImageProcessor.Layers.LayerCustomShapeCustomColor;
import ImageProcessor.Layers.LayerCustomText;
import ImageProcessor.Layers.LayerFixedImage;
import ImageProcessor.Layers.LayerFixedTextCustomStyleCustomColor;
import ImageProcessor.Layers.LayerMovableFixedImage;
import ImageProcessor.Layers.LayerRandomImageAllocation;
import ImageProcessor.Layers.LayerRandomImageDispersion;
import Layers.SubClasses.QuadrupletFloat;
import ResourcesManager.XmlManager;
import interfaces.Interface;
import java.awt.image.BufferedImage;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.w3c.dom.Element;
import static staticFunctions.StaticImageEditing.overlayImages;

/**
 *
 * @author Camille LECOURT
 */
public abstract class Layer extends DesignNode {
        
        private QuadrupletFloat posSize;
        public static final Map<String, Class<? extends Layer>> layersTypesMap = Map.of(
                "Fixed_Image", LayerFixedImage.class,
                "Custom_Image", LayerCustomImage.class,
                "Custom_Color", LayerCustomColor.class,
                "Custom_Text", LayerCustomText.class,
                "Fixed_Text_Custom_Color_Custom_Style", LayerFixedTextCustomStyleCustomColor.class,
                "Mouvable_Fixed_Image", LayerMovableFixedImage.class,
                "Custom_Shape_Custom_Color", LayerCustomShapeCustomColor.class,
                "Random_Image_Dispersion", LayerRandomImageDispersion.class,
                "Random_Image_Allocations", LayerRandomImageAllocation.class
        );

        public Layer(DesignNode upperDE, Element elt) throws GetAttributeValueException {
                super(upperDE, elt);
        }

        @Override
        public void DRYUpdate() {
                try {
                        BufferedImage image_get;

                        // Getting the right imageGet
                        DesignNode lowerDN = this.getLowerDE(ImageTransformer.class);
                        if (lowerDN == null) {
                                lowerDN = this.getLowerDE(ImageGenerator.class);
                        }
                        if (lowerDN == null) {
                                throw new DesingNodeLowerNodeIsAnormalyVoidException("The Layer" + this.name + " does not have the ImageGenerator");
                        }
                        image_get = lowerDN.getImageOut();

                        DesignNode lowerLayer = this.getLowerDE(Layer.class);
                        if (lowerLayer == null) {
                                this.imageOut = image_get;
                        } else {
                                this.imageOut = overlayImages(lowerLayer.getImageOut(), image_get);
                        }

                } catch (DesingNodeLowerNodeIsAnormalyVoidException ex) {
                        Logger.getLogger(Layer.class.getName()).log(Level.SEVERE, null, ex);
                }
        }

        /**
         * This code will load the layer using a Strin identifier to get the
         * type of the Layer
         *
         * @param layerType * @return
         * @param upperNode
         * @param layerInfo
         * @return
         * @throws GetAttributeValueException
         */
        public static ImageProcessor.Layer createLayer(String layerType, DesignNode upperNode, Element layerInfo) throws GetAttributeValueException {

                if (!layersTypesMap.containsKey(layerType)) {
                        throw new GetAttributeValueException("This interface type does not exist : " + layerType);
                }

                try {

                        Class<? extends ImageProcessor.Layer> subclass = layersTypesMap.get(layerType);
                        Constructor<? extends ImageProcessor.Layer> constructor = subclass.getConstructor(DesignNode.class, Element.class);

                        return constructor.newInstance(upperNode, layerInfo);

                } catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException ex) {
                        Logger.getLogger(Interface.class.getName()).log(Level.SEVERE, null, ex);
                        ex.printStackTrace(); // Print the stack trace

                        return null;
                }
        }

        
        @Override
        void generateFromElement(Element elt) throws GetAttributeValueException {
                //Get dim and size of the layer
                Element subElt = (Element) elt.getElementsByTagName("pos").item(0);
                float pos_x = XmlManager.getFloatAttribute(subElt, "pos_x", 0);
                float pos_y = XmlManager.getFloatAttribute(subElt, "pos_y", 0);

                subElt = (Element) elt.getElementsByTagName("size").item(0);
                float size_x = XmlManager.getFloatAttribute(subElt, "size_x", 0);
                float size_y = XmlManager.getFloatAttribute(subElt, "size_y", 0);

                 posSize = new QuadrupletFloat(pos_x, pos_y, size_x, size_y);
                 
                 DRYGenerateFromElement(elt);
                 
        }
        
        abstract void DRYGenerateFromElement(Element elt);
}
