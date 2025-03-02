/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package imageBuilder;

import Exeptions.ResourcesFileErrorException;
import imageloaderinterface.ImageLoaderInterface;
import interfaces.Interface;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.VBox;
import previewimagebox.PreviewImageBox;


    


/**
 * 
 *
 * @author Camille LECOURT
 */
public class SubImageBuilderInterface extends TitledPane {
        private String name;
        public SubImageBuilderInterface(String name){
                this.name=name;
                initialiseInterface();
                
        }
    @FXML
        private VBox vboxInterface;

        @FXML
        private PreviewImageBox Preview;

    

        
   protected void initialiseInterface() {
                try {
                        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/InterfaceSubImageBuilder.fxml"));
                        if (fxmlLoader == null) {
                                throw new ResourcesFileErrorException();
                        }
                        fxmlLoader.setRoot(this);
                        fxmlLoader.setController(this);

                        fxmlLoader.load();

                          this.setText(name);
                } catch (IOException | ResourcesFileErrorException | IllegalArgumentException ex) {
                        Logger.getLogger(ImageLoaderInterface.class.getName()).log(Level.SEVERE, null, ex);
                }
        }
        
        
         public void linkInterface(Interface inter) {
                vboxInterface.getChildren().add(inter);
                inter.refreshLayers();
                inter.refreshImageBuilders();
        }
         

}
