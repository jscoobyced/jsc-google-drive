package net.rochefolle.googledrive.utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import com.google.api.services.drive.model.File;

public class FileUtils {

    private static final String SYNC_TIME_FILE = ".data/.sync";

    public static java.io.File toRelative(final java.io.File file, final String root) {
        final Path pathAbsolute = Paths.get(file.getAbsolutePath());
        final Path pathBase = Paths.get(root);
        return pathBase.relativize(pathAbsolute).toFile();
    }

    public static String getParent(final File file) {
        List<String> parents = file.getParents();
        if (parents == null) {
            parents = new ArrayList<String>();
        }
        return parents.stream().collect(Collectors.joining(java.io.File.separator));
    }

    public static java.io.File getFile(final File file, final java.io.File parent) {
        final java.io.File localDirectory = new java.io.File(parent, getParent(file));
        return new java.io.File(localDirectory, file.getName());
    }

    public static void writeLastSynchronizationTime() throws IOException {
        final String value = Long.valueOf(new Date().getTime()).toString();
        writeLastSynchronizationTime(value);
    }

    public static void writeLastSynchronizationTime(String value) throws IOException {
        try {
            final BufferedWriter writer = new BufferedWriter(new FileWriter(SYNC_TIME_FILE));
            writer.write(value);
            writer.close();
        } catch (IOException exception) {
            // Just return 0 next read
        }
    }

    public static long readLastSynchronizationTime() {
        String currentLine = "0";
        try {
            BufferedReader reader = new BufferedReader(new FileReader(SYNC_TIME_FILE));
            currentLine = reader.readLine();
            reader.close();
        } catch (IOException exception) {
            // Just return 0
        }
        return Long.parseLong(currentLine);
    }
}