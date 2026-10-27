package com.babacosmetics.app;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
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
import java.util.List;

public class ProductActivity extends BaseActivity {

    public static void open(Activity a, String handle) {
        Intent i = new Intent(a, ProductActivity.class);
        i.putExtra("handle", handle);
        a.startActivity(i);
    }

    private String handle;
    private FrameLayout body;
    private LinearLayout bottom;
    private TextView cartCount;
    private Product product;
    private Product.Variant selected;
    private int qty = 1;

    private TextView priceView, compareView, offView, stockView, qtyView;
    private TextView addBtn, buyBtn;
    private final List<TextView> variantChips = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        handle = getIntent().getStringExtra("handle");
        if (handle == null) handle = "";

        LinearLayout root = U.vertical(this);
        root.setBackgroundColor(0xFFFFFFFF);

        LinearLayout bar = U.topBar(this, "", true);
        ImageView share = U.icon(this, R.drawable.ic_share, Config.TEXT, 22);
        U.clickable(share, v -> {
            String t = product != null ? product.title : "Baba Cosmetics";
            U.share(this, t + "\n" + Config.BASE + "/products/" + handle);
        });
        bar.addView(share);

        FrameLayout cartBox = new FrameLayout(this);
        ImageView cart = U.icon(this, R.drawable.ic_cart, Config.TEXT, 22);
        cartBox.addView(cart);
        cartCount = U.text(this, "", 10, 0xFFFFFFFF, true);
        cartCount.setGravity(Gravity.CENTER);
        cartCount.setMinWidth(U.dp(this, 17));
        cartCount.setPadding(U.dp(this, 4), 0, U.dp(this, 4), 0);
        cartCount.setBackground(U.round(Config.BRAND_DARK, U.dp(this, 9), 0, 0));
        FrameLayout.LayoutParams cl = new FrameLayout.LayoutParams(U.WRAP, U.dp(this, 17), Gravity.TOP | Gravity.END);
        cl.setMargins(0, U.dp(this, 5), U.dp(this, 3), 0);
        cartBox.addView(cartCount, cl);
        U.clickable(cartBox, v -> openCart());
        bar.addView(cartBox);

        root.addView(bar, U.lp(U.MATCH, U.WRAP));
        root.addView(U.divider(this));

        body = new FrameLayout(this);
        root.addView(body, U.lp(U.MATCH, 0, 1));

        bottom = U.horizontal(this);
        bottom.setBackgroundColor(0xFFFFFFFF);
        bottom.setPadding(U.dp(this, 12), U.dp(this, 10), U.dp(this, 12), U.dp(this, 10));
        bottom.setVisibility(View.GONE);
        root.addView(U.divider(this));
        root.addView(bottom, U.lp(U.MATCH, U.WRAP));

        setContentView(root);

