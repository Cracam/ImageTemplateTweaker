/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package interfaces;

import ResourcesManager.ResourcesManager;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 * This class have  the only function of generating the image out from a file
 * 
 * @author Camille LECOURT
 */
public class InterfaceFixedImage extends Interface{

        public InterfaceFixedImage(String interfaceName, String tabName, ResourcesManager designResources) {
                super(interfaceName, tabName, designResources);
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
                //Do nothing because there is no interface
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
                        return ResizeImage(ImageIO.read(imageFile), x,y);
                } catch (IOException ex) {
                        Logger.getLogger(InterfaceFixedImage.class.getName()).log(Level.SEVERE, null, ex);
                        return null;
                }
        }
        
}
