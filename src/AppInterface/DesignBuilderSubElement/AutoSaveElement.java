package AppInterface.DesignBuilderSubElement;

import AppInterface.Popups.AlertPopup;
import Exeptions.ResourcesFileErrorException;
import java.io.File;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Menu;

public abstract class AutoSaveElement extends Menu {

        protected final String fxmlFileName;
        protected final File elementDir;
        protected final ArrayList<AutoSaveElement> menuElements = new ArrayList<>();
        protected final AutoSaveElement upperAutoSaveElement;

        protected AutoSaveElement(String fxmlFileName, File elementDir, AutoSaveElement upperAutoSaveElement) {
                this.fxmlFileName = fxmlFileName;
                this.elementDir = elementDir;
                this.upperAutoSaveElement = upperAutoSaveElement;
                initialiseInterface();
        }

        protected void updateAutoSaveList() {
                AutoSaveElement autoSaveElement;

                if (elementDir.isDirectory()) {
                        File[] subDirectories = elementDir.listFiles(File::isDirectory);

                        if (subDirectories != null) {
                                for (File subDir : subDirectories) {
                                        System.out.println("Autosave Detected for " + this.getClass().getName() + "     dir  :  " + subDir.getName());
                                        autoSaveElement = findMatchingAutoSaveElement(menuElements, subDir);
                                        if (autoSaveElement != null) {
                                                menuElements.add(autoSaveElement);
                                                autoSaveElement.updateAutoSaveList();
                                        } else {
                                                DRYaddAutoSaveElement(subDir);

                                        }
                                }
                        }
                }
        }

        protected abstract void DRYaddAutoSaveElement(File subDir);

        protected void initialiseInterface() {
                try {
                        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(fxmlFileName));
                        if (fxmlLoader == null) {
                                throw new ResourcesFileErrorException();
                        }
                        this.getItems().clear();
                        fxmlLoader.setRoot(this);
                        fxmlLoader.setController(this);
                        fxmlLoader.load();
                        initialiseMenu();
                        //  this.getItems().removeLast();
                        //   this.getItems().removeLast();
                        //     System.out.println(this.getItems());

                } catch (IOException | ResourcesFileErrorException | IllegalArgumentException ex) {
                        Logger.getLogger(AutoSaveElement.class.getName()).log(Level.SEVERE, null, ex);
                }
        }

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

        protected void addMenuElement(AutoSaveElement element) {
                menuElements.add(element);
                this.getItems().add(0, element);
        }

        protected abstract void initialiseMenu();

        /**
         * Removes all occurrences of a substring from a given string.
         *
         * @param original The original string
         * @param toRemove The substring to remove
         * @return The string with all occurrences of the substring removed
         */
        public static String removeAllOccurrences(String original, String toRemove) {
                if (original == null || toRemove == null || toRemove.isEmpty()) {
                        return original;
                }

                return original.replace(toRemove, "");
        }

        protected String getName() {
                return this.elementDir.getName();
        }

        public AutoSaveElement getUpperAutoSaveElement() {
                return upperAutoSaveElement;
        }

        /**
         * Deletes the elementDir directory and all its contents.
         *
         */
        public void deleteElementDir() {
                if (elementDir == null || !elementDir.exists()) {
                        return;
                }

                try {
                        Files.walkFileTree(elementDir.toPath(), new SimpleFileVisitor<Path>() {
                                @Override
                                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                                        Files.delete(file);
                                        return FileVisitResult.CONTINUE;
                                }

                                @Override
                                public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                                        Files.delete(dir);
                                        return FileVisitResult.CONTINUE;
                                }
                        });
                        AlertPopup.showInfoAlert("Tout les dossier ont été éffacé avec succès");
                        this.upperAutoSaveElement.refreshStructure();
                } catch (IOException e) {
                        Logger.getLogger(AutoSaveElement.class.getName()).log(Level.SEVERE, "Failed to delete directory: " + elementDir, e);
                }
        }

        protected void refreshStructure() {
                // Supprimer tous les éléments de l'interface d'abord
                for (AutoSaveElement element : menuElements) {
                        this.getItems().remove(element);
                }

                // Effacer la liste menuElements
                menuElements.clear();

                // Mettre à jour la liste avec les nouveaux éléments
                updateAutoSaveList();
        }

}
