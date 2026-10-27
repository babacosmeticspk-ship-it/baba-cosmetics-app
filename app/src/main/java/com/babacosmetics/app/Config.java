package com.babacosmetics.app;

/**
 * App settings. Change values here and rebuild the app.
 */
public final class Config {
    private Config() {}

    /** Your Shopify store address (no slash at the end). */
    public static final String BASE = "https://babacosmetics.com";

    public static final String LOGO =
            "https://cdn.shopify.com/s/files/1/0632/0337/7241/files/LOGO_ecdffdbf-7ac6-4045-8a38-024980327e99.png?v=1759939619";

    /** Brand colours (theme colour of the website). */
    public static final int BRAND = 0xFFF69679;
    public static final int BRAND_DARK = 0xFFD8634A;
    public static final int BRAND_LIGHT = 0xFFFEF0EB;
    public static final int TEXT = 0xFF222222;
    public static final int MUTED = 0xFF8A8A8A;
    public static final int LINE = 0xFFEDEDED;
    public static final int BG = 0xFFF8F7F6;

    /** Free delivery threshold in rupees. Set to 0 to hide the free delivery message. */
    public static final double FREE_DELIVERY_ABOVE = 3000;

    /** Home page banners: {image URL, link it opens}. Taken from the website's home page. */
    public static final String[][] BANNERS = {
            {"https://babacosmetics.com/cdn/shop/files/parlor-pack-banner-desktop.jpg?v=1793099209&width=1200",
                    "/products/apple-24k-gold-ultra-glow-cream-bleach-500ml"},
            {"https://babacosmetics.com/cdn/shop/files/BB_crean_banner.webp?v=1788166398&width=1200",
                    "/products/perfect-glow-bb-cream"},
            {"https://babacosmetics.com/cdn/shop/files/Web_Banner_5.webp?v=1783575071&width=1200",
                    "/collections/serums"},
            {"https://babacosmetics.com/cdn/shop/files/Web_Banner_6.webp?v=1783575072&width=1200",
                    "/collections/serums"},
            {"https://babacosmetics.com/cdn/shop/files/Web_Banner_4.webp?v=1783575071&width=1200",
                    "/collections/serums"},
            {"https://babacosmetics.com/cdn/shop/files/men-glow-landscape.webp?v=1786002317&width=1200",
                    "/products/men-glow-fairness-expert-facewash-100ml"},
            {"https://babacosmetics.com/cdn/shop/files/men-detox-landscape.webp?v=1786002351&width=1200",
                    "/products/men-glow-detox-expert-facewash-100ml"},
    };

    /** Second row of promo posters on the home page: {image URL, link}. */
    public static final String[][] PROMOS = {
            {"https://babacosmetics.com/cdn/shop/files/baba-sunblock-roadtrip-poster.webp?v=1786004185&width=900",
                    "/products/sun-block-100ml"},
            {"https://babacosmetics.com/cdn/shop/files/bb.webp?v=1786007081&width=900",
                    "/products/perfect-glow-bb-cream"},
    };

    /** Categories, in the same order as the website menu: {name, collection handle}. */
    public static final String[][] COLLECTIONS = {
            {"Bundles", "bundle"},
            {"Men Glow Facewash", "men-glow-facewash"},
            {"BB Cream", "bb-cream"},
            {"Sunblock", "sunblock"},
            {"Face Wash", "face-wash-range"},
            {"Serums", "serums"},
            {"Bleach", "bleach"},
            {"Charcoal Mask", "charcoal-mask"},
            {"Finger Wax", "finger-wax"},
            {"Hair Removal Cream", "hair-removal-cream"},
            {"Organic Products", "organic-products"},
            {"Scrub", "scrub"},
            {"Sikable Hair", "sikable-hair"},
            {"Skin Polish", "skin-polish"},
            {"Urgent Facial", "urgent-facial"},
    };
}
