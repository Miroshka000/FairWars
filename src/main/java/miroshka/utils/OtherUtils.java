package miroshka.utils;

import cn.nukkit.utils.Config;
import cn.nukkit.utils.ConfigSection;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class OtherUtils {
    public static String y2j(File file) {
        Config yamlConfig = new Config(file, Config.YAML);
        ConfigSection section = yamlConfig.getRootSection();
        return (new GsonBuilder()).create().toJson(section);
    }

    public static void readJar(String fileName, String jarDir, String path) {
        try {
            JarFile jarFile = new JarFile(jarDir);
            JarEntry entry = jarFile.getJarEntry(fileName);
            InputStream input = jarFile.getInputStream(entry);

            File targetFile = new File(path);
            File parentDir = targetFile.getParentFile();
            if (!parentDir.exists()) {
                parentDir.mkdirs();
            }

            Files.copy(input, targetFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
            input.close();
            jarFile.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void copyFile(File source, File dest) throws IOException {
        Files.copy(source.toPath(), dest.toPath(), StandardCopyOption.REPLACE_EXISTING);
    }

    public static void copyDir(String sourceDir, String destDir) throws IOException {
        File srcDir = new File(sourceDir);
        File destDirFile = new File(destDir);

        if (!destDirFile.exists()) {
            destDirFile.mkdirs();
        }

        for (File file : srcDir.listFiles()) {
            File destFile = new File(destDirFile, file.getName());
            if (file.isDirectory()) {
                copyDir(file.getPath(), destFile.getPath());
            } else {
                copyFile(file, destFile);
            }
        }
    }

    public static void delDir(String dir) throws IOException {
        File dirFile = new File(dir);
        if (dirFile.isDirectory()) {
            for (File file : dirFile.listFiles()) {
                if (file.isDirectory()) {
                    delDir(file.getPath());
                } else {
                    file.delete();
                }
            }
        }
        dirFile.delete();
    }
}
