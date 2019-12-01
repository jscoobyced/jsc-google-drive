package net.rochefolle.googledrive;

import com.google.api.services.drive.model.File;

import net.rochefolle.googledrive.utils.FileUtils;
import net.rochefolle.googledrive.utils.StringUtils;

public class FileData {
    private String filename;
    private String relativePath;
    private String absolutePath;
    private long size;
    private long lastModified;
    private String fileId;
    private boolean isSynchronized;
    private Object originalFile;

    public String getFilename() {
        return filename;
    }

    public Object getOriginalFile() {
        return originalFile;
    }

    public void setOriginalFile(Object originalFile) {
        this.originalFile = originalFile;
    }

    public boolean isSynchronized() {
        return isSynchronized;
    }

    public void setSynchronized(boolean isSynchronized) {
        this.isSynchronized = isSynchronized;
    }

    public String getFileId() {
        return fileId;
    }

    public void setFileId(String fileId) {
        this.fileId = fileId;
    }

    public long getLastModified() {
        return lastModified;
    }

    public void setLastModified(long lastModified) {
        this.lastModified = lastModified;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public String getAbsolutePath() {
        return absolutePath;
    }

    public void setAbsolutePath(String absolutePath) {
        this.absolutePath = absolutePath;
    }

    public String getRelativePath() {
        return relativePath;
    }

    public void setRelativePath(String relativePath) {
        this.relativePath = relativePath;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public static FileData toFileData(java.io.File file, String drivePath) {
        FileData fileData = new FileData();
        fileData.setAbsolutePath(file.getPath());
        fileData.setFilename(file.getName());
        fileData.setLastModified(file.lastModified());
        fileData.setRelativePath(FileUtils.toRelative(file, drivePath).getPath());
        fileData.setSize(file.length());
        fileData.setOriginalFile(file);
        return fileData;
    }

    public static FileData toFileData(File file, String drivePath) {
        java.io.File remote = FileUtils.getFile(file, new java.io.File(drivePath));
        FileData fileData = new FileData();
        fileData.setAbsolutePath(remote.getPath());
        fileData.setFileId(file.getId());
        fileData.setFilename(remote.getName());
        fileData.setLastModified(file.getModifiedTime() == null ? 0l : file.getModifiedTime().getValue());
        fileData.setRelativePath(FileUtils.toRelative(remote, drivePath).getPath());
        if(fileData.getRelativePath().contains("null")) {
            System.out.println(file);
        }
        fileData.setSize(file.getSize() == null ? 0l : file.getSize());
        fileData.setOriginalFile(file);
        return fileData;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Filename: ").append(getFilename()).append("\n");
        sb.append("Path: ").append(getRelativePath()).append("\n");
        sb.append("Full path: ").append(getAbsolutePath()).append("\n");
        sb.append("FileId: ").append(getFileId()).append("\n");
        sb.append("Size: ").append(StringUtils.humanReadableByteCount(getSize(), false)).append("\n");
        sb.append("Last Modified: ").append(StringUtils.longDateToString(getLastModified()));
        sb.append("\n");
        return sb.toString();
    }

}