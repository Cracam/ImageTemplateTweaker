/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package interfaces;

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
import javafx.scene.control.TitledPane;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import previewimagebox.PreviewImageBox;

/**
 *This class is a interface of a custom img Image loaded by the user
 * @author Camille LECOURT
 */
public class InterfaceCustomImage extends Interface {

        
          @FXML
        private PreviewImageBox Preview;
        @FXML
        private ImageLoaderInterface LoaderInterface;
        @FXML
        private TitledPane CustomImageTiledPane;

        public InterfaceCustomImage(String interfaceName, ResourcesManager designResources) {
                super(interfaceName, designResources);
        }
        
        
        
        @Override
        protected void initialiseInterface() {
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
                                        //    System.out.println("trigered");
                                        LoaderInterface.setChanged(false);
                                     
                                        this.refreshLayers();
                                        this.refreshImageBuilders();
                                }
                        });
                        
                        
                } catch (IOException | ResourcesFileErrorException | IllegalArgumentException ex) {
                        Logger.getLogger(ImageLoaderInterface.class.getName()).log(Level.SEVERE, null, ex);
                }
        }

        
        
        
        
        
        @Override
        public Node saveInterfaceData() {
                String imageName = "Image_" + interfaceName + ".png";

                XmlManager xmlManager = new XmlManager();
                xmlManager.addChild("Image", Map.of("image_name", imageName));

                //save the image into the Design zip
                this.designResources.setBufferedImage(interfaceName, imageName, LoaderInterface.getImage_out());

                return xmlManager.createDesignParamElement("DesignParam", "LayerName", interfaceName);
        }

        
        
        @Override
        public void loadInterfaceData(Element dataOfTheLayer ) {
                String imageName = dataOfTheLayer.getElementsByTagName("Image").item(0).getAttributes().getNamedItem("image_name").getNodeValue();
                LoaderInterface.loadImage(this.designResources.get(imageName));
        }
        
        
        
        /**
         * Return the out of this interface with the good size
         * @param x
         * @param y
         * @return 
         */
        public BufferedImage getImageOut(int x, int y){
                    return ResizeImage(LoaderInterface.getImage_out(), x,y);
        }
        
        
}
