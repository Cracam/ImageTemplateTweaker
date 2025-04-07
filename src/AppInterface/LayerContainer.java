/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package AppInterface;

import Exeptions.ResourcesFileErrorException;
import ResourcesManager.XmlChild;
import java.io.IOException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.VBox;
import org.w3c.dom.Document;

/**
 *
 * @author Camille LECOURT
 */
public class LayerContainer extends InterfaceContainer {

        @FXML 
        private VBox containerVBox;
        
        @FXML
        private TitledPane titledPane;
        
        private String tabName;
        

        public LayerContainer( InterfaceNode upperIN,String name) {
                super( upperIN,name);
                setContainerVBox(containerVBox);
        }

        @Override
        public XmlChild DRYsaveDesign() {
                return null; // nothing to save
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
                          // Définir le style CSS pour changer la taille du texte
     //   titledPane.setStyle("-fx-font-size: 20px;");
                        
                        
                } catch (IOException | ResourcesFileErrorException | IllegalArgumentException ex) {
                       java.util.logging.Logger.getLogger(DesignBuilder.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
                }
        }
        
           /**
         * TESTED
         *
         * @return
         */
        @Override
        public String DRYComputeUniqueID() {
                return DesignInterfaceLinker.getIdentifier(this.getClass()) + getName() + tabName;
        }

        public String getTabName() {
                return tabName;
        }

        public void setTabName(String tabName) {
                this.tabName = tabName;
        }
      
        
}
