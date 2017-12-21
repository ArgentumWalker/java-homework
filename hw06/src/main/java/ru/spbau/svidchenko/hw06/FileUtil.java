package ru.spbau.svidchenko.hw06;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Util for loading classes from directory
 */
public final class FileUtil {

    static public ArrayList<Class<?>> loadClasses(String path) throws IOException {
        List<Path> files = Files.walk(Paths.get(path))
                .map(Path::toAbsolutePath)
                .filter(Files::isRegularFile)
                .filter((p) -> p.toString().endsWith(".class"))
                .collect(Collectors.toList());
        List<URL> urls = files.stream()
                .map((p) -> {try {return p.toFile().toURI().toURL();} catch (Exception e) {return null;}})
                .collect(Collectors.toList());
        URL[] urlArray = new URL[urls.size()];
        for (int i = 0; i < urls.size(); i++) {
            urlArray[i] = urls.get(i);
        }
        ClassLoader loader = new URLClassLoader(urlArray);
        ArrayList<Class<?>> result = new ArrayList<>();
        for (Path p : files) {
            String classPath = p.toString();
            char separator = File.separatorChar;
            int position = classPath.indexOf(separator);
            while (position >= 0) {
                try {
                    String className = classPath.substring(position + 1, classPath.length() -".class".length())
                                    .replace(separator, '.');
                    Class<?> aClass = loader.loadClass(className);
                    if (aClass != null) {
                        result.add(aClass);
                        break;
                    }
                } catch (ClassNotFoundException e) {
                    //It's ok
                }
                position = classPath.indexOf(separator, position + 1);
            }
        }
        return result;
    }
}
