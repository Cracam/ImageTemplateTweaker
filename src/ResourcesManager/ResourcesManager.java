package ResourcesManager;

import java.awt.image.BufferedImage;
import java.io.*;
import java.util.HashMap;
import java.util.Map;
import javax.imageio.ImageIO;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveInputStream;
import org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream;
import org.apache.commons.compress.parallel.InputStreamSupplier;

public class ResourcesManager {

        private final Map<String, File> fileMap;
        private String zipFilePath;
        private String password;
        private boolean usePassword;

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

        public ResourcesManager(String password) {
                this.password = password;
                this.usePassword = true;
                this.fileMap = new HashMap<>();
        }

        public ResourcesManager() {
                this.usePassword = false;
                this.fileMap = new HashMap<>();
        }

        private void loadZipFile() {
                try (FileInputStream fis = new FileInputStream(zipFilePath); InputStream inputStream = usePassword ? new InputStreamSupplier(fis, password.toCharArray()) : fis; ZipArchiveInputStream zis = new ZipArchiveInputStream(inputStream)) {

                        ZipArchiveEntry zipEntry = zis.getNextZipEntry();
                        while (zipEntry != null) {
                                String fileName = zipEntry.getName();
                                File tempFile = File.createTempFile(fileName, null);
                                tempFile.deleteOnExit();

                                try (FileOutputStream fos = new FileOutputStream(tempFile)) {
                                        byte[] buffer = new byte[1024];
                                        int length;
                                        while ((length = zis.read(buffer)) > 0) {
                                                fos.write(buffer, 0, length);
                                        }
                                }

                                fileMap.put(fileName, tempFile);
                                zis.closeEntry();
                                zipEntry = zis.getNextZipEntry();
                        }
                } catch (IOException e) {
                        e.printStackTrace();
                }
        }

        public void save() {
                try (FileOutputStream fos = new FileOutputStream(zipFilePath); ZipArchiveOutputStream zos = new ZipArchiveOutputStream(fos)) {

                        if (usePassword) {
                                zos.setPassword(password.toCharArray());
                        }

                        for (Map.Entry<String, File> entry : fileMap.entrySet()) {
                                String fileName = entry.getKey();
                                File file = entry.getValue();
                                try (FileInputStream fis = new FileInputStream(file)) {
                                        ZipArchiveEntry zipEntry = new ZipArchiveEntry(fileName);
                                        zos.putArchiveEntry(zipEntry);

                                        byte[] buffer = new byte[1024];
                                        int length;
                                        while ((length = fis.read(buffer)) >= 0) {
                                                zos.write(buffer, 0, length);
                                        }

                                        zos.closeArchiveEntry();
                                }
                        }
                } catch (IOException e) {
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
                try (FileOutputStream fos = new FileOutputStream(filepath); ZipArchiveOutputStream zos = new ZipArchiveOutputStream(fos)) {
                        zos.setPassword(password.toCharArray());
                } catch (IOException e) {
                        e.printStackTrace();
                }
        }

        public void createNewZip(String filepath) {
                this.zipFilePath = filepath;
                this.usePassword = false;
                this.fileMap.clear(); // Clear the existing file map
                try (FileOutputStream fos = new FileOutputStream(filepath); ZipArchiveOutputStream zos = new ZipArchiveOutputStream(fos)) {
                        // No password set
                } catch (IOException e) {
                        e.printStackTrace();
                }
        }
}
