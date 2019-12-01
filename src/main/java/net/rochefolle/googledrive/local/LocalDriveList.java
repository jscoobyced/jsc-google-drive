package net.rochefolle.googledrive.local;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import net.rochefolle.googledrive.FileData;

public class LocalDriveList {
    public List<FileData> getFiles(final String dataDirectory) throws IOException {
        final List<FileData> files = new ArrayList<>();
        Files.find(Paths.get(dataDirectory), 100, (p, bfa) -> bfa.isRegularFile())
                .forEach(file -> files.add(FileData.toFileData(file.toFile(), dataDirectory)));
        return files;
    }
}