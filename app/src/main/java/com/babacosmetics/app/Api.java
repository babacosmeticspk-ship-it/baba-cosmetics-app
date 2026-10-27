package com.babacosmetics.app;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/** Reads live data from the Shopify store (no API key needed). */
public final class Api {
    private Api() {}

    public static byte[] getBytes(String address) throws IOException {
        HttpURLConnection c = (HttpURLConnection) new URL(address).openConnection();
        c.setConnectTimeout(15000);
        c.setReadTimeout(25000);
        c.setInstanceFollowRedirects(true);
        c.setRequestProperty("User-Agent", "BabaCosmeticsApp/1.0 (Android)");
        c.setRequestProperty("Accept", "*/*");
        try {
            int code = c.getResponseCode();
            if (code >= 400) throw new IOException("HTTP " + code);
            InputStream in = new BufferedInputStream(c.getInputStream());
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            byte[] buf = new byte[16 * 1024];
            int n;
            while ((n = in.read(buf)) != -1) out.write(buf, 0, n);
            in.close();
            return out.toByteArray();
        } finally {
            c.disconnect();
        }
    }

    public static String get(String address) throws IOException {
        return new String(getBytes(address), StandardCharsets.UTF_8);
    }

    private static List<Product> parseList(String json) throws JSONException {
        List<Product> list = new ArrayList<>();
        JSONArray a = new JSONObject(json).optJSONArray("products");
        if (a == null) return list;
        for (int i = 0; i < a.length(); i++) {
            JSONObject o = a.optJSONObject(i);
            if (o != null) list.add(Product.from(o));
        }
        return list;
    }

    private static List<Product> paged(String path) throws Exception {
        List<Product> all = new ArrayList<>();
        for (int page = 1; page <= 20; page++) {
            List<Product> part = parseList(get(Config.BASE + path + "?limit=250&page=" + page));
            all.addAll(part);
            if (part.size() < 250) break;
        }
        return all;
    }

    public static List<Product> allProducts() throws Exception {
        return paged("/products.json");
    }

    public static List<Product> collection(String handle) throws Exception {
        return paged("/collections/" + handle + "/products.json");
    }

    public static Product product(String handle) throws Exception {
        JSONObject o = new JSONObject(get(Config.BASE + "/products/" + handle + ".json")).getJSONObject("product");
        return Product.from(o);
    }

    /** Collection handle -> image URL, from the public collections list. */
    public static Map<String, String> collectionImages() throws Exception {
        Map<String, String> map = new HashMap<>();
        JSONArray a = new JSONObject(get(Config.BASE + "/collections.json?limit=250")).optJSONArray("collections");
        if (a == null) return map;
        for (int i = 0; i < a.length(); i++) {
            JSONObject o = a.optJSONObject(i);
            if (o == null) continue;
            JSONObject im = o.optJSONObject("image");
            if (im != null && !im.optString("src", "").isEmpty()) map.put(o.optString("handle"), im.optString("src"));
        }
        return map;
    }

    /** Ask Shopify's image CDN for a smaller copy of an image. */
    public static String sized(String url, int width) {
        if (url == null) return null;
        if (url.startsWith("//")) url = "https:" + url;
        if (url.contains("width=")) return url;
        return url + (url.contains("?") ? "&" : "?") + "width=" + width;
    }
}
