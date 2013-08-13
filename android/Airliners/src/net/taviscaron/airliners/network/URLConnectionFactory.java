package net.taviscaron.airliners.network;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;

/**
 * URL connection factory
 * @author Andrei Senchuk
 */
public abstract class URLConnectionFactory {
    public static final URLConnectionFactory DEFAULT_FACTORY = new URLConnectionFactory() {
        @Override
        public URLConnection openConnection(URL url) throws IOException {
            return url.openConnection();
        }
    };

    public abstract URLConnection openConnection(URL url) throws IOException;
}
