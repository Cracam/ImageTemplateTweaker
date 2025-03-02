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
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.List;
import org.w3c.dom.Element;
import staticFunctions.ObjectDispersion;

/**
 *
 * @author Camille LECOURT
 */
public class LayerRandomImageDispersion extends Layer {

        private float maxXSize;
        private float minXSize;
        private float maxYSize;
        private float minYSize;

        private float maxInterval;
        private float minInterval;

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
        public void readNode(Element paramNode) {

                // Supposons que XmlManager a une méthode getIntAttribute similaire à getFloatAttribute
                Element element = (Element) paramNode.getElementsByTagName("Size").item(0);
                this.maxXSize = XmlManager.getFloatAttribute(element, "maxXSize", this.maxXSize);
                this.minXSize = XmlManager.getFloatAttribute(element, "minXSize", this.minXSize);
                this.maxYSize = XmlManager.getFloatAttribute(element, "maxYSize", this.maxYSize);
                this.minYSize = XmlManager.getFloatAttribute(element, "minYSize", this.minYSize);

                element = (Element) paramNode.getElementsByTagName("Interval").item(0);
                this.maxInterval = XmlManager.getFloatAttribute(element, "maxInterval", this.maxInterval);
                this.minInterval = XmlManager.getFloatAttribute(element, "minInterval", this.minInterval);

                element = (Element) paramNode.getElementsByTagName("Interface").item(0);

                imageBuilder = new SubImageBuilder(this.linkedImagesBuilder.getDesignBuilder(), element, maxXSize, maxYSize, this, 2);

                ((InterfaceRandomImageDispersion) (this.linkedInterface)).setLowerImageBuilder(imageBuilder);

        }

        @Override
        public void refreshImageGet() {

                BufferedImage bufferedImage = this.imageBuilder.getImageOut();
                // Créer une instance de ObjectDispersion
                InterfaceRandomImageDispersion inter = ((InterfaceRandomImageDispersion) (this.linkedInterface));
                int lowIntervalSize = (int) ((this.minInterval + inter.getLowIntervalSize() * (maxInterval - minInterval)) * this.linkedImagesBuilder.getPixelMmFactor());
                int highIntervalSize = (int) ((this.minInterval + inter.getHighIntervalSize() * (maxInterval - minInterval)) * this.linkedImagesBuilder.getPixelMmFactor());

                int lowImageXSize = (int) ((this.minXSize + inter.getLowImageSize() * (maxXSize - minXSize)) * this.linkedImagesBuilder.getPixelMmFactor());
                int highImageXSize = (int) ((this.minXSize + inter.getHighImageSize() * (maxXSize - minXSize)) * this.linkedImagesBuilder.getPixelMmFactor());

//System.out.println("########"+ lowIntervalSize+"   "+ highIntervalSize+"   "+ lowImageXSize+"   "+ highImageXSize+"   "+ lowImageYSize+"   "+ highImageYSize);
//System.out.println("------------------"+this.maxXSize +"   "+ this.minXSize +"   "+       this.maxYSize +"   "+ this.minYSize+"   "+ this.maxInterval +"   "+this.minInterval);
                ObjectDispersion disperser = new ObjectDispersion(bufferedImage, lowIntervalSize, highIntervalSize, lowImageXSize, highImageXSize, this.pixelPosSize.getSize_x(), this.pixelPosSize.getSize_y());

                // Disperser les objets
                List<ObjectDispersion.ObjectPosition> positions = disperser.disperseObjects();

                this.image_get = new BufferedImage(this.pixelPosSize.getSize_x(), this.pixelPosSize.getSize_y(), BufferedImage.TYPE_INT_ARGB);
                Graphics2D g2d = this.image_get.createGraphics();
                // Activer l'anticrénelage
                g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Dessiner chaque image à la position, taille et angle spécifiés
                for (ObjectDispersion.ObjectPosition position : positions) {
                        AffineTransform transform = new AffineTransform();
                        transform.translate(position.x, position.y);
                        transform.rotate(Math.toRadians(position.angle), position.width / 2, position.height / 2);
                        transform.scale(position.width / (double) bufferedImage.getWidth(), position.height / (double) bufferedImage.getHeight());
                        g2d.drawImage(bufferedImage, transform, null);
                }

                g2d.dispose();

        }
}
