package AppInterface.DesignBuilderMainElements;

import java.io.File;

/**
 *
 * @author Camille LECOURT
 */
public class LocalFilesManagement {

        private static final String APP_FOLDER_NAME = "imageTemplateTweaker";
        private static final String MODELS_DATA_DIR = "ModelsData";
        private static final String DESIGNS_DIR = "Designs";
        private static final String LOCAL_DATA_DIR = "LocalData";

        private String appDir;

        public LocalFilesManagement() {
                String userHome = System.getProperty("user.home");
                // System.out.println("Répertoire personnel de l'utilisateur : " + userHome);

                appDir = userHome + File.separator + "." + APP_FOLDER_NAME;
                File appDirFile = new File(appDir);
                if (!appDirFile.exists()) {
                        appDirFile.mkdir();
                }
                System.out.println("Répertoire de l'application : " + appDir);

                // Création des sous-dossiers
                String[] subDirs = {MODELS_DATA_DIR, DESIGNS_DIR, LOCAL_DATA_DIR};
                for (String subDir : subDirs) {
                        File subDirFile = new File(appDir + File.separator + subDir);
                        if (!subDirFile.exists()) {
                                subDirFile.mkdir();
                        }
                        System.out.println("Répertoire créé : " + subDirFile.getPath());
                }
        }

        public String getModelsDataDir() {
                return appDir + File.separator + MODELS_DATA_DIR;
        }

        public String getDesignsDir() {
                return appDir + File.separator + DESIGNS_DIR;
        }

        public String getLocalDataDir() {
                return appDir + File.separator + LOCAL_DATA_DIR;
        }
}
