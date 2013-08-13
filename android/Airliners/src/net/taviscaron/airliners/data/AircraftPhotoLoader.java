package net.taviscaron.airliners.data;

import android.content.Context;
import net.taviscaron.airliners.model.AircraftPhoto;

import java.net.URLStreamHandler;

/**
 * AircraftPhoto loader
 * @author Andrei Senchuk
 */
public class AircraftPhotoLoader extends BaseLoader<AircraftPhoto> {
    public AircraftPhotoLoader(Context context) {
        super(context, "/photo/", AircraftPhoto.class);
    }

    public AircraftPhotoLoader(Context context, URLStreamHandler urlStreamHandler) {
        super(context, "/photo/", AircraftPhoto.class, urlStreamHandler);
    }

    @Override
    protected String requestURLFromParams(Object param) {
        return baseUrl + param;
    }
}
