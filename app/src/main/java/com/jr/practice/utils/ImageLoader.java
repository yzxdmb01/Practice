package com.jr.practice.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v4.util.LruCache;
import android.text.TextUtils;
import android.widget.ImageView;

import com.jakewharton.disklrucache.DiskLruCache;
import com.jr.practice.R;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by Administrator on 2016-08-17.
 */

public class ImageLoader {
    private static final long DISK_CACHE_SIZE = 1024 * 1024 * 50;
    private static final int DISK_CACHE_INDEX = 0;
    private static final int IO_BUFFER_SIZE = 8 * 1024;
    private static final int TAG_KEY_URI = R.id.imageloader_uri;
    private static final int MESSAGE_POST_RESULT = 1;
    private static final int CPU_COUNT = Runtime.getRuntime().availableProcessors();
    private static final int CORE_POOL_SIZE = CPU_COUNT + 1;
    private static final int MAXIMUM_POOL_SIZE = CPU_COUNT * 2 + 1;
    private static final long KEEP_ALIVE_TIME = 10l;

    private boolean mIsDiskLruCacheCreated = false;
    private ImageResizer mImageResizer = new ImageResizer();


    private Context mContext;
    private LruCache<String, Bitmap> mMemoryCache;
    private DiskLruCache mDiskLruCache;

    private static final ThreadFactory sThreadFactory = new ThreadFactory() {
        private final AtomicInteger mCount = new AtomicInteger(1);

        @Override
        public Thread newThread(Runnable r) {
            return new Thread(r, "ImageLoader#" + mCount.getAndIncrement());
        }
    };

    private static final Executor THREAD_POOL_EXEXCTOR = new ThreadPoolExecutor(
            CORE_POOL_SIZE, MAXIMUM_POOL_SIZE, KEEP_ALIVE_TIME, TimeUnit.SECONDS,
            new LinkedBlockingQueue<>(), sThreadFactory
    );

