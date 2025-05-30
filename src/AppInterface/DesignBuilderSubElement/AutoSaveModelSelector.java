package AppInterface.DesignBuilderSubElement;

import java.io.File;
import javafx.fxml.FXML;
import javafx.scene.control.Menu;
import java.util.ArrayList;

public class AutoSaveModelSelector extends AutoSaveElement {

        @FXML
        private Menu menuDesign;

        private ArrayList<AutoSaveDesignSelector> autoSaveDesignSelector = new ArrayList<>();
        private final AutoSaveMenu autoSaveMenu;

        public AutoSaveModelSelector(AutoSaveMenu autoSaveMenu, File modelDir) {
                super("/AutoSaveMenu.fxml", modelDir);
                this.autoSaveMenu = autoSaveMenu;
                initialiseInterface();
        }

        @FXML
        private void deleteAll() {
                // Implement the logic for deleting all items here
                System.out.println("Delete All action triggered");
        }

        @Override
        protected void DRYupdateAutoSaveList(File subDir) {
                autoSaveDesignSelector.add(new AutoSaveDesignSelector(this, subDir));
                this.getItems().add(0,autoSaveDesignSelector.getLast());
                autoSaveDesignSelector.getLast().updateAutoSaveList();
        }
}
