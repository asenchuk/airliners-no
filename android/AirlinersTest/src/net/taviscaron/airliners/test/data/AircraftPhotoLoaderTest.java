package net.taviscaron.airliners.test.data;

import android.os.Looper;
import android.test.InstrumentationTestCase;
import junit.framework.Assert;
import net.taviscaron.airliners.data.AircraftPhotoLoader;
import net.taviscaron.airliners.data.BaseLoader;
import net.taviscaron.airliners.model.AircraftPhoto;
import net.taviscaron.airliners.test.mocks.MockURLStreamHandler;

import java.io.File;
import java.net.URL;
import java.util.concurrent.CountDownLatch;

public class AircraftPhotoLoaderTest extends InstrumentationTestCase {
    private AircraftPhotoLoader loader;

    @Override
    protected void setUp() throws Exception {
        MockURLStreamHandler urlStreamHandler = new MockURLStreamHandler(getInstrumentation().getContext()) {
            @Override
            protected File fileForURL(URL url) {
                return new File("example.com/api", url.getPath());
            }
        };

        loader = new AircraftPhotoLoader(getInstrumentation().getTargetContext(), urlStreamHandler);
    }

    public void testNormalLoad() throws Exception {
        final CountDownLatch cdl = new CountDownLatch(1);
        loader.load("123456", new BaseLoader.BaseLoaderCallback<AircraftPhoto>() {
            private boolean loadStartedCalled = false;

            @Override
            public void loadStarted(BaseLoader<AircraftPhoto> loader) {
                Assert.assertSame(Thread.currentThread(), Looper.getMainLooper().getThread());
                Assert.assertEquals(AircraftPhotoLoaderTest.this.loader, loader);
                loadStartedCalled = true;
            }

            @Override
            public void loadFinished(BaseLoader<AircraftPhoto> loader, AircraftPhoto photo) {
                Assert.assertSame(Thread.currentThread(), Looper.getMainLooper().getThread());
                Assert.assertTrue("Load started should be called before load finished", loadStartedCalled);
                Assert.assertEquals(AircraftPhotoLoaderTest.this.loader, loader);
                Assert.assertNotNull("Photo should be loaded", photo);
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
                Assert.assertEquals(1457, photo.getCount());
                cdl.countDown();
            }
        });
        cdl.await();
    }

    public void testNotExistentIdLoad() throws Exception {
        final CountDownLatch cdl = new CountDownLatch(1);
        loader.load("0000000", new BaseLoader.BaseLoaderCallback<AircraftPhoto>() {
            private boolean loadStartedCalled = false;

            @Override
            public void loadStarted(BaseLoader<AircraftPhoto> loader) {
                Assert.assertSame(Thread.currentThread(), Looper.getMainLooper().getThread());
                Assert.assertEquals(AircraftPhotoLoaderTest.this.loader, loader);
                loadStartedCalled = true;
            }

            @Override
            public void loadFinished(BaseLoader<AircraftPhoto> loader, AircraftPhoto photo) {
                Assert.assertSame(Thread.currentThread(), Looper.getMainLooper().getThread());
                Assert.assertTrue("Load started should be called before load finished", loadStartedCalled);
                Assert.assertEquals(AircraftPhotoLoaderTest.this.loader, loader);
                Assert.assertNull("Photo should not be loaded", photo);
                cdl.countDown();
            }
        });
        cdl.await();
    }

    public void testExtraAttributesAndAttributesMissingLoad() throws Exception {
        final CountDownLatch cdl = new CountDownLatch(1);
        loader.load("123457", new BaseLoader.BaseLoaderCallback<AircraftPhoto>() {
            private boolean loadStartedCalled = false;

            @Override
            public void loadStarted(BaseLoader<AircraftPhoto> loader) {
                Assert.assertSame(Thread.currentThread(), Looper.getMainLooper().getThread());
                Assert.assertEquals(AircraftPhotoLoaderTest.this.loader, loader);
                loadStartedCalled = true;
            }

            @Override
            public void loadFinished(BaseLoader<AircraftPhoto> loader, AircraftPhoto photo) {
                Assert.assertSame(Thread.currentThread(), Looper.getMainLooper().getThread());
                Assert.assertTrue("Load started should be called before load finished", loadStartedCalled);
                Assert.assertEquals(AircraftPhotoLoaderTest.this.loader, loader);
                Assert.assertNotNull("Photo should be loaded", photo);
                cdl.countDown();
            }
        });
        cdl.await();
    }
}
