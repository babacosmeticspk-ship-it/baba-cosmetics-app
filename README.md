# Baba Cosmetics App — v2 (new design)

Splash (logo) → loading → Home. Products, prices, pictures and banners load LIVE from www.babacosmetics.com.

## Features
- Animated splash screen with logo + loading line + dots
- Home: banner slider, Crazy Offers, promo cards, category circles, trending collections,
  Bundles, Today's Best Deals, Best Sellers, Serums, Face Wash, "Why Baba" trust strip
- Categories tab (side list + products), collection pages with sorting
- Product page: photo gallery, discount, variants, quantity, details, Add to Cart / Buy Now
- Cart with FREE-delivery progress bar (Rs. 3,000), Shopify checkout, "Order on WhatsApp"
- Wishlist, Search with recent/popular searches, Voice search (mic), Account / help page
- Android back button support

## Build the APK (GitHub Actions)
1. Put all these files in your GitHub repo (replace the old app files).
2. Push to `main` → Actions tab → "Build Baba Cosmetics APK" runs automatically
   (or press "Run workflow").
3. When it finishes, open the run → Artifacts → download **BabaCosmetics-App** → install `app-debug.apk`.

## Change colours
`www/css/app.css` → top `:root` block (`--brand`, `--gold`).

## Change home banners / announcement
`www/js/app.js` → `BANNERS`, `WIDE_BANNER`, `ANNOUNCE` at the top.
