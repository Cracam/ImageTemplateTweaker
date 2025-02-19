package Layers;

import Exceptions.ResourcesFileErrorException;
import Layers.SubClasses.QuadrupletFloat;
import ResourcesManager.ResourcesManager;
import ResourcesManager.XmlManager;
import imageBuilder.ImageBuilder;
import interfaces.Interface;
import interfaces.InterfaceCustomText;
import interfaces.InterfaceMouvableFixedImage;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import org.w3c.dom.Element;
import staticFunctions.StaticImageEditing;

/**
 *
 * @author camil
 */
public class LayerMovableFixedImage extends Layer {

        private String imageName;
 private float X_MaxOffset=(float) 1.0;
        private float X_MinOffset=(float) -1.0;
        private float Y_MaxOffset=(float) 1.0;
        private float Y_MinOffset=(float) -1.0;
        private File imageGetRaw=null;
        
        private float posRefX;
        private float posRefY;
        
        public LayerMovableFixedImage(String layerName, ResourcesManager modelResources, Interface layerInterface, ImageBuilder linkedImageBuilder, QuadrupletFloat posSize) {
                super(layerName, modelResources, layerInterface, linkedImageBuilder, posSize);
                this.posRefX=posSize.getPos_x();//save the original postion
                this.posRefY=posSize.getPos_x();//save the original postion
                
        }

     

        @Override
        public void refreshImageGet() {
                    refreshOffset(((InterfaceMouvableFixedImage) (this.linkedInterface)).getSliderXValue(),((InterfaceMouvableFixedImage) (this.linkedInterface)).getSliderYValue());
               
                try {
                        if (imageGetRaw == null) {
                                imageGetRaw = this.modelResources.get(this.imageName);

                                if (imageGetRaw == null) {
                                        throw new ResourcesFileErrorException("This file dont exist : " + this.imageName);
                                }

                        }
                        this.image_get = StaticImageEditing.ResizeImage(ImageIO.read(imageGetRaw), this.pixelPosSize.getSize_x(), this.pixelPosSize.getSize_y());

                } catch (IOException | ResourcesFileErrorException ex) {
                        Logger.getLogger(LayerMovableFixedImage.class.getName()).log(Level.SEVERE, null, ex);
                }

        }

            /**
         * this function will refresh the position of the object with it's offset.
         * 
         * @param rawOffestX
         * @param rawOffestY 
         */
        public void refreshOffset(float rawOffestX , float rawOffestY){
                
                float offsetX;
                if(rawOffestX>0){
                        offsetX=this.X_MaxOffset*rawOffestX;
                }else{
                          offsetX=-this.X_MinOffset*rawOffestX;
                }
                
                float offsetY;
                if(rawOffestY>0){
                        offsetY=this.Y_MaxOffset*rawOffestY;
                }else{
                          offsetY=-this.Y_MinOffset*rawOffestY;
                }
                
                posSize=new QuadrupletFloat(posRefX+offsetX,posRefY+offsetY, posSize.getSize_x(), posSize.getSize_y());
             pixelPosSize.computePixelPosSize(posSize, linkedImagesBuilder.getPixelMmFactor());
        }
        

   

  

        @Override
        public void DPIChanged() {
        }
        
 /**
         * 
         * @param paramNode
         */
        @Override
        public void readNode(Element paramNode) {
                this.imageName = paramNode.getElementsByTagName("Image").item(0).getAttributes().getNamedItem("image_name").getNodeValue();
              Element element=  (Element) paramNode.getElementsByTagName("OffSet").item(0);
              this.X_MaxOffset=XmlManager.getFloatAttribute(element,"X_MaxOffset", this.X_MaxOffset);
              this.X_MinOffset=XmlManager.getFloatAttribute(element,"X_MinOffset", this.X_MinOffset);
              this.Y_MaxOffset=XmlManager.getFloatAttribute(element,"Y_MaxOffset", this.Y_MaxOffset);
              this.Y_MinOffset=XmlManager.getFloatAttribute(element,"Y_MinOffset", this.Y_MinOffset);
        }
        
     
     
}
