package ru.alterland.java.launcher;

import ru.alterland.Main;

import java.io.*;
import java.util.Properties;

public class Settings {
    private static String fileName = "settings.properties";
    public static String filePath = FilesManager.userFolderPath + fileName;
    private static FileInputStream fileInputStream;
    private static FileOutputStream fileOutputStream;
    private static Properties properties = new Properties();

    public static String get(String property) {
        try {
            if (!new File(filePath).exists()) fileOutputStream = new FileOutputStream(filePath);
            fileInputStream = new FileInputStream(filePath);
            properties.load(fileInputStream);
            return properties.getProperty(property);
        } catch (IOException e) {
            e.printStackTrace();
            Main.shutdown();
            return null;
        }
    }

    public static void add(String property, String value){
        try {
            fileOutputStream = new FileOutputStream(filePath);
            fileInputStream = new FileInputStream(filePath);
            properties.load(fileInputStream);
            properties.put(property, value);
        } catch (IOException e) {
            e.printStackTrace();
            Main.shutdown();
        }
    }

    public static void save() {
        try {
            properties.store(fileOutputStream, null);
        } catch (IOException e) {
            e.printStackTrace();
            Main.shutdown();
        }
    }

}
