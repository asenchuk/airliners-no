package net.taviscaron.airliners.data;

import android.content.Context;
import net.taviscaron.airliners.model.AircraftPhoto;

import java.net.URLStreamHandler;

/**
 * AircraftPhoto loader
 * @author Andrei Senchuk
 */
public class AircraftPhotoLoader extends BaseLoader<String, AircraftPhoto> {
    public AircraftPhotoLoader(Context context) {
        super(context, "/photo/", AircraftPhoto.class);
    }

    public AircraftPhotoLoader(Context context, URLStreamHandler urlStreamHandler) {
        super(context, "/photo/", AircraftPhoto.class, urlStreamHandler);
    }

    @Override
    protected String requestURLFromParam(String param) {
        return baseUrl + param;
    }
}
