package lsieun.utils;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;
import java.util.logging.Level;

import static lsieun.utils.LogUtils.audit;
import static lsieun.utils.LogUtils.err;

public class PropertyUtils {
    private static final String rootPath = Thread.currentThread().getContextClassLoader().getResource("").getPath();
    private static final Properties props = new Properties();

    static {
        try {
            audit.config(() -> "Root Path: " + rootPath);
            File dirFile = new File(rootPath);
            File[] files = dirFile.listFiles(file -> file.getName().endsWith(".properties"));
            if (files == null || files.length < 1) {
                throw new RuntimeException("can not find any properties files.");
            }

            for (File f : files) {
                audit.config(() -> "READ File: " + f.getAbsolutePath());
                try (
                        FileInputStream fin = new FileInputStream(f);
                        BufferedInputStream in = new BufferedInputStream(fin)
                ) {
                    props.load(in);
                }
            }
        } catch (Exception ex) {
            err.log(Level.SEVERE, "unexpected error: " + ex.getMessage(), ex);
            System.exit(1);
        }
    }

    public static String getRootPath() {
        return rootPath;
    }

    public static String getProperty(String key) {
        return props.getProperty(key);
    }

    public static int getInt(String key) {
        String value = props.getProperty(key);
        return Integer.parseInt(value);
    }

    public static void main(String[] args) {
        String value = PropertyUtils.getProperty("smtp.server");
        System.out.println(value);
        value = PropertyUtils.getProperty("audit.level");
        System.out.println(value);
    }
}
