/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package InterfaceTest;



import javafx.application.Application;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.transform.Rotate;
import javafx.stage.Stage;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;
import javax.imageio.ImageIO;
import staticFunctions.ObjectDispersion;

public class ImageDispersionTest extends Application {

    @Override
    public void start(Stage primaryStage) throws IOException {
        // Charger l'image à disperser
 BufferedImage bufferedImage = ImageIO.read(new File("C:\\BACKUP\\ENSE3\\Foyer\\Cartes Vertes\\A Distribuer\\tools_Bureau\\graine.png"));
        // Créer une instance de ObjectDispersion
        ObjectDispersion disperser = new ObjectDispersion(bufferedImage, 20, 100, 10, 50, 800, 600);

        // Disperser les objets
        List<ObjectDispersion.ObjectPosition> positions = disperser.disperseObjects();

        // Créer un Pane pour afficher les images
        Pane pane = new Pane();

        // Ajouter les images au Pane
        for (ObjectDispersion.ObjectPosition position : positions) {
            Image image = SwingFXUtils.toFXImage(bufferedImage, null);
            ImageView imageView = new ImageView(image);

            // Conserver le ratio d'aspect
            imageView.setFitHeight(position.height);
            imageView.setPreserveRatio(true);

            imageView.setLayoutX(position.x);
            imageView.setLayoutY(position.y);
            imageView.getTransforms().add(new Rotate(position.angle, position.width / 2, position.height / 2));
            pane.getChildren().add(imageView);
        }

        // Configurer la scène et afficher la fenêtre
        Scene scene = new Scene(pane, 800, 600);
        primaryStage.setTitle("Image Dispersion Test");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
