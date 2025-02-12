/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package interfaces;

import Exeptions.ResourcesFileErrorException;
import ResourcesManager.ResourcesManager;
import imageloaderinterface.ImageLoaderInterface;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Slider;
import javafx.scene.image.ImageView;
import javax.imageio.ImageIO;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import previewimagebox.PreviewImageBox;
import staticFunctions.StaticImageEditing;

/**
 * This class have  the only function of generating the image out from a file
 * 
 * @author Camille LECOURT
 */
public class InterfaceMouvableFixedImage extends Interface{

        @FXML
        private PreviewImageBox PreviewBox;
        
        @FXML
        private Slider slider_X;
        
        @FXML
        private Slider slider_Y;
        
        @FXML
        private PreviewImageBox Preview;
        
        
        
        
        
        public InterfaceMouvableFixedImage(String interfaceName, ResourcesManager designResources) {
                super(interfaceName, designResources);
                this.haveGraphicInterface = false;
        }
        

        @Override
        public Node saveInterfaceData() {
               //Do nothing because ther is no data in the interface to load
               return null;
        }

        @Override
        public void loadInterfaceData(Element dataOfTheLayer) {
                //Do nothing because ther is no data in the interface to save
        }

        @Override
        protected void initialiseInterface() {
               try {
                        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/LayerFixedMovableImage.fxml"));
                        if (fxmlLoader == null) {
                                throw new ResourcesFileErrorException();
                        }
                        fxmlLoader.setRoot(this);
                        fxmlLoader.setController(this);

                        fxmlLoader.load();
                        this.Preview.toggleFixedSize();
                        
                        
                   
                        slider_X.setMin(0.001);
                        slider_X.setMax(0.999);
                        slider_X.setValue(0.5);
                        slider_X.setBlockIncrement(0.001);
                        
                        slider_Y.setMin(0.001);
                        slider_Y.setMax(0.999);
                        slider_Y.setValue(0.5);
                        slider_Y.setBlockIncrement(0.001);
                        
                        
                } catch (IOException | ResourcesFileErrorException | IllegalArgumentException ex) {
                        Logger.getLogger(ImageLoaderInterface.class.getName()).log(Level.SEVERE, null, ex);
                }
        }
        
        
        /**
         * Return the image resized
         * @param x
         * @param y
         * @param imageFile
         * @return 
         */
        public BufferedImage getImageOut(int x,int y,File imageFile){
                try {
                        return StaticImageEditing.ResizeImage(ImageIO.read(imageFile), x,y);
                } catch (IOException ex) {
                        Logger.getLogger(InterfaceMouvableFixedImage.class.getName()).log(Level.SEVERE, null, ex);
                        return null;
                }
        }
        
        
        
         @Override
        public void refreshPreview(String imageBuilderName, ImageView previewImage){
        }
        
}
