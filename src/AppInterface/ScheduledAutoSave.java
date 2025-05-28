package AppInterface;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public final class ScheduledAutoSave {

        private final ScheduledExecutorService scheduler;

        private final DesignBuilder DB;
        private int maxAutoSaveNumber=5;
        private int timeIntervalBetweenAutoSave=1;

        public ScheduledAutoSave(DesignBuilder DB) {
                this.DB = DB;
                this.scheduler = Executors.newScheduledThreadPool(1);
                scheduleTask();
        }

        /**
         * Lance une action toutes les X minutes.
         *
         */
        public void scheduleTask() {
                Runnable runnable = () -> this.autoSave();
                scheduler.scheduleAtFixedRate(runnable, 1, timeIntervalBetweenAutoSave, TimeUnit.MINUTES);
        }

        /**
         * Arrête le planificateur de tâches.
         */
        public void shutdown() {
                scheduler.shutdown();
        }

        private void autoSave() {
                String rootPath = DB.getDesignSavesPath();
                System.out.println("AutoSave:RUNN "+rootPath+"    "+DB.getModelName());
                                
                if (DB.getModelName() != null) {
                        String designName = DB.getZipDesingName();
                        Path saveDirPath = Paths.get(rootPath, designName);

                        try {
                                // Créer le dossier de sauvegarde s'il n'existe pas
                                if (!Files.exists(saveDirPath)) {
                                        Files.createDirectories(saveDirPath);
                                }

                                // Compter le nombre de fichiers ZIP existants
                                File[] zipFiles = saveDirPath.toFile().listFiles((dir, name) -> name.endsWith(".zip"));
                                int zipFileCount = (zipFiles != null) ? zipFiles.length : 0;

                                // Générer le nom du fichier ZIP avec la date et l'heure actuelles
                                String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                                String zipFileName = designName + "_" + timeStamp + ".zip";
                                Path newZipFilePath = saveDirPath.resolve(zipFileName);

                                // Si le nombre maximum de sauvegardes est atteint, effacer le dernier fichier ZIP
                                if (zipFileCount >= maxAutoSaveNumber) {
                                        Arrays.sort(zipFiles, Comparator.comparingLong(File::lastModified).reversed());
                                        Files.delete(zipFiles[zipFiles.length - 1].toPath());
                                }

                                // Créer le nouveau fichier ZIP
                                // Ajoutez ici la logique pour créer le fichier ZIP
                                System.out.println("AutoSave: Création du fichier ZIP " + newZipFilePath.toString());
                                
                                DB.saveDesign(newZipFilePath.toString());
                        } catch (IOException e) {
                                e.printStackTrace();
                        }
                }
        }

     

        public int getMaxAutoSaveNumber() {
                return maxAutoSaveNumber;
        }

        public void setMaxAutoSaveNumber(int maxAutoSaveNumber) {
                this.maxAutoSaveNumber = maxAutoSaveNumber;
        }

        public int getTimeIntervalBetweenAutoSave() {
                return timeIntervalBetweenAutoSave;
        }

        public void setTimeIntervalBetweenAutoSave(int timeIntervalBetweenAutoSave) {
                this.timeIntervalBetweenAutoSave = timeIntervalBetweenAutoSave;
        }

}
