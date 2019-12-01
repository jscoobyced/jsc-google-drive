package net.rochefolle.googledrive.remote;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;

import com.google.api.client.http.FileContent;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.File;

public class DriveUpload {
    private final String APP_DATA_FOLDER = "appDataFolder";

    private Drive service;

    public DriveUpload(Drive service) {
        this.service = service;
    }

    public File uploadFile(String source, String parent) throws IOException {
        return uploadFile(source, parent, true);
    }

    public File uploadFile(String source, String parent, boolean isAppDataFolder) throws IOException {
        Path path = Paths.get(source);
        String content = Files.probeContentType(path);
        FileContent mediaContent = new FileContent(content, path.toFile());
        return service.files().create(getFile(source, parent == null ? "" : parent, isAppDataFolder), mediaContent)
                .setFields("id").execute();
    }

    public File updateFile(String source, String parent, String fileId) throws IOException {
        return updateFile(source, parent, fileId, true);
    }

    public File updateFile(String source, String parent, String fileId, boolean isAppDataFolder) throws IOException {
        return service.files().update(fileId, getFile(source, parent, isAppDataFolder)).execute();
    }

    private File getFile(String source, String parent, boolean isAppDataFolder) {
        java.io.File upload = new java.io.File(source);
        File fileMetadata = new File();
        if (isAppDataFolder) {
            fileMetadata.setParents(Collections.singletonList(APP_DATA_FOLDER));
        }
        Path fullPath = Paths.get(parent, upload.getName());
        fileMetadata.setName(fullPath.toFile().getPath());
        return fileMetadata;
    }
}