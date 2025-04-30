package ResourcesManager;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.nio.charset.StandardCharsets;

public class PasswordManager {

        private static final String MASTER_KEY = "votreClregegefsse123"; // Master key of 16 characters for AES-128
        private static final String ALGORITHM = "AES";
        private final SecretKeySpec secretKey;
        private final ArrayList<PasswordEntry> passwordEntries;
        private final String filePath;

        /**
         * Constructor that reads the encrypted main key from a text file and
         * decrypts it with the master key. Also reads and stores the encrypted
         * password entries from the file.
         *
         * @param encryptedKeyFilePath The path to the file containing the
         * encrypted main key and password entries.
         * @throws Exception In case of an error reading or decrypting the file.
         */
        public PasswordManager(String encryptedKeyFilePath) throws Exception {
                this.filePath = encryptedKeyFilePath;
                this.passwordEntries = new ArrayList<>();

                String encryptedMainKey = readEncryptedKeyFromFile(encryptedKeyFilePath);
                String mainKey = decryptKey(encryptedMainKey, padKey(MASTER_KEY));
                System.out.println("Decrypted key : " + mainKey);
                mainKey = padKey(mainKey); // Ensure the key is padded to the correct length
                this.secretKey = new SecretKeySpec(mainKey.getBytes(StandardCharsets.UTF_8), ALGORITHM);

                // Read and store the encrypted password entries from the file
                try (BufferedReader reader = new BufferedReader(new FileReader(encryptedKeyFilePath))) {
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
         * Adds a new password entry to the list and encrypts it.
         *
         * @param folderName The folder name to associate with the password.
         * @param password The password to add.
         * @throws Exception In case of an encryption error.
         */
        public void addPasswordEntry(String folderName, String password) throws Exception {
                String encryptedFolderName = encrypt(folderName);
                String encryptedPassword = encrypt(password);
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

        public static void main(String[] args) {
                try {
                        // Step 1: Encrypt the main key and password entries, then store them in a file
                        String mainKey = "votreClePrincipale123"; // Main key to encrypt
                        PasswordEntry[] entries = {
                                new PasswordEntry("folder1", "password1"),
                                new PasswordEntry("folder2", "password2"),
                                new PasswordEntry("folder3", "password3")
                        };
                        String encryptedKeyFilePath = "encrypted_key_and_passwords.txt"; // Path to the file where the encrypted key and password entries will be stored
                        encryptAndStoreMainKeyAndPasswords(mainKey, entries, encryptedKeyFilePath);

                        // Step 2: Use the encrypted main key to decrypt the password entries
                        PasswordManager manager = new PasswordManager(encryptedKeyFilePath);

                        // Add a new password entry
                        manager.addPasswordEntry("newfolder", "newpassword");

                        // Save all password entries to the file
                        manager.saveAllPasswords();

                        // Read and decrypt the password entries from the file
                        try (BufferedReader reader = new BufferedReader(new FileReader(encryptedKeyFilePath))) {
                                reader.readLine(); // Skip the first line (encrypted main key)
                                String line;
                                while ((line = reader.readLine()) != null) {
                                        String[] parts = line.split("::");
                                        if (parts.length == 2) {
                                                String decryptedFolderName = manager.decrypt(parts[0]);
                                                String decryptedPassword = manager.decrypt(parts[1]);
                                                System.out.println("Decrypted folder name: " + decryptedFolderName);
                                                System.out.println("Decrypted password: " + decryptedPassword);
                                        }
                                }
                        }

                        // Test the new function to get password by folder name
                        String folderNameToSearch = "folder2";
                        String password = manager.getPasswordByFolderName(folderNameToSearch);
                        if (password != null) {
                                System.out.println("Password for folder '" + folderNameToSearch + "': " + password);
                        } else {
                                System.out.println("Folder '" + folderNameToSearch + "' not found.");
                        }
                } catch (Exception e) {
                        e.printStackTrace();
                }
        }
}

class PasswordEntry {

        private final String encryptedFolderName;
        private final String encryptedPassword;

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
}
