/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Layers;

import Layers.SubClasses.QuadrupletFloat;
import ResourcesManager.ResourcesManager;
import imageBuilder.ImageBuilder;
import imageloaderinterface.ImageLoaderInterface;
import interfaces.Interface;
import java.awt.image.BufferedImage;
import javafx.fxml.FXML;
import org.w3c.dom.Element;
import previewimagebox.PreviewImageBox;
import javafx.scene.control.TitledPane;
import staticFunctions.StaticImageEditing;

/**
 *
 * @author Camille LECOURT
 */
public class LayerCustomImage extends Layer {

        @FXML
        private PreviewImageBox Preview;
        @FXML
        private ImageLoaderInterface LoaderInterface;
        @FXML
        private TitledPane CustomImageTiledPane;

        public LayerCustomImage(String layerName, ResourcesManager modelResources, Interface layerInterface, ImageBuilder linkedImageBuilder, QuadrupletFloat posSize) {
                super(layerName, modelResources, layerInterface, linkedImageBuilder, posSize);
        }

        
        @Override
        public void refreshImageGet() {
                this.image_get= StaticImageEditing.ResizeImage(LoaderInterface.getImage_out(), this.pixelPosSize.getSize_x(), this.pixelPosSize.getSize_y());
        }

      


        @Override
        public void readNode(Element paramNode) {
        }

        @Override
        public void refreshPreview() {
                this.refreshPreview(this.Preview);
        }

        /**
         * The work of our method does not change if the DPI change
         */
        @Override
        public void DPIChanged() {
        }

      

        

}
