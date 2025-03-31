/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ImageProcessor.ImagesTransformers;

import Exceptions.ResourcesFileErrorException;
import Exceptions.XMLExeptions.XMLErrorInModelException;
import ImageProcessor.DesignNode;
import ImageProcessor.ImageGenerators.GeneratorFixedImage;
import ImageProcessor.ImageTransformer;
import static ResourcesManager.XmlManager.extractSingleElement;
import static ResourcesManager.XmlManager.getIntAttribute;
import static ResourcesManager.XmlManager.getStringAttribute;
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
 * @author Camille LECOURT
 */
public class TransformerMask extends ImageTransformer {

        private int[][] opacityMap;
        private BufferedImage image_getRaw;

        private String imageName;
        private BufferedImage imageRaw;

        private boolean invert;

        public TransformerMask(DesignNode upperDE, Element elt) throws XMLErrorInModelException {
                super(upperDE, elt);
        }

        @Override
        protected void generateFromElement() throws XMLErrorInModelException {
                //  this.imageName = elt.getElementsByTagName("Image").item(0).getAttributes().getNamedItem("image_name").getNodeValue();
                Element subElt = extractSingleElement(elt.getElementsByTagName("Image"));
                this.imageName = getStringAttribute(subElt, "image_name", "ERROR");
                
                this.invert=(1==getIntAttribute(subElt, "invert", 0));

                try {
                        File imageFile = getModelRessources().get(this.imageName);

                        if (imageFile == null) {
                                throw new ResourcesFileErrorException("This file dont exist : " + this.imageName);
                        }
                        imageRaw = ImageIO.read(imageFile);

                } catch (IOException | ResourcesFileErrorException ex) {
                        Logger.getLogger(GeneratorFixedImage.class.getName()).log(Level.SEVERE, null, ex);
                }
        }

        @Override
        public void DRY_DRYUpdate(BufferedImage img_in) {
                if (opacityMap != null) {
                        if (img_in.getWidth() != opacityMap.length) {
                                if (img_in.getHeight() != opacityMap[0].length) {
                                        refreshOpacityMap(img_in);
                                }
                        }
                        this.imageOut = StaticImageEditing.applyMask(img_in, opacityMap);
                } else {
                        refreshOpacityMap(img_in);
                }
        }

        private void refreshOpacityMap(BufferedImage img_in) {
                opacityMap = StaticImageEditing.transformToOpacityArray(StaticImageEditing.ResizeImage(imageRaw, img_in.getWidth(), img_in.getHeight()));
                if (invert) {
                        StaticImageEditing.invertOpacityTable(opacityMap);
                }
        }

}
