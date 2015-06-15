package net.taviscaron.airliners.test.util;

import java.io.File;

public class TestUtil {
    public static void deleteRecursively(String path) {
        deleteRecursively(new File(path));
    }

    public static void deleteRecursively(File file) {
        if(file.exists()) {
            if(file.isDirectory()) {
                for(File f : file.listFiles()) {
                    deleteRecursively(f);
                }
            }

            if(!file.delete()) {
                throw new RuntimeException("Can't delete: " + file.getAbsoluteFile());
            }
        }
    }
}
