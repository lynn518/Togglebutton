package com.lynn518.togglebutton;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.widget.CompoundButton;


/**
 * Created by ZL on 16/1/14.
 * 自定义控件 : 开关
 */
public class ToggleButton extends CompoundButton {
    private static final String TAG = "ToggleButton";
    private Paint backgroundPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint togglePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Path backgroundPath = new Path();
    private Path togglePath_on = new Path();
    private Path togglePath_off = new Path();
    private float padding;
    private Path toggleDrawPath = new Path();
    private int color_on;
    private int color_off;

    public ToggleButton(Context context) {
        this(context, null);
    }

    public ToggleButton(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ToggleButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.setClickable(true);
        initAttrs(attrs);
        initPaints();
    }

    public void setColor_off(int color_off) {
        this.color_off = color_off;
    }

    public void setColor_on(int color_on) {
        this.color_on = color_on;
    }

    public void setPadding(int padding) {
        this.padding = padding;
    }

    private void initPaints() {
        int color = isChecked() ? color_on : color_off;
        backgroundPaint.setColor(color);
        togglePaint.setColor(Color.WHITE);
    }

    private void initAttrs(AttributeSet attrs) {
        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.ToggleButton);
        color_on = a.getColor(R.styleable.ToggleButton_onColor, Color.GREEN);
        color_off = a.getColor(R.styleable.ToggleButton_offColor, Color.LTGRAY);
        padding = a.getDimension(R.styleable.ToggleButton_padding, 1);
        a.recycle();
    }

    private void initPath() {
        int height = getHeight();
        int width = getWidth();
        if (height > 0 && width > 0) {
            int radius = Math.min(width, height) / 2;
            backgroundPath.reset();
            togglePath_on.reset();
            togglePath_off.reset();
            RectF left = new RectF(0, 0, 2 * radius, 2 * radius);
            RectF right = new RectF(width - 2 * radius, 0, width, height);
            backgroundPath.addArc(left, 90, 180);
            backgroundPath.addRect(radius, 0, width - radius, height, Path.Direction.CCW);
            backgroundPath.addArc(right, 270, 180);

            RectF on = new RectF(width - 2 * radius - padding, padding, width - padding, height - padding);
            RectF off = new RectF(padding, padding, 2 * radius - padding, 2 * radius - padding);
            togglePath_on.addArc(on, 90, 360);
            togglePath_off.addArc(off, 90, 360);
            toggleDrawPath.set(isChecked() ? togglePath_on : togglePath_off);
        }

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawPath(backgroundPath, backgroundPaint);
        canvas.drawPath(toggleDrawPath, togglePaint);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        final int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        final int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        if (widthMode != MeasureSpec.EXACTLY) {
            int widthSize = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 50, displayMetrics);
            widthMeasureSpec = MeasureSpec.makeMeasureSpec(widthSize, MeasureSpec.EXACTLY);
        }
        if (heightMode != MeasureSpec.EXACTLY) {
            int heightSize = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 30, displayMetrics);
            heightMeasureSpec = MeasureSpec.makeMeasureSpec(heightSize, MeasureSpec.EXACTLY);
        }
        setMeasuredDimension(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        initPath();
    }

    @Override
    public void setChecked(boolean checked) {
        super.setChecked(checked);
        if (backgroundPaint != null) {
            backgroundPaint.setColor(checked ? color_on : color_off);
        }
        if (toggleDrawPath != null) {
            toggleDrawPath.reset();
            toggleDrawPath.set(checked ? togglePath_on : togglePath_off);
        }
        invalidate();
    }

}
