/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package interfaces;

import AppInterface.DesignBuilder;
import imageBuilder.SubImageBuilder;
import javafx.beans.value.ObservableValue;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import static staticFunctions.StaticImageEditing.convertToFXImage;

/**
 *
 * @author Camille LECOURT
 */
public abstract class InterfaceWithSubImageBuilder extends Interface {

         SubImageBuilder lowerImageBuilder;
 private  ImageView prev;
  private  VBox vboxInterface;
  
        public InterfaceWithSubImageBuilder(String interfaceName, DesignBuilder designBuilder){
                
                super(interfaceName, designBuilder);
          
        }

        
        void link(ImageView prev,VBox vboxInterface){
                      this.vboxInterface=vboxInterface;
                this.prev=prev;
        }
        
        public void setLowerImageBuilder(SubImageBuilder lowerImageBuilder) {

                this.lowerImageBuilder = lowerImageBuilder;

                lowerImageBuilder.isChanged().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
                        if (newValue) {

                                lowerImageBuilder.setChanged(false);
                                this.refreshLayers();
                                this.refreshImageBuilders();
                        }
                });
                    lowerImageBuilder.refreshAll();
                refreshSubInterface();
               vboxInterface.getChildren().add( this.lowerImageBuilder.getSubInterface());
                lowerImageBuilder.refreshAll();

        }
       public void refreshSubInterface() {
               if(prev!=null){
                        this.prev.setImage(convertToFXImage(this.lowerImageBuilder.getImageOut()));
                        prev.setStyle("-fx-border-color: blue; -fx-border-width: 2px; -fx-border-style: solid;");
               }
        }
}
