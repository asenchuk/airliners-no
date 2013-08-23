package net.taviscaron.airliners.data;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParseException;
import net.taviscaron.airliners.R;
import net.taviscaron.airliners.util.IOUtil;

import java.io.*;
import java.net.*;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Entities base loader
 * @param <T> entity type
 */
public abstract class BaseLoader<T> {
    private static final String TAG = "BaseLoader";
    protected static final Executor executor = Executors.newCachedThreadPool();
    protected static final Gson gson = new GsonBuilder().create();

    public static interface BaseLoaderCallback<T> {
        public void loadStarted(BaseLoader<T> loader);
        public void loadFinished(BaseLoader<T> loader, T obj);
    }

    protected final Handler handler;
    protected final URLStreamHandler urlStreamHandler;
    protected final String baseUrl;
    protected final Class<T> clazz;
    protected final Context context;

    public BaseLoader(Context context, String relativeUrl, Class<T> clazz) {
        this(context, relativeUrl, clazz, null);
    }

    public BaseLoader(Context context, String relativeUrl, Class<T> clazz, URLStreamHandler urlStreamHandler) {
        if(!relativeUrl.startsWith("/")) {
            throw new IllegalArgumentException("Relative url should start with '/'");
        }

        String rootUrl = context.getString(R.string.api_base_url);
        if(rootUrl.endsWith("/")) {
            rootUrl = rootUrl.substring(0, rootUrl.length() - 1);
        }

        this.baseUrl = rootUrl + relativeUrl;
        this.handler = new Handler(Looper.getMainLooper());
        this.context = context;
        this.urlStreamHandler = urlStreamHandler;
        this.clazz = clazz;
    }

    protected String requestURLFromParams(Object param) {
        @SuppressWarnings("unchecked")
        Map<String, Object> params = (Map<String, Object>)param;
        String url = baseUrl;

        if(params != null) {
            StringBuilder sb = new StringBuilder();
            for(String key : params.keySet()) {
                String value = params.get(key).toString();

                if(sb.length() == 0) {
                    sb.append((baseUrl.contains("?")) ? '&' : '?');
                }

                try {
                    key = URLEncoder.encode(key, "UTF-8");
                    value = URLEncoder.encode(value, "UTF-8");
                    sb.append(String.format("%s=%s", key, value));
                } catch (UnsupportedEncodingException e) {
                    throw new RuntimeException(e);
                }
            }

            url += sb;
        }

        return url;
    }

    public void load(Object param, BaseLoaderCallback<T> callback) {
        final String url = requestURLFromParams(param);
        final BaseLoaderCallback<T> finalCallback = callback;

        callbackLoadStarted(callback);
        executor.execute(new Runnable() {
            @Override
            public void run() {
                T result = null;

                Reader reader = null;
                try {
                    URL connectionUrl = new URL(null, url, urlStreamHandler);
                    HttpURLConnection connection = (HttpURLConnection)connectionUrl.openConnection();

                    if(connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                        InputStream is = connection.getInputStream();
                        reader = new InputStreamReader(is);
                        result = gson.fromJson(reader, clazz);
                    }
                } catch (MalformedURLException e) {
                    Log.w(TAG, "Bad url: " + url, e);
                } catch (IOException e) {
                    Log.w(TAG, "IO was bad: " + url, e);
                } catch (JsonParseException e) {
                    Log.w(TAG, "Bad JSON", e);
                } finally {
                    IOUtil.close(reader);
                }

                callbackLoadFinished(finalCallback, result);
            }
        });
    }

    protected void callbackLoadStarted(final BaseLoaderCallback<T> callback) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                callback.loadStarted(BaseLoader.this);
            }
        });
    }

    protected void callbackLoadFinished(final BaseLoaderCallback<T> callback, final T obj) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                callback.loadFinished(BaseLoader.this, obj);
            }
        });
    }
}
