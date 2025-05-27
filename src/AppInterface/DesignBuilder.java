package AppInterface;

import AppInterface.Popups.AlertPopup;
import AppInterface.Popups.ComboBoxPopup;
import static AppInterface.Popups.ConfirmPopup.showConfirmationDialog;
import static AppInterface.Popups.PasswordPopup.showPasswordDialog;
import AppInterface.Popups.SpinnerPopup;
import Exceptions.ResourcesFileErrorException;
import Exceptions.ThisInterfaceDoesNotExistException;
import Exceptions.ThisZIPFileIsNotADesignException;
import Exceptions.XMLExeptions.XMLErrorInModelException;
import ImageProcessor.ImageBuilder;
import ResourcesManager.PasswordManager;
import static ResourcesManager.PasswordManager.isPasswordCorrect;
import ResourcesManager.ResourcesManager;
import static ResourcesManager.XmlManager.extractSingleElement;
import static ResourcesManager.XmlManager.getIntAttribute;
import static ResourcesManager.XmlManager.getStringAttribute;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Slider;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.scene.transform.Scale;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javax.imageio.ImageIO;
import javax.swing.JOptionPane;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import net.lingala.zip4j.ZipFile;
import net.lingala.zip4j.model.FileHeader;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import previewimagebox.PreviewImageBox;

/**
 *
 * @author LECOURT Camille
 */
public class DesignBuilder extends Application {

        private static int index;
        private int id;

        private ResourcesManager modelResources;
        private ResourcesManager designResources;
        private PasswordManager passwordManager;

        private LocalFilesManagement localFiles;

        //Information on the model
        private String modelName; // the model Name
        private String designName;
        private String description; // The description of themodel
        private String defaultDesignName; // the default design name (inside the zip of the model) it's the file we will copy if the user use a model a reference for it's new Design.
        private String author;
        private String designPath = null; // the path of the desing currently opened ==null if nothing save yet (use to save function)

        private final ArrayList<ImageBuilder> imageBuilders = new ArrayList<>();

        private InterfacesManager interfacesManager;

        private Scene scene;

        private int DPI = 300;
        private int maxDPI = 300;

        private Random random;

        @FXML
        private TabPane tabPane;

        @FXML
        private PreviewImageBox preview;

        @FXML
        private SplitPane core;

        @FXML
        private Menu NewModel;
        @FXML
        private MenuItem Msave;
        @FXML
        private MenuItem MsaveAs;
        @FXML
        private MenuItem Mclose;

        @FXML
        private MenuItem menuExport;
        @FXML
        private MenuItem menuAdminMode;
        
        
        
        
        
        private boolean adminModeUnlocked = false;
        private String passwordExport = "########";

        @FXML
        private Slider sliderScale;
        
        @FXML
       private TextField  autorTextField;

         @FXML
         private TextField designTextField;

        private ArrayList<String> modelFileNames;

        private int totalUniqueNumber = 0;

        /**
         * @param args the command line arguments
         */
        public static void main(String[] args) {

                launch(args);

        }

        private void loadNewModel(String filePath) {//récusivité ave MDP en entré
                // Open the ZIP file
                ZipFile zipFile = new ZipFile(filePath);
                try {
                        if (zipFile.isEncrypted()) {
                                String password = "";

                                try {
                                        if (passwordManager.getPasswordByFolderName(modelName) != null) {
                                                password = passwordManager.getPasswordByFolderName(modelName); // Implement this method to get the password from the user

                                                if (isPasswordCorrect(zipFile, password)) {
                                                        zipFile.setPassword(password.toCharArray());
                                                        loadNewModel(zipFile);
                                                        return;
                                                }
                                        }
                                } catch (Exception ex) {//in case the file is corrupted
                                        System.out.println("Fichier de mot de passes corompu destruction de ces fichier");

                                        Path path = Paths.get(this.localFiles.getModelsDataDir() + "/encrypted_passwords.txt");

                                        try {
                                                Files.delete(path);
                                                System.out.println("Le fichier a été supprimé avec succès.");
                                        } catch (IOException e) {
                                                System.err.println("Erreur lors de la suppression du fichier : " + e.getMessage());
                                        }

                                        passwordManager = new PasswordManager(this.localFiles.getModelsDataDir());
                                }

                                //in case were not able to open the file we ask the use for a passqord
                                loadNewModelAskingPassword(zipFile, password);

                        } else {
                                loadNewModel(zipFile);
                        }
                } catch (net.lingala.zip4j.exception.ZipException ex) {
                        System.out.println("Error lors de l'ouverture du fichier .ZIP");
                        Logger.getLogger(DesignBuilder.class.getName()).log(Level.SEVERE, null, ex);
                }
        }

