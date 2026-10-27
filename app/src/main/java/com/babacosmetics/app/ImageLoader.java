package com.babacosmetics.app;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Looper;
import android.util.LruCache;
import android.widget.ImageView;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/** Small image downloader with a memory cache. */
public final class ImageLoader {
    private ImageLoader() {}

    public interface Callback {
        void loaded(Bitmap b);
    }

    private static final ExecutorService POOL = Executors.newFixedThreadPool(4);
    private static final Handler MAIN = new Handler(Looper.getMainLooper());
    private static final LruCache<String, Bitmap> CACHE =
            new LruCache<String, Bitmap>((int) Math.min(Runtime.getRuntime().maxMemory() / 6, 64L * 1024 * 1024)) {
                @Override
                protected int sizeOf(String key, Bitmap value) {
                    return value.getByteCount();
                }
            };

    public static void load(ImageView view, String url) {
        load(view, url, null);
    }

    public static void load(final ImageView view, final String url, final Callback cb) {
        view.setTag(R.id.image_url, url);
        if (url == null) {
            view.setImageDrawable(null);
            return;
        }
        Bitmap cached = CACHE.get(url);
        if (cached != null) {
            view.setImageBitmap(cached);
            if (cb != null) cb.loaded(cached);
            return;
        }
        view.setImageDrawable(null);
        POOL.execute(() -> {
            Bitmap bm = null;
            try {
                byte[] data = Api.getBytes(url);
                BitmapFactory.Options o = new BitmapFactory.Options();
                o.inJustDecodeBounds = true;
                BitmapFactory.decodeByteArray(data, 0, data.length, o);
                int sample = 1;
                while (o.outWidth / (sample * 2) >= 1200) sample *= 2;
                BitmapFactory.Options o2 = new BitmapFactory.Options();
                o2.inSampleSize = sample;
                bm = BitmapFactory.decodeByteArray(data, 0, data.length, o2);
                if (bm != null) CACHE.put(url, bm);
            } catch (Exception ignored) {
            }
            final Bitmap result = bm;
            MAIN.post(() -> {
                if (result != null && url.equals(view.getTag(R.id.image_url))) {
                    view.setImageBitmap(result);
                    view.setAlpha(0f);
                    view.animate().alpha(1f).setDuration(200).start();
                    if (cb != null) cb.loaded(result);
                }
            });
        });
    }
}
