package AppInterface.DesignBuilderSubElement;

import static AppInterface.Popups.ConfirmPopup.showConfirmationDialog;
import java.io.File;
import java.io.FilenameFilter;
import javafx.fxml.FXML;

public class AutoSaveDesignSelector extends AutoSaveElement {

        public AutoSaveDesignSelector(AutoSaveModelSelector autoSaveModelSelector, File designDir) {
                super("/AutoSaveMenu.fxml", designDir, autoSaveModelSelector);
                initialiseInterface();
        }

 @FXML
    private void deleteAll() {
         // Implement the logic for deleting all items here
         Runnable ifYes = this::deleteElementDir;
         showConfirmationDialog("Etes vous sûr de vouloir supprimer toutes les sauvgardes automatiques ?", ifYes, null);
    }

        @Override
        protected void updateAutoSaveList() {
                FilenameFilter zipFilter = (File dir, String name) -> name.toLowerCase().endsWith(".zip");

                if (elementDir.isDirectory()) {
                        File[] zipFiles = elementDir.listFiles(zipFilter);

                        if (zipFiles != null) {
                                for (File zipFile : zipFiles) {
                                        System.out.println("Fichier ZIP trouvé : " + zipFile.getName());
                                        if (findMatchingAutoSaveElement(menuElements, zipFile) == null) {

                                                DRYaddAutoSaveElement(zipFile);
                                        }
                                }
                        }
                } else {
                        System.out.println("No autosave found for " + elementDir);
                }
        }

        @Override
        protected void DRYaddAutoSaveElement(File zipFile) {
                AutoSaveItem element = new AutoSaveItem(this, zipFile);
                addMenuElement(element);
                element.updateAutoSaveList();
        }

        @Override
        protected void initialiseMenu() {
                String name;
                name = elementDir.getName();
                name = removeAllOccurrences(name, this.upperAutoSaveElement.getName() + "_");
                this.setText(name);
        }
}
