/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package AppInterface;

import Exeptions.ResourcesFileErrorException;
import com.sun.javafx.logging.PlatformLogger.Level;
import imageloaderinterface.ImageLoaderInterface;
import java.io.IOException;
import java.lang.System.Logger;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.VBox;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 *
 * @author Camille LECOURT
 */
public class LayerContainer extends InterfaceContainer {

        @FXML 
        private VBox containerVBox;
        
        @FXML
        private TitledPane titledPane;
        
        

        public LayerContainer(String name, InterfaceNode upperIN) {
                super(name, upperIN);
                giveVBox(containerVBox);
        }

        @Override
        public Element DRYsaveDesign(Document doc) {
                throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
        }

        @Override
        protected void initialiseInterface() {
                 try {
                        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/InterfaceLayerContainer.fxml"));
                        if (fxmlLoader == null) {
                                throw new ResourcesFileErrorException();
                        }
                        fxmlLoader.setRoot(this);
                        fxmlLoader.setController(this);

                        fxmlLoader.load();
                        
                        this.titledPane.setText(this.getName());

                } catch (IOException | ResourcesFileErrorException | IllegalArgumentException ex) {
                       java.util.logging.Logger.getLogger(DesignBuilder.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
                }
        }
        
        
        @Override
        public void placeInterface(InterfaceNode lowerInerface) {
//                if(lowerInerface.getClass()!=LayerContainer.class){
                      this.containerVBox.getChildren().add(0,lowerInerface);
//                }else{
//                         if (upperInterface != null) {
//                                upperInterface.placeInterface(lowerInerface);
//                        }
//                }
        }
        
}
