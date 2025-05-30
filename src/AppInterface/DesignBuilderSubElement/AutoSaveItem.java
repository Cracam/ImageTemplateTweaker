package AppInterface.DesignBuilderSubElement;

import java.io.File;
import javafx.fxml.FXML;

/**
 *
 * @author Camille LECOURT
 */
public class AutoSaveItem extends AutoSaveElement {

        private final AutoSaveDesignSelector autoSaveDesign;

        public AutoSaveItem(AutoSaveDesignSelector autoSaveDesign, File modelDir) {
                super("/AutoSaveItem.fxml",modelDir);
                this.autoSaveDesign = autoSaveDesign;
                initialiseInterface();
        }

        @Override
        protected void updateAutoSaveList() {
                
        }
        
        @FXML
        private void deleteAll(){
                
        }
        
        @FXML
         private void  load(){
                 
         }

        @Override
        protected void DRYupdateAutoSaveList(File subDir) {
        }


}
