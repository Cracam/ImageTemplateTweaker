package AppInterface.Popups;

import java.util.ArrayList;
import java.util.function.Consumer;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.util.Pair;

public class ComboBoxPopup {

    /**
     * Affiche une boîte de dialogue avec une ComboBox pour sélectionner une option.
     *
     * @param message Le message à afficher dans la boîte de dialogue.
     * @param options La liste des options à afficher dans la ComboBox.
     * @param onOK La méthode à exécuter si l'utilisateur clique sur "Oui".
     * @param onCancel La méthode à exécuter si l'utilisateur clique sur "Annuler".
     */
    public static void showComboBoxDialog(String message, ArrayList<String> options, Consumer<String> onOK, Runnable onCancel) {
        Dialog<Pair<String, ButtonType>> dialog = new Dialog<>();
        dialog.setTitle("Sélection d'une option");
        dialog.setHeaderText(null);

        // Ajouter les boutons "Oui" et "Annuler"
        ButtonType buttonTypeYes = new ButtonType("Oui", ButtonData.OK_DONE);
        ButtonType buttonTypeCancel = new ButtonType("Annuler", ButtonData.CANCEL_CLOSE);

        dialog.getDialogPane().getButtonTypes().addAll(buttonTypeYes, buttonTypeCancel);

        // Créer une ComboBox pour les options
        ComboBox<String> comboBox = new ComboBox<>();
        comboBox.getItems().addAll(options);
        comboBox.setPromptText("Sélectionnez une option");

        GridPane grid = new GridPane();
        grid.add(comboBox, 0, 0);
        dialog.getDialogPane().setContent(grid);

        // Convertir le résultat en une paire contenant l'option sélectionnée et le type de bouton
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == buttonTypeYes) {
                return new Pair<>(comboBox.getValue(), dialogButton);
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
