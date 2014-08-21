package com.faizmalkani.floatingactionbutton;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.view.animation.TranslateAnimation;
import android.widget.AbsListView;

import com.nineoldandroids.animation.ObjectAnimator;
import com.nineoldandroids.view.ViewHelper;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class FloatingActionButton extends View {

    private final Interpolator mInterpolator = new AccelerateDecelerateInterpolator();
    private final Paint mButtonPaint;
    private final Paint mDrawablePaint;
    private Bitmap mBitmap;
    private int mScreenHeight;
    private int mColor;
    private float mCurrentY;
    private boolean mHidden = false;
    private float currentY;
    private Display display;

    public FloatingActionButton(Context context) {
        this(context, null);
    }

    public FloatingActionButton(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public FloatingActionButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        mButtonPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mDrawablePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.FloatingActionButton);
        mColor = a.getColor(R.styleable.FloatingActionButton_color, Color.WHITE);
        mButtonPaint.setStyle(Paint.Style.FILL);
        mButtonPaint.setColor(mColor);
        float radius, dx, dy;
        radius = a.getFloat(R.styleable.FloatingActionButton_shadowRadius, 10.0f);
        dx = a.getFloat(R.styleable.FloatingActionButton_shadowDx, 0.0f);
        dy = a.getFloat(R.styleable.FloatingActionButton_shadowDy, 3.5f);
        int color = a.getInteger(R.styleable.FloatingActionButton_shadowColor, Color.argb(100, 0, 0, 0));
        mButtonPaint.setShadowLayer(radius, dx, dy, color);

        Drawable drawable = a.getDrawable(R.styleable.FloatingActionButton_drawable);
        if (null != drawable) {
            mBitmap = ((BitmapDrawable) drawable).getBitmap();
        }
        setWillNotDraw(false);
        try {
            this.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        } catch (NoSuchMethodError e2) {
            // http://stackoverflow.com/questions/16990588/setlayertype-substitute-for-android-2-3-3
            try {
                Method setLayerTypeMethod = this.getClass().getMethod(
                        "setLayerType", new Class[] { int.class, Paint.class });
                if (setLayerTypeMethod != null)
                    setLayerTypeMethod.invoke(this, new Object[] { LAYER_TYPE_SOFTWARE, null });
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }

        WindowManager mWindowManager = (WindowManager)
                context.getSystemService(Context.WINDOW_SERVICE);
        display = mWindowManager.getDefaultDisplay();
        Point size = getSize();
        mScreenHeight = size.y;
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    @SuppressWarnings("deprecation")
    protected Point getSize() {
        final Point point = new Point();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            display.getSize(point);
        } else {
            point.x = display.getWidth();
            point.y = display.getHeight();
        }
        return point;
    }

    public void setColor(int color) {
        mColor = color;
        mButtonPaint.setColor(mColor);
        invalidate();
    }

    public void setDrawable(Drawable drawable) {
        mBitmap = ((BitmapDrawable) drawable).getBitmap();
        invalidate();
    }


    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawCircle(getWidth() / 2, getHeight() / 2, (float) (getWidth() / 2.6), mButtonPaint);
        if (null != mBitmap) {
            canvas.drawBitmap(mBitmap, (getWidth() - mBitmap.getWidth()) / 2,
                    (getHeight() - mBitmap.getHeight()) / 2, mDrawablePaint);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int color;
        if (event.getAction() == MotionEvent.ACTION_UP) {
            color = mColor;
        } else {
            color = darkenColor(mColor);
        }
        mButtonPaint.setColor(color);
        invalidate();
        return super.onTouchEvent(event);
    }

    public void hide(boolean hide) {
        try {
            if (mHidden == hide) {
                currentY = ViewHelper.getY(this);
                com.nineoldandroids.animation.ObjectAnimator mHideAnimation = com.nineoldandroids.animation.ObjectAnimator.ofFloat(this,
                        "Y", mScreenHeight);
                mHideAnimation.setInterpolator(new AccelerateInterpolator());
                mHideAnimation.start();
            }
        } catch (Exception e) {
            currentY = ViewHelper.getY(this);
            Animation animation = new TranslateAnimation(0.0f, 0.0f, 0.0f,
                    currentY);
            animation.setDuration(1000);
            animation.setFillAfter(true);
            startAnimation(animation);
            setVisibility(View.GONE);
        }
        mHidden = true;
    }

    public void show(boolean hide) {
        try {
            if (mHidden == hide) {
                ObjectAnimator mShowAnimation = ObjectAnimator.ofFloat(this,
                        "Y", currentY);
                mShowAnimation.setInterpolator(new DecelerateInterpolator());
                mShowAnimation.start();

            }
        } catch (Exception e) {
            setVisibility(View.VISIBLE);
            currentY = ViewHelper.getY(this);
            Animation animation = new TranslateAnimation(0.0f, 0.0f, currentY,
                    0.0f);
            animation.setDuration(500);
            this.startAnimation(animation);
        }
        mHidden = false;
    }

    public void listenTo(AbsListView listView) {
        if (null != listView) {
            listView.setOnScrollListener(new DirectionScrollListener(this));
        }
    }

    public static int darkenColor(int color) {
        float[] hsv = new float[3];
        Color.colorToHSV(color, hsv);
        hsv[2] *= 0.8f;
        return Color.HSVToColor(hsv);
    }
}