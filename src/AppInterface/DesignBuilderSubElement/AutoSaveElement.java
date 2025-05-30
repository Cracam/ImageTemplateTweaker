package AppInterface.DesignBuilderSubElement;

import Exeptions.ResourcesFileErrorException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Menu;

public abstract class AutoSaveElement extends Menu {

    protected final String fxmlFileName;
    protected final File elementDir;
    protected final ArrayList<AutoSaveElement> menuElements = new ArrayList<>();
    protected final AutoSaveElement upperAutoSaveElement;

    protected AutoSaveElement(String fxmlFileName, File elementDir,AutoSaveElement upperAutoSaveElement) {
        this.fxmlFileName = fxmlFileName;
        this.elementDir = elementDir;
        this.upperAutoSaveElement=upperAutoSaveElement;
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
                        autoSaveElement.updateAutoSaveList();
                    } else {
                        DRYupdateAutoSaveList(subDir);
                    }
                }
            }
        } else {
            System.out.println("No Autosave are detected for " + this.getClass().getName());
        }
    }

    protected abstract void DRYupdateAutoSaveList(File subDir);

    protected void initialiseInterface() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(fxmlFileName));
            if (fxmlLoader == null) {
                throw new ResourcesFileErrorException();
            }
            fxmlLoader.setRoot(this);
            fxmlLoader.setController(this);
            fxmlLoader.load();
            initialiseMenu();

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
    
    
    protected String getName(){
            return this.elementDir.getName();
    }

        public AutoSaveElement getUpperAutoSaveElement() {
                return upperAutoSaveElement;
        }
    
  
}
