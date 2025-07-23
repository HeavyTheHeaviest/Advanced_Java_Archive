package ex01;

import java.io.*;
import java.nio.file.*;
import java.security.*;
import java.util.zip.*;

public class A {

    public static void createArchive(String sourceDir, String outputZip) throws IOException {
        Path zipFilePath = Paths.get(outputZip);
        Path sourcePath = Paths.get(sourceDir);

        try (ZipOutputStream zipOut = new ZipOutputStream(Files.newOutputStream(zipFilePath));
             DirectoryStream<Path> stream = Files.newDirectoryStream(sourcePath)) { // Użycie DirectoryStream zamiast Stream<Path>

            for (Path path : stream) {
                ZipEntry entry = new ZipEntry(sourcePath.relativize(path).toString());
                zipOut.putNextEntry(entry);
                if (Files.isRegularFile(path)) { // Sprawdzamy, czy to plik, a nie katalog
                    Files.copy(path, zipOut);
                }
                zipOut.closeEntry();
            }
        }
    }

    public static String generateMD5(String filePath) throws Exception {
        MessageDigest md = MessageDigest.getInstance("MD5");
        try (InputStream is = Files.newInputStream(Paths.get(filePath));
             DigestInputStream dis = new DigestInputStream(is, md)) {

            while (dis.read() != -1) {
            }
        }
        byte[] digest = md.digest();
        StringBuilder hexString = new StringBuilder();
        for (byte b : digest) {
            hexString.append(String.format("%02x", b));
        }
        return hexString.toString();
    }

    public static void saveMD5(String zipFilePath, String md5FilePath) throws Exception {
        String md5 = generateMD5(zipFilePath);
        Files.write(Paths.get(md5FilePath), md5.getBytes());
    }

    public static boolean verifyMD5(String zipFilePath, String md5FilePath) throws Exception {
        String calculatedMD5 = generateMD5(zipFilePath);
        String savedMD5 = Files.readString(Paths.get(md5FilePath)).trim();
        return calculatedMD5.equals(savedMD5);
    }
}
