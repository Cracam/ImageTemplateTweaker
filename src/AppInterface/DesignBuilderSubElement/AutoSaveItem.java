package AppInterface.DesignBuilderSubElement;

import static AppInterface.Popups.ConfirmPopup.showConfirmationDialog;
import java.io.File;
import javafx.fxml.FXML;

public class AutoSaveItem extends AutoSaveElement {


    public AutoSaveItem(AutoSaveDesignSelector autoSaveDesign, File modelDir) {
        super("/AutoSaveItem.fxml", modelDir,autoSaveDesign);
        initialiseInterface();
    }

    @Override
    protected void updateAutoSaveList() {
        // No implementation needed
    }

  @FXML
    private void deleteAll() {
         // Implement the logic for deleting all items here
         Runnable ifYes = this::deleteElementDir;
         showConfirmationDialog("Etes vous sûr de vouloir supprimer cette sauvgarde automatique ?", ifYes, null);

    }
         
         

    @FXML
    private void load() {
        // Implement the logic for loading an item here
    }

    @Override
    protected void DRYaddAutoSaveElement(File subDir) {
        // No implementation needed
    }
    
    @Override
        protected void initialiseMenu() {
                String name;
                name=elementDir.getName();
                 name=removeAllOccurrences(name,this.upperAutoSaveElement.getName()+"_");
                 name=removeAllOccurrences(name,".zip");
                 name=reformatDateTime(name);
               this.setText(name);

        }
        
        /**
     * Reformats the given date and time string.
     *
     * @param dateTime The original date and time string in the format "yyyyMMdd_HHmmss"
     * @return The reformatted date and time string in the format "yyyy/MM/dd : HH'h' mm'm' ss's'"
     */
    public static String reformatDateTime(String dateTime) {
        if (dateTime == null || dateTime.length() != 15 || dateTime.charAt(8) != '_') {
            throw new IllegalArgumentException("Invalid date time format. Expected format: yyyyMMdd_HHmmss");
        }

        String datePart = dateTime.substring(0, 8);
        String timePart = dateTime.substring(9);

        String formattedDate = String.format("%s/%s/%s",
            datePart.substring(0, 4),
            datePart.substring(4, 6),
            datePart.substring(6, 8));

        String formattedTime = String.format("%sh %sm %ss",
            timePart.substring(0, 2),
            timePart.substring(2, 4),
            timePart.substring(4, 6));

        return formattedDate + " : " + formattedTime;
    }
}
