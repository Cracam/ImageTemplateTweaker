/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package AppInterface.Interfaces;

import AppInterface.DesignInterfaceLinker;
import AppInterface.InterfaceContainer;
import Exeptions.ResourcesFileErrorException;
import AppInterface.InterfaceNode;
import AppInterface.LayersContainer;
import Exceptions.XMLExeptions.XMLErrorInModelException;
import ResourcesManager.XmlChild;
import ResourcesManager.XmlManager;
import imageloaderinterface.ImageLoaderInterface;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import org.w3c.dom.Element;
import staticFunctions.StaticImageEditing;

/**
 * This class is a interface of a custom img Image loaded by the user
 *
 * @author Camille LECOURT
 */
public class InterfaceCustomImage extends InterfaceNode {

        @FXML
        private ImageLoaderInterface loaderInterface;

        public InterfaceCustomImage(InterfaceNode upperIN,String name) {
                super(upperIN,name);
                 upperInterface.placeInterface(this);
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
                        loaderInterface.isChanged().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
                                if (newValue) {
                                        //    System.out.println("trigered");
                                        loaderInterface.setChanged(false);
                                        this.updateLinkedDesignNodes();
                                }
                        });

                } catch (IOException | ResourcesFileErrorException | IllegalArgumentException ex) {
                        Logger.getLogger(ImageLoaderInterface.class.getName()).log(Level.SEVERE, null, ex);
                }
        }

        @Override
        public XmlChild DRYsaveDesign() {
                String imageName = "Image_" + this.getUpperIN(LayersContainer.class).ComputeUniqueID() + ".png";
                XmlChild XmlTextBuilder = new XmlChild(DesignInterfaceLinker.getIdentifier(this.getClass()));

                if (loaderInterface.getImage_out() != null) {
                        XmlTextBuilder.addAttribute("image_name", imageName);
                        //save the image into the Design zip
                     getDesignRessources().setBufferedImage(imageName, loaderInterface.getImage_out());
                }
                return XmlTextBuilder;
        }

        /**
         * Return the out of this interface with the good size
         *
         * @param x
         * @param y
         * @return
         */
        public BufferedImage getImageOut(int x, int y) {
                return StaticImageEditing.ResizeImage(loaderInterface.getImage_out(), x, y);
        }

        @Override
        protected void DRYLoadDesign(Element element) throws XMLErrorInModelException {
                //                Element nodeimageName = (Element) dataOfTheLayer.getElementsByTagName("Image").item(0).getAttributes().getNamedItem("image_name");
//                      if(nodeimageName!=null){
//                             
//                              LoaderInterface.loadImage(this.designBuilder.getDesignResources().get(nodeimageName.getNodeValue()));
//
//                      }
                throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
        }

}
