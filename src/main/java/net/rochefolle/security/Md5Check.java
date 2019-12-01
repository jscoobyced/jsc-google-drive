package net.rochefolle.security;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.xml.bind.DatatypeConverter;

public class Md5Check {
    public static boolean checkMd5(String filename, String checksum) throws NoSuchAlgorithmException, IOException {
        if (checksum == null) {
            return true;
        }
        MessageDigest md = MessageDigest.getInstance("MD5");
        md.update(Files.readAllBytes(Paths.get(filename)));
        byte[] digest = md.digest();
        String fileChecksum = DatatypeConverter.printHexBinary(digest).toUpperCase();
        return fileChecksum.equals(checksum);
    }
}