/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package AppInterface.Popups;

/**
 *
 * @author Camille LECOURT
 */
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

public class ConfirmPopup {

        /**
         * Affiche une boîte de dialogue de confirmation avec des boutons "Oui"
         * et "Non".
         *
         * @param message Le message à afficher dans la boîte de dialogue.
         * @param onYes La méthode à exécuter si l'utilisateur clique sur "Oui".
         * @param onNo La méthode à exécuter si l'utilisateur clique sur "Non".
         */
        public static void showConfirmationDialog(String message, Runnable onYes, Runnable onNo) {
                Alert alert = new Alert(AlertType.CONFIRMATION);
                alert.setTitle("Confirmation");
                alert.setHeaderText(null);
                alert.setContentText(message);

                // Ajouter les boutons "Oui" et "Non"
                ButtonType buttonTypeYes = new ButtonType("Oui");
                ButtonType buttonTypeNo = new ButtonType("Non");

                alert.getButtonTypes().setAll(buttonTypeYes, buttonTypeNo);

                // Afficher la boîte de dialogue et attendre la réponse de l'utilisateur
                Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
                stage.setAlwaysOnTop(true); // Pour s'assurer que la boîte de dialogue est toujours au premier plan
                alert.showAndWait().ifPresent(response -> {
                        if (response == buttonTypeYes) {
                                if (onYes != null) {
                                        onYes.run();
                                }
                        } else {
                                if (onNo != null) {
                                        onNo.run();
                                }

                        }
                        stage.close(); // Fermer la boîte de dialogue après la réponse
                });
        }
}
