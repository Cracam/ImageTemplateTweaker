/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package interfaces;

import Exeptions.ResourcesFileErrorException;
import imageBuilder.SubImageBuilder;
import imageloaderinterface.ImageLoaderInterface;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Spinner;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

/**
 *
 * @author Camille LECOURT
 */
public class SubInterfaceRandomImageAllocation extends HBox {

        private final InterfaceRandomImageAllocations upperInterface;
        private final String name;

        @FXML
        private VBox vboxInterface;

        private SubImageBuilder lowerImageBuilder;

        @FXML
        private Spinner numberSelector;

        @FXML
        private ImageView prev;

        public SubInterfaceRandomImageAllocation(String interfaceName, InterfaceRandomImageAllocations upperInterface, SubImageBuilder lowerImageBuilder) {
                this.upperInterface = upperInterface;
                this.name = interfaceName;
                this.lowerImageBuilder = lowerImageBuilder;
                initialiseInterface();
        }

        protected void initialiseInterface() {
                try {
                        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/SubInterfaceRandomImageAllocation.fxml"));
                        if (fxmlLoader == null) {
                                throw new ResourcesFileErrorException();
                        }
                        fxmlLoader.setRoot(this);
                        fxmlLoader.setController(this);

                        fxmlLoader.load();
                        this.getChildren().add(lowerImageBuilder.getSubInterface());
                        this.prev = lowerImageBuilder.getPreview();

                } catch (IOException | ResourcesFileErrorException | IllegalArgumentException ex) {
                        Logger.getLogger(ImageLoaderInterface.class.getName()).log(Level.SEVERE, null, ex);
                }
        }

        @FXML
        private void update() {
                this.upperInterface.uptadeInterface();
        }

}
