import net.lingala.zip4j.ZipFile;
import net.lingala.zip4j.model.FileHeader;

import java.util.List;

public class ZipFileExample {
    public static void main(String[] args) {
        try {
            String zipFilePath = "C:\\BACKUP\\ENSE3\\Foyer\\Programme_Java\\Batcher_Foyer\\test_data\\modelTestProtected.zip";
            String password = "ense3";

            ZipFile zipFile = new ZipFile(zipFilePath);
            if (zipFile.isEncrypted()) {
                zipFile.setPassword(password.toCharArray());
            }

            // List all files in the ZIP archive
            List<FileHeader> fileHeaders = zipFile.getFileHeaders();
            for (FileHeader fileHeader : fileHeaders) {
                System.out.println("File: " + fileHeader.getFileName());
            }

            // Retrieve the specific file header
            FileHeader fileHeader = zipFile.getFileHeader("modelTestProtected/Model_Param.xml");
            if (fileHeader != null) {
                System.out.println("Found file: " + fileHeader.getFileName());
            } else {
                System.out.println("File not found.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
