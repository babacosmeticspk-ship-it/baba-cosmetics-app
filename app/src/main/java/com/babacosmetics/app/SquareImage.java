package com.babacosmetics.app;

import android.content.Context;
import android.widget.ImageView;

/** ImageView that is always as tall as it is wide. */
public class SquareImage extends ImageView {
    public SquareImage(Context c) {
        super(c);
    }

    @Override
    protected void onMeasure(int widthSpec, int heightSpec) {
        int w = MeasureSpec.getSize(widthSpec);
        super.onMeasure(MeasureSpec.makeMeasureSpec(w, MeasureSpec.EXACTLY),
                MeasureSpec.makeMeasureSpec(w, MeasureSpec.EXACTLY));
    }
}
