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
import javafx.scene.image.ImageView;
import javax.imageio.ImageIO;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import staticFunctions.StaticImageEditing;

/**
 * This class have  the only function of generating the image out from a file
 * 
 * @author Camille LECOURT
 */
public class InterfaceFixedImage extends Interface{

        
        
        
        public InterfaceFixedImage(String interfaceName, ResourcesManager designResources) {
                super(interfaceName, designResources);
                this.haveGraphicInterface = false;
        }
        

        @Override
        public Node saveInterfaceData(Document doc) {
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
        
        
   
        
        
        
         @Override
        public void refreshPreview(String imageBuilderName, ImageView previewImage){
        }
        
}
