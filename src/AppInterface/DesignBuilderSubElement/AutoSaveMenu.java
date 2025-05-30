package AppInterface.DesignBuilderSubElement;

import AppInterface.DesignBuilder;
import java.io.File;
import javafx.fxml.FXML;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;

public class AutoSaveMenu extends AutoSaveElement {

    @FXML
    private SeparatorMenuItem sepItem;
    @FXML
    private MenuItem delItem;

    private final DesignBuilder DB;

    public AutoSaveMenu(DesignBuilder DB) {
        super("/AutoSaveMenu.fxml", new File(DB.getLocalFiles().getDesignsDir()),null);
        this.DB = DB;
        initialiseInterface();
    }

    @FXML
    private void deleteAll() {
        // Implement the logic for deleting all items here
    }

    @Override
    protected void DRYupdateAutoSaveList(File subDir) {
        AutoSaveModelSelector element = new AutoSaveModelSelector(this, subDir);
        addMenuElement(element);
        element.updateAutoSaveList();
    }

    public void updateAutosaveMenu() {
        updateAutoSaveList();
    }

        @Override
        protected void initialiseMenu() {
                //nothing
        }
}
