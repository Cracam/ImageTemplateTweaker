/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package InterfaceTest;

/**
 *
 * @author LECOURT Camille
 */
import ImageProcessor.ImageGenerator.LayerCustomImage;
import ResourcesManager.ResourcesManager;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class LayerTest extends Application {

         @Override
         public void start(Stage primaryStage) {
                  
                ResourcesManager res_m1=new ResourcesManager("C:\\BACKUP\\ENSE3\\Foyer\\Programme_Java\\Batcher_Foyer\\resources\\res_1.zip");
                 ResourcesManager res_m2=new ResourcesManager("C:\\BACKUP\\ENSE3\\Foyer\\Programme_Java\\Batcher_Foyer\\resources\\res_2.zip");
                  LayerCustomImage layer = new LayerCustomImage("Image Name",res_m1,res_m2);
                  Scene scene = new Scene(layer);
                  primaryStage.setTitle("Gradient Interface Test");
                  primaryStage.setScene(scene);
                  primaryStage.show();
         }

         public static void main(String[] args) {
                  launch(args);
         }
}
