package com.babacosmetics.app;

import android.content.Context;
import android.content.SharedPreferences;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/** Shopping cart saved on the phone. Checkout goes through the Shopify cart link. */
public final class Cart {
    private Cart() {}

    public static class Item {
        public long variantId;
        public String handle = "";
        public String title = "";
        public String variant = "";
        public String image;
        public double price;
        public int qty;
    }

    private static final List<Item> ITEMS = new ArrayList<>();
    private static SharedPreferences prefs;

    public static synchronized void init(Context c) {
        if (prefs != null) return;
        prefs = c.getApplicationContext().getSharedPreferences("cart", Context.MODE_PRIVATE);
        ITEMS.clear();
        try {
            JSONArray a = new JSONArray(prefs.getString("items", "[]"));
            for (int i = 0; i < a.length(); i++) {
                JSONObject o = a.getJSONObject(i);
                Item it = new Item();
                it.variantId = o.getLong("v");
                it.handle = o.optString("h");
                it.title = o.optString("t");
                it.variant = o.optString("vt");
                it.image = o.isNull("i") ? null : o.optString("i", null);
                it.price = o.optDouble("p", 0);
                it.qty = o.optInt("q", 1);
                if (it.qty > 0) ITEMS.add(it);
            }
        } catch (Exception ignored) {
        }
    }

    private static void save() {
        if (prefs == null) return;
        JSONArray a = new JSONArray();
        try {
            for (Item it : ITEMS) {
                JSONObject o = new JSONObject();
                o.put("v", it.variantId);
                o.put("h", it.handle);
                o.put("t", it.title);
                o.put("vt", it.variant);
                o.put("i", it.image == null ? JSONObject.NULL : it.image);
                o.put("p", it.price);
                o.put("q", it.qty);
                a.put(o);
            }
        } catch (Exception ignored) {
        }
        prefs.edit().putString("items", a.toString()).apply();
    }

    public static synchronized void add(Product p, Product.Variant v, int qty) {
        for (Item it : ITEMS) {
            if (it.variantId == v.id) {
                it.qty += qty;
                save();
                return;
            }
        }
        Item it = new Item();
        it.variantId = v.id;
        it.handle = p.handle;
        it.title = p.title;
        it.variant = v.isDefault() ? "" : v.title;
        it.image = p.image();
        it.price = v.price;
        it.qty = qty;
        ITEMS.add(it);
        save();
    }

    public static synchronized void setQty(long variantId, int qty) {
        for (int i = 0; i < ITEMS.size(); i++) {
            if (ITEMS.get(i).variantId == variantId) {
                if (qty <= 0) ITEMS.remove(i);
                else ITEMS.get(i).qty = Math.min(qty, 99);
                break;
            }
        }
        save();
    }

    public static synchronized void clear() {
        ITEMS.clear();
        save();
    }

    public static synchronized List<Item> items() {
        return new ArrayList<>(ITEMS);
    }

    public static synchronized int count() {
        int n = 0;
        for (Item it : ITEMS) n += it.qty;
        return n;
    }

    public static synchronized double total() {
        double t = 0;
        for (Item it : ITEMS) t += it.price * it.qty;
        return t;
    }

    /** Shopify "cart permalink": opens checkout with these items already added. */
    public static synchronized String checkoutUrl() {
        StringBuilder sb = new StringBuilder(Config.BASE).append("/cart/");
        for (int i = 0; i < ITEMS.size(); i++) {
            if (i > 0) sb.append(',');
            sb.append(ITEMS.get(i).variantId).append(':').append(ITEMS.get(i).qty);
        }
        return sb.toString();
    }

    public static String buyNowUrl(long variantId, int qty) {
        return Config.BASE + "/cart/" + variantId + ":" + qty;
    }
}
