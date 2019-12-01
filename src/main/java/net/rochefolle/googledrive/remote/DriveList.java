package net.rochefolle.googledrive.remote;

import java.io.IOException;
import java.util.ArrayList;

import com.google.api.services.drive.Drive;
import com.google.api.services.drive.Drive.Files.List;
import com.google.api.services.drive.model.FileList;

import net.rochefolle.googledrive.FileData;

public class DriveList {
    private Drive service;

    public DriveList(Drive service) {
        this.service = service;
    }

    public java.util.List<FileData> getFiles(String dataDrive) throws IOException {
        return getFiles(dataDrive, true);
    }

    public java.util.List<FileData> getFiles(String dataDrive, boolean isAppData) throws IOException {
        List files = service.files().list();
        if (isAppData) {
            files.setSpaces("appDataFolder");
        }
        final FileList result = files.setPageSize(10).setFields("nextPageToken, files(id, name, modifiedTime, size)")
                .execute();
        java.util.List<FileData> remoteFiles = new ArrayList<>();
        result.getFiles().forEach(file -> remoteFiles.add(FileData.toFileData(file, dataDrive)));
        return remoteFiles;
    }
}