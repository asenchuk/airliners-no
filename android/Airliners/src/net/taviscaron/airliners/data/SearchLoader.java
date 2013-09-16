package net.taviscaron.airliners.data;

import android.content.Context;
import android.util.Log;
import net.taviscaron.airliners.model.SearchParams;
import net.taviscaron.airliners.model.SearchResult;
import net.taviscaron.airliners.util.IOUtil;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URLStreamHandler;

/**
 * Search loader
 * @author Andrei Senchuk
 */
public class SearchLoader extends BaseLoader<SearchParams, SearchResult> {

    public static SearchLoader createTopLoader(Context context) {
        return createTopLoader(context, null);
    }

    public static SearchLoader createTopLoader(Context context, URLStreamHandler urlStreamHandler) {
        return new SearchLoader(context, "/top", urlStreamHandler);
    }

    public static SearchLoader createTop15Loader(Context context) {
        return createTop15Loader(context, null);
    }

    public static SearchLoader createTop15Loader(Context context, URLStreamHandler urlStreamHandler) {
        return new SearchLoader(context, "/top15", urlStreamHandler);
    }

    public static SearchLoader createSearchLoader(Context context) {
        return createSearchLoader(context, null);
    }

    public static SearchLoader createSearchLoader(Context context, URLStreamHandler urlStreamHandler) {
        return new SearchLoader(context, "/search", urlStreamHandler);
    }

    private SearchLoader(Context context, String relativeUrl, URLStreamHandler urlStreamHandler) {
        super(context, relativeUrl, SearchResult.class, urlStreamHandler);
    }

    @Override
    protected void customizeHttpURLConnectionForParam(SearchParams param, HttpURLConnection urlConnection) throws IOException {
        String json = gson.toJson(param);

        urlConnection.setRequestMethod("POST");
        urlConnection.setRequestProperty("content-type", "application/json");
        urlConnection.setRequestProperty("content-size", String.valueOf(json.length()));
        urlConnection.setDoOutput(true);

        OutputStream os = null;
        try {
            os = urlConnection.getOutputStream();
            os.write(json.getBytes());
        } catch (IOException e) {
            // noop
        } finally {
            IOUtil.close(os);
        }
    }
}
