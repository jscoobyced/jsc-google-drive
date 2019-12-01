package net.rochefolle.googledrive;

import java.io.IOException;
import java.util.List;

import com.google.api.services.drive.Drive;

import net.rochefolle.googledrive.remote.DriveDelete;
import net.rochefolle.googledrive.remote.DriveList;
import net.rochefolle.googledrive.utils.FileUtils;

public class DrivePurge {
    private Drive service;

    public DrivePurge(Drive service) {
        this.service = service;
    }

    public void purge(final String driveData) throws IOException {
        DriveList driveList = new DriveList(service);
        List<FileData> files = driveList.getFiles(driveData);
        DriveDelete driveDelete = new DriveDelete(service);
        files.forEach(file -> {
            try {
                System.out.printf("Deleting remote file: %s\n", file.getRelativePath());
                driveDelete.deleteFile(file.getFileId());
                FileUtils.writeLastSynchronizationTime("0");
            } catch (Exception e) {
                // Nevermind
            }
        });
    }
}