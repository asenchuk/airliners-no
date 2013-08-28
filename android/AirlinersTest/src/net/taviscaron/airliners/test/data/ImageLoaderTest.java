package net.taviscaron.airliners.test.data;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Looper;
import android.test.InstrumentationTestCase;
import junit.framework.Assert;
import net.taviscaron.airliners.data.ImageLoader;
import net.taviscaron.airliners.test.mocks.MockURLStreamHandler;
import net.taviscaron.airliners.test.util.TestUtil;

import java.io.File;
import java.net.URLStreamHandler;
import java.util.concurrent.CountDownLatch;

public class ImageLoaderTest extends InstrumentationTestCase {
    private String CACHE_TAG = ImageLoader.IMAGE_CACHE_TAG;
    private File cacheBaseDir;
    private ImageLoader imageLoader;
    private Bitmap loadedBitmap;

    @Override
    protected void setUp() throws Exception {
        Context testContext = getInstrumentation().getContext();
        Context targetContext = getInstrumentation().getTargetContext();

        cacheBaseDir = new File(targetContext.getExternalCacheDir(), CACHE_TAG);

        // clean cache dir
        TestUtil.deleteRecursively(cacheBaseDir);
        Assert.assertFalse("cache directory should be empty on test starting", cacheBaseDir.exists());

        URLStreamHandler urlStreamHandler = new MockURLStreamHandler(testContext);
        imageLoader = new ImageLoader(targetContext, CACHE_TAG, urlStreamHandler);
        Assert.assertTrue("cache directory should be created on ImageLoader instantiating", cacheBaseDir.exists());
    }

    public void testImageLoading() throws Exception {
        final String expectedUrl = "http://example.com/red.png";
        final String expectedFileName = "red.png";
        final File file = new File(cacheBaseDir, expectedFileName);

        Assert.assertFalse("Cached file should not exist", file.exists());

        // Stage #1: load from network
        final CountDownLatch cdl1 = new CountDownLatch(1);
        imageLoader.loadImage(expectedUrl, new ImageLoader.ImageLoaderCallback() {
            boolean loadFromNetworkCalled = false;

            @Override
            public void imageLoaded(ImageLoader loader, String url, Bitmap bitmap, String imageCachePath) {
                Assert.assertSame(Thread.currentThread(), Looper.getMainLooper().getThread());
                Assert.assertTrue("Load from network started callback was not called", loadFromNetworkCalled);
                Assert.assertEquals(imageLoader, loader);
                Assert.assertEquals(expectedUrl, url);
                Assert.assertNotNull(bitmap);
                Assert.assertTrue("File should be loaded to cache", file.exists());
                Assert.assertEquals(file.getAbsolutePath(), imageCachePath);
                loadedBitmap = bitmap;
                cdl1.countDown();
            }

            @Override
            public void imageLoadFailed(ImageLoader loader, String url) {
                Assert.assertSame(Thread.currentThread(), Looper.getMainLooper().getThread());
                Assert.assertEquals(imageLoader, loader);
                Assert.assertEquals(expectedUrl, url);
                Assert.fail("Image should not fail to load");
                cdl1.countDown();
            }

            @Override
            public void imageLoadStarted(ImageLoader loader, String url) {
                Assert.assertSame(Thread.currentThread(), Looper.getMainLooper().getThread());
                loadFromNetworkCalled = true;
                Assert.assertEquals(imageLoader, loader);
                Assert.assertEquals(expectedUrl, url);
            }
        });
        cdl1.await();
    }

    public void testFailedImageLoading() throws Exception {
        final CountDownLatch cdl = new CountDownLatch(1);
        imageLoader.loadImage("http://example.com/non-existent-file", new ImageLoader.ImageLoaderCallback() {
            boolean loadFromNetworkCalled = false;

            @Override
            public void imageLoaded(ImageLoader loader, String url, Bitmap bitmap, String imageCachePath) {
                Assert.assertSame(Thread.currentThread(), Looper.getMainLooper().getThread());
                Assert.fail("Image should not be loaded");
                cdl.countDown();
            }

            @Override
            public void imageLoadFailed(ImageLoader loader, String url) {
                Assert.assertSame(Thread.currentThread(), Looper.getMainLooper().getThread());
                Assert.assertEquals(imageLoader, loader);
                cdl.countDown();
            }

            @Override
            public void imageLoadStarted(ImageLoader loader, String url) {
                Assert.assertSame(Thread.currentThread(), Looper.getMainLooper().getThread());
                loadFromNetworkCalled = true;
                Assert.assertEquals(imageLoader, loader);
            }
        });
        cdl.await();
    }

    public void testConcurrentLoading() throws Exception {
        loadedBitmap = null;
        int concurrentCount = 10;

        final CountDownLatch cdl = new CountDownLatch(concurrentCount);
        for(int i = 0; i < concurrentCount; i++) {
            imageLoader.loadImage("http://example.com/blue.png", new ImageLoader.ImageLoaderCallback() {
                @Override
                public void imageLoaded(ImageLoader loader, String url, Bitmap bitmap, String imageCachePath) {
                    Assert.assertNotNull(bitmap);
                    if(loadedBitmap != null) {
                        Assert.assertTrue("Loaded bitmaps should be same", loadedBitmap.sameAs(bitmap));
                    } else {
                        loadedBitmap = bitmap;
                    }
                    cdl.countDown();
                }

                @Override
                public void imageLoadFailed(ImageLoader loader, String url) {
                    Assert.fail("Load should not be failed");
                    cdl.countDown();
                }

                @Override
                public void imageLoadStarted(ImageLoader loader, String url) {
                }
            });
        }
        cdl.await();
    }
}
