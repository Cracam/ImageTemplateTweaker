package ResourcesManager;

import static AppInterface.Popups.AlertPopup.showInfoAlert;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import net.lingala.zip4j.ZipFile;
import net.lingala.zip4j.model.FileHeader;
import net.lingala.zip4j.model.ZipParameters;
import net.lingala.zip4j.model.enums.AesKeyStrength;
import net.lingala.zip4j.model.enums.EncryptionMethod;

public class ResourcesManager {

        private final Map<String, byte[]> fileMap;
        private final Map<String, BufferedImage> imageMap;
        private final Map<String, ZipFile> zipFileMap;
        private String zipFilePath;
        private String password;
        private boolean usePassword;
        private ZipFile zipFile;
        private Path tempDir;

        public ResourcesManager(String zipFilePath, String password) {
                this.zipFilePath = zipFilePath;
                this.password = password;
                this.usePassword = true;
                this.fileMap = new HashMap<>();
                this.imageMap = new HashMap<>();
                this.zipFileMap = new HashMap<>();
                loadZipFile();
        }

        public ResourcesManager(String zipFilePath) {
                this.zipFilePath = zipFilePath;
                this.usePassword = false;
                this.fileMap = new HashMap<>();
                this.imageMap = new HashMap<>();
                this.zipFileMap = new HashMap<>();
                loadZipFile();
        }

        public ResourcesManager(ZipFile zipFile) {
                this.zipFile = zipFile;
                this.zipFilePath = zipFile.getFile().getPath();
                this.usePassword = false; //because if it use password it will be already used
                this.fileMap = new HashMap<>();
                this.imageMap = new HashMap<>();
                this.zipFileMap = new HashMap<>();
                loadZipFile();
        }

        public ResourcesManager(byte[] zipContent) {
                this.usePassword = false;
                this.fileMap = new HashMap<>();
                this.imageMap = new HashMap<>();
                this.zipFileMap = new HashMap<>();
                loadZipFromBytes(zipContent);
        }

        public ResourcesManager() {
                this.usePassword = false;
                this.fileMap = new HashMap<>();
                this.imageMap = new HashMap<>();
                this.zipFileMap = new HashMap<>();
        }

        public void setPassword(String password) {
                this.password = password;
                this.usePassword = true;
        }

        public void removePassword() {
                this.password = "";
                this.usePassword = false;
        }

        private void loadZipFile() {
                try {
                        if (zipFile == null) {
                                zipFile = new ZipFile(zipFilePath);
                        }
                        if (usePassword) {
                                zipFile.setPassword(password.toCharArray());
                        }

                        // Utiliser le répertoire temporaire système
                        Path tempDir = Paths.get(System.getProperty("java.io.tmpdir"), "temp_" + System.currentTimeMillis());
                        // System.out.println("-------------- Temp path : "+tempDir);
                        if (!Files.exists(tempDir)) {
                                Files.createDirectories(tempDir);
                        }

                        // Extraire tous les fichiers dans le répertoire temporaire
                        zipFile.extractAll(tempDir.toString());

                        // Traiter chaque fichier dans le répertoire temporaire
                        processDirectory(tempDir);

                        // Supprimer le répertoire temporaire après avoir traité tous les fichiers
                        deleteDirectory(tempDir);

                        System.out.println("------------------------------" + toString());
                } catch (Exception e) {
                        e.printStackTrace();
                }
        }

        private void loadZipFromBytes(byte[] zipContent) {
                try {
                        // Créer un fichier temporaire pour contenir le contenu du zip
                        Path tempFile = Files.createTempFile("temp_zip", ".zip");
                        Files.write(tempFile, zipContent);

                        // Créer un ZipFile à partir du fichier temporaire
                        zipFile = new ZipFile(tempFile.toFile());

                        // Utiliser le répertoire temporaire système
                        Path tempDir = Paths.get(System.getProperty("java.io.tmpdir"), "temp_" + System.currentTimeMillis());
                        if (!Files.exists(tempDir)) {
                                Files.createDirectories(tempDir);
                        }

                        // Extraire tous les fichiers dans le répertoire temporaire
                        zipFile.extractAll(tempDir.toString());

                        // Traiter chaque fichier dans le répertoire temporaire
                        processDirectory(tempDir);

                        // Supprimer le répertoire temporaire après avoir traité tous les fichiers
                        deleteDirectory(tempDir);

                        // Supprimer le fichier temporaire
                        Files.delete(tempFile);

                        System.out.println("------------------------------" + toString());
                } catch (Exception e) {
                        e.printStackTrace();
                }
        }