    private Handler mMainHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            LoaderResult loaderResult = (LoaderResult) msg.obj;
            ImageView imageView = loaderResult.imageView;
            String uri = (String) imageView.getTag(TAG_KEY_URI);
            if (TextUtils.equals(uri, loaderResult.uri)) {
                imageView.setImageBitmap(loaderResult.bitmap);
            } else {
                L.e("set image bitmap,but url has changed,ignored!");
            }
        }
    };

    public ImageLoader(Context context) {
        mContext = context.getApplicationContext();
        int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
        int cacheSize = maxMemory / 8;
        //创建内存缓存
        mMemoryCache = new LruCache<String, Bitmap>(cacheSize) {
            @Override
            protected int sizeOf(String key, Bitmap value) {
                return value.getRowBytes() * value.getHeight() / 1024;
            }
        };

        //创建磁盘缓存
        File diskCacheDir = getDiskCacheDir(mContext, "bitmap");
        if (!diskCacheDir.exists()) {
            diskCacheDir.mkdirs();
        }
        if (getUsableSpace(diskCacheDir) > DISK_CACHE_SIZE) {
            try {
                /**
                 * 第一个参数：缓存的目录
                 * 第二个参数：应用版本号，一般设为1就可以，当版本号发生改变时DiskLruCache会请清空之前所有的缓存文件
                 * 第三个参数：单个节点所对应的数据的个数，一般设置为1即可
                 * 第四个参数：缓存的总大小
                 */
                mDiskLruCache = DiskLruCache.open(diskCacheDir, 1, 1, DISK_CACHE_SIZE);
                mIsDiskLruCacheCreated = true;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void addBitmapToMemoryCache(String key, Bitmap bitmap) {
        if (getBitmapFromMemCache(key) == null) {
            mMemoryCache.put(key, bitmap);
        }
    }

    private Bitmap getBitmapFromMemCache(String key) {
        return mMemoryCache.get(key);
    }

    private Bitmap loadBitmapFromMemCache(String uri) {
        String key = CommonUtils.hashKeyFormatUrl(uri);
        if (!TextUtils.isEmpty(key)) {
            return getBitmapFromMemCache(key);
        }
        return null;
    }

    /**
     * 磁盘缓存的添加和读取功能稍微复杂一些。
     * 磁盘缓存的添加需要通过Editor来完成，Editor提供了commit和abort方法来提交和撤销对文件系统的写操作，
     * 这个方法在http中获得了bitmap写入到DiskLruCache中，在写入到了内存LruCache中
     */
    private Bitmap loadBitmapFromHttp(String url, int reqWidth, int reqHeight) throws IOException {
        //此方法不应该出现在主线程，如果出现在主线程则抛出个异常
        if (Looper.myLooper() == Looper.getMainLooper()) {
            throw new RuntimeException("can not visit network from UI Thread");
        }
        if (mDiskLruCache == null) {
            return null;
        }
        //因为图片的url中很可能出现特殊字符，将影响url在Android中的使用，一般采用url的MD5值作为key
        String key = CommonUtils.hashKeyFormatUrl(url);
        DiskLruCache.Editor editor = mDiskLruCache.edit(key);
        if (editor != null) {
            /*由于前面在DiskLruCache的open方法中设置了一个节点只能有一个数据，因此
            * DISK_CACHE_INDEX设置为0就可以了*/
            //通过editor可以获得一个输出流，如下
            OutputStream outputStream = editor.newOutputStream(DISK_CACHE_INDEX);
            //有了文件输出流，当从网络下载图片时，图片就可以通过这个文件输出流写到文件系统上
            if (downloadUrlToStream(url, outputStream)) {
                editor.commit();
            } else {
                editor.abort();
            }
            mDiskLruCache.flush();
        }
        return loadBitmapFromDiskCache(url, reqWidth, reqHeight);
    }

    /**
     * 在磁盘缓存中加载bitmap
     *
     * @param url
     * @param reqWidth
     * @param reqHeight
     * @return
     */
    private Bitmap loadBitmapFromDiskCache(String url, int reqWidth, int reqHeight) throws IOException {
        if (Looper.myLooper() == Looper.getMainLooper()) {
            L.d("load bitmap from UI Thread, it's not recommended!");
        }
        if (mDiskLruCache == null) {
            return null;
        }
        Bitmap bitmap = null;
        String key = CommonUtils.hashKeyFormatUrl(url);
        DiskLruCache.Snapshot snapshot = mDiskLruCache.get(key);
        if (snapshot != null) {
            FileInputStream fileInputStream = (FileInputStream) snapshot.getInputStream(DISK_CACHE_INDEX);
            FileDescriptor fileDescriptor = fileInputStream.getFD();
            bitmap = mImageResizer.decodeSimpleBitmapFromFileDescriptor(fileDescriptor, reqWidth, reqHeight);
            if (bitmap != null) {
                addBitmapToMemoryCache(key, bitmap);
            }
        }
        return bitmap;
    }

    private boolean downloadUrlToStream(String urlString, OutputStream outputStream) {
        HttpURLConnection urlConnection = null;
        BufferedOutputStream out = null;
        BufferedInputStream in = null;
        try {
            final URL url = new URL(urlString);
            urlConnection = (HttpURLConnection) url.openConnection();
            in = new BufferedInputStream(urlConnection.getInputStream(), IO_BUFFER_SIZE);
            out = new BufferedOutputStream(outputStream, IO_BUFFER_SIZE);

            int b;
            while ((b = in.read()) != -1) {
                out.write(b);
            }
            return true;
        } catch (MalformedURLException e) {
//            e.printStackTrace();
        } catch (IOException e) {
//            e.printStackTrace();
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            CommonUtils.closeStream(in);
            CommonUtils.closeStream(out);
        }
        return false;
    }

    private Bitmap downloadBitmapFromUrl(String urlString) {
        Bitmap bitmap = null;
        HttpURLConnection urlConnection = null;
        BufferedInputStream in = null;
        try {
            final URL url = new URL(urlString);
            urlConnection = (HttpURLConnection) url.openConnection();
            in = new BufferedInputStream(urlConnection.getInputStream(), IO_BUFFER_SIZE);
            bitmap = BitmapFactory.decodeStream(in);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            L.d("Error in downloadBitmap:" + e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            CommonUtils.closeStream(in);
        }
        return bitmap;
    }

    private long getUsableSpace(File path) {
        return path.getUsableSpace();
    }

    //用于获取磁盘缓存目录
    private File getDiskCacheDir(Context mContext, String name) {
        boolean externalStorageAvailable = Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
        String cachePath;
        if (externalStorageAvailable) {
            cachePath = mContext.getExternalCacheDir().getPath();
        } else {
            cachePath = mContext.getCacheDir().getPath();
        }

        return new File(cachePath + File.separator + name);
    }

    /**
     * load bitmap from memory cache or disk cache or network
     *
     * @param uri       http url
     * @param reqWidth  the width ImageView desired
     * @param reqHeight
     * @return
     */
    public Bitmap loadBitmap(String uri, int reqWidth, int reqHeight) {
        //先查找内存中的
        Bitmap bitmap = loadBitmapFromMemCache(uri);
        if (bitmap != null) {
            L.d("loadBitmapFromMemCache,url:" + uri);
            return bitmap;
        }
        try {
            //在磁盘缓存中查找
            bitmap = loadBitmapFromDiskCache(uri, reqWidth, reqHeight);
            if (bitmap != null) {
                L.d("loadBitmapFromDiskCache,uri:" + uri);
                return bitmap;
            }
            //都没有，在网络中获取
            bitmap = loadBitmapFromHttp(uri, reqWidth, reqHeight);
            L.d("loadBitmapFromHttp,uri:" + uri);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (bitmap != null && !mIsDiskLruCacheCreated) {
            L.d("encounter error, DiskLruCache is not created.");
            bitmap = downloadBitmapFromUrl(uri);
        }

        return bitmap;
    }

    public void bindBitmap(final String uri, final ImageView imageView) {
        bindBitmap(uri, imageView, 0, 0);
    }

    public void bindBitmap(final String uri, final ImageView imageView, final int reqWidth, final int reqHeight) {
        imageView.setTag(TAG_KEY_URI, uri);
        Bitmap bitmap = loadBitmapFromMemCache(uri);
        if (bitmap != null) {
            imageView.setImageBitmap(bitmap);
            return;
        }
        Runnable loadBitmapTask = () -> {
            Bitmap bitmap1 = loadBitmap(uri, reqWidth, reqHeight);
            if (bitmap1 != null) {
                LoaderResult loaderResult = new LoaderResult(imageView, uri, bitmap1);
//                imageView.setImageBitmap(bitmap1);
                mMainHandler.obtainMessage(MESSAGE_POST_RESULT, loaderResult).sendToTarget();
            } else {
                Bitmap bitmap2 = BitmapFactory.decodeResource(mContext.getResources(), R.mipmap.shibainu2);
                LoaderResult loaderResult = new LoaderResult(imageView, uri, bitmap2);
//                imageView.setImageBitmap(bitmap1);
                mMainHandler.obtainMessage(MESSAGE_POST_RESULT, loaderResult).sendToTarget();
            }
        };
        THREAD_POOL_EXEXCTOR.execute(loadBitmapTask);
    }

    private static class LoaderResult {
        public ImageView imageView;
        public String uri;
        public Bitmap bitmap;

        public LoaderResult(ImageView imageView, String uri, Bitmap bitmap) {
            this.imageView = imageView;
            this.uri = uri;
            this.bitmap = bitmap;
        }
    }
}
