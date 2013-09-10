package net.taviscaron.airliners.util;

import android.content.Context;
import android.util.Log;

import java.io.*;

/**
 * Common IO util
 * @author Andrei Senchuk
 */
public class IOUtil {
    public static final String TAG = "IOUtil";
    public static final int DEFAULT_BUFFER_SIZE = 1024;

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

    public static File getExternalCacheDir(Context context, String subdir) {
        File externalCacheDir = context.getExternalCacheDir();

        File nomedia = new File(externalCacheDir, ".nomedia");
        if(!nomedia.exists()) {
            try {
                nomedia.createNewFile();
            } catch (IOException e) {
                Log.w(TAG, "Can't create .nomedia: " + nomedia, e);
            }
        }

        File cache = new File(externalCacheDir, subdir);
        if(!cache.exists()) {
            if(!cache.mkdirs()) {
                Log.w(TAG, "Can't create cache dir");
            }
        }

        return cache;
    }
}