        Product cached = Store.byHandle(handle);
        if (cached != null) bind(cached);
        else load();
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateCount();
    }

    private void updateCount() {
        int n = Cart.count();
        cartCount.setText(String.valueOf(n));
        cartCount.setVisibility(n > 0 ? View.VISIBLE : View.GONE);
    }

    private void openCart() {
        Intent i = new Intent(this, MainActivity.class);
        i.putExtra("tab", 2);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(i);
    }

    private void load() {
        body.removeAllViews();
        ProgressBar pb = new ProgressBar(this);
        body.addView(pb, new FrameLayout.LayoutParams(U.WRAP, U.WRAP, Gravity.CENTER));
        Async.run(() -> Api.product(handle), new Async.Done<Product>() {
            @Override
            public void ok(Product p) {
                Store.put(p);
                bind(p);
            }

            @Override
            public void fail(Exception e) {
                body.removeAllViews();
                body.addView(U.message(ProductActivity.this, "Could not load this product.", "Try again", v -> load()));
            }
        });
    }

    private void bind(Product p) {
        product = p;
        selected = p.firstVariant();
        body.removeAllViews();

        ScrollView scroll = new ScrollView(this);
        LinearLayout page = U.vertical(this);
        scroll.addView(page);

        // Photos
        List<String> imgs = new ArrayList<>(p.images);
        if (!imgs.isEmpty()) {
            Slider photos = new Slider(this, imgs, false, 1f, false, 1000);
            photos.setBackgroundColor(Config.BG);
            page.addView(photos, U.lp(U.MATCH, U.WRAP));
        }

        LinearLayout info = U.vertical(this);
        info.setPadding(U.dp(this, 16), U.dp(this, 16), U.dp(this, 16), U.dp(this, 24));
        page.addView(info);

        TextView title = U.text(this, p.title, 20, Config.TEXT, true);
        info.addView(title);

        LinearLayout prices = U.horizontal(this);
        prices.setPadding(0, U.dp(this, 8), 0, U.dp(this, 4));
        priceView = U.text(this, "", 22, Config.BRAND_DARK, true);
        compareView = U.strike(U.text(this, "", 14, Config.MUTED, false));
        compareView.setPadding(U.dp(this, 10), 0, 0, 0);
        offView = U.text(this, "", 12, 0xFFFFFFFF, true);
        offView.setBackground(U.round(Config.BRAND_DARK, U.dp(this, 10), 0, 0));
        offView.setPadding(U.dp(this, 8), U.dp(this, 2), U.dp(this, 8), U.dp(this, 2));
        prices.addView(priceView);
        prices.addView(compareView);
        prices.addView(offView, U.margins(this, U.WRAP, U.WRAP, 10, 0, 0, 0));
        info.addView(prices);

        stockView = U.text(this, "", 13, 0xFF2E8B57, true);
        info.addView(stockView);

        // Options (size etc.)
        if (p.hasOptions()) {
            info.addView(label("Option"));
            HorizontalScrollView hs = new HorizontalScrollView(this);
            hs.setHorizontalScrollBarEnabled(false);
            LinearLayout chips = U.horizontal(this);
            variantChips.clear();
            for (final Product.Variant v : p.variants) {
                TextView chip = U.text(this, v.title, 13, Config.TEXT, true);
                chip.setPadding(U.dp(this, 14), U.dp(this, 8), U.dp(this, 14), U.dp(this, 8));
                chip.setTag(v);
                chip.setOnClickListener(x -> {
                    selected = v;
                    refresh();
                });
                variantChips.add(chip);
                chips.addView(chip, U.margins(this, U.WRAP, U.WRAP, 0, 0, 8, 0));
            }
            hs.addView(chips);
            info.addView(hs);
        }

        // Quantity
        info.addView(label("Quantity"));
        LinearLayout qrow = U.horizontal(this);
        qrow.setBackground(U.round(0xFFFFFFFF, U.dp(this, 20), Config.LINE, U.dp(this, 1)));
        TextView minus = U.text(this, "−", 20, Config.TEXT, true);
        TextView plus = U.text(this, "+", 20, Config.TEXT, true);
        qtyView = U.text(this, "1", 15, Config.TEXT, true);
        qtyView.setGravity(Gravity.CENTER);
        for (TextView b : new TextView[]{minus, plus}) {
            b.setGravity(Gravity.CENTER);
            b.setPadding(U.dp(this, 16), U.dp(this, 4), U.dp(this, 16), U.dp(this, 4));
        }
        U.clickable(minus, v -> {
            qty = Math.max(1, qty - 1);
            refresh();
        });
        U.clickable(plus, v -> {
            qty = Math.min(99, qty + 1);
            refresh();
        });
        qrow.addView(minus);
        qrow.addView(qtyView, U.lp(U.dp(this, 36), U.WRAP));
        qrow.addView(plus);
        LinearLayout qholder = U.horizontal(this);
        qholder.addView(qrow, U.lp(U.WRAP, U.WRAP));
        info.addView(qholder);

        // Trust badges
        LinearLayout trust = U.horizontal(this);
        trust.setPadding(0, U.dp(this, 18), 0, 0);
        String[] badges = {"Cash on\nDelivery", "Fast\nDelivery", "100%\nOriginal"};
        for (String b : badges) {
            TextView t = U.text(this, b, 12, Config.BRAND_DARK, true);
            t.setGravity(Gravity.CENTER);
            t.setPadding(U.dp(this, 4), U.dp(this, 10), U.dp(this, 4), U.dp(this, 10));
            t.setBackground(U.round(Config.BRAND_LIGHT, U.dp(this, 12), 0, 0));
            LinearLayout.LayoutParams tl = U.lp(0, U.WRAP, 1);
            tl.setMargins(U.dp(this, 3), 0, U.dp(this, 3), 0);
            trust.addView(t, tl);
        }
        info.addView(trust);

        // Description
        String desc = cleanHtml(p.bodyHtml);
        if (!desc.isEmpty()) {
            info.addView(label("Description"));
            TextView d = U.text(this, "", 14, 0xFF444444, false);
            d.setLineSpacing(U.dp(this, 3), 1f);
            d.setText(fromHtml(desc));
            info.addView(d);
        }

        body.addView(scroll, new FrameLayout.LayoutParams(U.MATCH, U.MATCH));

        // Bottom buttons
        bottom.removeAllViews();
        addBtn = U.button(this, "Add to Cart", false);
        buyBtn = U.button(this, "Buy Now", true);
        addBtn.setOnClickListener(v -> {
            if (selected == null || !selected.available) return;
            Cart.add(product, selected, qty);
            updateCount();
            U.toast(this, "Added to cart");
        });
        buyBtn.setOnClickListener(v -> {
            if (selected == null || !selected.available) return;
            WebActivity.open(this, Cart.buyNowUrl(selected.id, qty), "Checkout", false);
        });
        LinearLayout.LayoutParams al = U.lp(0, U.WRAP, 1);
        al.rightMargin = U.dp(this, 6);
        LinearLayout.LayoutParams bl = U.lp(0, U.WRAP, 1);
        bl.leftMargin = U.dp(this, 6);
        bottom.addView(addBtn, al);
        bottom.addView(buyBtn, bl);
        bottom.setVisibility(View.VISIBLE);

        refresh();
    }

    private TextView label(String s) {
        TextView t = U.text(this, s, 15, Config.TEXT, true);
        t.setPadding(0, U.dp(this, 18), 0, U.dp(this, 8));
        return t;
    }

    private void refresh() {
        Product.Variant v = selected;
        qtyView.setText(String.valueOf(qty));
        if (v == null) {
            priceView.setText("");
            compareView.setVisibility(View.GONE);
            offView.setVisibility(View.GONE);
            stockView.setText("Not available");
            stockView.setTextColor(0xFFC0392B);
            return;
        }
        priceView.setText(U.money(v.price));
        if (v.compareAt > v.price) {
            compareView.setText(U.money(v.compareAt));
            compareView.setVisibility(View.VISIBLE);
        } else {
            compareView.setVisibility(View.GONE);
        }
        int off = v.discountPercent();
        offView.setText(off + "% OFF");
        offView.setVisibility(off > 0 ? View.VISIBLE : View.GONE);
        stockView.setText(v.available ? "In stock" : "Sold out");
        stockView.setTextColor(v.available ? 0xFF2E8B57 : 0xFFC0392B);

        for (TextView chip : variantChips) {
            boolean on = chip.getTag() == v;
            Product.Variant cv = (Product.Variant) chip.getTag();
            chip.setTextColor(on ? 0xFFFFFFFF : (cv.available ? Config.TEXT : Config.MUTED));
            chip.setBackground(U.round(on ? Config.BRAND_DARK : 0xFFFFFFFF, U.dp(this, 20),
                    Config.LINE, on ? 0 : U.dp(this, 1)));
        }

        boolean ok = v.available;
        addBtn.setEnabled(ok);
        buyBtn.setEnabled(ok);
        addBtn.setAlpha(ok ? 1f : 0.4f);
        buyBtn.setAlpha(ok ? 1f : 0.4f);
        buyBtn.setText(ok ? "Buy Now" : "Sold out");
    }

    private static String cleanHtml(String html) {
        if (html == null) return "";
        String s = html.replaceAll("(?is)<img[^>]*>", "")
                .replaceAll("(?is)<script.*?</script>", "")
                .replaceAll("(?is)<style.*?</style>", "")
                .replaceAll("(?is)<iframe.*?</iframe>", "");
        String plain = s.replaceAll("<[^>]*>", "").replace("&nbsp;", " ").trim();
        return plain.isEmpty() ? "" : s;
    }

    @SuppressWarnings("deprecation")
    private static CharSequence fromHtml(String html) {
        CharSequence cs = Build.VERSION.SDK_INT >= 24
                ? Html.fromHtml(html, Html.FROM_HTML_MODE_COMPACT)
                : Html.fromHtml(html);
        // Trim blank lines at the end
        int end = cs.length();
        while (end > 0 && Character.isWhitespace(cs.charAt(end - 1))) end--;
        return cs.subSequence(0, end);
    }
}
