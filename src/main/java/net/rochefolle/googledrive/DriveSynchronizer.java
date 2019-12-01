package net.rochefolle.googledrive;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.List;

import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.File;

import net.rochefolle.googledrive.local.LocalDriveList;
import net.rochefolle.googledrive.remote.DriveDelete;
import net.rochefolle.googledrive.remote.DriveDownload;
import net.rochefolle.googledrive.remote.DriveList;
import net.rochefolle.googledrive.remote.DriveUpload;
import net.rochefolle.googledrive.utils.FileUtils;

public class DriveSynchronizer {
    private List<FileData> filesToCopy;
    private List<FileData> filesToDelete;
    private long lastSynchronizationTime = -1;
    private Drive service;

    public void synchronize(final Drive service, final String drivePath) throws IOException, GeneralSecurityException {
        this.service = service;
        synchronize(drivePath, true);
    }

    public void synchronize(final String drivePath, final boolean isAppData)
            throws IOException, GeneralSecurityException {
        List<FileData> remoteFiles = getRemoteFiles(drivePath, isAppData);
        List<FileData> localFiles = getLocalFiles(drivePath);
        findFilesToSynchronize(remoteFiles, localFiles);
        synchronizeRemoteFiles(drivePath);
        findFilesToSynchronize(localFiles, remoteFiles);
        synchronizeLocalFiles(service, drivePath, isAppData);
        FileUtils.writeLastSynchronizationTime();
    }

    private void findFilesToSynchronize(List<FileData> sourceFiles, List<FileData> destinationFiles) {
        filesToCopy = new ArrayList<>();
        filesToDelete = new ArrayList<>();
        sourceFiles.forEach(source -> {
            destinationFiles.forEach(destination -> {
                if (source.getRelativePath().equals(destination.getRelativePath())) {
                    if (source.getLastModified() > getLastSynchronizationTime()) {
                        if (source.getFileId() == null && destination.getFileId() != null) {
                            source.setFileId(destination.getFileId());
                        }
                        filesToCopy.add(source);
                    }
                    source.setSynchronized(true);
                }
            });
            if (!source.isSynchronized()) {
                if (source.getLastModified() > getLastSynchronizationTime()) {
                    filesToCopy.add(source);
                } else {
                    filesToDelete.add(source);
                }
            }
        });
    }

    private void synchronizeRemoteFiles(final String drivePath) {
        final DriveDownload driveDownload = new DriveDownload(service, drivePath);
        final DriveDelete driveDelete = new DriveDelete(service);
        System.out.println("\nSynchronizing remote files.\nFiles to download:");
        filesToCopy.forEach(file -> {
            System.out.println(file.getRelativePath());
            try {
                driveDownload.downloadFile((File) file.getOriginalFile());
            } catch (IOException e) {
                // Need to take care of that
            }
        });
        System.out.println("Files to delete: ");
        filesToDelete.forEach(file -> {
            System.out.println(file.getRelativePath());
            try {
                driveDelete.deleteFile(file.getFileId());
            } catch (IOException e) {
                // Need to take care of that
            }
        });
    }

    private void synchronizeLocalFiles(final Drive service, final String drivePath, final boolean isAppData) {
        final DriveUpload driveUpload = new DriveUpload(service);
        System.out.println("\nSynchronizing local files.");
        filesToCopy.forEach(file -> {
            try {
                if (file.getFileId() != null) {
                    System.out.printf("Updating: %s: \n", file.getRelativePath());
                    driveUpload.updateFile(file.getAbsolutePath(), new java.io.File(file.getRelativePath()).getParent(),
                            file.getFileId(), isAppData);
                } else {
                    System.out.printf("Uploading: %s \n", file.getRelativePath());
                    driveUpload.uploadFile(file.getAbsolutePath(), new java.io.File(file.getRelativePath()).getParent(),
                            isAppData);
                }
            } catch (IOException e) {
                // Need to take care of that
            }
        });
        System.out.println("Files to delete: ");
        filesToDelete.forEach(file -> {
            System.out.print(file.getRelativePath());
            if(((java.io.File) file.getOriginalFile()).delete()) {
                System.out.println(" --> deleted");
            }
        });
    }

    private List<FileData> getRemoteFiles(final String drivePath, final boolean isAppData) throws IOException {
        // upload(service, isAppData);
        DriveList driveList = new DriveList(service);
        List<FileData> remoteFiles = driveList.getFiles(drivePath, isAppData);
        // delete(service, remoteFiles);
        return remoteFiles;
    }

    private List<FileData> getLocalFiles(final String drivePath) throws IOException {
        LocalDriveList localDriveList = new LocalDriveList();
        List<FileData> localFiles = localDriveList.getFiles(drivePath);
        return localFiles;
    }

    private long getLastSynchronizationTime() {
        if (lastSynchronizationTime == -1) {
            lastSynchronizationTime = FileUtils.readLastSynchronizationTime();
        }
        return lastSynchronizationTime;
    }
}