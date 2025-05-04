/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package AppInterface.Popups;

import java.util.function.Consumer;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.PasswordField;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.util.Pair;

public class PasswordPopup {

    /**
     * Affiche une boîte de dialogue pour saisir un mot de passe avec des boutons "OK"
     * et "Annuler".
     *
     * @param message Le message à afficher dans la boîte de dialogue.
     * @param onOK La méthode à exécuter si l'utilisateur clique sur "OK".
     * @param onCancel La méthode à exécuter si l'utilisateur clique sur "Annuler".
     */
    public static void showPasswordDialog(String message, Consumer<String> onOK, Runnable onCancel) {
        Dialog<Pair<String, ButtonType>> dialog = new Dialog<>();
        dialog.setTitle("Saisie du mot de passe");
        dialog.setHeaderText(null);

        // Ajouter les boutons "OK" et "Annuler"
        ButtonType buttonTypeOK = new ButtonType("OK", ButtonData.OK_DONE);
        ButtonType buttonTypeCancel = new ButtonType("Annuler", ButtonData.CANCEL_CLOSE);

        dialog.getDialogPane().getButtonTypes().addAll(buttonTypeOK, buttonTypeCancel);

        // Créer un champ de saisie pour le mot de passe
        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Entrez votre mot de passe");

        GridPane grid = new GridPane();
        grid.add(new Label(message), 0, 0);
        grid.add(passwordField, 0, 1);
        dialog.getDialogPane().setContent(grid);

        // Convertir le résultat en une paire contenant le mot de passe et le type de bouton
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == buttonTypeOK) {
                return new Pair<>(passwordField.getText(), dialogButton);
            }
            return new Pair<>(null, dialogButton);
        });

        // Afficher la boîte de dialogue et attendre la réponse de l'utilisateur
        Stage stage = (Stage) dialog.getDialogPane().getScene().getWindow();
        stage.setAlwaysOnTop(true); // Pour s'assurer que la boîte de dialogue est toujours au premier plan
        dialog.showAndWait().ifPresent(result -> {
            if (result.getValue() == buttonTypeOK) {
                if (onOK != null) {
                    onOK.accept(result.getKey());
                }
            } else {
                if (onCancel != null) {
                    onCancel.run();
                }
            }
            stage.close(); // Fermer la boîte de dialogue après la réponse
        });
    }
}

