package com.babacosmetics.app;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/** In-memory copy of the store's products while the app is open. */
public final class Store {
    private Store() {}

    private static List<Product> all = null;
    private static final Map<String, Product> BY_HANDLE = new HashMap<>();

    public static synchronized boolean loaded() {
        return all != null;
    }

    public static synchronized void set(List<Product> list) {
        List<Product> sorted = new ArrayList<>(list);
        // In-stock products first, keep the store's own order otherwise.
        Collections.sort(sorted, (a, b) -> Boolean.compare(!a.isAvailable(), !b.isAvailable()));
        all = sorted;
        for (Product p : sorted) BY_HANDLE.put(p.handle, p);
    }

    public static synchronized List<Product> all() {
        return all == null ? new ArrayList<>() : new ArrayList<>(all);
    }

    public static synchronized Product byHandle(String handle) {
        return BY_HANDLE.get(handle);
    }

    public static synchronized void put(Product p) {
        BY_HANDLE.put(p.handle, p);
    }
}
