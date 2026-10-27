package com.babacosmetics.app;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/** A product as returned by Shopify's public products.json. */
public class Product {

    public static class Variant {
        public long id;
        public String title;
        public double price;
        public double compareAt;
        public boolean available;

        public boolean isDefault() {
            return title == null || title.isEmpty() || "Default Title".equalsIgnoreCase(title);
        }

        public int discountPercent() {
            if (compareAt <= price || compareAt <= 0) return 0;
            return (int) Math.round((compareAt - price) * 100.0 / compareAt);
        }
    }

    public long id;
    public String title = "";
    public String handle = "";
    public String bodyHtml = "";
    public String type = "";
    public final List<Variant> variants = new ArrayList<>();
    public final List<String> images = new ArrayList<>();

    public static Product from(JSONObject o) {
        Product p = new Product();
        p.id = o.optLong("id");
        p.title = o.optString("title", "");
        p.handle = o.optString("handle", "");
        p.bodyHtml = o.isNull("body_html") ? "" : o.optString("body_html", "");
        p.type = o.optString("product_type", "");

        JSONArray vs = o.optJSONArray("variants");
        if (vs != null) {
            for (int i = 0; i < vs.length(); i++) {
                JSONObject v = vs.optJSONObject(i);
                if (v == null) continue;
                Variant x = new Variant();
                x.id = v.optLong("id");
                x.title = v.optString("title", "");
                x.price = money(v, "price");
                x.compareAt = money(v, "compare_at_price");
                x.available = v.optBoolean("available", true);
                p.variants.add(x);
            }
        }

        JSONArray imgs = o.optJSONArray("images");
        if (imgs != null) {
            for (int i = 0; i < imgs.length(); i++) {
                JSONObject im = imgs.optJSONObject(i);
                if (im != null) {
                    String src = im.optString("src", "");
                    if (!src.isEmpty()) p.images.add(src);
                }
            }
        }
        if (p.images.isEmpty()) {
            JSONObject im = o.optJSONObject("image");
            if (im != null && !im.optString("src", "").isEmpty()) p.images.add(im.optString("src"));
        }
        return p;
    }

    private static double money(JSONObject v, String key) {
        if (!v.has(key) || v.isNull(key)) return 0;
        try {
            return Double.parseDouble(v.optString(key, "0"));
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    public Variant firstVariant() {
        for (Variant v : variants) if (v.available) return v;
        return variants.isEmpty() ? null : variants.get(0);
    }

    public boolean isAvailable() {
        for (Variant v : variants) if (v.available) return true;
        return false;
    }

    public boolean hasOptions() {
        return variants.size() > 1 || (variants.size() == 1 && !variants.get(0).isDefault());
    }

    public String image() {
        return images.isEmpty() ? null : images.get(0);
    }

    public String url() {
        return Config.BASE + "/products/" + handle;
    }
}
