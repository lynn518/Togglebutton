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
 *
 * 自定义控件 : 开关
 */
public class ToggleButton extends CompoundButton {
    private static final String TAG = "ToggleButton";
    //背景画笔(画下面的背景)
    private Paint backgroundPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    //开关画笔(画上面的小圆)
    private Paint togglePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    //背景路径
    private Path backgroundPath = new Path();
    //打开时上面圆的路径
    private Path togglePath_on = new Path();
    //关闭时上面圆的路径
    private Path togglePath_off = new Path();
    //上面圆和底部背景之间的间距
    private float padding =1;
    //onDraw时的路径
    private Path toggleDrawPath = new Path();
    //打开的颜色
    private int onColor;
    //关闭的颜色
    private int offColor;

    public ToggleButton(Context context) {
        this(context, null);
    }

    public ToggleButton(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public void setOffColor(int offColor) {
        this.offColor = offColor;
    }

    public void setOnColor(int onColor) {
        this.onColor = onColor;
    }

    public void setPadding(int padding) {
        this.padding = padding;
    }

    public ToggleButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.setClickable(true);
        initAttrs(attrs);
        initPaints();
    }

    /**
     * 初始化画笔
     */
    private void initPaints() {
        togglePaint.setColor(Color.WHITE);
    }

    /**
     * 初始化attrs
     * @param attrs
     */
    private void initAttrs(AttributeSet attrs){
        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.ToggleButton);
        onColor = a.getColor(R.styleable.ToggleButton_onColor,Color.GREEN);
        offColor = a.getColor(R.styleable.ToggleButton_offColor, Color.LTGRAY);
        a.recycle();
    }


    /**
     * 初始化Paths
     */
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
            toggleDrawPath.set(isChecked()?togglePath_on : togglePath_off);
        }

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        backgroundPaint.setColor(isChecked() ? onColor : offColor);
        canvas.drawPath(backgroundPath, backgroundPaint);
        canvas.drawPath(toggleDrawPath, togglePaint);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);

        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        if(widthMode != MeasureSpec.EXACTLY){
            widthSize = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 50, displayMetrics);
            widthMeasureSpec = MeasureSpec.makeMeasureSpec(widthSize, MeasureSpec.EXACTLY);
        }

        if(heightMode != MeasureSpec.EXACTLY){
            heightSize = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 30, displayMetrics);
            heightMeasureSpec = MeasureSpec.makeMeasureSpec(heightSize, MeasureSpec.EXACTLY);
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        initPath();
    }

    @Override
    public void setChecked(boolean checked) {
        super.setChecked(checked);
        boolean isChecked = isChecked();
        if (toggleDrawPath != null) {
            toggleDrawPath.reset();
            toggleDrawPath.set(isChecked?togglePath_on : togglePath_off);
        }
        invalidate();
    }
}
