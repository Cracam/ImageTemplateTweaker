/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package AppInterface.Popups;


import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Spinner;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.util.Pair;
import javafx.util.converter.IntegerStringConverter;

public class SpinnerPopup {

    /**
     * Affiche une boîte de dialogue avec un Spinner pour sélectionner une valeur numérique.
     *
     * @param message Le message à afficher dans la boîte de dialogue.
     * @param minValue La valeur minimale du Spinner.
     * @param maxValue La valeur maximale du Spinner.
     * @param initialValue La valeur initiale du Spinner.
     * @param onOK La méthode à exécuter si l'utilisateur clique sur "Oui".
     * @param onCancel La méthode à exécuter si l'utilisateur clique sur "Annuler".
     */
    public static void showSpinnerDialog(String message, int minValue, int maxValue, int initialValue, java.util.function.Consumer<Integer> onOK, Runnable onCancel) {
        Dialog<Pair<Integer, ButtonType>> dialog = new Dialog<>();
        dialog.setTitle("Sélection d'une valeur");
        dialog.setHeaderText(message);

        // Ajouter les boutons "Oui" et "Annuler"
        ButtonType buttonTypeYes = new ButtonType("Oui", ButtonData.OK_DONE);
        ButtonType buttonTypeCancel = new ButtonType("Annuler", ButtonData.CANCEL_CLOSE);

        dialog.getDialogPane().getButtonTypes().addAll(buttonTypeYes, buttonTypeCancel);

        // Créer un Spinner pour les valeurs numériques
        Spinner<Integer> spinner = new Spinner<>(minValue, maxValue, initialValue);
        spinner.setEditable(true);
        spinner.setPromptText("Sélectionnez une valeur");

        // Utiliser un StringConverter pour convertir les valeurs en chaînes et vice versa
        spinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(minValue, maxValue, initialValue));
        spinner.getValueFactory().setConverter(new IntegerStringConverter());

        GridPane grid = new GridPane();
        grid.add(spinner, 0, 0);
        dialog.getDialogPane().setContent(grid);

        // Convertir le résultat en une paire contenant la valeur sélectionnée et le type de bouton
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == buttonTypeYes) {
                return new Pair<>(spinner.getValue(), dialogButton);
            }
            return new Pair<>(null, dialogButton);
        });

        // Afficher la boîte de dialogue et attendre la réponse de l'utilisateur
        Stage stage = (Stage) dialog.getDialogPane().getScene().getWindow();
        stage.setAlwaysOnTop(true); // Pour s'assurer que la boîte de dialogue est toujours au premier plan
        dialog.showAndWait().ifPresent(result -> {
            if (result.getValue() == buttonTypeYes) {
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