        private void loadNewModelAskingPassword(ZipFile zipFile, String password) {
                // Use a lambda expression to capture the parameters
                Consumer<String> ifYes = pwd -> YESloadNewModelAskingPassword(zipFile, pwd);
                // System.out.println("GGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGG          " + !isPasswordCorrect(zipFile, password) + "__" + password);

                if (!isPasswordCorrect(zipFile, password)) {
                        showPasswordDialog("Veuillez entrer le mot de passe du modele : " + modelName, ifYes, null);
                } else {
                        try {
                                passwordManager.addPasswordEntry(modelName, password);
                                passwordManager.saveAllPasswords();
                        } catch (Exception ex) {
                                Logger.getLogger(DesignBuilder.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        zipFile.setPassword(password.toCharArray());
                        loadNewModel(zipFile);
                }
        }

        private void YESloadNewModelAskingPassword(ZipFile zipFile, String password) {
                loadNewModelAskingPassword(zipFile, password);
        }

        private void loadNewModel(ZipFile unlockedZipFile) {
                try {
                        String tempModelName = modelName;
                        this.close();
                        this.modelResources = new ResourcesManager(unlockedZipFile);
                        this.modelName = tempModelName;

                        // Extract the Model_Param.xml file
                        FileHeader fileHeader = unlockedZipFile.getFileHeader("Model_Param.xml");
                        if (fileHeader == null) {
                                fileHeader = unlockedZipFile.getFileHeader(this.modelName + "/Model_Param.xml");
                                if (fileHeader == null) {
                                        throw new IOException("Model_Param.xml not found in the ZIP file.\n" + this.modelName + "/Model_Param.xml" + "\nmodelTestProtected/Model_Param.xml");
                                }
                        }

                        InputStream inputStream = unlockedZipFile.getInputStream(fileHeader);

                        // Parse the XML file
                        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                        DocumentBuilder builder = factory.newDocumentBuilder();
                        Document document = builder.parse(inputStream);

                        // Get the root element
                        Element rootElement = document.getDocumentElement();
                        //       this.modelName = rootElement.getAttribute("name");

                        Element informationsNode = extractSingleElement(rootElement.getElementsByTagName("Informations"));

                        Element subElt = extractSingleElement(informationsNode.getElementsByTagName("DefaultDesign"));
                        this.description = getStringAttribute(subElt, "name", "");

                        subElt = extractSingleElement(informationsNode.getElementsByTagName("Description"));
                        this.defaultDesignName = getStringAttribute(subElt, "Description", "");

                        subElt = extractSingleElement(informationsNode.getElementsByTagName("Export"));
                        this.maxDPI = getIntAttribute(subElt, "maxDPI", maxDPI);
                        this.sliderScale.setMax(maxDPI);
                        passwordExport = "#######################[[[[[[[[";
                        this.passwordExport = getStringAttribute(subElt, "PassWord", passwordExport);

                        // Get all "Output" nodes
                        NodeList outputNodes = rootElement.getElementsByTagName("Output");

                        // Print the names of all "Output" nodes
                        for (int i = 0; i < outputNodes.getLength(); i++) {//we begin by one to avoid the description node
                                Node outputNode = outputNodes.item(i);

                                System.out.println("Output Node: " + outputNode.getNodeName());
                                try {
                                        ImageBuilder imgBuild = new ImageBuilder(null, (Element) outputNode, this);
                                        imageBuilders.add(imgBuild);

                                        System.out.println("\n ##########################################################################\n    "
                                                + imgBuild.toString()
                                                + "\n ##########################################################################\n    ");

                                } catch (XMLErrorInModelException ex) {
                                        Logger.getLogger(DesignBuilder.class.getName()).log(Level.SEVERE, null, ex);
                                }
                        }

                        designPath = null; //reset the design path to null to force a new document

                        interfacesManager.createInterfaceFromImageBuilderList(this.imageBuilders);

                        refreshEverything();

                        Msave.setDisable(false);
                        MsaveAs.setDisable(false);
                        Mclose.setDisable(false);
                        menuAdminMode.setDisable(false);

                } catch (ParserConfigurationException | SAXException | IOException ex) {
                        Logger.getLogger(DesignBuilder.class.getName()).log(Level.SEVERE, null, ex);
                }
        }

        /**
         * This method is used for the user to load new models inside the App
         * model bank if will ask the user if need to overwrite model
         */
        @FXML
        private void copyZipFiles() {
                String destinationDirectory = this.localFiles.getModelsDataDir();

                // Créer un FileChooser pour sélectionner les fichiers
                FileChooser fileChooser = new FileChooser();
                fileChooser.setTitle("Sélectionnez un ou plusieurs fichiers .zip");
                fileChooser.getExtensionFilters().addAll(
                        new FileChooser.ExtensionFilter("ZIP Files", "*.zip")
                );

                // Afficher la boîte de dialogue de sélection de fichiers
                java.util.List<File> selectedFiles = fileChooser.showOpenMultipleDialog(null);

                if (selectedFiles != null && !selectedFiles.isEmpty()) {
                        // Copier chaque fichier sélectionné dans le dossier de destination
                        for (File file : selectedFiles) {
                                try {
                                        Path sourcePath = file.toPath();
                                        Path destinationPath = new File(destinationDirectory + "/" + file.getName()).toPath();

                                        // Vérifier si le fichier existe déjà
                                        if (Files.exists(destinationPath)) {
                                                // Demander confirmation à l'utilisateur
                                                int response = JOptionPane.showConfirmDialog(null,
                                                        "Le fichier " + file.getName() + " existe déjà. Voulez-vous le remplacer ?",
                                                        "Confirmation",
                                                        JOptionPane.YES_NO_OPTION,
                                                        JOptionPane.QUESTION_MESSAGE);

                                                if (response == JOptionPane.YES_OPTION) {
                                                        Files.copy(sourcePath, destinationPath, StandardCopyOption.REPLACE_EXISTING);
                                                        System.out.println("Fichier copié : " + file.getName());
                                                }
                                        } else {
                                                Files.copy(sourcePath, destinationPath, StandardCopyOption.REPLACE_EXISTING);
                                                System.out.println("Fichier copié : " + file.getName());
                                        }
                                } catch (IOException e) {
                                        System.err.println("Erreur lors de la copie du fichier : " + file.getName());
                                        e.printStackTrace();
                                }
                        }
                        initNewDesign();
                }
        }

        
             /**
         * This method is used for the user to load new password files and the
         * password to the password manager
         */
        @FXML
        private void addModelPassword() {
                String destinationDirectory = this.localFiles.getModelsDataDir();

                // Créer un FileChooser pour sélectionner les fichiers texte
                FileChooser fileChooser = new FileChooser();
                fileChooser.setTitle("Sélectionnez un ou plusieurs fichiers texte de mots de passe");
                fileChooser.getExtensionFilters().addAll(
                        new FileChooser.ExtensionFilter("Text Files", "*.txt")
                );

                // Afficher la boîte de dialogue de sélection de fichiers
                java.util.List<File> selectedFiles = fileChooser.showOpenMultipleDialog(null);

                if (selectedFiles != null && !selectedFiles.isEmpty()) {
                        // Traiter chaque fichier texte sélectionné
                        for (File file : selectedFiles) {
                                // Exécuter le code spécifié pour chaque fichier texte
                                PasswordManager temporaryPasswordManager = new PasswordManager(file.getAbsolutePath());
                                try {
                                        this.passwordManager = PasswordManager.mergePasswordManagers(this.passwordManager, temporaryPasswordManager);
                                } catch (Exception ex) {
                                        Logger.getLogger(DesignBuilder.class.getName()).log(Level.SEVERE, null, ex);
                                }
                        }
                        initNewDesign();
                        try {
                                passwordManager.saveAllPasswords();
                        } catch (Exception ex) {
                                Logger.getLogger(DesignBuilder.class.getName()).log(Level.SEVERE, null, ex);
                        }
                }
        }
        
        
        
        @FXML
        private void closeConfirm() {
                Runnable ifYes = this::close;
                showConfirmationDialog("Avez vous bien sauvgardé ce que vous souhaitez ?", ifYes, null);
        }
        
        
        

        private void close() {
                //clean interface
                tabPane.getTabs().clear();
                interfacesManager = new InterfacesManager(tabPane);
                preview.clearAllImagesViews();

                //closing layers
                imageBuilders.clear();

                this.modelName = null;

                Msave.setDisable(true);
                MsaveAs.setDisable(true);
                Mclose.setDisable(true);
                menuAdminMode.setDisable(true);
                menuExport.setDisable(true);

        }

        public String getRootPath() {
                return Paths.get(".").toAbsolutePath().normalize().toString();
        }

        /**
         * Load the model into the menu item
         */
        private void initNewDesign() {
                // Obtenez le chemin du dossier ModelData
                String modelDataPath = this.localFiles.getModelsDataDir();

                // Créez un objet File pour le dossier
                File modelDataFolder = new File(modelDataPath);

                // Vérifiez si le dossier existe et est un dossier
                if (modelDataFolder.exists() && modelDataFolder.isDirectory()) {
                        // Filtre pour ne sélectionner que les fichiers .zip
                        FilenameFilter zipFilter = (dir, name) -> name.toLowerCase().endsWith(".zip");

                        // Liste pour stocker les noms des fichiers .zip
                        modelFileNames = new ArrayList<>();

                        // Lister les fichiers .zip dans le dossier
                        String[] zipFiles = modelDataFolder.list(zipFilter);
                        if (zipFiles != null) {
                                for (String fileName : zipFiles) {
                                        modelFileNames.add(fileName);
                                }
                        }
                        NewModel.getItems().clear();
                        // Ajouter des MenuItem au menu NewModel pour chaque fichier .zip
                        for (String zipFileName : modelFileNames) {
                                // Enlever l'extension .zip
                                String nameWithoutExtension = removeZipExtension(zipFileName);

                                MenuItem menuItem = new MenuItem(nameWithoutExtension);
                                NewModel.getItems().add(menuItem);

                                // Ajouter un gestionnaire d'événements si nécessaire
                                menuItem.setOnAction(event -> {
                                        System.out.println("Selected: " + nameWithoutExtension);
                                        // Ajoutez ici le code à exécuter lorsque l'élément de menu est sélectionné

                                        initNewDesignConfirm(nameWithoutExtension);

                                });
                        }
                } else {
                        System.out.println("Le dossier ModelData n'existe pas ou n'est pas un dossier.");
                }
        }

        private void initNewDesignConfirm(String nameWithoutExtension) {
                String modelAddress = this.localFiles.getModelsDataDir() + "/" + nameWithoutExtension + ".zip";
                if (this.modelName == null) {
                        this.modelName = nameWithoutExtension;
                        loadNewModel(modelAddress);
                        if(this.modelResources!=null) {
                                loadADefaultDesign();
                        }
                        
                } else {
                        DesignBuilder DB = this;

                        Runnable ifYes = new Runnable() {
                                @Override
                                public void run() {
                                        DB.modelName = nameWithoutExtension;
                                        DB.loadNewModel(modelAddress);
                                        loadADefaultDesign();
                                }
                        };
                        showConfirmationDialog("Avez-vous bien sauvegardé ce que vous souhaitez ?", ifYes, null);
                }
        }

        /**
         * this method will look fo the zip inside the model (to get the design
         * and onpen it or propose the user to chose wich one he want to oppen)
         */
        private void loadADefaultDesign() {
                List<String> defaultDesign = this.modelResources.getNestedZipFilesWithDesignData();

                if (!defaultDesign.isEmpty()) {
                        Consumer<String> onOK = selectedOption -> {
                                loadNewDesign(modelResources.get(selectedOption));

                                System.out.println("Option Selected: " + selectedOption);
                        };

                        ComboBoxPopup.showComboBoxDialog("Choisissez le désign par défaut que vous souhaiter ouvrir", (ArrayList<String>) defaultDesign, onOK, null);

                        return;
                }

                if (defaultDesign.size() == 1) {
                        loadNewDesign(modelResources.get(defaultDesign.get(0)));
                        System.out.println(" Option Selected: 0");
                        return;
                }

                System.out.println("No default Design");
        }

        /**
         * Méthode pour enlever l'extension .zip d'un nom de fichier.
         *
         * @param fileName Le nom du fichier avec l'extension .zip.
         * @return Le nom du fichier sans l'extension .zip.
         */
        private String removeZipExtension(String fileName) {
                if (fileName.toLowerCase().endsWith(".zip")) {
                        return fileName.substring(0, fileName.length() - 4);
                }
                return fileName;
        }

// Méthode statique pour combiner deux ArrayList en évitant les doublons
        public static <T> ArrayList<T> combineArrayLists(ArrayList<T> list1, ArrayList<T> list2) {
                Set<T> combinedSet = new HashSet<>(list1);
                combinedSet.addAll(list2);
                return new ArrayList<>(combinedSet);
        }

        private void refreshEverything() {
                for (int i = 0; i < imageBuilders.size(); i++) {
                        this.imageBuilders.get(i).updateFromDown();
                        refreshPreviewImageBox(this.imageBuilders.get(i));

                }
        }

        private void refreshEverythingWithDPIChange() {
                for (int i = 0; i < imageBuilders.size(); i++) {
                        this.imageBuilders.get(i).refreshDPIFromDown();
                        refreshPreviewImageBox(this.imageBuilders.get(i));

                }
        }

        @FXML
        private void loadDesignConfirm() {
                if (this.modelName == null) {
                        loadDesign();
                } else {
                        Runnable ifYes = this::loadDesign;
                        showConfirmationDialog("Avez vous bien sauvgardé ce que vous souhaitez ?", ifYes, null);
                }

        }

        private void loadDesign() {
                FileChooser fileChooser = new FileChooser();
                fileChooser.setTitle("Choisir un modèle de carte à charger");
                fileChooser.getExtensionFilters().addAll(
                        new FileChooser.ExtensionFilter("ZIP Files", "*.zip"));

                // Utilisez showOpenDialog pour charger un fichier existant
                File selectedFile = fileChooser.showOpenDialog(null);
                if (selectedFile != null) {
                        // Affichez le chemin du fichier sélectionné
                        String filePath = selectedFile.getAbsolutePath();
                        System.out.println("Chemin du fichier ZIP sélectionné : " + filePath);

                        loadNewDesign(filePath);

                        // Vous pouvez également passer ce chemin à une autre méthode si nécessaire
                        // processZipFile(filePath);
                }

                System.out.println("loadDesign");
        }

        private void loadNewDesign(byte[] file) {
                try {
                        designPath = "";
                        this.designResources = new ResourcesManager(file);
                        DRYLoadNewDesign();

                } catch (ThisInterfaceDoesNotExistException | ThisZIPFileIsNotADesignException | ParserConfigurationException ex) {
                        Logger.getLogger(DesignBuilder.class.getName()).log(Level.SEVERE, null, ex);
                }
        }

        private void loadNewDesign(String filepath) {
                try {
                        designPath = filepath;
                        this.designResources = new ResourcesManager(filepath);
                        DRYLoadNewDesign();
                } catch (ThisInterfaceDoesNotExistException | ThisZIPFileIsNotADesignException | ParserConfigurationException ex) {
                        Logger.getLogger(DesignBuilder.class.getName()).log(Level.SEVERE, null, ex);
                }
        }

        /**
         * This program will be used to create a new model it will set a
         * resource Manager element, (it will not save the design until the
         * first save)
         *
         * It will pen an xml file in order to
         *
         * @param filepath
         */
        private void DRYLoadNewDesign() throws ParserConfigurationException, ThisZIPFileIsNotADesignException, ThisInterfaceDoesNotExistException {

                // Create a DocumentBuilderFactory
                DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                // Create a DocumentBuilder
                DocumentBuilder builder = factory.newDocumentBuilder();
                // Parse the XML file and return a DOM Document object
                Document document;
                try {
                        document = builder.parse(new ByteArrayInputStream(this.designResources.get("DesignData.xml")));
                } catch (SAXException | IOException ex) {
                        Logger.getLogger(DesignBuilder.class.getName()).log(Level.SEVERE, null, ex);
                        AlertPopup.showErrorAlert("The selected file is not a DESIGN file (no 'DesignData.xml' file inside or corrupted");
                        throw new ThisZIPFileIsNotADesignException();
                }
                // Get the root element
                Element rootElement = document.getDocumentElement();

                String modelNameForThisDesign = rootElement.getAttribute("Model_name");
                if (!modelNameForThisDesign.equals(this.modelName)) {
                        if (modelFileNames.contains(modelNameForThisDesign + ".zip")) {
                                this.modelName = modelNameForThisDesign;
                                loadNewModel(this.localFiles.getModelsDataDir() + "/" + modelName + ".zip");
                        } else {
                                AlertPopup.showErrorAlert("The MODEL ( " + modelNameForThisDesign + " ) necessary for the selected DESIGN does not exist on the inside the 'ModelsData' folder (in the root of this software)");
                                throw new ThisInterfaceDoesNotExistException();
                        }
                }
                this.designName = rootElement.getAttribute("Design_name");
                this.designTextField.setText(this.designName);
                this.author = rootElement.getAttribute("Author_name");
                this.autorTextField.setText(this.author);

                // Get all "Output" nodes
                NodeList allInterfaces = rootElement.getElementsByTagName("Interfaces");

                NodeList InterfacesNodes = allInterfaces.item(0).getChildNodes();
                //  System.out.println("#####"+InterfacesNodes.getLength());
                this.interfacesManager.loadInterfaces(InterfacesNodes);

                refreshEverything();

        }

        /**
         * This method saves the current design to an XML file.
         *
         * @param filepath
         */
        private void saveDesign(String filepath) {
                try {
                        tabPane.setDisable(true);
                        this.designResources.createNewZip(filepath);

                        // Créez un ByteArrayOutputStream pour capturer le contenu XML
                        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                        StreamResult result = new StreamResult(outputStream);

                        // Create a DocumentBuilderFactory
                        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                        DocumentBuilder builder = factory.newDocumentBuilder();
                        Document document = builder.newDocument();
                        // Create the root element
                        Element rootElement = document.createElement("Design");
                        document.appendChild(rootElement);
                        // Set attributes for the root element
                        rootElement.setAttribute("Design_name", this.designName);
                        rootElement.setAttribute("Author_name", this.author);
                        rootElement.setAttribute("Model_name", this.modelName);

                        // Save interfaces
                        Element interfacesElement = document.createElement("Interfaces");

                        interfacesElement = interfacesManager.saveInterfaces(interfacesElement, document);
                        rootElement.appendChild(interfacesElement);
                        // Write the content into XML file
                        TransformerFactory transformerFactory = TransformerFactory.newInstance();
                        Transformer transformer = transformerFactory.newTransformer();
                        DOMSource source = new DOMSource(document);
                        transformer.transform(source, result);

                        // Convertissez le contenu du ByteArrayOutputStream en un tableau de bytes
                        byte[] xmlBytes = outputStream.toByteArray();

                        // Créez un objet File en mémoire avec le contenu XML
                        File xmlFile = new File("DesignData.xml");
                        // Vous pouvez également utiliser un nom de fichier temporaire si vous ne voulez pas spécifier un nom
                        // File xmlFile = File.createTempFile("DesignData", ".xml");

                        // Écrivez les bytes dans le fichier (en mémoire)
                        java.io.FileOutputStream fos = new java.io.FileOutputStream(xmlFile);
                        fos.write(xmlBytes);

                        this.designResources.set("DesignData.xml", xmlFile);
                        this.designResources.save();
                        tabPane.setDisable(false);

                } catch (TransformerConfigurationException ex) {
                        tabPane.setDisable(false);
                        Logger.getLogger(DesignBuilder.class.getName()).log(Level.SEVERE, null, ex);
                } catch (TransformerException | IOException | ParserConfigurationException ex) {
                        tabPane.setDisable(false);
                        Logger.getLogger(DesignBuilder.class.getName()).log(Level.SEVERE, null, ex);
                }
        }

        @Override
        public void start(Stage primarystage) throws Exception {
                try {
                        newSeed();
                        localFiles = new LocalFilesManagement();
                        passwordManager = new PasswordManager(this.localFiles.getModelsDataDir());
                       

                        this.id = DesignBuilder.index;
                        DesignBuilder.index++;

                        URL[] urlList = new URL[1];
                        URL inter_principalle = getClass().getClassLoader().getResource("InterfaceBatcher.fxml");
                        urlList[0] = inter_principalle;
//                           System.out.println(inter_principalle);

                        if (CheckArrayHaveNull(urlList)) {
                                throw new ResourcesFileErrorException("One or more files are missing in the ressources files");
                        }

                        FXMLLoader loader = new FXMLLoader(inter_principalle);
                        loader.setController(this);
                        Parent root = loader.load();

                        interfacesManager = new InterfacesManager(tabPane);

                        initNewDesign();

                        primarystage.setTitle("Batcher FOYER");
                        scene = new Scene(root);
                        primarystage.setScene(scene);
                        primarystage.show();

                        designResources = new ResourcesManager();

                        Scale scale = new Scale();
                        scale.setX(1); // Échelle horizontale
                        scale.setY(1); // Échelle verticale
                        scale.setPivotX(0); // Point de pivot pour la transformation (coin supérieur gauche)
                        scale.setPivotY(0); // Point de pivot pour la transformation (coin supérieur gauche)
                        scene.getRoot().getTransforms().add(scale);

                         initializeAutorTextField();
                        //refreshEverything();

                } catch (ResourcesFileErrorException e) {

                }
        }

//         
        @FXML
        private void saveDesing() {
                if (this.designPath != null) {
                        saveDesign(this.designPath);
                } else {
                        saveAsDesign();
                }
        }

        /**
         * This function is call for a save as it : -open a filepath selectin -
         * launch the save in the selected path
         *
         * @throws IOException
         * @throws TransformerException
         */
        @FXML
        private void saveAsDesign() {

                FileChooser fileChooser = new FileChooser();
                fileChooser.setTitle("Choisisser où sauvgader le modèle de carte");
                fileChooser.getExtensionFilters().addAll(
                        new FileChooser.ExtensionFilter("ZIP Files", "*.zip"));
                File selectedFile = fileChooser.showSaveDialog(null);
                if (selectedFile != null) {
                        this.designPath = selectedFile.getAbsolutePath();
                        saveDesign(this.designPath);
                        System.out.println("----- The Design is saved");
                }

        }

        public int getId() {
                return id;
        }

        /**
         * This function retunr true if one or more null element is detected in
         * the @array
         *
         * @param Array
         * @return
         */
        private static boolean CheckArrayHaveNull(Object[] Array) {
                // Check if array contains null elements
                boolean containsNull = false;
                for (Object obj : Array) {
                        if (obj == null) {
                                containsNull = true;
                                break;
                        }
                }
                return containsNull;
        }

        /**
         *
         * @param fileName
         * @return Return the absoute address of the fileName
         */
        public static String getResourcesPath(String fileName) {
                // Obtenir le répertoire courant du projet
                File currentDir = new File("");

                // Obtenir le chemin d'accès absolu du répertoire courant
                return currentDir.getAbsolutePath() + "/resources/" + fileName;
        }

        public float getPixelMmFactor() {
                return (float) (this.DPI / 25.4);
        }

        public ResourcesManager getModelResources() {
                return modelResources;
        }

        private void setModelResources(ResourcesManager templateResources) {
                this.modelResources = templateResources;
        }

        public ResourcesManager getDesignResources() {
                return designResources;
        }

        private void setDesignResources(ResourcesManager designResources) {
                this.designResources = designResources;
        }

        /**
         * This method will refresh the main preview
         */
        public void refreshPreview() {
//                this.preview.clearAllImagesViews();
//                for (ImageBuilder_old imageBuilder : imageBuilders) {
//                        imageBuilder.refreshPreview(this.preview);
//                }
        }

        @FXML
        private void updateScale() {
                this.DPI = (int) this.sliderScale.getValue();
                refreshEverythingWithDPIChange();
        }

        @FXML
        private void unlockAdminMode() {
                testUnlockAdminMode("");

        }

        private void initializeAutorTextField() {
                String filePath = this.localFiles.getLocalDataDir() + "/UserData.txt";
                try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
                        String line = reader.readLine();
                        if (line != null) {
                                this.autorTextField.setText(line); 
                                this.author =line;
                        }
                } catch (IOException e) {
                        // Handle the exception, e.g., log it or show an error message
                        e.printStackTrace();
                }
        }

        @FXML
        private void updateAutorTextField() {
                this.author = this.autorTextField.getText();
            //    System.out.println("#### Autor text update");
                String filePath = this.localFiles.getLocalDataDir() + "/UserData.txt";
                try (FileWriter writer = new FileWriter(filePath)) {
                        writer.write(this.author);
                } catch (IOException e) {
                        // Handle the exception, e.g., log it or show an error message
                        e.printStackTrace();
                }
        }
        
         @FXML
        private void updateDesignTextField() {
                this.designName=this.designTextField.getText();
                if(!"".equals(this.designPath)){
                        this.saveDesing();
                }
        }


        private void testUnlockAdminMode(String pswd) {

                if (pswd.equals(this.passwordExport)) {
                        adminModeUnlocked = true;
                        this.menuAdminMode.setDisable(true);
                        this.menuExport.setDisable(false);
                        this.maxDPI = 600;
                } else {
                        adminModeUnlocked = false;
                        this.menuAdminMode.setDisable(false);
                        this.menuExport.setDisable(true);
                        //      System.out.println("Tested password : "+pswd+ " real one : "+passwordExport);
                        Consumer<String> ifYes = pwd -> {
                                testUnlockAdminMode(pwd);
                        };
                        showPasswordDialog("Veuillez entrer le mot de passe pour valider le mode administrateur  : " + modelName, ifYes, null);
                }

        }

        @FXML
        private void exportCard() {
                if (adminModeUnlocked) {
                        // Open the spinner dialog to select the number of images to export
                        SpinnerPopup.showSpinnerDialog(
                                "Sélectionnez le nombre d'images à exporter",
                                1, // Minimum value
                                100, // Maximum value
                                1, // Initial value
                                numImages -> {
                                        // If the user clicks "Yes", proceed to save the images
                                        saveImagesToDirectory(numImages);
                                },
                                () -> {
                                        // If the user clicks "Cancel", do nothing or handle cancellation
                                        System.out.println("Export cancelled");
                                }
                        );
                }
        }

        /**
         * Save the different image of the Design for export
         *
         * @param numImages
         */
        private void saveImagesToDirectory(int numImages) {
                DirectoryChooser directoryChooser = new DirectoryChooser();
                directoryChooser.setTitle("Select a Directory");

                File selectedDirectory = directoryChooser.showDialog(null);

                if (selectedDirectory != null) {
                        String batchDirectoryName = getNextBatchDirectoryName(selectedDirectory);
                        Path batchDirectoryPath = Paths.get(selectedDirectory.getAbsolutePath(), batchDirectoryName);

                        try {
                                Files.createDirectories(batchDirectoryPath);
                                System.out.println("Directory created: " + batchDirectoryPath.toString());

                                // Loop to save the specified number of images
                                int oldDPI = DPI;
                                this.DPI = 600;
                                refreshEverythingWithDPIChange();
                                for (int i = 0; i < numImages; i++) {
                                        for (ImageBuilder imgBuilder : this.imageBuilders) {
                                                saveImageToDirectory(imgBuilder.getImageOut(), batchDirectoryPath.toString(), imgBuilder.getName() + i + ".png");
                                                this.refreshEverything();
                                        }
                                }
                                this.DPI = oldDPI;
                                refreshEverythingWithDPIChange();
                        } catch (IOException e) {
                                e.printStackTrace();
                        }
                }
        }

        private String getNextBatchDirectoryName(File parentDirectory) {
                int i = 0;
                String batchDirectoryName;
                do {
                        batchDirectoryName = "Batch_" + i;
                        i++;
                } while (new File(parentDirectory, batchDirectoryName).exists());
                return batchDirectoryName;
        }

        public static void saveImageToDirectory(BufferedImage image, String directoryPath, String fileName) {
                try {
                        File outputFile = new File(directoryPath, fileName);
                        ImageIO.write(image, "png", outputFile);
                        System.out.println("Image saved: " + outputFile.getAbsolutePath());
                } catch (IOException e) {
                        e.printStackTrace();
                }
        }

        public void refreshPreviewImageBox(ImageBuilder imgBuilder) {
                preview.setImageView(imgBuilder.DRYComputeUniqueID(), staticFunctions.StaticImageEditing.createImageView(imgBuilder.getImageOut()));
                //System.out.println("#######"+imgBuilder.getId());
        }

        /**
         * this will return a unique number (use to save image correctly in case
         * of random iamge allocator)
         *
         * @return
         */
        public int getUniqueNumber() {
                this.totalUniqueNumber++;
                return totalUniqueNumber;
        }

        public Random getRandom() {
                return random;
        }

        // Méthode pour générer une nouvelle graine et créer un nouvel objet Random
        public void newSeed() {
                long newSeed;
                if (random == null) {
                        newSeed = 84844551;
                } else {
                        newSeed = this.random.nextLong();

                }

                // Créer un nouvel objet Random avec la nouvelle graine
                this.random = new Random(newSeed);
                System.out.println("Nouvelle graine générée : " + newSeed);
        }

}
//####################
///######### CLEAN AND BUILD
//####################

//CMD to  package in .exe
//jpackage --input C:\BACKUP\ENSE3\Foyer\Programme_Java\Batcher_Foyer\dist --name TestBatcher --main-jar Batcher_Foyer.jar --main-class designBuilder.DesignBuilder --type exe --java-options "--module-path 'C:\softs\java\javafx-sdk-23.0.1\lib' --add-modules javafx.controls,javafx.fxml" --app-version 1.11 --icon C:\BACKUP\ENSE3\Foyer\Programme_Java\icon.ico --dest C:\BACKUP\ENSE3\Foyer\Programme_Java\Export_APP
//jpackage --input C:\BACKUP\ENSE3\Foyer\Programme_Java\Batcher_Foyer\dist --name CarteFoyerCreator --main-jar Batcher_Foyer.jar --main-class AppInterface.DesignBuilder --type exe --java-options "--module-path 'C:\softs\java\javafx-sdk-23.0.1\lib' --add-modules javafx.controls,javafx.fxml" --app-version 1.XX --icon C:\BACKUP\ENSE3\Foyer\Programme_Java\icon.ico --dest C:\BACKUP\ENSE3\Foyer\Programme_Java\Export_APP
//attention si changement de localisation de main class$
//avant cd  C:\softs\java\jdk-23\bin  \\pour avoir les logs 
//"C:\BACKUP\ENSE3\Foyer\Programme_Java\Export_APP\console_log.txt" 2>&1
