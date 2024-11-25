/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Layers;

import Exeptions.ResourcesFileErrorException;
import ResourcesManager.ResourcesManager;
import ResourcesManager.XmlManager;
import imageloaderinterface.ImageLoaderInterface;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import previewimagebox.PreviewImageBox;
import javafx.scene.control.TitledPane;

/**
 *
 * @author Camille LECOURT
 */
public class LayerCustomImage extends Layer {

        private String imagename;

        @FXML
        private PreviewImageBox Preview;
        @FXML
        private ImageLoaderInterface LoaderInterface;
        @FXML
        private TitledPane CustomImageTiledPane;

        public LayerCustomImage(String layerName, ResourcesManager modelResources, ResourcesManager designResources) {
                super(layerName, modelResources, designResources);
        }

        @Override
        BufferedImage generateImageget() {
                return LoaderInterface.getImage_out();
        }

        @Override
        void initialiseInterface() {
                try {
                        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/LayerCustomImage.fxml"));
                        if (fxmlLoader == null) {
                                throw new ResourcesFileErrorException();
                        }
                        fxmlLoader.setRoot(this);
                        fxmlLoader.setController(this);

                        fxmlLoader.load();

                        // Add a listener to the changed property
                        LoaderInterface.isChanged().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
                                if (newValue) {
                                        System.out.println("trigered");
                                        LoaderInterface.setChanged(false);
                                        refreshPreview();
                                }
                        });

                } catch (IOException | ResourcesFileErrorException | IllegalArgumentException ex) {
                        Logger.getLogger(ImageLoaderInterface.class.getName()).log(Level.SEVERE, null, ex);
                }
        }

        @Override
        Node getLayerParameter() {
                XmlManager xmlManager = new XmlManager();
                xmlManager.addChild("Image", Map.of("image_name", this.imagename));
                return xmlManager.createDesignParamElement("DesignParam");
        }

        /**
         * We dont need any information
         *
         * @param paramNode
         */
        @Override
        void readNode(Element paramNode) {
        }

        @Override
        void refreshPreview() {
                this.refreshPreview(this.Preview);
        }

}
