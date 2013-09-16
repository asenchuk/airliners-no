package net.taviscaron.airliners.test.data;

import android.content.Context;
import android.os.Looper;
import android.test.InstrumentationTestCase;
import junit.framework.Assert;
import net.taviscaron.airliners.data.BaseLoader;
import net.taviscaron.airliners.data.SearchLoader;
import net.taviscaron.airliners.model.AircraftSearchResult;
import net.taviscaron.airliners.model.SearchParams;
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

    public void testSearchLoader() throws Exception {
        SearchParams searchParams = new SearchParams();
        searchParams.setAircraft("Boeing 777");
        searchParams.setAirline("Nord Wind");
        searchParams.setLimit(40);

        final CountDownLatch cdl = new CountDownLatch(1);
        searchLoader.load(searchParams, new BaseLoader.BaseLoaderCallback<SearchParams, SearchResult>() {
            private boolean loadStartedCalled = false;

            @Override
            public void loadStarted(BaseLoader<SearchParams, SearchResult> loader) {
                Assert.assertSame(Thread.currentThread(), Looper.getMainLooper().getThread());
                Assert.assertEquals(searchLoader, loader);
                loadStartedCalled = true;
            }

            @Override
            public void loadFinished(BaseLoader<SearchParams, SearchResult> loader, SearchResult result) {
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
