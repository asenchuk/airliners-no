package net.taviscaron.airliners.test.data;

import android.content.Context;
import android.os.Looper;
import android.test.InstrumentationTestCase;
import junit.framework.Assert;
import net.taviscaron.airliners.data.BaseLoader;
import net.taviscaron.airliners.data.SearchLoader;
import net.taviscaron.airliners.model.AircraftSearchResult;
import net.taviscaron.airliners.model.SearchResult;
import net.taviscaron.airliners.test.mocks.MockURLStreamHandler;

import java.io.File;
import java.net.URL;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

public class SearchLoaderTest extends InstrumentationTestCase {
    private SearchLoader searchLoader;

    @Override
    protected void setUp() throws Exception {
        MockURLStreamHandler urlStreamHandler = new MockURLStreamHandler(getInstrumentation().getContext()) {
            @Override
            protected File fileForURL(URL url) {
                return new File("example.com/api", url.getPath());
            }
        };

        Context targetContext = getInstrumentation().getTargetContext();
        searchLoader = SearchLoader.createSearchLoader(targetContext, urlStreamHandler);
    }

    public void testRequestBuilder() {
        String aircraft = "b777";
        String airline = "aa";
        String cn = "123";
        String code = "111";
        String country = "US";
        String date = "12 12 12";
        Integer limit = 23;
        Integer page = 2;
        String place = "London, Heathrow";
        String reg = "AA-1233";
        String remark = "test";
        Integer year = 2013;

        SearchLoader.RequestBuilder rb = new SearchLoader.RequestBuilder();
        rb.setAircraft(aircraft);
        rb.setAirline(airline);
        rb.setCn(cn);
        rb.setCode(code);
        rb.setCountry(country);
        rb.setDate(date);
        rb.setLimit(limit);
        rb.setPage(page);
        rb.setPlace(place);
        rb.setReg(reg);
        rb.setRemark(remark);
        rb.setYear(year);
        Map<String, Object> params = rb.createParams();

        Assert.assertEquals(aircraft, params.get(SearchLoader.AIRCRAFT_PARAM));
        Assert.assertEquals(airline, params.get(SearchLoader.AIRLINE_PARAM));
        Assert.assertEquals(cn, params.get(SearchLoader.CN_PARAM));
        Assert.assertEquals(code, params.get(SearchLoader.CODE_PARAM));
        Assert.assertEquals(country, params.get(SearchLoader.COUNTRY_PARAM));
        Assert.assertEquals(date, params.get(SearchLoader.DATE_PARAM));
        Assert.assertEquals(limit, params.get(SearchLoader.LIMIT_PARAM));
        Assert.assertEquals(page, params.get(SearchLoader.PAGE_PARAM));
        Assert.assertEquals(place, params.get(SearchLoader.PLACE_PARAM));
        Assert.assertEquals(reg, params.get(SearchLoader.REG_PARAM));
        Assert.assertEquals(remark, params.get(SearchLoader.REMARK_PARAM));
        Assert.assertEquals(year, params.get(SearchLoader.YEAR_PARAM));
    }

    public void testSearchLoader() throws Exception {
        SearchLoader.RequestBuilder rb = new SearchLoader.RequestBuilder();
        rb.setAircraft("Boeing 777");
        rb.setAirline("Nord Wind");
        rb.setLimit(40);

        final CountDownLatch cdl = new CountDownLatch(1);
        rb.execute(searchLoader, new BaseLoader.BaseLoaderCallback<SearchResult>() {
            private boolean loadStartedCalled = false;

            @Override
            public void loadStarted(BaseLoader<SearchResult> loader) {
                Assert.assertSame(Thread.currentThread(), Looper.getMainLooper().getThread());
                Assert.assertEquals(searchLoader, loader);
                loadStartedCalled = true;
            }

            @Override
            public void loadFinished(BaseLoader<SearchResult> loader, SearchResult result) {
                Assert.assertSame(Thread.currentThread(), Looper.getMainLooper().getThread());
                Assert.assertTrue("Load started should be called before load finished", loadStartedCalled);
                Assert.assertEquals(searchLoader, loader);
                Assert.assertNotNull("Search results should be loaded", result);

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

                cdl.countDown();
            }
        });
        cdl.await();
    }
}
