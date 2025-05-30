package AppInterface.DesignBuilderSubElement;

import java.io.File;
import javafx.fxml.FXML;
import javafx.scene.control.Menu;

public class AutoSaveModelSelector extends AutoSaveElement {

    @FXML
    private Menu menuDesign;


    public AutoSaveModelSelector(AutoSaveMenu autoSaveMenu, File elementDir) {
        super("/AutoSaveMenu.fxml", elementDir,autoSaveMenu);
        initialiseInterface();
    }

    @FXML
    private void deleteAll() {
        // Implement the logic for deleting all items here
        System.out.println("Delete All action triggered");
    }

    @Override
    protected void DRYupdateAutoSaveList(File subDir) {
        AutoSaveDesignSelector element = new AutoSaveDesignSelector(this, subDir);
        addMenuElement(element);
        element.updateAutoSaveList();
    }

        @Override
        protected void initialiseMenu() {
               this.setText(elementDir.getName());
        }
}
