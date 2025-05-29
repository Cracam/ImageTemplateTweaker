package AppInterface.DesignBuilderSubElement;

import Exeptions.ResourcesFileErrorException;
import java.io.File;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AutoSaveModelSelector extends AutoSaveElement {

    @FXML
    private Menu loadMenu;

    @FXML
    private MenuItem deleteAllMenuItem;

    
    private ArrayList<AutoSaveDesignSelector> autoSaveDesignSelector=new ArrayList<>();
    private final AutoSaveMenu autoSaveMenu;
    private final File modelDir;

        public AutoSaveModelSelector(AutoSaveMenu autoSaveMenu, File modelDir) {
                this.autoSaveMenu = autoSaveMenu;
                this.modelDir=modelDir;
                initialiseInterface();
        }

    protected void initialiseInterface() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/path/to/your/fxml/CustomMenuInterface.fxml"));
            if (fxmlLoader == null) {
                throw new ResourcesFileErrorException();
            }
            fxmlLoader.setRoot(this);
            fxmlLoader.setController(this);
            fxmlLoader.load();

            // Add action handler for the "Delete All" menu item
            deleteAllMenuItem.setOnAction(event -> deleteAll());

        } catch (IOException | ResourcesFileErrorException | IllegalArgumentException ex) {
            Logger.getLogger(AutoSaveModelSelector.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void deleteAll() {
        // Implement the logic for deleting all items here
        System.out.println("Delete All action triggered");
    }

        @Override
        public void updateAutoSaveList() {
                File designsOfModelDir = this.modelDir;

                // Vérifier si le chemin est un répertoire
                if (designsOfModelDir.isDirectory()) {
                        // Lister tous les fichiers et dossiers dans le répertoire
                        File[] subDirectories = designsOfModelDir.listFiles(File::isDirectory);

                        // Parcourir tous les dossiers
                        if (subDirectories != null) {
                                for (File subDir : subDirectories) {
                                        // Faire quelque chose avec chaque dossier
                                        System.out.println("Autosave Detected " + subDir.getName());
                                       autoSaveDesignSelector.add(new AutoSaveDesignSelector(this,subDir));
                                }
                        }
                } else {
                        System.out.println("No Autosave are detected");
                }
        }


}
