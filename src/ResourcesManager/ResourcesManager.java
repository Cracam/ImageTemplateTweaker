/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ResourcesManager;

/**
 *
 * @author LECOURT Camille
 *
 *
 * il faut refaire la création du resoucres anager; Il faudra autoriser sa
 * création qna s qu'il ne soi lié à une adresse
 *
 */
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;
import javax.imageio.ImageIO;

public class ResourcesManager {

        private final Map<String, File> fileMap;
        private String zipFilePath;

        public ResourcesManager(String zipFilePath) {
                this.zipFilePath = zipFilePath;
                this.fileMap = new HashMap<>();
                loadZipFile();
        }
        
   
        public ResourcesManager(){
                 this.fileMap = new HashMap<>();
        }
        
        

        private void loadZipFile() {
                try (FileInputStream fis = new FileInputStream(zipFilePath); ZipInputStream zis = new ZipInputStream(fis)) {

                        ZipEntry zipEntry = zis.getNextEntry();
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
                                zipEntry = zis.getNextEntry();
                        }
                } catch (IOException e) {
                        e.printStackTrace();
                }
        }

        public void save() {
                try (FileOutputStream fos = new FileOutputStream(zipFilePath); ZipOutputStream zos = new ZipOutputStream(fos)) {

                        for (Map.Entry<String, File> entry : fileMap.entrySet()) {
                                String fileName = entry.getKey();
                                File file = entry.getValue();
                                FileInputStream fis = new FileInputStream(file);
                                ZipEntry zipEntry = new ZipEntry(fileName);
                                zos.putNextEntry(zipEntry);

                                byte[] buffer = new byte[1024];
                                int length;
                                while ((length = fis.read(buffer)) >= 0) {
                                        zos.write(buffer, 0, length);
                                }

                                zos.closeEntry();
                                fis.close();
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
                }

        }

        public void createNewZip(String filepath) {
               this.zipFilePath =filepath;
                this.fileMap.clear(); // Clear the existing file map
                try (FileOutputStream fos = new FileOutputStream(filepath); ZipOutputStream zos = new ZipOutputStream(fos)) {

                } catch (IOException e) {
                        e.printStackTrace();
                }
        }

}
