package AppInterface.DesignBuilderSubElement;

import java.io.File;
import javafx.fxml.FXML;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;

public class AutoSaveModelSelector extends AutoSaveElement {

 @FXML
    private SeparatorMenuItem sepItem;
    @FXML
    private MenuItem delItem;


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
