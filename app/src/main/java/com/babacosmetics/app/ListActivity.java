package com.babacosmetics.app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/** Shows the products of one category, or search results. */
public class ListActivity extends BaseActivity {

    public static void openCollection(Activity a, String handle, String title) {
        Intent i = new Intent(a, ListActivity.class);
        i.putExtra("collection", handle);
        i.putExtra("title", title);
        a.startActivity(i);
    }

    public static void openSearch(Activity a) {
        Intent i = new Intent(a, ListActivity.class);
        i.putExtra("search", true);
        a.startActivity(i);
    }

    private FrameLayout body;
    private String collection;
    private boolean searchMode;
    private EditText searchBox;
    private TextView countView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        collection = getIntent().getStringExtra("collection");
        searchMode = getIntent().getBooleanExtra("search", false);
        String title = getIntent().getStringExtra("title");

        LinearLayout root = U.vertical(this);
        root.setBackgroundColor(0xFFFFFFFF);
        LinearLayout bar = U.topBar(this, searchMode ? "" : (title == null ? "Products" : title), true);
        if (searchMode) {
            View t = bar.findViewWithTag("title");
            if (t != null) bar.removeView(t);
            searchBox = new EditText(this);
            searchBox.setHint("Search products…");
            searchBox.setSingleLine(true);
            searchBox.setInputType(InputType.TYPE_CLASS_TEXT);
            searchBox.setImeOptions(EditorInfo.IME_ACTION_SEARCH);
            searchBox.setTextSize(15);
            searchBox.setTextColor(Config.TEXT);
            searchBox.setBackground(U.round(Config.BG, U.dp(this, 22), Config.LINE, U.dp(this, 1)));
            searchBox.setPadding(U.dp(this, 16), U.dp(this, 9), U.dp(this, 16), U.dp(this, 9));
            LinearLayout.LayoutParams sl = U.lp(0, U.WRAP, 1);
            sl.rightMargin = U.dp(this, 12);
            bar.addView(searchBox, sl);
            searchBox.addTextChangedListener(new TextWatcher() {
                public void beforeTextChanged(CharSequence s, int a, int b, int c) {}

                public void onTextChanged(CharSequence s, int a, int b, int c) {}

                public void afterTextChanged(Editable s) {
                    showSearch(s.toString());
                }
            });
        }
        root.addView(bar, U.lp(U.MATCH, U.WRAP));
        root.addView(U.divider(this));
        countView = U.text(this, "", 12, Config.MUTED, false);
        countView.setPadding(U.dp(this, 16), U.dp(this, 10), U.dp(this, 16), U.dp(this, 2));
        root.addView(countView);
        body = new FrameLayout(this);
        root.addView(body, U.lp(U.MATCH, 0, 1));
        setContentView(root);

        if (searchMode) {
            if (Store.loaded()) {
                showSearch("");
            } else {
                loading();
                Async.run(Api::allProducts, new Async.Done<List<Product>>() {
                    @Override
                    public void ok(List<Product> r) {
                        Store.set(r);
                        showSearch(searchBox.getText().toString());
                    }

                    @Override
                    public void fail(Exception e) {
                        error(() -> recreate());
                    }
                });
            }
            searchBox.requestFocus();
            searchBox.postDelayed(() -> {
                InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                if (imm != null) imm.showSoftInput(searchBox, InputMethodManager.SHOW_IMPLICIT);
            }, 250);
        } else {
            loadCollection();
        }
    }

    private void loading() {
        body.removeAllViews();
        body.addView(new ProgressBar(this), new FrameLayout.LayoutParams(U.WRAP, U.WRAP, Gravity.CENTER));
    }

    private void error(Runnable retry) {
        body.removeAllViews();
        body.addView(U.message(this, "Could not load products.\nPlease check your internet connection.",
                "Try again", v -> retry.run()), new FrameLayout.LayoutParams(U.MATCH, U.WRAP));
    }

    private void loadCollection() {
        loading();
        Async.run(() -> Api.collection(collection), new Async.Done<List<Product>>() {
            @Override
            public void ok(List<Product> r) {
                for (Product p : r) Store.put(p);
                // Show in-stock items first
                List<Product> list = new ArrayList<>();
                for (Product p : r) if (p.isAvailable()) list.add(p);
                for (Product p : r) if (!p.isAvailable()) list.add(p);
                show(list, "No products in this category yet.");
            }

            @Override
            public void fail(Exception e) {
                error(() -> loadCollection());
            }
        });
    }

    private void showSearch(String q) {
        if (!Store.loaded()) return;
        String query = q.trim().toLowerCase(Locale.ROOT);
        List<Product> out = new ArrayList<>();
        for (Product p : Store.all()) {
            if (query.isEmpty()
                    || p.title.toLowerCase(Locale.ROOT).contains(query)
                    || p.type.toLowerCase(Locale.ROOT).contains(query)) {
                out.add(p);
            }
        }
        show(out, "No products found for \"" + q.trim() + "\".");
    }

    private void show(List<Product> list, String emptyMsg) {
        body.removeAllViews();
        countView.setText(list.size() + (list.size() == 1 ? " product" : " products"));
        if (list.isEmpty()) {
            body.addView(U.message(this, emptyMsg, null, null), new FrameLayout.LayoutParams(U.MATCH, U.WRAP));
            return;
        }
        ScrollView scroll = new ScrollView(this);
        scroll.addView(U.productGrid(this, list, null));
        body.addView(scroll, new FrameLayout.LayoutParams(U.MATCH, U.MATCH));
    }
}
