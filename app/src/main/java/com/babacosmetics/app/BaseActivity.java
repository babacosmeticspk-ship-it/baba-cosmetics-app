package com.babacosmetics.app;

import android.app.Activity;
import android.os.Bundle;

public abstract class BaseActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Cart.init(this);
    }
}
