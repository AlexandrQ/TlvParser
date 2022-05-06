package main.java.utils;

import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;

public class FileCheckerUtils {

    private static final String FILE_EXTENSION_SEPARATOR = "\\.";
    private static final String BINARY_EXTENSION = "bin";
    private static final String JSON_EXTENSION = "json";

    private FileCheckerUtils() {}

    public static boolean isValidBinaryFile(String fileName) {
        return isNotEmptyString(fileName) &&
                isValidFilename(fileName, BINARY_EXTENSION) &&
                Files.exists(Path.of(fileName), LinkOption.NOFOLLOW_LINKS);
    }

    public static boolean isValidJsonFileName(String fileName) {
        return isNotEmptyString(fileName) &&
                isValidFilename(fileName, JSON_EXTENSION);
    }

    public static boolean isValidFilename(String fileName, String fileExtension) {
        if(isNotEmptyString(fileExtension)) {
            String[] parts = fileName.split(FILE_EXTENSION_SEPARATOR);
            return parts.length == 2 && !parts[0].isEmpty() && fileExtension.equals(parts[1]);
        }
        return false;
    }

    public static boolean isNotEmptyString(String string) {
        return string != null && !string.isEmpty();
    }

}
