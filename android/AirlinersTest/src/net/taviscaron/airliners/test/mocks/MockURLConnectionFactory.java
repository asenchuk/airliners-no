package net.taviscaron.airliners.test.mocks;

import android.content.Context;
import net.taviscaron.airliners.network.URLConnectionFactory;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.Arrays;

/**
 * Mocked URLConnectionFactory. Returns HttpURLConnections which is
 * pointed to asset with following path: [host]/path/to/resource
 * @author Andrei Senchuk
 */
public class MockURLConnectionFactory extends URLConnectionFactory {
    private Context context;

    public MockURLConnectionFactory(Context context) {
        this.context = context;
    }

    @Override
    public URLConnection openConnection(URL url) throws IOException {
        final File file = new File(url.getHost(), url.getPath());
        final String filename = file.getName();
        final String directory = file.getParent();
        return new HttpURLConnection(url) {
            @Override
            public void connect() throws IOException {
            }

            @Override
            public InputStream getInputStream() throws IOException {
                return context.getAssets().open(file.getPath());
            }

            @Override
            public int getResponseCode() throws IOException {
                String[] assets = context.getAssets().list(directory);
                return (Arrays.binarySearch(assets, filename) < 0) ? HTTP_NOT_FOUND : HTTP_OK;
            }

            @Override
            public void disconnect() {
            }

            @Override
            public boolean usingProxy() {
                return false;
            }
        };
    }
}
