package AppInterface.DesignBuilderSubElement;

import AppInterface.DesignBuilder;
import java.io.File;
import java.util.ArrayList;
import javafx.fxml.FXML;
import javafx.scene.control.Menu;

/**
 *
 * @author Camille LECOURT
 */
public class AutoSaveMenu extends AutoSaveElement {

        @FXML
        private Menu menuModel;

        private ArrayList<AutoSaveModelSelector> autoSaveDesign = new ArrayList<>(); //represent each desing autosaved

        private final DesignBuilder DB;

        public AutoSaveMenu(DesignBuilder DB) {
                super("/AutoSaveMenu.fxml", new File(DB.getLocalFiles().getDesignsDir()));
                this.DB = DB;
                initialiseInterface();
        }


        @FXML
        private void deleteAll() {

        }

        @Override
        protected void DRYupdateAutoSaveList(File subDir) {
               autoSaveDesign.add(new AutoSaveModelSelector(this, subDir));
                this.getItems().add(0,autoSaveDesign.getLast());
                 autoSaveDesign.getLast().updateAutoSaveList();
        }

        
        public void updateAutosaveMenu(){
                updateAutoSaveList();
        }
}
