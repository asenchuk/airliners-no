package net.taviscaron.airliners.util;

import android.content.Context;
import android.util.Log;

import java.io.*;
import java.net.HttpURLConnection;

/**
 * Common IO util
 * @author Andrei Senchuk
 */
public class IOUtil {
    public static final String TAG = "IOUtil";
    public static final int DEFAULT_BUFFER_SIZE = 1024;
    public static final String TMP_DIR_NAME = "temp";

    public static void copy(InputStream in, OutputStream out, int bufferSize) throws IOException {
        byte[] buffer = new byte[bufferSize];

        int len;
        while((len = in.read(buffer)) != -1) {
            out.write(buffer, 0, len);
        }
    }

    public static void copy(InputStream in, OutputStream out) throws IOException {
        copy(in, out, DEFAULT_BUFFER_SIZE);
    }

    public static void close(Closeable closeable) {
        if(closeable != null) {
            try {
                closeable.close();
            } catch (IOException e) {
                Log.w(TAG, "Can't close " + closeable, e);
            }
        }
    }

    public static void disconnect(HttpURLConnection conn) {
        if(conn != null) {
            conn.disconnect();
        }
    }

    public static File getExternalCacheDir(Context context, String subdir) {
        File externalCacheDir = context.getExternalCacheDir();
        placeNoMedia(externalCacheDir);
        File cache = new File(externalCacheDir, subdir);
        ensureDirectoryExist(cache);
        return cache;
    }

    public static File getExternalFilesDir(Context context, String subdir) {
        File filesBaseDir = context.getExternalFilesDir(null);
        placeNoMedia(filesBaseDir);
        File externalFilesDir = new File(filesBaseDir, subdir);
        ensureDirectoryExist(externalFilesDir);
        return externalFilesDir;
    }

    public static File getTempDir(Context context) {
        return getExternalCacheDir(context, TMP_DIR_NAME);
    }

    public static File createTempFile(Context context, String prefix) throws IOException {
        return File.createTempFile(prefix, null, getTempDir(context));
    }

    public static void placeNoMedia(File path) {
        File nomedia = new File(path, ".nomedia");
        if(!nomedia.exists()) {
            try {
                nomedia.createNewFile();
            } catch (IOException e) {
                Log.w(TAG, "Can't create .nomedia: " + nomedia, e);
            }
        }
    }

    public static void ensureDirectoryExist(File path) {
        if(!path.exists()) {
            if(!path.mkdirs()) {
                Log.w(TAG, "Can't create dirs: " + path);
            }
        }
    }
}
