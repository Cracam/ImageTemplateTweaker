package AppInterface.DesignBuilderSubElement;

import AppInterface.InterfaceNode;
import java.io.File;
import java.io.FilenameFilter;
import javafx.fxml.FXML;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import java.util.ArrayList;

public class AutoSaveDesignSelector extends AutoSaveElement {

        @FXML
        private Menu menuVersion;

        @FXML
        private MenuItem deleteAllMenuItem;

        private InterfaceNode upperIN;

        private final ArrayList<AutoSaveItem> loadItems = new ArrayList<>();
        private final AutoSaveModelSelector autoSaveModelSelector;

        public AutoSaveDesignSelector(AutoSaveModelSelector autoSaveModelSelector, File designDir) {
                super("/AutoSaveMenu.fxml", designDir);
                this.autoSaveModelSelector = autoSaveModelSelector;
                initialiseInterface();
        }

        @FXML
        private void deleteAll() {
                // Implement the logic for deleting all items here
                System.out.println("Delete All action triggered");
        }

        @Override
        protected void updateAutoSaveList() {

                // Filtre pour les fichiers .zip
                FilenameFilter zipFilter = new FilenameFilter() {
                        @Override
                        public boolean accept(File dir, String name) {
                                return name.toLowerCase().endsWith(".zip");
                        }
                };

                // Vérifier si le chemin est un répertoire
                if (elementDir.isDirectory()) {
                        // Lister tous les fichiers .zip dans le répertoire
                        File[] zipFiles = elementDir.listFiles(zipFilter);

                        // Parcourir tous les fichiers .zip
                        if (zipFiles != null) {
                                for (File zipFile : zipFiles) {
                                        // Faire quelque chose avec chaque fichier .zip
                                        System.out.println("Fichier ZIP trouvé : " + zipFile.getName());
                                        DRYupdateAutoSaveList(zipFile);
                                }
                        }
                } else {
                        System.out.println("No autosave Foud for " + elementDir);
                }
        }

        @Override
        protected void DRYupdateAutoSaveList(File zipFile) {
                loadItems.add(new AutoSaveItem(this, zipFile));
                this.getItems().add(0,loadItems.getLast());
                loadItems.getLast().updateAutoSaveList();
        }
}
