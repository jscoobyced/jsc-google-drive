package net.rochefolle.googledrive;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import com.google.api.services.drive.model.File;

import net.rochefolle.googledrive.utils.FileUtils;

public class FileOperator {
    private final String BACKUP_FOLDER = ".data/.backup";
    private java.io.File dataDirectory;
    private java.io.File backupDirectory;

    public FileOperator(String dataDirectory) {
        this.dataDirectory = existsOrCreate(dataDirectory);
        this.backupDirectory = existsOrCreate(BACKUP_FOLDER);
    }

    public boolean backup(File file) {
        Path source = Paths.get(getLocalFile(file).getAbsolutePath());
        Path backup = Paths.get(getBackupFile(file).getAbsolutePath());
        existsOrCreate(source.toFile().getParent());
        existsOrCreate(backup.toFile().getParent());
        try {
            Files.move(source, backup, StandardCopyOption.REPLACE_EXISTING);
            return true;
        } catch (IOException exception) {
        }
        return false;
    }

    public boolean restore(File file) throws IOException {
        Path backup = Paths.get(getBackupFile(file).getAbsolutePath());
        Path restore = Paths.get(getLocalFile(file).getAbsolutePath());
        existsOrCreate(backup.toFile().getParent());
        existsOrCreate(restore.toFile().getParent());
        try {
            Files.move(backup, restore, StandardCopyOption.REPLACE_EXISTING);
            return true;
        } catch (IOException exception) {
        }
        return false;
    }

    public java.io.File existsOrCreate(String directoryName) {
        java.io.File directory = new java.io.File(directoryName);
        if (directory.exists() || directory.mkdirs()) {
            return directory;
        }
        return null;
    }

    public java.io.File getLocalFile(File file) {
        return FileUtils.getFile(file, this.dataDirectory);
    }

    public java.io.File getBackupFile(File file) {
        return FileUtils.getFile(file, this.backupDirectory);
    }
}