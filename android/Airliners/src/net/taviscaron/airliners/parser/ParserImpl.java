package net.taviscaron.airliners.parser;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.taviscaron.airliners.model.AircraftPhoto;
import net.taviscaron.airliners.model.AircraftSearchResult;
import net.taviscaron.airliners.model.SearchResult;

/**
 * Parser implementation
 * @author Andrei Senchuk
 */
public class ParserImpl implements Parser {
    private Gson gson;

    public ParserImpl() {
        gson = new GsonBuilder().create();
    }

    public AircraftPhoto parsePhoto(String json) {
        return gson.fromJson(json, AircraftPhoto.class);
    }

    public SearchResult parseSearchResult(String json) {
        return gson.fromJson(json, SearchResult.class);
    }
}
