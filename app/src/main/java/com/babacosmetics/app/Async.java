package com.babacosmetics.app;

import android.os.Handler;
import android.os.Looper;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/** Runs network work in the background and returns the result on the main thread. */
public final class Async {
    private Async() {}

    private static final ExecutorService POOL = Executors.newCachedThreadPool();
    private static final Handler MAIN = new Handler(Looper.getMainLooper());

    public interface Task<T> {
        T run() throws Exception;
    }

    public interface Done<T> {
        void ok(T result);

        void fail(Exception e);
    }

    public static <T> void run(final Task<T> task, final Done<T> done) {
        POOL.execute(() -> {
            try {
                final T r = task.run();
                MAIN.post(() -> done.ok(r));
            } catch (final Exception e) {
                MAIN.post(() -> done.fail(e));
            }
        });
    }
}
