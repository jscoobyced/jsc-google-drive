package net.rochefolle.googledrive;

import java.io.IOException;
import java.util.List;

import com.google.api.services.drive.Drive;

import net.rochefolle.googledrive.remote.DriveList;

public class DriveLister {
    private Drive service;

    public DriveLister(Drive service) {
        this.service = service;
    }

    public void list(final String driveData) throws IOException {
        DriveList driveList = new DriveList(service);
        List<FileData> files = driveList.getFiles(driveData);
        System.out.println("Remote files:");
        files.forEach(file -> {
            System.out.println(file.getRelativePath());
        });
    }
}