/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package interfaces;

import Exeptions.ResourcesFileErrorException;
import Layers.Layer;
import designBuilder.DesignBuilder;
import imageloaderinterface.ImageLoaderInterface;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.EventType;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.HBox;
import org.controlsfx.control.RangeSlider;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import previewimagebox.PreviewImageBox;

/**
 *
 * @author Camille LECOURT
 */
public class InterfaceRandomImageDispersion extends Interface{

           @FXML
        private PreviewImageBox Preview;

           
           
        
         @FXML
        private RangeSlider DS_imageSize;
        
          
        @FXML
        private RangeSlider DS_intervalSize;
        
        @FXML
        private HBox HboxInterface;
        
        private Layer linkedLayer;
        
        public InterfaceRandomImageDispersion(String interfaceName, DesignBuilder designBuilder) {
                super(interfaceName, designBuilder);
        }

        @Override
        protected void initialiseInterface() {
                try {
                        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/InterfaceCustomShapeCustomColor.fxml"));
                        if (fxmlLoader == null) {
                                throw new ResourcesFileErrorException();
                        }
                        fxmlLoader.setRoot(this);
                        fxmlLoader.setController(this);

                        fxmlLoader.load();

                        
                        setPreview(Preview);
                } catch (IOException | ResourcesFileErrorException | IllegalArgumentException ex) {
                        Logger.getLogger(ImageLoaderInterface.class.getName()).log(Level.SEVERE, null, ex);
                }
        }
        
        public void linkLayer(Layer linkedLayer){
               this.linkedLayer= linkedLayer;
        }
        
        public void linkInterface(Interface inter){
                HboxInterface.getChildren().add(inter);
        }

        @Override
        public Node saveInterfaceData(Document doc) {
                throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
        }

        @Override
        public void loadInterfaceData(Element dataOfTheLayer) {
                throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
        }
        
}
