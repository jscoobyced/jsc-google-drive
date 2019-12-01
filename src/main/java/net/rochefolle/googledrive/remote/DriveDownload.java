package net.rochefolle.googledrive.remote;

import java.io.FileOutputStream;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;

import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.File;

import net.rochefolle.googledrive.FileOperator;
import net.rochefolle.security.Md5Check;

public class DriveDownload {

    private Drive service;
    private FileOperator fileOperator;

    public DriveDownload(Drive service, String dataDirectory) {
        this.service = service;
        this.fileOperator = new FileOperator(dataDirectory);
    }

    public void downloadFile(File file) throws IOException {
        if (fileOperator.getLocalFile(file).exists()) {
            if (!fileOperator.backup(file)) {
                throw new IOException("Cannot make local backup.");
            }
        }
        try {
            fileOperator.existsOrCreate(fileOperator.getLocalFile(file).getParent());
            FileOutputStream outputStream = new FileOutputStream(fileOperator.getLocalFile(file));
            service.files().get(file.getId()).executeMediaAndDownloadTo(outputStream);
        } catch (IOException exception) {
            fileOperator.restore(file);
            throw exception;
        }
        try {
            if (!Md5Check.checkMd5(fileOperator.getLocalFile(file).getAbsolutePath(), file.getMd5Checksum())) {
                fileOperator.restore(file);
                throw new IOException("Checksum check failed.");
            }
        } catch (NoSuchAlgorithmException exception) {
            fileOperator.restore(file);
            throw new IOException("Cannot calculate MD5 checksum.");
        }
    }
}