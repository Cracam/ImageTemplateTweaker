package ResourcesManager;

import AppInterface.DesignBuilder;
import static ResourcesManager.PasswordEntry.RandomKeyGenerator.generateRandomKey;
import com.sun.javafx.logging.PlatformLogger.Level;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.System.Logger;
import java.util.ArrayList;
import java.util.Base64;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;


public class PasswordManager {

        private static final String MASTER_KEY = "votreClregegefsse123"; // Master key of 16 characters for AES-128
        private static final String ALGORITHM = "AES";
        private  SecretKeySpec secretKey;
        private  ArrayList<PasswordEntry> passwordEntries;
        private  String filePath;

        /**
         * Constructor that reads the encrypted main key from a text file and
         * decrypts it with the master key. Also reads and stores the encrypted
         * password entries from the file.
         *
         * @param encryptedKeyFilePath The path to the file containing the
         * encrypted main key and password entries.
         */
        public PasswordManager(String encryptedKeyFilePath)  {
                try {
                        this.filePath = encryptedKeyFilePath+"/encrypted_passwords.txt";
                        this.passwordEntries = new ArrayList<>();
                          String mainKey;
                        try{
                                 String encryptedMainKey = readEncryptedKeyFromFile(filePath);
                                  mainKey = decryptKey(encryptedMainKey, padKey(MASTER_KEY));

                        }catch(IOException ex){
                                mainKey=generateRandomKey(24);
                                //java.util.logging.Logger.getLogger(DesignBuilder.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
                                
                        }
                        
                        System.out.println("Decrypted key : " + mainKey);
                        mainKey = padKey(mainKey); // Ensure the key is padded to the correct length
                        this.secretKey = new SecretKeySpec(mainKey.getBytes(StandardCharsets.UTF_8), ALGORITHM);
                        // Read and store the encrypted password entries from the file
                        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
                                reader.readLine(); // Skip the first line (encrypted main key)
                                String line;
                                while ((line = reader.readLine()) != null) {
                                        String[] parts = line.split("::");
                                        if (parts.length == 2) {
                                                String encryptedFolderName = parts[0];
                                                String encryptedPassword = parts[1];
                                                passwordEntries.add(new PasswordEntry(encryptedFolderName, encryptedPassword));
                                        }
                                }
                        }
                } catch (Exception ex) {
                  //      secretKey = null;
                       // passwordEntries = null;
              //          Logger.getLogger(PasswordManager.class.getName()).log(Level.SEVERE, null, ex);
                }
        }

        /**
         * Reads the encrypted main key from a text file.
         *
         * @param filePath The path to the file containing the encrypted key.
         * @return The encrypted main key.
         * @throws IOException In case of an error reading the file.
         */
        private String readEncryptedKeyFromFile(String filePath) throws IOException {
                try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
                        return reader.readLine(); // Assumes the first line is the encrypted main key
                }
        }

        /**
         * Decrypts the main key with the master key.
         *
         * @param encryptedKey The encrypted main key.
         * @param masterKey The master key.
         * @return The decrypted main key.
         * @throws Exception In case of a decryption error.
         */
        private String decryptKey(String encryptedKey, String masterKey) throws Exception {
                SecretKeySpec decryptedKey = new SecretKeySpec(masterKey.getBytes(StandardCharsets.UTF_8), ALGORITHM);
                Cipher cipher = Cipher.getInstance(ALGORITHM);
                cipher.init(Cipher.DECRYPT_MODE, decryptedKey);
                byte[] decryptedBytes = cipher.doFinal(Base64.getDecoder().decode(encryptedKey));
                return new String(decryptedBytes, StandardCharsets.UTF_8);
        }

        /**
         * Pads the key to ensure it is of a valid length for AES.
         *
         * @param key The key to pad.
         * @return The padded key.
         */
        private static String padKey(String key) {
                int keyLength = key.getBytes(StandardCharsets.UTF_8).length;
                if (keyLength == 16 || keyLength == 24 || keyLength == 32) {
                        return key; // Key is already of a valid length
                }
                // Pad the key to the nearest valid length
                int targetLength = 16;
                if (keyLength > 16) {
                        targetLength = 24;
                }
                if (keyLength > 24) {
                        targetLength = 32;
                }
                return String.format("%-" + targetLength + "s", key).replace(' ', '0');
        }

        /**
         * Encrypts a string.
         *
         * @param data The string to encrypt.
         * @return The encrypted string encoded in Base64.
         * @throws Exception In case of an encryption error.
         */
        public String encrypt(String data) throws Exception {
                Cipher cipher = Cipher.getInstance(ALGORITHM);
                cipher.init(Cipher.ENCRYPT_MODE, secretKey);
                byte[] encryptedBytes = cipher.doFinal(data.getBytes(StandardCharsets.UTF_8));
                return Base64.getEncoder().encodeToString(encryptedBytes);
        }

        /**
         * Decrypts a string.
         *
         * @param encryptedData The encrypted string encoded in Base64.
         * @return The decrypted string.
         * @throws Exception In case of a decryption error.
         */
        public String decrypt(String encryptedData) throws Exception {
                Cipher cipher = Cipher.getInstance(ALGORITHM);
                cipher.init(Cipher.DECRYPT_MODE, secretKey);
                byte[] decryptedBytes = cipher.doFinal(Base64.getDecoder().decode(encryptedData));
                return new String(decryptedBytes, StandardCharsets.UTF_8);
        }

        /**
         * Adds a new password entry to the list and encrypts it. If a folder
         * with the same name already exists, updates the password.
         *
         * @param folderName The folder name to associate with the password.
         * @param password The password to add.
         * @throws Exception In case of an encryption error.
         */
        public void addPasswordEntry(String folderName, String password) throws Exception {
                String encryptedFolderName = encrypt(folderName);
                String encryptedPassword = encrypt(password);

                // Check if the folder name already exists
                for (PasswordEntry entry : passwordEntries) {
                        if (entry.getFolderName().equals(encryptedFolderName)) {
                                // Update the password if the folder name exists
                                entry.setPassword(encryptedPassword);
                                return;
                        }
                }

                // Add a new entry if the folder name does not exist
                passwordEntries.add(new PasswordEntry(encryptedFolderName, encryptedPassword));
        }


        /**
         * Saves all password entries to the file.
         *
         * @throws Exception In case of a file writing error.
         */
        public void saveAllPasswords() throws Exception {
                try (FileWriter writer = new FileWriter(filePath)) {
                        // Write the encrypted main key first
                        String encryptedMainKey = encryptKey(new String(secretKey.getEncoded(), StandardCharsets.UTF_8), padKey(MASTER_KEY));
                        writer.write(encryptedMainKey + "\n");

                        // Write each encrypted password entry
                        for (PasswordEntry entry : passwordEntries) {
                                writer.write(entry.getEncryptedFolderName() + "::" + entry.getEncryptedPassword() + "\n");
                        }
                }
                System.out.println("All passwords saved to: " + filePath);
        }

        /**
         * Encrypts the main key with the master key and writes it to a file
         * along with encrypted password entries.
         *
         * @param mainKey The main key to encrypt.
         * @param entries The password entries to encrypt.
         * @param outputFilePath The path to the file where the encrypted key
         * and password entries will be written.
         * @throws Exception In case of an encryption or file writing error.
         */
        public static void encryptAndStoreMainKeyAndPasswords(String mainKey, PasswordEntry[] entries, String outputFilePath) throws Exception {
                mainKey = padKey(mainKey); // Ensure the key is padded to the correct length
                String encryptedMainKey = encryptKey(mainKey, padKey(MASTER_KEY));
                try (FileWriter writer = new FileWriter(outputFilePath)) {
                        writer.write(encryptedMainKey + "\n"); // Write the encrypted main key
                        for (PasswordEntry entry : entries) {
                                String encryptedFolderName = encryptPassword(entry.getFolderName(), mainKey);
                                String encryptedPassword = encryptPassword(entry.getPassword(), mainKey);
                                writer.write(encryptedFolderName + "::" + encryptedPassword + "\n");
                        }
                }
                System.out.println("Encrypted main key and passwords stored in: " + outputFilePath);
        }

        /**
         * Encrypts a key with the master key.
         *
         * @param key The key to encrypt.
         * @param masterKey The master key.
         * @return The encrypted key encoded in Base64.
         * @throws Exception In case of an encryption error.
         */
        private static String encryptKey(String key, String masterKey) throws Exception {
                SecretKeySpec secretKey = new SecretKeySpec(masterKey.getBytes(StandardCharsets.UTF_8), ALGORITHM);
                Cipher cipher = Cipher.getInstance(ALGORITHM);
                cipher.init(Cipher.ENCRYPT_MODE, secretKey);
                byte[] encryptedBytes = cipher.doFinal(key.getBytes(StandardCharsets.UTF_8));
                return Base64.getEncoder().encodeToString(encryptedBytes);
        }

        /**
         * Encrypts a password with the main key.
         *
         * @param data The data to encrypt.
         * @param mainKey The main key.
         * @return The encrypted data encoded in Base64.
         * @throws Exception In case of an encryption error.
         */
        private static String encryptPassword(String data, String mainKey) throws Exception {
                SecretKeySpec secretKey = new SecretKeySpec(mainKey.getBytes(StandardCharsets.UTF_8), ALGORITHM);
                Cipher cipher = Cipher.getInstance(ALGORITHM);
                cipher.init(Cipher.ENCRYPT_MODE, secretKey);
                byte[] encryptedBytes = cipher.doFinal(data.getBytes(StandardCharsets.UTF_8));
                return Base64.getEncoder().encodeToString(encryptedBytes);
        }

        /**
         * Returns the password associated with the given folder name.
         *
         * @param folderName The folder name to search for.
         * @return The decrypted password associated with the folder name, or
         * null if not found.
         * @throws Exception In case of a decryption error.
         */
        public String getPasswordByFolderName(String folderName) throws Exception {
                for (PasswordEntry entry : passwordEntries) {
                        String decryptedFolderName = decrypt(entry.getEncryptedFolderName());
                        if (decryptedFolderName.equals(folderName)) {
                                return decrypt(entry.getEncryptedPassword());
                        }
                }
                return null; // Return null if the folder name is not found
        }
}

class PasswordEntry {

        private final String encryptedFolderName;
        private String encryptedPassword;

        public PasswordEntry(String encryptedFolderName, String encryptedPassword) {
                this.encryptedFolderName = encryptedFolderName;
                this.encryptedPassword = encryptedPassword;
        }

        public String getEncryptedFolderName() {
                return encryptedFolderName;
        }

        public String getEncryptedPassword() {
                return encryptedPassword;
        }

        public String getFolderName() {
                return encryptedFolderName;
        }

        public String getPassword() {
                return encryptedPassword;
        }
        
        public void setPassword(String password) {
                encryptedPassword = password;
        }

        public class RandomKeyGenerator {

                

                public static String generateRandomKey(int length) {
                        SecureRandom secureRandom = new SecureRandom();
                        byte[] randomBytes = new byte[length];
                        secureRandom.nextBytes(randomBytes);
                        return Base64.getUrlEncoder().withoutPadding().encodeToString(randomBytes);
                }
        }


}
