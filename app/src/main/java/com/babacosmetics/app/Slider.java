package com.babacosmetics.app;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.util.List;

/** Swipeable image slider with dots (used for banners and product photos). */
public class Slider extends FrameLayout {

    public interface OnSlideClick {
        void click(int index);
    }

    private final List<String> urls;
    private final ImageView image;
    private final LinearLayout dots;
    private final boolean auto;
    private final boolean fitRatioToImage;
    private final int loadWidth;
    private final Handler handler = new Handler(Looper.getMainLooper());
    private final GestureDetector detector;
    private float ratio;
    private int index = 0;
    private OnSlideClick listener;

    private final Runnable tick = new Runnable() {
        @Override
        public void run() {
            show(index + 1);
            handler.postDelayed(this, 4500);
        }
    };

    public Slider(Context c, List<String> urls, boolean auto, float ratio, boolean fitRatioToImage, int loadWidth) {
        super(c);
        this.urls = urls;
        this.auto = auto;
        this.ratio = ratio;
        this.fitRatioToImage = fitRatioToImage;
        this.loadWidth = loadWidth;

        image = new ImageView(c);
        image.setScaleType(ImageView.ScaleType.FIT_CENTER);
        addView(image, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));

        dots = new LinearLayout(c);
        dots.setOrientation(LinearLayout.HORIZONTAL);
        LayoutParams dlp = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT,
                Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL);
        dlp.bottomMargin = U.dp(c, 8);
        addView(dots, dlp);
        if (urls.size() > 1) {
            for (int i = 0; i < urls.size(); i++) {
                View d = new View(c);
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(U.dp(c, 7), U.dp(c, 7));
                lp.leftMargin = lp.rightMargin = U.dp(c, 3);
                dots.addView(d, lp);
            }
        }

        detector = new GestureDetector(c, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onDown(MotionEvent e) {
                return true;
            }

            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                if (listener != null) listener.click(index);
                return true;
            }

            @Override
            public boolean onScroll(MotionEvent e1, MotionEvent e2, float dx, float dy) {
                if (Math.abs(dx) > Math.abs(dy) && getParent() != null) {
                    getParent().requestDisallowInterceptTouchEvent(true);
                }
                return false;
            }

            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float vx, float vy) {
                if (Math.abs(vx) > Math.abs(vy) && Math.abs(vx) > 300) {
                    show(vx < 0 ? index + 1 : index - 1);
                    restartAuto();
                    return true;
                }
                return false;
            }
        });

        show(0);
    }

    public void setOnSlideClick(OnSlideClick l) {
        listener = l;
    }

    public void show(int i) {
        if (urls.isEmpty()) return;
        index = (i % urls.size() + urls.size()) % urls.size();
        ImageLoader.load(image, Api.sized(urls.get(index), loadWidth), b -> {
            if (fitRatioToImage && b.getWidth() > 0) {
                float r = (float) b.getHeight() / b.getWidth();
                if (Math.abs(r - ratio) > 0.01f) {
                    ratio = Math.max(0.25f, Math.min(r, 1.4f));
                    requestLayout();
                }
            }
        });
        for (int d = 0; d < dots.getChildCount(); d++) {
            dots.getChildAt(d).setBackground(U.round(d == index ? Config.BRAND_DARK : 0x55FFFFFF,
                    U.dp(getContext(), 4), d == index ? 0 : 0x33000000, d == index ? 0 : 1));
        }
    }

    private void restartAuto() {
        handler.removeCallbacks(tick);
        if (auto && urls.size() > 1) handler.postDelayed(tick, 4500);
    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        detector.onTouchEvent(e);
        if (e.getAction() == MotionEvent.ACTION_UP || e.getAction() == MotionEvent.ACTION_CANCEL) {
            if (getParent() != null) getParent().requestDisallowInterceptTouchEvent(false);
        }
        return true;
    }

    @Override
    protected void onMeasure(int widthSpec, int heightSpec) {
        int w = MeasureSpec.getSize(widthSpec);
        int h = Math.round(w * ratio);
        super.onMeasure(MeasureSpec.makeMeasureSpec(w, MeasureSpec.EXACTLY),
                MeasureSpec.makeMeasureSpec(h, MeasureSpec.EXACTLY));
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        restartAuto();
    }

    @Override
    protected void onDetachedFromWindow() {
        handler.removeCallbacks(tick);
        super.onDetachedFromWindow();
    }
}
