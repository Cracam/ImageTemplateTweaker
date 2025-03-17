package ImageProcessor.ImagesTransformers;

import Exceptions.XMLExeptions.XMLErrorInModelException;
import ImageProcessor.DesignNode;
import ImageProcessor.ImageTransformer;
import ImageProcessor.Layer;
import static ResourcesManager.XmlManager.extractSingleElement;
import static ResourcesManager.XmlManager.getFloatAttribute;
import java.awt.image.BufferedImage;
import java.io.File;
import org.w3c.dom.Element;

/**
 *
 * @author camil
 */
public class TransformerMovableImage extends ImageTransformer {

        private String imageName;
 private float X_MaxOffset=(float) 1.0;
        private float X_MinOffset=(float) -1.0;
        private float Y_MaxOffset=(float) 1.0;
        private float Y_MinOffset=(float) -1.0;
        private File imageGetRaw=null;
        
        private float posRefX;
        private float posRefY;

        public TransformerMovableImage(DesignNode upperDE, Element elt) throws XMLErrorInModelException {
                super(upperDE, elt);
        }
        
   

     

//        @Override
//        public void refreshImageGet() {
//                    refreshOffset(((InterfaceMouvableFixedImage) (this.linkedInterface)).getSliderXValue(),((InterfaceMouvableFixedImage) (this.linkedInterface)).getSliderYValue());
//               
//                try {
//                        if (imageGetRaw == null) {
//                                imageGetRaw = this.modelResources.get(this.imageName);
//
//                                if (imageGetRaw == null) {
//                                        throw new ResourcesFileErrorException("This file dont exist : " + this.imageName);
//                                }
//
//                        }
//                        this.image_get = StaticImageEditing.ResizeImage(ImageIO.read(imageGetRaw), this.pixelPosSize.getSize_x(), this.pixelPosSize.getSize_y());
//
//                } catch (IOException | ResourcesFileErrorException ex) {
//                        Logger.getLogger(TransformerMovableFixedImage.class.getName()).log(Level.SEVERE, null, ex);
//                }
//
//        }
//
//            /**
//         * this function will refresh the position of the object with it's offset.
//         * 
//         * @param rawOffestX
//         * @param rawOffestY 
//         */
//        public void refreshOffset(float rawOffestX , float rawOffestY){
//                
//                float offsetX;
//                if(rawOffestX>0){
//                        offsetX=this.X_MaxOffset*rawOffestX;
//                }else{
//                          offsetX=-this.X_MinOffset*rawOffestX;
//                }
//                
//                float offsetY;
//                if(rawOffestY>0){
//                        offsetY=this.Y_MaxOffset*rawOffestY;
//                }else{
//                          offsetY=-this.Y_MinOffset*rawOffestY;
//                }
//                
//                posSize=new QuadrupletFloat(posRefX+offsetX,posRefY+offsetY, posSize.getSize_x(), posSize.getSize_y());
//             pixelPosSize.computePixelPosSize(posSize, linkedImagesBuilder.getPixelMmFactor());
//        }
        

   

// /**
//         * 
//         * @param paramNode
//         */
//        @Override
//        public void readNode(Element paramNode) {
//                this.imageName = paramNode.getElementsByTagName("Image").item(0).getAttributes().getNamedItem("image_name").getNodeValue();
//              Element element=  (Element) paramNode.getElementsByTagName("OffSet").item(0);
//              this.X_MaxOffset=XmlManager.getFloatAttribute(element,"X_MaxOffset", this.X_MaxOffset);
//              this.X_MinOffset=XmlManager.getFloatAttribute(element,"X_MinOffset", this.X_MinOffset);
//              this.Y_MaxOffset=XmlManager.getFloatAttribute(element,"Y_MaxOffset", this.Y_MaxOffset);
//              this.Y_MinOffset=XmlManager.getFloatAttribute(element,"Y_MinOffset", this.Y_MinOffset);
//        }

        @Override
        protected void generateFromElement() throws XMLErrorInModelException {
               
              Element subElt= extractSingleElement(elt.getElementsByTagName("OffSet"));
              this.X_MaxOffset=getFloatAttribute(subElt,"X_MaxOffset", this.X_MaxOffset);
              this.X_MinOffset=getFloatAttribute(subElt,"X_MinOffset", this.X_MinOffset);
              this.Y_MaxOffset=getFloatAttribute(subElt,"Y_MaxOffset", this.Y_MaxOffset);
              this.Y_MinOffset=getFloatAttribute(subElt,"Y_MinOffset", this.Y_MinOffset);
        }

        @Override
        public void DRY_DRYUpdate(BufferedImage img_in) {
              Layer layer = this.getUpperDN(Layer.class);
        }
        
     
     
}
