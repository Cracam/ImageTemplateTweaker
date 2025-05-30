package AppInterface.DesignBuilderSubElement;

import Exeptions.ResourcesFileErrorException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Menu;

/**
 *
 * @author Camille LECOURT
 */
public abstract class AutoSaveElement extends Menu {

        protected final String fxmlFileName;
        protected final File elementDir;
       protected final ArrayList<? extends AutoSaveElement> menuElement = new ArrayList<>();


        // Constructeur de la classe abstraite
        protected AutoSaveElement(String fxmlFileName, File elementDir) {
                this.fxmlFileName = fxmlFileName;
                this.elementDir = elementDir;
                initialiseInterface();
        }

        /**
         * This function will look in a folder to get every subDir and lauch
         * custom action on it
         */
        protected void updateAutoSaveList() {
                AutoSaveElement autoSaveElement;

                // Vérifier si le chemin est un répertoire
                if (elementDir.isDirectory()) {
                        // Lister tous les fichiers et dossiers dans le répertoire
                        File[] subDirectories = elementDir.listFiles(File::isDirectory);

                        // Parcourir tous les dossiers
                        if (subDirectories != null) {
                                for (File subDir : subDirectories) {
                                        // Faire quelque chose avec chaque dossier
                                        System.out.println("Autosave Detected for " + this.getClass().getName() + "     dir  :  " + subDir.getName());
                                      autoSaveElement=  findMatchingAutoSaveElement(menuElement,subDir);
                                        if(autoSaveElement!=null){
                                                autoSaveElement.updateAutoSaveList();
                                        }else{
                                                DRYupdateAutoSaveList(subDir);
                                        }
                                }
                        }
                } else {
                        System.out.println("No Autosave are detected for " + this.getClass().getName());
                }
        }

        /**
         * This will be use to initilaise a new menu element
         *
         * @param subDir
         */
        protected abstract void DRYupdateAutoSaveList(File subDir);

        /**
         * will load the interface of the sub menu
         */
        protected void initialiseInterface() {
                try {
                        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(fxmlFileName));
                        if (fxmlLoader == null) {
                                throw new ResourcesFileErrorException();
                        }
                        fxmlLoader.setRoot(this);
                        fxmlLoader.setController(this);
                        fxmlLoader.load();
                       

                } catch (IOException | ResourcesFileErrorException | IllegalArgumentException ex) {
                        Logger.getLogger(AutoSaveModelSelector.class.getName()).log(Level.SEVERE, null, ex);
                }
        }

        /**
         * This function check if and Autosave element corresponf to one of the
         * list
         *
         * @param <T> extended class of the Autosave element
         * @param autoSaveElements
         * @param file
         * @return
         */
        public static <T extends AutoSaveElement> T findMatchingAutoSaveElement(ArrayList<T> autoSaveElements, File file) {
                if (autoSaveElements == null || file == null) {
                        return null;
                }

                for (T element : autoSaveElements) {
                        if (element != null && element.elementDir != null && element.elementDir.equals(file)) {
                                return element;
                        }
                }

                return null;
        }

}
