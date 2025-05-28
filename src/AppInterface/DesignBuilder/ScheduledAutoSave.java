package AppInterface.DesignBuilder;

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
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public final class ScheduledAutoSave {

        private final ScheduledExecutorService scheduler;
        private ScheduledFuture<?> scheduledFuture;
        private final DesignBuilder DB;
        private int maxAutoSaveNumber = 5;
        private int timeIntervalBetweenAutoSave = 10;
        private boolean isPaused = false;

        public ScheduledAutoSave(DesignBuilder DB) {
                this.DB = DB;
                this.scheduler = Executors.newScheduledThreadPool(1);
                scheduleTask();
        }

        /**
         * Schedule a task to run at fixed intervals.
         */
        public void scheduleTask() {
                Runnable runnable = () -> {
                        if (!isPaused) {
                                this.autoSave();
                        }
                };
                scheduledFuture = scheduler.scheduleAtFixedRate(runnable, 1, timeIntervalBetweenAutoSave, TimeUnit.SECONDS);
        }

        /**
         * Pause the scheduled task.
         */
        public void pause() {
                isPaused = true;
                System.out.println("SCHEDULE : PAUSE");
        }

        /**
         * Resume the scheduled task.
         */
        public void resume() {
                isPaused = false;
                System.out.println("SCHEDULE : RESUME");

        }

        /**
         * Stop the task scheduler.
         */
        public void shutdown() {
                scheduler.shutdown();
        }

        
        /**
         * This metod find the autosave 
         */
        private void autoSave() {
                String rootPath = DB.getDesignSavesPath();
                System.out.println("AutoSave:RUNN " + rootPath + "    " + DB.getModelName());

                if (DB.getModelName() != null) {
                        System.out.println("AutoSave:RUNNV2 " + rootPath + "    " + DB.getZipDesingName());
                        String designName = DB.getZipDesingName();
                        Path saveDirPath = Paths.get(rootPath, designName);
                        System.out.println("AutoSave: CONSTRUCT PATH " + saveDirPath);
                        try {
                                // Create the save directory if it doesn't exist
                                if (!Files.exists(saveDirPath)) {
                                        Files.createDirectories(saveDirPath);
                                        System.out.println("AutoSave: CREATE DIR " + saveDirPath);
                                }

                                // Count the number of existing ZIP files
                                File[] zipFiles = saveDirPath.toFile().listFiles((dir, name) -> name.endsWith(".zip"));
                                int zipFileCount = (zipFiles != null) ? zipFiles.length : 0;

                                // Generate the ZIP file name with the current date and time
                                String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                                String zipFileName = designName + "_" + timeStamp + ".zip";
                                Path newZipFilePath = saveDirPath.resolve(zipFileName);

                                System.out.println("AutoSave: CREATE FILE PATH " + newZipFilePath);
                                // If the maximum number of saves is reached, delete the oldest ZIP file
                                if (zipFileCount >= maxAutoSaveNumber) {
                                        Arrays.sort(zipFiles, Comparator.comparingLong(File::lastModified).reversed());
                                        Files.delete(zipFiles[zipFiles.length - 1].toPath());
                                }

                                // Create the new ZIP file
                                // Add your logic here to create the ZIP file
                                System.out.println("AutoSave: Creating ZIP file " + newZipFilePath.toString());

                                DB.saveDesign(newZipFilePath.toString());
                        } catch (IOException e) {
                                e.printStackTrace();
                                System.out.println("AutoSave: ERROR");

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
                if (scheduledFuture != null) {
                        scheduledFuture.cancel(false);
                        scheduleTask();
                }
        }

        /**
         * This method give the defaut design name
         * here i chose one autosave per unamed model
         * @return
         */
        public String getFormattedModelName(DesignBuilder designBuilder) {
                // Create a SimpleDateFormat object with the desired pattern
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd_MM_yyyy_HH_mm");
                // Get the current date and time
                String currentDateTime = dateFormat.format(new Date());
                // Concatenate the modelName with the formatted date and time
                //return this.modelName + "_UNNAMED_DESIGN_" + currentDateTime;
                return this.DB.modelName + "_UNNAMED_DESIGN";
        }
        
        
}
