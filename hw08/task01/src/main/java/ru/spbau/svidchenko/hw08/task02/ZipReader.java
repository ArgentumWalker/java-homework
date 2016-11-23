package ru.spbau.svidchenko.hw08.task02;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Enumeration;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;

public class ZipReader {
    public static void main(String[] args) {
        if (args.length != 2) {
            System.out.println("Ну вот зачем так делать, а?");
        } else {
            try {
                unzipFiles(new File(args[0]), args[1]);
            }
            catch (Exception e) {
                System.out.println("Все шломалось. Несите еще этих ваших архивов.");
            }
        }
    }
    public static void unzipFiles(File directory, String regex) throws Exception {
        File[] files = directory.listFiles();
        Pattern p = Pattern.compile(regex);
        for (int i = 0; i < files.length; i++) {
            File file = files[i];
            if (file.isDirectory()) {
                unzipFiles(file, regex);
                continue;
            }
            ZipFile zip;
            try {
                zip = new ZipFile(file);
            }
            catch (FileNotFoundException | ZipException e) {
                continue; //Means that file not a zip (or empty zip)
            }
            Path archivePath = file.getParentFile().toPath().resolve(
                    file.getName().substring(0, file.getName().length() - 4));
            Enumeration<? extends ZipEntry> entries = zip.entries();
            while (entries.hasMoreElements()) {
                ZipEntry entry = entries.nextElement();
                if (p.matcher(entry.getName()).matches()) {
                    Path newFilePath = archivePath.resolve(entry.getName());
                    if (entry.isDirectory()) {
                        if (!Files.exists(newFilePath)) {
                            Files.createDirectories(newFilePath);
                        }
                    } else {
                        if (!Files.exists(newFilePath.getParent())) {
                            Files.createDirectories(newFilePath.getParent());
                        }
                        Files.copy(zip.getInputStream(entry), newFilePath, StandardCopyOption.REPLACE_EXISTING);
                    }
                }
            }
        }
    }
}
