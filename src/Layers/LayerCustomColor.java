/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Layers;

import GradientCreatorInterface.GradientCreatorInterface;
import Layers.SubClasses.QuadrupletFloat;
import ResourcesManager.ResourcesManager;
import imageBuilder.ImageBuilder;
import interfaces.Interface;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.scene.control.TitledPane;
import javax.imageio.ImageIO;
import org.w3c.dom.Element;
import previewimagebox.PreviewImageBox;
import staticFunctions.StaticImageEditing;

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

       

 
        @Override
        public void refreshImageGet(){
                this.image_get=this.gradientPicker.getImageOut(opacityMap);
        }

        @Override
        public void refreshPreview() {
                this.refreshPreview(this.Preview);
                //   System.out.println(toString());
        }
        
        
        
        @Override
        void DPIChanged() {
                BufferedImage resizedImageGetRaw = StaticImageEditing.ResizeImage(this.image_getRaw, this.pixelPosSize.getSize_x(), this.pixelPosSize.getSize_y());
                this.opacityMap =StaticImageEditing.transformToOpacityArray(resizedImageGetRaw);
        }
     

        

        @Override
        public void readNode(Element paramNode) {
                this.imageName = paramNode.getElementsByTagName("Image").item(0).getAttributes().getNamedItem("image_name").getNodeValue();
                try {
                        File imageFile = this.modelResources.get(this.imageName);
                        this.image_getRaw= ImageIO.read(imageFile);

                        //set the default size of the image_raw
                        BufferedImage resizedImageGetRaw = StaticImageEditing.ResizeImage(this.image_getRaw, this.pixelPosSize.getSize_x(), this.pixelPosSize.getSize_y());
                        this.opacityMap=StaticImageEditing.transformToOpacityArray(resizedImageGetRaw);

                } catch (IOException ex) {
                        Logger.getLogger(LayerCustomColor.class.getName()).log(Level.SEVERE, null, ex);
                }
        }

    
       

        @Override
        public String toString() {
                return "LayerCustomColor{" + "imageName=" + imageName + '}';
        }

 


   

}
