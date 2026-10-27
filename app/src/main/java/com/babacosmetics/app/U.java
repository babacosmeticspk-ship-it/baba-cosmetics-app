package com.babacosmetics.app;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.RippleDrawable;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

/** UI helpers so screens can be built in a few lines. */
public final class U {
    private U() {}

    public static final int MATCH = ViewGroup.LayoutParams.MATCH_PARENT;
    public static final int WRAP = ViewGroup.LayoutParams.WRAP_CONTENT;

    public static int dp(Context c, float v) {
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, v, c.getResources().getDisplayMetrics()));
    }

    public static String money(double v) {
        NumberFormat f = NumberFormat.getNumberInstance(Locale.US);
        f.setMaximumFractionDigits(v == Math.floor(v) ? 0 : 2);
        f.setMinimumFractionDigits(0);
        return "Rs. " + f.format(v);
    }

    public static GradientDrawable round(int color, float radius, int strokeColor, int strokeWidth) {
        GradientDrawable g = new GradientDrawable();
        g.setColor(color);
        g.setCornerRadius(radius);
        if (strokeWidth > 0) g.setStroke(strokeWidth, strokeColor);
        return g;
    }

    public static Drawable ripple(Drawable content) {
        Drawable mask = content != null ? content : new ColorDrawable(0xFFFFFFFF);
        return new RippleDrawable(ColorStateList.valueOf(0x22000000), content, mask);
    }

    public static void clickable(View v, View.OnClickListener l) {
        v.setClickable(true);
        v.setFocusable(true);
        if (v.getBackground() == null) v.setBackground(ripple(null));
        else v.setBackground(ripple(v.getBackground()));
        v.setOnClickListener(l);
    }

    public static TextView text(Context c, String s, float sp, int color, boolean bold) {
        TextView t = new TextView(c);
        t.setText(s);
        t.setTextSize(TypedValue.COMPLEX_UNIT_SP, sp);
        t.setTextColor(color);
        if (bold) t.setTypeface(Typeface.create("sans-serif-medium", Typeface.NORMAL));
        return t;
    }

    public static TextView button(Context c, String label, boolean filled) {
        TextView b = text(c, label, 15, filled ? 0xFFFFFFFF : Config.BRAND_DARK, true);
        b.setGravity(Gravity.CENTER);
        b.setPadding(dp(c, 16), dp(c, 13), dp(c, 16), dp(c, 13));
        b.setBackground(round(filled ? Config.BRAND_DARK : 0xFFFFFFFF, dp(c, 26),
                Config.BRAND_DARK, filled ? 0 : dp(c, 1.5f)));
        b.setClickable(true);
        b.setFocusable(true);
        b.setBackground(ripple(b.getBackground()));
        return b;
    }

    public static ImageView icon(Context c, int res, int color, int sizeDp) {
        ImageView i = new ImageView(c);
        i.setImageResource(res);
        i.setColorFilter(color);
        int pad = dp(c, 10);
        i.setPadding(pad, pad, pad, pad);
        i.setLayoutParams(new LinearLayout.LayoutParams(dp(c, sizeDp + 20), dp(c, sizeDp + 20)));
        return i;
    }

    public static LinearLayout.LayoutParams lp(int w, int h) {
        return new LinearLayout.LayoutParams(w, h);
    }

    public static LinearLayout.LayoutParams lp(int w, int h, float weight) {
        return new LinearLayout.LayoutParams(w, h, weight);
    }

    public static LinearLayout.LayoutParams margins(Context c, int w, int h, int l, int t, int r, int b) {
        LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(w, h);
        p.setMargins(dp(c, l), dp(c, t), dp(c, r), dp(c, b));
        return p;
    }

    public static LinearLayout vertical(Context c) {
        LinearLayout l = new LinearLayout(c);
        l.setOrientation(LinearLayout.VERTICAL);
        return l;
    }

    public static LinearLayout horizontal(Context c) {
        LinearLayout l = new LinearLayout(c);
        l.setOrientation(LinearLayout.HORIZONTAL);
        l.setGravity(Gravity.CENTER_VERTICAL);
        return l;
    }

    public static View divider(Context c) {
        View v = new View(c);
        v.setBackgroundColor(Config.LINE);
        v.setLayoutParams(new LinearLayout.LayoutParams(MATCH, Math.max(1, dp(c, 0.7f))));
        return v;
    }

    public static void toast(Context c, String s) {
        Toast.makeText(c, s, Toast.LENGTH_SHORT).show();
    }

    public static TextView strike(TextView t) {
        t.setPaintFlags(t.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        return t;
    }

    /** Top bar with optional back arrow and title. Returns the bar; extra icons can be added to it. */
    public static LinearLayout topBar(final Activity a, String title, boolean back) {
        LinearLayout bar = horizontal(a);
        bar.setBackgroundColor(0xFFFFFFFF);
        bar.setPadding(dp(a, 4), 0, dp(a, 4), 0);
        bar.setMinimumHeight(dp(a, 56));
        if (back) {
            ImageView b = icon(a, R.drawable.ic_back, Config.TEXT, 24);
            clickable(b, v -> a.finish());
            bar.addView(b);
        } else {
            View s = new View(a);
            bar.addView(s, lp(dp(a, 12), 1));
        }
        TextView t = text(a, title, 18, Config.TEXT, true);
        t.setSingleLine(true);
        t.setEllipsize(TextUtils.TruncateAt.END);
        t.setTag("title");
        bar.addView(t, lp(0, WRAP, 1));
        return bar;
    }

    /** Opens a website link inside the app (product page, category, or web page). */
    public static void openLink(Activity a, String link) {
        if (link == null || link.isEmpty()) return;
        String path = link.replace("https://www.babacosmetics.com", "").replace("https://babacosmetics.com", "");
        int q = path.indexOf('?');
        if (q >= 0) path = path.substring(0, q);
        if (path.startsWith("/products/")) {
            ProductActivity.open(a, path.substring("/products/".length()));
        } else if (path.startsWith("/collections/")) {
            String h = path.substring("/collections/".length());
            String name = h;
            for (String[] col : Config.COLLECTIONS) if (col[1].equals(h)) name = col[0];
            ListActivity.openCollection(a, h, name);
        } else {
            WebActivity.open(a, path.startsWith("/") ? Config.BASE + path : link, "Baba Cosmetics", false);
        }
    }

    /** Product card used in all product grids. */
    public static View productCard(final Activity a, final Product p, final Runnable onCartChanged) {
        LinearLayout card = vertical(a);
        card.setBackground(round(0xFFFFFFFF, dp(a, 14), Config.LINE, dp(a, 1)));
        card.setClipToOutline(true);

        FrameLayout imgBox = new FrameLayout(a);
        SquareImage img = new SquareImage(a);
        img.setScaleType(ImageView.ScaleType.CENTER_CROP);
        img.setBackgroundColor(Config.BG);
        imgBox.addView(img, new FrameLayout.LayoutParams(MATCH, WRAP));
        ImageLoader.load(img, Api.sized(p.image(), 500));

        Product.Variant v = p.firstVariant();
        int off = v == null ? 0 : v.discountPercent();
        if (!p.isAvailable()) {
            TextView so = text(a, "Sold out", 11, 0xFFFFFFFF, true);
            so.setBackground(round(0xCC333333, dp(a, 10), 0, 0));
            so.setPadding(dp(a, 8), dp(a, 3), dp(a, 8), dp(a, 3));
            FrameLayout.LayoutParams sl = new FrameLayout.LayoutParams(WRAP, WRAP, Gravity.TOP | Gravity.START);
            sl.setMargins(dp(a, 8), dp(a, 8), 0, 0);
            imgBox.addView(so, sl);
        } else if (off > 0) {
            TextView badge = text(a, "-" + off + "%", 11, 0xFFFFFFFF, true);
            badge.setBackground(round(Config.BRAND_DARK, dp(a, 10), 0, 0));
            badge.setPadding(dp(a, 8), dp(a, 3), dp(a, 8), dp(a, 3));
            FrameLayout.LayoutParams bl = new FrameLayout.LayoutParams(WRAP, WRAP, Gravity.TOP | Gravity.START);
            bl.setMargins(dp(a, 8), dp(a, 8), 0, 0);
            imgBox.addView(badge, bl);
        }
        card.addView(imgBox, lp(MATCH, WRAP));

        LinearLayout info = vertical(a);
        info.setPadding(dp(a, 10), dp(a, 8), dp(a, 10), dp(a, 10));
        TextView title = text(a, p.title, 13, Config.TEXT, false);
        title.setMaxLines(2);
        title.setMinLines(2);
        title.setEllipsize(TextUtils.TruncateAt.END);
        info.addView(title);

        LinearLayout prices = horizontal(a);
        prices.setPadding(0, dp(a, 4), 0, dp(a, 8));
        if (v != null) {
            prices.addView(text(a, money(v.price), 14, Config.BRAND_DARK, true));
            if (v.compareAt > v.price) {
                TextView old = strike(text(a, money(v.compareAt), 11, Config.MUTED, false));
                old.setPadding(dp(a, 6), 0, 0, 0);
                prices.addView(old);
            }
        }
        info.addView(prices);

        TextView add = text(a, p.isAvailable() ? (p.hasOptions() ? "Choose option" : "Add to cart") : "Sold out",
                12, p.isAvailable() ? Config.BRAND_DARK : Config.MUTED, true);
        add.setGravity(Gravity.CENTER);
        add.setPadding(0, dp(a, 7), 0, dp(a, 7));
        add.setBackground(round(p.isAvailable() ? Config.BRAND_LIGHT : 0xFFF1F1F1, dp(a, 18), 0, 0));
        if (p.isAvailable()) {
            clickable(add, x -> {
                if (p.hasOptions()) {
                    ProductActivity.open(a, p.handle);
                } else {
                    Product.Variant fv = p.firstVariant();
                    if (fv == null) return;
                    Cart.add(p, fv, 1);
                    toast(a, "Added to cart");
                    if (onCartChanged != null) onCartChanged.run();
                }
            });
        }
        info.addView(add, lp(MATCH, WRAP));
        card.addView(info, lp(MATCH, WRAP));

        clickable(card, x -> ProductActivity.open(a, p.handle));
        return card;
    }

    /** Two-column grid of product cards. */
    public static LinearLayout productGrid(Activity a, List<Product> products, Runnable onCartChanged) {
        LinearLayout grid = vertical(a);
        grid.setPadding(dp(a, 10), 0, dp(a, 10), dp(a, 10));
        for (int i = 0; i < products.size(); i += 2) {
            LinearLayout row = new LinearLayout(a);
            row.setOrientation(LinearLayout.HORIZONTAL);
            for (int j = 0; j < 2; j++) {
                LinearLayout.LayoutParams cl = lp(0, WRAP, 1);
                cl.setMargins(dp(a, 5), dp(a, 5), dp(a, 5), dp(a, 5));
                if (i + j < products.size()) {
                    row.addView(productCard(a, products.get(i + j), onCartChanged), cl);
                } else {
                    row.addView(new View(a), cl);
                }
            }
            grid.addView(row, lp(MATCH, WRAP));
        }
        return grid;
    }

    /** Centered message with an optional action button (for empty / error states). */
    public static LinearLayout message(Context c, String msg, String action, View.OnClickListener l) {
        LinearLayout box = vertical(c);
        box.setGravity(Gravity.CENTER);
        box.setPadding(dp(c, 24), dp(c, 48), dp(c, 24), dp(c, 48));
        TextView t = text(c, msg, 15, Config.MUTED, false);
        t.setGravity(Gravity.CENTER);
        box.addView(t);
        if (action != null) {
            TextView b = button(c, action, true);
            b.setOnClickListener(l);
            box.addView(b, margins(c, WRAP, WRAP, 0, 16, 0, 0));
        }
        return box;
    }

    public static void share(Activity a, String text) {
        Intent i = new Intent(Intent.ACTION_SEND);
        i.setType("text/plain");
        i.putExtra(Intent.EXTRA_TEXT, text);
        a.startActivity(Intent.createChooser(i, "Share"));
    }
}
