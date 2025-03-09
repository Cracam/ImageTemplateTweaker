/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ImageProcessor.ImagesTransformers;

import Exceptions.XMLExeptions.GetAttributeValueException;
import ImageProcessor.DesignNode;
import ImageProcessor.ImageTransformer;
import java.awt.image.BufferedImage;
import org.w3c.dom.Element;

/**
 *
 * @author Camille LECOURT
 */
public  class TransformerCustomColor extends ImageTransformer {


        private int[][]opacityMap;
        private BufferedImage image_getRaw;

        public TransformerCustomColor(DesignNode upperDE, Element elt, String name) throws GetAttributeValueException {
                super(upperDE, elt, name);
        }

      
       

 
//        public void refreshImageGet(){
//                this.image_get=((InterfaceCustomColor) (this.linkedInterface)).getImageOut(0,0,opacityMap);
//        }
//        
//
//      
//        
//        
//        void DPIChanged() {
//                BufferedImage resizedImageGetRaw = StaticImageEditing.ResizeImage(this.image_getRaw, this.pixelPosSize.getSize_x(), this.pixelPosSize.getSize_y());
//                this.opacityMap =StaticImageEditing.transformToOpacityArray(resizedImageGetRaw);
//        }
//     
//
//        
//
//        
//        public void readNode(Element paramNode) {
//                this.imageName = paramNode.getElementsByTagName("Image").item(0).getAttributes().getNamedItem("image_name").getNodeValue();
//                try {
//                        File imageFile = this.modelResources.get(this.imageName);
//                        this.image_getRaw= ImageIO.read(imageFile);
//
//                        //set the default size of the image_raw
//                        BufferedImage resizedImageGetRaw = StaticImageEditing.ResizeImage(this.image_getRaw, this.pixelPosSize.getSize_x(), this.pixelPosSize.getSize_y());
//                        this.opacityMap=StaticImageEditing.transformToOpacityArray(resizedImageGetRaw);
//
//                } catch (IOException ex) {
//                        Logger.getLogger(TransformerCustomColor.class.getName()).log(Level.SEVERE, null, ex);
//                }
//        }

    
       


       @Override
        protected void DRYgenerateFromElement(Element elt) throws GetAttributeValueException {
                throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
        }
        public void DRY_DRYUpdate(BufferedImage img_in) {
                throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
        }

 


   

}
