package net.rochefolle.googledrive;

import java.io.File;
import java.io.IOException;
import java.security.GeneralSecurityException;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.drive.Drive;

public class DriveApp {
    private static final String DATA_DIRECTORY_PATH = "drive";
    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
    private static final String APPLICATION_NAME = "JSC Google Drive";

    public static void main(String arg[]) throws Exception {
        String action = "sync";
        if (arg != null && arg.length == 1 && arg[0] != null) {
            action = arg[0];
        }
        Drive service = getDrive();
        String driveData = new File(DATA_DIRECTORY_PATH).getAbsolutePath();
        if (action.equals("list")) {
            DriveLister driveLister = new DriveLister(service);
            driveLister.list(driveData);
        } else if (action.equals("purge")) {
            DrivePurge drivePurge = new DrivePurge(service);
            drivePurge.purge(driveData);

        } else {
            DriveSynchronizer driveSynchronizer = new DriveSynchronizer();
            driveSynchronizer.synchronize(service, driveData);
        }
    }

    private static Drive getDrive() throws IOException, GeneralSecurityException {
        final DriveSecurity driveSecurity = new DriveSecurity();
        final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
        final Drive service = new Drive.Builder(HTTP_TRANSPORT, JSON_FACTORY,
                driveSecurity.getCredentials(HTTP_TRANSPORT)).setApplicationName(APPLICATION_NAME).build();
        return service;
    }
}