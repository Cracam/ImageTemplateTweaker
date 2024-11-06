package InterfaceTest;



/**
 *
 * @author LECOURT Camille
 */
import GradientCreatorInterface.GradientCreatorInterface;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class GradientInterfaceTest extends Application {

         @Override
         public void start(Stage primaryStage) {
                  

                  GradientCreatorInterface gradientInterface = new GradientCreatorInterface();
                  Scene scene = new Scene(gradientInterface);
                  primaryStage.setTitle("Gradient Interface Test");
                  primaryStage.setScene(scene);
                  primaryStage.show();
         }

         public static void main(String[] args) {
                  launch(args);
         }
}
