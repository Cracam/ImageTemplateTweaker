package ResourcesManager;

import java.awt.image.BufferedImage;
import java.io.*;
import java.util.HashMap;
import java.util.Map;
import javax.imageio.ImageIO;
import net.lingala.zip4j.ZipFile;
import net.lingala.zip4j.model.ZipParameters;
import net.lingala.zip4j.model.enums.AesKeyStrength;
import net.lingala.zip4j.model.enums.EncryptionMethod;

public class ResourcesManager {

        private final Map<String, File> fileMap;
        private String zipFilePath;
        private String password;
        private boolean usePassword;
        private ZipFile zipFile;

        public ResourcesManager(String zipFilePath, String password) {
                this.zipFilePath = zipFilePath;
                this.password = password;
                this.usePassword = true;
                this.fileMap = new HashMap<>();
                loadZipFile();
        }

        public ResourcesManager(String zipFilePath) {
                this.zipFilePath = zipFilePath;
                this.usePassword = false;
                this.fileMap = new HashMap<>();
                loadZipFile();
        }

        public ResourcesManager(ZipFile zipFile) {
                this.zipFile = zipFile;
                this.zipFilePath = zipFile.getFile().getPath();
                this.usePassword = false; //because if it use passworld it will be already used

                this.fileMap = new HashMap<>();
                loadZipFile();
        }

        public ResourcesManager() {
                this.usePassword = false;
                this.fileMap = new HashMap<>();
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
                        zipFile.extractAll(System.getProperty("java.io.tmpdir"));

                        for (File file : new File(System.getProperty("java.io.tmpdir")).listFiles()) {
                                fileMap.put(file.getName(), file);
                        }
                } catch (Exception e) {
                        e.printStackTrace();
                }
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

                        for (Map.Entry<String, File> entry : fileMap.entrySet()) {
                                zipFile.addFile(entry.getValue(), parameters);
                        }
                } catch (Exception e) {
                        e.printStackTrace();
                }
        }

        public File get(String key) {
                return fileMap.get(key);
        }

        public void set(String key, File file) {
                fileMap.put(key, file);
        }

        public void setBufferedImage(String imageName, BufferedImage image) {
                // Create a File object
                File outputFile = new File(imageName);

                // Write the BufferedImage to the file
                try {
                        ImageIO.write(image, "png", outputFile);
                        System.out.println("Image successfully written to file.");
                        fileMap.put(imageName, outputFile);

                } catch (IOException e) {
                        e.printStackTrace();
                }
        }

        public void createNewZip(String filepath, String password) {
                this.zipFilePath = filepath;
                this.password = password;
                this.usePassword = true;
                this.fileMap.clear(); // Clear the existing file map
                try {
                        zipFile = new ZipFile(filepath);
                        ZipParameters parameters = new ZipParameters();
                        parameters.setEncryptFiles(true);
                        parameters.setEncryptionMethod(EncryptionMethod.AES);
                        parameters.setAesKeyStrength(AesKeyStrength.KEY_STRENGTH_256);

                        // Add files to the zip
                        for (Map.Entry<String, File> entry : fileMap.entrySet()) {
                                zipFile.addFile(entry.getValue(), parameters);
                        }
                } catch (Exception e) {
                        e.printStackTrace();
                }
        }

        public void createNewZip(String filepath) {
                this.zipFilePath = filepath;
                this.usePassword = false;
                this.fileMap.clear(); // Clear the existing file map
                try {
                        zipFile = new ZipFile(filepath);
                        ZipParameters parameters = new ZipParameters();
                        parameters.setEncryptFiles(false);

                        // Add files to the zip
                        for (Map.Entry<String, File> entry : fileMap.entrySet()) {
                                zipFile.addFile(entry.getValue(), parameters);
                        }
                } catch (Exception e) {
                        e.printStackTrace();
                }
        }
}
