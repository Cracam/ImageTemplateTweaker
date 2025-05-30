package AppInterface.DesignBuilderSubElement;

import static AppInterface.Popups.ConfirmPopup.showConfirmationDialog;
import java.io.File;
import javafx.fxml.FXML;

public class AutoSaveModelSelector extends AutoSaveElement {

        public AutoSaveModelSelector(AutoSaveMenu autoSaveMenu, File elementDir) {
        super("/AutoSaveMenu.fxml", elementDir,autoSaveMenu);
        initialiseInterface();
    }

  @FXML
    private void deleteAll() {
         // Implement the logic for deleting all items here
         Runnable ifYes = this::deleteElementDir;
         showConfirmationDialog("Etes vous sûr de vouloir supprimer toutes les sauvgardes automatiques de ce modèle ?", ifYes, null);
    }

    @Override
    protected void DRYaddAutoSaveElement(File subDir) {
        AutoSaveDesignSelector element = new AutoSaveDesignSelector(this, subDir);
        addMenuElement(element);
        element.updateAutoSaveList();
    }

        @Override
        protected void initialiseMenu() {
               this.setText(elementDir.getName());
        }
}
