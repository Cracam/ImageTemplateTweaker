package AppInterface.DesignBuilderSubElement;

import AppInterface.DesignBuilder;
import java.io.File;
import java.util.ArrayList;
import javafx.scene.control.Menu;

/**
 *
 * @author Camille LECOURT
 */
public class AutoSaveMenu extends AutoSaveElement {

        private ArrayList<AutoSaveModelSelector> autoSaveDesign = new ArrayList<>(); //represent each desing autosaved
        
        private DesignBuilder DB;
        
        public AutoSaveMenu(DesignBuilder DB){
                this.DB=DB;
        }
        
        
        public void updateAutoSaveList() {
                File designsDir = new File(DB.getLocalFiles().getDesignsDir());

                // Vérifier si le chemin est un répertoire
                if (designsDir.isDirectory()) {
                        // Lister tous les fichiers et dossiers dans le répertoire
                        File[] subDirectories = designsDir.listFiles(File::isDirectory);

                        // Parcourir tous les dossiers
                        if (subDirectories != null) {
                                for (File subDir : subDirectories) {
                                        // Faire quelque chose avec chaque dossier
                                        System.out.println("Autosave Detected " + subDir.getName());
                                       autoSaveDesign.add(new AutoSaveModelSelector(this,subDir));
                                }
                        }
                } else {
                        System.out.println("No Autosave are detected");
                }
        }

        @Override
        void initialiseInterface() {
                throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
        }
}
