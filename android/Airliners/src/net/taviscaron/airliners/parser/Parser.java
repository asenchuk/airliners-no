package net.taviscaron.airliners.parser;

import net.taviscaron.airliners.model.AircraftPhoto;
import net.taviscaron.airliners.model.SearchResult;

/**
 * Entity parser interface
 * @author Andrei Senchuk
 */
public interface Parser {
    public AircraftPhoto parsePhoto(String json);
    public SearchResult parseSearchResult(String json);
}
