package net.taviscaron.airliners.test.model;

import android.test.InstrumentationTestCase;
import junit.framework.Assert;
import net.taviscaron.airliners.model.AircraftPhoto;
import net.taviscaron.airliners.model.AircraftSearchResult;
import net.taviscaron.airliners.model.SearchResult;
import net.taviscaron.airliners.parser.ParserFactory;
import net.taviscaron.airliners.parser.Parser;

public class JsonParsingTest extends InstrumentationTestCase {
    private Parser parser = ParserFactory.createParser();

    public void testAircraftPhoto() {
        String json = "{\"id\":\"2246100\",\"prev\":\"2246099\",\"next\":\"2246101\",\"image\":\"http://cdn-www.airliners.net/aviation-photos/photos/0/0/1/2246100.jpg\",\"airline\":\"S7 - Siberia Airlines\",\"aircraft\":\"Airbus A319-114\",\"takenAt\":\"Moscow - Domodedovo (DME / UUDD)\",\"takenOn\":\"Russia, March 12, 2013\",\"reg\":\"VP-BHP\",\"cn\":\"2618\",\"author\":\"Osipov Dmitry\",\"pos\":1284,\"count\":1455}";
        AircraftPhoto photo = parser.parsePhoto(json);
        Assert.assertNotNull(photo);
        Assert.assertEquals("2246100", photo.getId());
        Assert.assertEquals("2246099", photo.getPrev());
        Assert.assertEquals("2246101", photo.getNext());
        Assert.assertEquals("http://cdn-www.airliners.net/aviation-photos/photos/0/0/1/2246100.jpg", photo.getImageUrl());
        Assert.assertEquals("S7 - Siberia Airlines", photo.getAirline());
        Assert.assertEquals("Airbus A319-114", photo.getAircraft());
        Assert.assertEquals("Moscow - Domodedovo (DME / UUDD)", photo.getTakenAt());
        Assert.assertEquals("Russia, March 12, 2013", photo.getTakenOn());
        Assert.assertEquals("VP-BHP", photo.getReg());
        Assert.assertEquals(null, photo.getCode());
        Assert.assertEquals("2618", photo.getCn());
        Assert.assertEquals("Osipov Dmitry", photo.getAuthor());
        Assert.assertEquals(null, photo.getRemark());
        Assert.assertEquals(1284, photo.getPos());
        Assert.assertEquals(1455, photo.getCount());
    }

    public void testAircraftExtraAttributes() {
        String json = "{\"unknownAttribute\":\"value\",\"unknownAttribute2\":\"value2\"}";
        AircraftPhoto photo = parser.parsePhoto(json);
        Assert.assertNotNull(photo);
    }

    public void testSearchResults() {
        String json = "{\"from\":1,\"to\":5,\"total\":1229,\"count\":5,\"items\":[{\"id\":\"2298308\",\"thumb\":\"http://cdn-www.airliners.net/aviation-photos/small/8/0/3/2298308.jpg\",\"aircraft\":\"Boeing 737-3Q8\",\"airline\":\"Belavia\",\"place\":\"Minsk - 2 (Velikiydvor) (MSQ / UMMS)\",\"country\":\"Belarus\",\"date\":\"July 27, 2013\",\"reg\":\"EW-254PA\",\"cn\":\"26294/2550\",\"author\":\"Alexey Yakubov\"},{\"id\":\"2298289\",\"thumb\":\"http://cdn-www.airliners.net/aviation-photos/small/9/8/2/2298289.jpg\",\"aircraft\":\"Boeing 737-31S\",\"airline\":\"Belavia\",\"place\":\"Minsk - 2 (Velikiydvor) (MSQ / UMMS)\",\"country\":\"Belarus\",\"date\":\"July 27, 2013\",\"reg\":\"EW-366PA\",\"cn\":\"29058/2946\",\"author\":\"Alexey Yakubov\"},{\"id\":\"2298216\",\"thumb\":\"http://cdn-www.airliners.net/aviation-photos/small/6/1/2/2298216.jpg\",\"aircraft\":\"Boeing 737-505\",\"airline\":\"Belavia\",\"place\":\"Barcelona (- El Prat) (BCN / LEBL)\",\"country\":\"Spain\",\"date\":\"May 25, 2013\",\"reg\":\"EW-294PA\",\"cn\":\"26338/2822\",\"author\":\"Ruben Gil\"},{\"id\":\"2297692\",\"thumb\":\"http://cdn-www.airliners.net/aviation-photos/small/2/9/6/2297692.jpg\",\"aircraft\":\"Embraer ERJ-170-200LR 175LR\",\"airline\":\"Belavia\",\"place\":\"Berlin - Schonefeld (SXF / EDDB)\",\"country\":\"Germany\",\"date\":\"July 2013\",\"reg\":\"EW-340PO\",\"cn\":\"17000350\",\"author\":\"Christopher Budde\"},{\"id\":\"2297461\",\"thumb\":\"http://cdn-www.airliners.net/aviation-photos/small/1/6/4/2297461.jpg\",\"aircraft\":\"Tupolev Tu-154M\",\"airline\":\"Belavia\",\"place\":\"Varna (VAR / LBWN)\",\"country\":\"Bulgaria\",\"date\":\"July 26, 2013\",\"reg\":\"EW-85703\",\"cn\":\"91A878\",\"author\":\"Manuel Mueller\"}]}";
        SearchResult result = parser.parseSearchResult(json);

        Assert.assertNotNull(result);
        Assert.assertNotNull(result.getItems());
        Assert.assertEquals(5, result.getItems().length);
        Assert.assertEquals(1, result.getFrom());
        Assert.assertEquals(5, result.getTo());
        Assert.assertEquals(1229, result.getTotal());
        Assert.assertEquals(5, result.getCount());

        AircraftSearchResult aircraft = result.getItems()[4];
        Assert.assertEquals("2297461", aircraft.getId());
        Assert.assertEquals("http://cdn-www.airliners.net/aviation-photos/small/1/6/4/2297461.jpg", aircraft.getThumbUrl());
        Assert.assertEquals("Tupolev Tu-154M", aircraft.getAircraft());
        Assert.assertEquals("Belavia", aircraft.getAirline());
        Assert.assertEquals("Varna (VAR / LBWN)", aircraft.getPlace());
        Assert.assertEquals("Bulgaria", aircraft.getCountry());
        Assert.assertEquals("July 26, 2013", aircraft.getDate());
        Assert.assertEquals("EW-85703", aircraft.getReg());
        Assert.assertEquals("91A878", aircraft.getCn());
        Assert.assertEquals("Manuel Mueller", aircraft.getAuthor());
    }
}
