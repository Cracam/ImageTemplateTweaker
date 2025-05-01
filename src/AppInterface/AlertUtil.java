package AppInterface;



/**
 *
 * @author Camille LECOURT
 */
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class AlertUtil {

        /**
         * Affiche une alerte d'erreur avec un message spécifié.
         *
         * @param message Le message à afficher dans l'alerte.
         */
        public static void showErrorAlert(String message) {
                Alert alert = new Alert(AlertType.ERROR);
                alert.setTitle("Erreur");
                alert.setHeaderText(null);
                alert.setContentText(message);
                alert.showAndWait();
        }

        /**
         * Affiche une alerte d'information avec un message spécifié.
         *
         * @param message Le message à afficher dans l'alerte.
         */
        public static void showInfoAlert(String message) {
                Alert alert = new Alert(AlertType.INFORMATION);
                alert.setTitle("Information");
                alert.setHeaderText(null);
                alert.setContentText(message);
                alert.showAndWait();
        }
}