        private void processDirectory(Path directory) {
                try {
                        Files.walk(directory).forEach(path -> {
                                if (Files.isRegularFile(path)) {
                                        String fileName = path.getFileName().toString(); // Use only the file name without the path
                                        File file = path.toFile();

                                        try {
                                                byte[] fileContent = Files.readAllBytes(path);

                                                if (fileName.toLowerCase().endsWith(".zip")) {
                                                        // Handle ZIP files
                                                        try {
                                                                ZipFile nestedZipFile = new ZipFile(file);
                                                                if (usePassword) {
                                                                        nestedZipFile.setPassword(password.toCharArray());
                                                                }
                                                                zipFileMap.put(fileName, nestedZipFile);
                                                                System.out.println("ZIP file detected and added: " + fileName); // Debugging line
                                                        } catch (Exception e) {
                                                                System.err.println("Error processing ZIP file: " + fileName);
                                                                e.printStackTrace();
                                                        }
                                                } else if (isImageFile(fileName)) {
                                                        // Handle image files
                                                        try {
                                                                BufferedImage image = ImageIO.read(file);
                                                                if (image != null) {
                                                                        imageMap.put(fileName, image);
                                                                } else {
                                                                        fileMap.put(fileName, fileContent);
                                                                }
                                                        } catch (Exception e) {
                                                                System.err.println("Error processing image file: " + fileName);
                                                                e.printStackTrace();
                                                        }
                                                } else {
                                                        // Handle other files
                                                        fileMap.put(fileName, fileContent);
                                                }
                                        } catch (IOException e) {
                                                System.err.println("Error reading file: " + fileName);
                                                e.printStackTrace();
                                        }

                                        // Delete the file after processing
                                        file.delete();
                                }
                        });
                } catch (Exception e) {
                        e.printStackTrace();
                }
        }

        private boolean isImageFile(String fileName) {
                String lowerCaseFileName = fileName.toLowerCase();
                return lowerCaseFileName.endsWith(".png")
                        || lowerCaseFileName.endsWith(".jpg")
                        || lowerCaseFileName.endsWith(".jpeg")
                        || lowerCaseFileName.endsWith(".gif")
                        || lowerCaseFileName.endsWith(".bmp");
        }

        public void save() {
                try {
                        if (zipFile == null) {
                                zipFile = new ZipFile(zipFilePath);
                        }
                        ZipParameters parameters = new ZipParameters();
                        parameters.setEncryptFiles(usePassword);
                        if (usePassword) {
                                parameters.setEncryptionMethod(EncryptionMethod.AES);
                                parameters.setAesKeyStrength(AesKeyStrength.KEY_STRENGTH_256);
                        }

                        // Utiliser le répertoire temporaire système
                        Path tempDir = Paths.get(System.getProperty("java.io.tmpdir"), "temp_" + System.currentTimeMillis());
                        if (!Files.exists(tempDir)) {
                                Files.createDirectories(tempDir);
                        }

                        // Save BufferedImages to temporary files and add to zip
                        for (Map.Entry<String, BufferedImage> entry : imageMap.entrySet()) {
                                Path tempFile = Files.createTempFile(tempDir, "zip_save", "_" + entry.getKey());
                                ImageIO.write(entry.getValue(), getFileExtension(entry.getKey()), tempFile.toFile());

                                // Rename the temporary file to the original file name
                                Path renamedFile = tempDir.resolve(entry.getKey());
                                Files.move(tempFile, renamedFile, StandardCopyOption.REPLACE_EXISTING);

                                zipFile.addFile(renamedFile.toFile(), parameters);
                                Files.delete(renamedFile); // Clean up temporary file
                        }

                        // Save nested ZipFiles to temporary files and add to zip
                        for (Map.Entry<String, ZipFile> entry : zipFileMap.entrySet()) {
                                Path tempFile = Files.createTempFile(tempDir, "zip_save", "_" + entry.getKey());

                                // Extract the nested zip file to a temporary location
                                entry.getValue().extractAll(tempFile.getParent().toString());

                                // Rename the temporary file to the original file name
                                Path renamedFile = tempDir.resolve(entry.getKey());
                                Files.move(tempFile, renamedFile, StandardCopyOption.REPLACE_EXISTING);

                                zipFile.addFile(renamedFile.toFile(), parameters);
                                Files.delete(renamedFile); // Clean up temporary file
                        }

                        // Add other files
                        for (Map.Entry<String, byte[]> entry : fileMap.entrySet()) {
                                Path tempFile = Files.createTempFile(tempDir, "zip_save", "_" + entry.getKey());
                                Files.write(tempFile, entry.getValue());

                                // Rename the temporary file to the original file name
                                Path renamedFile = tempDir.resolve(entry.getKey());
                                Files.move(tempFile, renamedFile, StandardCopyOption.REPLACE_EXISTING);

                                zipFile.addFile(renamedFile.toFile(), parameters);
                                Files.delete(renamedFile); // Clean up temporary file
                        }

                        // Supprimer le répertoire temporaire après avoir traité tous les fichiers
                        deleteDirectory(tempDir);
                } catch (Exception e) {
                        e.printStackTrace();
                }
        }

   

