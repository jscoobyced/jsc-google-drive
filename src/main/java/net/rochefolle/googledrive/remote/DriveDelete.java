package net.rochefolle.googledrive.remote;

import java.io.IOException;

import com.google.api.services.drive.Drive;

public class DriveDelete {
    private Drive service;

    public DriveDelete(Drive service) {
        this.service = service;
    }

    public void deleteFile(String fileId) throws IOException {
        service.files().delete(fileId).execute();
    }
}