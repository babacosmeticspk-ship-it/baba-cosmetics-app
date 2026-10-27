package com.babacosmetics.app;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends BaseActivity {

    private static final int TAB_HOME = 0, TAB_CATEGORIES = 1, TAB_CART = 2;

    private FrameLayout content;
    private final TextView[] tabLabels = new TextView[4];
    private final ImageView[] tabIcons = new ImageView[4];
    private TextView cartBadge;
    private int tab = TAB_HOME;
    private View homeView;
    private LinearLayout homeGrid;
    private Map<String, String> collectionImages = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LinearLayout root = U.vertical(this);
        root.setBackgroundColor(0xFFFFFFFF);
        content = new FrameLayout(this);
        content.setBackgroundColor(0xFFFFFFFF);
        root.addView(content, U.lp(U.MATCH, 0, 1));
        root.addView(U.divider(this));
        root.addView(bottomNav(), U.lp(U.MATCH, U.WRAP));
        setContentView(root);
        int start = getIntent().getIntExtra("tab", TAB_HOME);
        showTab(start >= TAB_HOME && start <= TAB_CART ? start : TAB_HOME);

        Async.run(Api::collectionImages, new Async.Done<Map<String, String>>() {
            @Override
            public void ok(Map<String, String> r) {
                collectionImages = r;
                if (tab == TAB_CATEGORIES) showTab(TAB_CATEGORIES);
            }

            @Override
            public void fail(Exception e) {
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateBadge();
        if (tab == TAB_CART) showTab(TAB_CART);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        int t = intent.getIntExtra("tab", -1);
        if (t >= TAB_HOME && t <= TAB_CART) showTab(t);
    }

    @Override
    public void onBackPressed() {
        if (tab != TAB_HOME) showTab(TAB_HOME);
        else super.onBackPressed();
    }

    // ---------------------------------------------------------------- bottom navigation

    private View bottomNav() {
        LinearLayout nav = U.horizontal(this);
        nav.setBackgroundColor(0xFFFFFFFF);
        String[] names = {"Home", "Categories", "Cart", "Account"};
        int[] icons = {R.drawable.ic_home, R.drawable.ic_grid, R.drawable.ic_cart, R.drawable.ic_person};
        for (int i = 0; i < 4; i++) {
            final int index = i;
            LinearLayout item = U.vertical(this);
            item.setGravity(Gravity.CENTER);
            item.setPadding(0, U.dp(this, 6), 0, U.dp(this, 6));

            FrameLayout iconBox = new FrameLayout(this);
            ImageView ic = new ImageView(this);
            ic.setImageResource(icons[i]);
            iconBox.addView(ic, new FrameLayout.LayoutParams(U.dp(this, 24), U.dp(this, 24), Gravity.CENTER));
            if (i == TAB_CART) {
                cartBadge = U.text(this, "0", 10, 0xFFFFFFFF, true);
                cartBadge.setGravity(Gravity.CENTER);
                cartBadge.setMinWidth(U.dp(this, 17));
                cartBadge.setPadding(U.dp(this, 4), 0, U.dp(this, 4), 0);
                cartBadge.setBackground(U.round(Config.BRAND_DARK, U.dp(this, 9), 0, 0));
                FrameLayout.LayoutParams bl = new FrameLayout.LayoutParams(U.WRAP, U.dp(this, 17), Gravity.TOP | Gravity.END);
                iconBox.addView(cartBadge, bl);
            }
            item.addView(iconBox, U.lp(U.dp(this, 40), U.dp(this, 28)));

            TextView label = U.text(this, names[i], 11, Config.MUTED, false);
            item.addView(label);
            tabIcons[i] = ic;
            tabLabels[i] = label;

            U.clickable(item, v -> {
                if (index == 3) {
                    WebActivity.open(this, Config.BASE + "/account", "My Account", false);
                } else {
                    showTab(index);
                }
            });
            nav.addView(item, U.lp(0, U.WRAP, 1));
        }
        return nav;
    }

    private void updateBadge() {
        if (cartBadge == null) return;
        int n = Cart.count();
        cartBadge.setText(n > 99 ? "99+" : String.valueOf(n));
        cartBadge.setVisibility(n > 0 ? View.VISIBLE : View.GONE);
    }

    private void showTab(int t) {
        tab = t;
        for (int i = 0; i < 4; i++) {
            int color = i == t ? Config.BRAND_DARK : Config.MUTED;
            tabIcons[i].setColorFilter(color);
            tabLabels[i].setTextColor(color);
        }
        content.removeAllViews();
        View v;
        if (t == TAB_HOME) {
            if (homeView == null) homeView = buildHome();
            v = homeView;
        } else if (t == TAB_CATEGORIES) {
            v = buildCategories();
        } else {
            v = buildCart();
        }
        content.addView(v, new FrameLayout.LayoutParams(U.MATCH, U.MATCH));
        updateBadge();
    }

    // ---------------------------------------------------------------- home

    private View buildHome() {
        ScrollView scroll = new ScrollView(this);
        scroll.setFillViewport(true);
        LinearLayout page = U.vertical(this);
        page.setPadding(0, 0, 0, U.dp(this, 16));
        scroll.addView(page);

        // Header: logo
        LinearLayout header = U.horizontal(this);
        header.setPadding(U.dp(this, 16), U.dp(this, 10), U.dp(this, 6), U.dp(this, 4));
        ImageView logo = new ImageView(this);
        logo.setAdjustViewBounds(true);
        logo.setScaleType(ImageView.ScaleType.FIT_START);
        ImageLoader.load(logo, Config.LOGO);
        header.addView(logo, U.lp(0, U.dp(this, 42), 1));
        ImageView share = U.icon(this, R.drawable.ic_share, Config.TEXT, 22);
        U.clickable(share, v -> U.share(this, "Shop Baba Cosmetics: " + Config.BASE));
        header.addView(share);
        page.addView(header);

        // Search box
        LinearLayout search = U.horizontal(this);
        search.setBackground(U.round(Config.BG, U.dp(this, 24), Config.LINE, U.dp(this, 1)));
        search.setPadding(U.dp(this, 4), 0, U.dp(this, 14), 0);
        ImageView sIcon = U.icon(this, R.drawable.ic_search, Config.MUTED, 20);
        search.addView(sIcon);
        search.addView(U.text(this, "Search products…", 14, Config.MUTED, false));
        U.clickable(search, v -> ListActivity.openSearch(this));
        page.addView(search, U.margins(this, U.MATCH, U.WRAP, 16, 6, 16, 12));

        // Banner slider
        List<String> bannerImages = new ArrayList<>();
        for (String[] b : Config.BANNERS) bannerImages.add(b[0]);
        Slider banners = new Slider(this, bannerImages, true, 0.42f, true, 1200);
        banners.setBackground(U.round(Config.BG, U.dp(this, 16), 0, 0));
        banners.setClipToOutline(true);
        banners.setOnSlideClick(i -> U.openLink(this, Config.BANNERS[i][1]));
        page.addView(banners, U.margins(this, U.MATCH, U.WRAP, 16, 0, 16, 12));

        // Free delivery + COD strip
        StringBuilder strip = new StringBuilder();
        if (Config.FREE_DELIVERY_ABOVE > 0) {
            strip.append("Free delivery above ").append(U.money(Config.FREE_DELIVERY_ABOVE)).append("  •  ");
        }
        strip.append("Cash on Delivery  •  100% Original");
        TextView stripView = U.text(this, strip.toString(), 12, Config.BRAND_DARK, true);
        stripView.setGravity(Gravity.CENTER);
        stripView.setSingleLine(true);
        stripView.setEllipsize(TextUtils.TruncateAt.END);
        stripView.setPadding(U.dp(this, 10), U.dp(this, 9), U.dp(this, 10), U.dp(this, 9));
        stripView.setBackground(U.round(Config.BRAND_LIGHT, U.dp(this, 12), 0, 0));
        page.addView(stripView, U.margins(this, U.MATCH, U.WRAP, 16, 0, 16, 6));

        // Categories row
        page.addView(sectionTitle("Shop by Category", "See all", v -> showTab(TAB_CATEGORIES)));
        HorizontalScrollView hs = new HorizontalScrollView(this);
        hs.setHorizontalScrollBarEnabled(false);
        LinearLayout chips = U.horizontal(this);
        chips.setPadding(U.dp(this, 12), 0, U.dp(this, 12), 0);
        for (final String[] col : Config.COLLECTIONS) {
            TextView chip = U.text(this, col[0], 13, Config.TEXT, true);
            chip.setPadding(U.dp(this, 14), U.dp(this, 8), U.dp(this, 14), U.dp(this, 8));
            chip.setBackground(U.round(0xFFFFFFFF, U.dp(this, 20), Config.LINE, U.dp(this, 1)));
            U.clickable(chip, v -> ListActivity.openCollection(this, col[1], col[0]));
            chips.addView(chip, U.margins(this, U.WRAP, U.WRAP, 4, 0, 4, 0));
        }
        hs.addView(chips);
        page.addView(hs);

        // Promo posters (two side by side)
        if (Config.PROMOS.length > 0) {
            LinearLayout promos = U.horizontal(this);
            promos.setPadding(U.dp(this, 11), U.dp(this, 14), U.dp(this, 11), 0);
            for (final String[] pr : Config.PROMOS) {
                final ImageView im = new ImageView(this);
                im.setAdjustViewBounds(true);
                im.setScaleType(ImageView.ScaleType.FIT_CENTER);
                im.setBackground(U.round(Config.BG, U.dp(this, 14), 0, 0));
                im.setClipToOutline(true);
                ImageLoader.load(im, pr[0], (Bitmap b) -> im.requestLayout());
                U.clickable(im, v -> U.openLink(this, pr[1]));
                promos.addView(im, U.margins(this, 0, U.WRAP, 5, 0, 5, 0));
                ((LinearLayout.LayoutParams) im.getLayoutParams()).weight = 1;
            }
            page.addView(promos);
        }

        // All products
        page.addView(sectionTitle("All Products", null, null));
        homeGrid = U.vertical(this);
        page.addView(homeGrid);
        loadHomeProducts();
        return scroll;
    }

    private void loadHomeProducts() {
        homeGrid.removeAllViews();
        ProgressBar pb = new ProgressBar(this);
        homeGrid.addView(pb, U.margins(this, U.WRAP, U.WRAP, 0, 30, 0, 30));
        homeGrid.setGravity(Gravity.CENTER_HORIZONTAL);
        Async.run(Api::allProducts, new Async.Done<List<Product>>() {
            @Override
            public void ok(List<Product> r) {
                Store.set(r);
                homeGrid.removeAllViews();
                if (r.isEmpty()) {
                    homeGrid.addView(U.message(MainActivity.this, "No products yet.", null, null));
                } else {
                    homeGrid.addView(U.productGrid(MainActivity.this, Store.all(), MainActivity.this::updateBadge),
                            U.lp(U.MATCH, U.WRAP));
                }
            }

            @Override
            public void fail(Exception e) {
                homeGrid.removeAllViews();
                homeGrid.addView(U.message(MainActivity.this,
                        "Could not load products.\nPlease check your internet connection.",
                        "Try again", v -> loadHomeProducts()));
            }
        });
    }

    private View sectionTitle(String title, String action, View.OnClickListener l) {
        LinearLayout row = U.horizontal(this);
        row.setPadding(U.dp(this, 16), U.dp(this, 18), U.dp(this, 16), U.dp(this, 10));
        row.addView(U.text(this, title, 17, Config.TEXT, true), U.lp(0, U.WRAP, 1));
        if (action != null) {
            TextView a = U.text(this, action, 13, Config.BRAND_DARK, true);
            a.setPadding(U.dp(this, 8), U.dp(this, 4), 0, U.dp(this, 4));
            U.clickable(a, l);
            row.addView(a);
        }
        return row;
    }

    // ---------------------------------------------------------------- categories

    private View buildCategories() {
        LinearLayout wrap = U.vertical(this);
        wrap.addView(U.topBar(this, "Categories", false));
        wrap.addView(U.divider(this));
        ScrollView scroll = new ScrollView(this);
        LinearLayout list = U.vertical(this);
        list.setPadding(U.dp(this, 12), U.dp(this, 8), U.dp(this, 12), U.dp(this, 16));
        scroll.addView(list);
        for (final String[] col : Config.COLLECTIONS) {
            LinearLayout row = U.horizontal(this);
            row.setPadding(U.dp(this, 10), U.dp(this, 10), U.dp(this, 6), U.dp(this, 10));
            row.setBackground(U.round(0xFFFFFFFF, U.dp(this, 14), Config.LINE, U.dp(this, 1)));

            FrameLayout thumb = new FrameLayout(this);
            thumb.setBackground(U.round(Config.BRAND_LIGHT, U.dp(this, 12), 0, 0));
            thumb.setClipToOutline(true);
            String img = collectionImages.get(col[1]);
            if (img != null) {
                ImageView iv = new ImageView(this);
                iv.setScaleType(ImageView.ScaleType.CENTER_CROP);
                ImageLoader.load(iv, Api.sized(img, 200));
                thumb.addView(iv, new FrameLayout.LayoutParams(U.MATCH, U.MATCH));
            } else {
                TextView letter = U.text(this, col[0].substring(0, 1), 20, Config.BRAND_DARK, true);
                letter.setGravity(Gravity.CENTER);
                thumb.addView(letter, new FrameLayout.LayoutParams(U.MATCH, U.MATCH));
            }
            row.addView(thumb, U.lp(U.dp(this, 54), U.dp(this, 54)));

            TextView name = U.text(this, col[0], 15, Config.TEXT, true);
            name.setPadding(U.dp(this, 14), 0, 0, 0);
            row.addView(name, U.lp(0, U.WRAP, 1));
            row.addView(U.icon(this, R.drawable.ic_chevron, Config.MUTED, 20));

            U.clickable(row, v -> ListActivity.openCollection(this, col[1], col[0]));
            list.addView(row, U.margins(this, U.MATCH, U.WRAP, 0, 5, 0, 5));
        }
        wrap.addView(scroll, U.lp(U.MATCH, 0, 1));
        return wrap;
    }

    // ---------------------------------------------------------------- cart

    private View buildCart() {
        LinearLayout wrap = U.vertical(this);
        wrap.setBackgroundColor(Config.BG);
        LinearLayout bar = U.topBar(this, "My Cart", false);
        wrap.addView(bar);
        wrap.addView(U.divider(this));

        List<Cart.Item> items = Cart.items();
        if (items.isEmpty()) {
            LinearLayout empty = U.message(this, "Your cart is empty.", "Start shopping", v -> showTab(TAB_HOME));
            wrap.addView(empty, U.lp(U.MATCH, 0, 1));
            return wrap;
        }

        ScrollView scroll = new ScrollView(this);
        LinearLayout list = U.vertical(this);
        list.setPadding(U.dp(this, 12), U.dp(this, 10), U.dp(this, 12), U.dp(this, 12));
        scroll.addView(list);

        for (final Cart.Item it : items) {
            LinearLayout row = U.horizontal(this);
            row.setBackground(U.round(0xFFFFFFFF, U.dp(this, 14), Config.LINE, U.dp(this, 1)));
            row.setPadding(U.dp(this, 10), U.dp(this, 10), U.dp(this, 4), U.dp(this, 10));

            ImageView img = new ImageView(this);
            img.setScaleType(ImageView.ScaleType.CENTER_CROP);
            img.setBackground(U.round(Config.BG, U.dp(this, 10), 0, 0));
            img.setClipToOutline(true);
            ImageLoader.load(img, Api.sized(it.image, 200));
            U.clickable(img, v -> ProductActivity.open(this, it.handle));
            row.addView(img, U.lp(U.dp(this, 76), U.dp(this, 76)));

            LinearLayout info = U.vertical(this);
            info.setPadding(U.dp(this, 12), 0, U.dp(this, 4), 0);
            TextView t = U.text(this, it.title, 14, Config.TEXT, true);
            t.setMaxLines(2);
            t.setEllipsize(TextUtils.TruncateAt.END);
            info.addView(t);
            if (it.variant != null && !it.variant.isEmpty()) {
                info.addView(U.text(this, it.variant, 12, Config.MUTED, false));
            }
            TextView price = U.text(this, U.money(it.price), 14, Config.BRAND_DARK, true);
            price.setPadding(0, U.dp(this, 4), 0, U.dp(this, 6));
            info.addView(price);
            info.addView(stepper(it.qty, q -> {
                Cart.setQty(it.variantId, q);
                showTab(TAB_CART);
            }));
            row.addView(info, U.lp(0, U.WRAP, 1));

            ImageView del = U.icon(this, R.drawable.ic_delete, Config.MUTED, 20);
            U.clickable(del, v -> {
                Cart.setQty(it.variantId, 0);
                showTab(TAB_CART);
            });
            row.addView(del);
            list.addView(row, U.margins(this, U.MATCH, U.WRAP, 0, 5, 0, 5));
        }
        wrap.addView(scroll, U.lp(U.MATCH, 0, 1));

        // Summary + checkout
        LinearLayout summary = U.vertical(this);
        summary.setBackgroundColor(0xFFFFFFFF);
        summary.setPadding(U.dp(this, 16), U.dp(this, 12), U.dp(this, 16), U.dp(this, 14));
        double total = Cart.total();
        if (Config.FREE_DELIVERY_ABOVE > 0) {
            String msg = total >= Config.FREE_DELIVERY_ABOVE
                    ? "You have unlocked FREE delivery!"
                    : "Add " + U.money(Config.FREE_DELIVERY_ABOVE - total) + " more for FREE delivery";
            TextView fd = U.text(this, msg, 12, Config.BRAND_DARK, true);
            fd.setGravity(Gravity.CENTER);
            fd.setPadding(U.dp(this, 8), U.dp(this, 7), U.dp(this, 8), U.dp(this, 7));
            fd.setBackground(U.round(Config.BRAND_LIGHT, U.dp(this, 10), 0, 0));
            summary.addView(fd, U.margins(this, U.MATCH, U.WRAP, 0, 0, 0, 10));
        }
        LinearLayout totalRow = U.horizontal(this);
        totalRow.addView(U.text(this, "Subtotal (" + Cart.count() + " items)", 14, Config.MUTED, false), U.lp(0, U.WRAP, 1));
        totalRow.addView(U.text(this, U.money(total), 18, Config.TEXT, true));
        summary.addView(totalRow);
        summary.addView(U.text(this, "Delivery charges are shown at checkout.", 11, Config.MUTED, false));
        TextView checkout = U.button(this, "Checkout", true);
        checkout.setOnClickListener(v -> WebActivity.open(this, Cart.checkoutUrl(), "Checkout", true));
        summary.addView(checkout, U.margins(this, U.MATCH, U.WRAP, 0, 12, 0, 0));
        wrap.addView(U.divider(this));
        wrap.addView(summary);
        return wrap;
    }

    interface QtyChange {
        void changed(int qty);
    }

    /** "−  2  +" quantity control. */
    View stepper(int qty, QtyChange cb) {
        LinearLayout s = U.horizontal(this);
        s.setBackground(U.round(0xFFFFFFFF, U.dp(this, 18), Config.LINE, U.dp(this, 1)));
        TextView minus = U.text(this, "−", 18, Config.TEXT, true);
        TextView plus = U.text(this, "+", 18, Config.TEXT, true);
        TextView n = U.text(this, String.valueOf(qty), 14, Config.TEXT, true);
        n.setGravity(Gravity.CENTER);
        for (TextView b : new TextView[]{minus, plus}) {
            b.setGravity(Gravity.CENTER);
            b.setPadding(U.dp(this, 12), U.dp(this, 2), U.dp(this, 12), U.dp(this, 2));
        }
        U.clickable(minus, v -> cb.changed(qty - 1));
        U.clickable(plus, v -> cb.changed(qty + 1));
        s.addView(minus);
        s.addView(n, U.lp(U.dp(this, 28), U.WRAP));
        s.addView(plus);
        LinearLayout holder = U.horizontal(this);
        holder.addView(s, U.lp(U.WRAP, U.WRAP));
        return holder;
    }
}
