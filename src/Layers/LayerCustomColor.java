/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Layers;

import Exeptions.ResourcesFileErrorException;
import GradientCreatorInterface.GradientCreatorInterface;
import Layers.SubClasses.QuadrupletFloat;
import ResourcesManager.ResourcesManager;
import ResourcesManager.XmlManager;
import imageBuilder.ImageBuilder;
import imageloaderinterface.ImageLoaderInterface;
import interfaces.Interface;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TitledPane;
import javax.imageio.ImageIO;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import previewimagebox.PreviewImageBox;

/**
 *
 * @author Camille LECOURT
 */
public  class LayerCustomColor extends Layer {

        private String imageName;

        @FXML
        private PreviewImageBox Preview;

        @FXML
        private GradientCreatorInterface gradientPicker;

        @FXML
        private TitledPane CustomImageTiledPane;

        private int[][]opacityMap;
        private BufferedImage image_getRaw;

        public LayerCustomColor(String layerName, ResourcesManager modelResources, Interface layerInterface, ImageBuilder linkedImageBuilder, QuadrupletFloat posSize) {
                super(layerName, modelResources, layerInterface, linkedImageBuilder, posSize);
        }

       

 
        BufferedImage generateImageget(){
                return this.gradientPicker.getImageOut(opacityMap);
        }

        @Override
        public void refreshPreview() {
                this.refreshPreview(this.Preview);
                //   System.out.println(toString());
        }

     

        

        @Override
        void readNode(Element paramNode, ImageBuilder imageBuilder) {
                String key = imageBuilder.getName();
                this.imageName = paramNode.getElementsByTagName("Image").item(0).getAttributes().getNamedItem("image_name").getNodeValue();
                try {
                        File imageFile = this.modelResources.get(this.imageName);
                        this.image_getRaw.put(key, ImageIO.read(imageFile));

                        //set the default size of the image_raw
                        BufferedImage resizedImageGetRaw = Layer.ResizeImage(this.image_getRaw.get(key), this.getPixelPosSize(key).getSize_x(), this.getPixelPosSize(key).getSize_y());
                        this.opacityMap.put(key, transformToOpacityArray(resizedImageGetRaw));

                } catch (IOException ex) {
                        Logger.getLogger(LayerCustomColor.class.getName()).log(Level.SEVERE, null, ex);
                }
        }

        /**
         * This method chnage all the size of the images raw
         */
        @Override
        public void DPIChanged() {
                this.image_getRaw.forEach((key, value) -> {
                        BufferedImage resizedImageGetRaw = Layer.ResizeImage(this.image_getRaw, this.getPixelPosSize(key).getSize_x(), this.getPixelPosSize(key).getSize_y());
                        this.opacityMap.put(key, transformToOpacityArray(resizedImageGetRaw));
                });
        }

        /**
         * This function extract the opacity data from the image to transform it
         * into array.
         *
         * @param image
         * @return
         */
        public static int[][] transformToOpacityArray(BufferedImage image) {
                int width = image.getWidth();
                int height = image.getHeight();
                int[][] opacityArray = new int[width][height];

                Raster raster = image.getAlphaRaster();
                if (raster == null) {
                        throw new IllegalArgumentException("The image does not have an alpha channel.");
                }

                for (int y = 0; y < height; y++) {
                        for (int x = 0; x < width; x++) {
                                int alpha = raster.getSample(x, y, 0);
                                opacityArray[x][y] = alpha;
                        }
                }
                return opacityArray;
        }

        @Override
        public String toString() {
                return "LayerCustomColor{" + "imageName=" + imageName + '}';
        }

   

}