        private String getFileExtension(String fileName) {
                int lastDotIndex = fileName.lastIndexOf('.');
                if (lastDotIndex > 0) {
                        return fileName.substring(lastDotIndex + 1);
                }
                return "png"; // default extension
        }

        public byte[] get(String key) {
                return fileMap.get(key);
        }

        public BufferedImage getBufferedImage(String key) {
                return imageMap.get(key);
        }

        public ZipFile getZipFile(String key) {
                return zipFileMap.get(key);
        }

        public void set(String key, byte[] fileContent) {
                fileMap.put(key, fileContent);
        }

        public void set(String key, File file) {
                try {
                        byte[] fileContent = Files.readAllBytes(file.toPath());
                        fileMap.put(key, fileContent);
                } catch (IOException e) {
                        System.err.println("Error reading file: " + file.getName());
                        e.printStackTrace();
                }
        }

        public void setBufferedImage(String imageName, BufferedImage image) {
                imageMap.put(imageName, image);
        }

        public void setZipFile(String key, ZipFile zipFile) {
                zipFileMap.put(key, zipFile);
        }

        /**
         * Used to load a new file
         *
         * @param filepath
         */
        public void createNewZip(String filepath) {
                this.zipFilePath = filepath;
                this.usePassword = false;
                this.fileMap.clear(); // Clear the existing file map
                this.imageMap.clear(); // Clear the existing image map
                this.zipFileMap.clear(); // Clear the existing zip file map
                try {
                        zipFile = new ZipFile(filepath);
                        ZipParameters parameters = new ZipParameters();
                        parameters.setEncryptFiles(false);

                        // Add files to the zip
                        for (Map.Entry<String, byte[]> entry : fileMap.entrySet()) {
                                File tempFile = File.createTempFile("zip_save", "_" + entry.getKey());
                                Files.write(tempFile.toPath(), entry.getValue());
                                zipFile.addFile(tempFile, parameters);
                                tempFile.delete(); // Clean up temporary file
                        }
                } catch (Exception e) {
                        e.printStackTrace();
                }
        }

        public List<String> getNestedZipFilesWithDesignData() {
                List<String> nestedZipFilesNames = new ArrayList<>();

                // Parcourez les fichiers ZIP dans la Map
                for (Map.Entry<String, ZipFile> entry : zipFileMap.entrySet()) {
                        ZipFile zipFile = entry.getValue();
                        System.out.println("Zip File detected : " + entry.getKey());
                        if (containsDesignDataXml(zipFile)) {
                                // Ajoutez le fichier ZIP à la liste des fichiers ZIP imbriqués
                                nestedZipFilesNames.add(entry.getKey());
                        }
                }
                return nestedZipFilesNames;
        }

        private boolean containsDesignDataXml(ZipFile zipFile) {
                try {
                        // Obtenez la liste des en-têtes de fichiers dans le fichier ZIP imbriqué
                        List<FileHeader> nestedFileHeaders = zipFile.getFileHeaders();

                        // Parcourez les en-têtes de fichiers
                        for (FileHeader nestedFileHeader : nestedFileHeaders) {
                                if (nestedFileHeader.getFileName().equals("DesignData.xml")) {
                                        return true;
                                }
                        }
                } catch (Exception e) {
                        System.out.println("Erreur lors de la vérification du fichier ZIP imbriqué: " + e.getMessage());
                }
                return false;
        }

        @Override
        public String toString() {
                StringBuilder sb = new StringBuilder();
                sb.append("ResourcesManager{\n");
                sb.append("  zipFilePath='").append(zipFilePath).append("'\n");
                sb.append("  usePassword=").append(usePassword).append("\n");
                sb.append("  fileMap=\n");
                for (Map.Entry<String, byte[]> entry : fileMap.entrySet()) {
                        sb.append("    ").append(entry.getKey()).append("\n");
                }
                sb.append("  imageMap=\n");
                for (Map.Entry<String, BufferedImage> entry : imageMap.entrySet()) {
                        sb.append("    ").append(entry.getKey()).append("\n");
                }
                sb.append("  zipFileMap=\n");
                for (Map.Entry<String, ZipFile> entry : zipFileMap.entrySet()) {
                        sb.append("    ").append(entry.getKey()).append("\n");
                }
                sb.append("  zipFile=").append(zipFile != null ? zipFile.getFile().getPath() : "null").append("\n");
                sb.append("}");
                return sb.toString();
        }

             public static void deleteDirectory(Path directory) throws IOException {
                if (Files.exists(directory)) {
                        Files.walk(directory)
                                .sorted((a, b) -> -a.compareTo(b)) // Reverse order to delete files before directories
                                .forEach(path -> {
                                        try {
                                                Files.delete(path);
                                        } catch (IOException e) {
                                                e.printStackTrace();
                                        }
                                });
                }
        }
}
