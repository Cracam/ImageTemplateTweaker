package AppInterface.DesignBuilderSubElement;

import AppInterface.DesignBuilder;
import static AppInterface.Popups.PasswordPopup.showPasswordDialog;
import java.io.File;
import java.util.function.Consumer;
import javafx.fxml.FXML;

public class AutoSaveMenu extends AutoSaveElement {

   

    private final DesignBuilder DB;

    public AutoSaveMenu(DesignBuilder DB) {
        super("/AutoSaveMenu.fxml", new File(DB.getLocalFiles().getDesignsDir()),null);
        this.DB = DB;
        initialiseInterface();
    }

    @FXML
    private void deleteAll() {

            Consumer<String> onOK = (String input) -> {
            if ("DELETE".equals(input)) {
                deleteElementDir();
            } 
        };
          
         showPasswordDialog("Etes vous sûr de vouloir supprimer toutes les sauvgardes automatiques ? \n Ecriver DELETE pour confirmer", onOK, null);
    }
    
    
    
    @Override
    protected void DRYaddAutoSaveElement(File subDir) {
        AutoSaveModelSelector element = new AutoSaveModelSelector(this, subDir);
        addMenuElement(element);
        element.updateAutoSaveList();
    }

    public void updateAutosaveMenu() {
        refreshStructure();
    }
    

        @Override
        protected void initialiseMenu() {
                //nothing
        }
}
