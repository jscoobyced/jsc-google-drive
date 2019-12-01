package net.rochefolle.googledrive.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class StringUtils {
    private static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public static String humanReadableByteCount(long bytes, boolean si) {
        int unit = si ? 1000 : 1024;
        if (bytes < unit)
            return bytes + " B";
        int exp = (int) (Math.log(bytes) / Math.log(unit));
        String pre = (si ? "kMGTPE" : "KMGTPE").charAt(exp - 1) + (si ? "" : "i");
        return String.format("%.1f %sB", bytes / Math.pow(unit, exp), pre);
    }

    public static String longDateToString(Date date) {
        return simpleDateFormat.format(date);
    }

    public static String longDateToString(long date) {
        return longDateToString(new Date(date));
    }
}