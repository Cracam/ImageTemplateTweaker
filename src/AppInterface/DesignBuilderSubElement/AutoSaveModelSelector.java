package AppInterface.DesignBuilderSubElement;

import AppInterface.InterfaceNode;
import Exeptions.ResourcesFileErrorException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AutoSaveModelSelector extends Menu {

    @FXML
    private Menu loadMenu;

    @FXML
    private MenuItem deleteAllMenuItem;

    private InterfaceNode upperIN;
    
    private ArrayList<AutoSaveDesignSelector> LoadItems=new ArrayList<>();

    public AutoSaveModelSelector() {
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


}
