package net.taviscaron.airliners.data;

import android.content.Context;
import net.taviscaron.airliners.model.SearchResult;

import java.net.URLStreamHandler;

/**
 * Search loader
 * @author Andrei Senchuk
 */
public class SearchLoader extends BaseLoader<SearchResult> {
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
}
