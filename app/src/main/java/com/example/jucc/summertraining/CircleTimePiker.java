package com.example.jucc.summertraining;

/**
 * Created by xiaoqiang on 2016/7/15.
 */

import android.content.Context;
import android.content.Loader;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import static android.R.attr.max;
import static android.R.attr.radius;
import static android.graphics.Paint.Style.STROKE;
import static com.example.jucc.summertraining.R.attr.roundProgressColor;
import static com.example.jucc.summertraining.R.attr.roundWidth;
import static com.example.jucc.summertraining.R.attr.style;


public class CircleTimePiker extends View {

    private static final double RADIAN = 180 / Math.PI;
    /**
     * 自身的宽高
     */
    int mWidth = 0;
    int mHeight = 0;

    /**
     * 圆环半径
     */
    private float mCircleRadius = 0;


    /**
     * 数字显示的位置距离圆心坐标的距离
     */
    private float mCircleInnerRadius = 0;

    /**
     * 圆心半径
     */
    //private float mCircleCenterRadius = 0;

    /**
     * 圆心颜色
     */
    private double mCurAngle = 0;

    /**
     * 指针宽度
     */
    private int mCirclePointWidth = 0;

    /**
     * 指针颜色
     */
    private int mCirclePointColor = 0;

    /**
     * 选中项下的圆形背景半径
     */
    private int mCircleTargetRadius = 0;

    /**
     * 选中项下的圆形背景颜色
     */
    private int mCircleTargetColor = 0;

    /**
     * 边距
     */
    private int margin = 0;

    /**
     * 画笔
     */
    private Paint mPaint = null;

    /**
     * 一圈有多少个选择项
     */
    private int mCircleCount = 12;

    /**
     * 相邻两个选项间的弧度
     */
    private float mSingleRadian = 0;


    /**
     * 中心点坐标
     */
    private Point mCenterPoint = null;

    /**
     * 无效的位置
     */
    private final int INVALID_POSITION = -1;

    /**
     * 当前选择的位置
     */
    private int mSelectPosition = 2;


    /**
     * 环形时间选择器的数据适配器
     */
    private TimeAdapter mAdapter;

    private OnSelectionChangeListener mSelectionChangeListener;

    /* =================get/set方法====================== */


    public void setmCenterPoint(Point CenterPoint) {
        mCenterPoint = CenterPoint;
    }

    public Point getmCenterPoint() {
        return mCenterPoint;
    }

    public float getmCircleRadius() {
        return mCircleRadius;
    }

    /**
     * 设置一圈有多少个item
     */
    public void setCircleCount(int cirlceCount) {
        mCircleCount = cirlceCount;
        reset();
    }

    /**
     * 设置数据适配器
     */
    public void setAdapter(TimeAdapter adapter) {
        mAdapter = adapter;
        setCircleCount(mAdapter.getCount());
    }

    /**
     * 设置位置改变的监听
     */
    public void setOnSelectionChangeListener(
            OnSelectionChangeListener selectionChangeListener) {
        mSelectionChangeListener = selectionChangeListener;

    }

    public CircleTimePiker(Context context) {
        this(context, null);
    }

    public CircleTimePiker(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CircleTimePiker(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        TypedArray a = context.obtainStyledAttributes(attrs,
                R.styleable.Circle_Selector);
//        mCircleCenterColor = a.getColor(
//                R.styleable.Circle_Selector_circle_center_color, Color.BLUE);
        mCirclePointColor = a.getColor(
                R.styleable.Circle_Selector_circle_point_color, Color.RED);
        mCircleTargetColor = a.getColor(
                R.styleable.Circle_Selector_circle_center_color, Color.GREEN);
//        mCircleCenterRadius = a.getInt(
//                R.styleable.Circle_Selector_circle_center_radius, 0);
        mCirclePointWidth = a.getInt(
                R.styleable.Circle_Selector_circle_point_width, 0);
        mCircleTargetRadius = a.getInt(
                R.styleable.Circle_Selector_circle_target_radius, 40);
        a.recycle();


        init();
    }

    /*
    **初始化mPaint
     */
    private void init() {
        mCenterPoint = new Point();
        mPaint = new Paint();
        mPaint.setAlpha(80);
        mPaint.setAntiAlias(true);
        mPaint.setColor(Color.CYAN);
        reset();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        mWidth = w;
        mHeight = h;
        // 计算出中心坐标值
        mCenterPoint.x = w / 2;
        mCenterPoint.y = h / 2;
        // 计算圆形半径
        int minSize = w > h ? h : w;
        mCircleRadius = (minSize - 2 * margin + 100) / 2;
        // 圆心半径
        //  mCircleCenterRadius = mCircleRadius * .05f;
        mCircleInnerRadius = mCircleRadius * .8f;
        super.onSizeChanged(w, h, oldw, oldh);
    }

    /*
    监听触摸事件,如果事件在圆圈内，则放弃事件
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        float x = event.getX() - mCenterPoint.x;
        float y = event.getY() - mCenterPoint.y;
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                if (x * x + y * y <= (mCircleRadius - 180) * (mCircleRadius - 180))
                    return false;
            case MotionEvent.ACTION_MOVE:
                if (x * x + y * y >= (mCircleRadius - 180) * (mCircleRadius - 180)) {
                    getTouchPositionFromPoint(event.getX(), event.getY());
                }
                break;
        }


        return true;

    }

    private void reset() {
        // 计算相邻两个item之间的弧度距离
        mSingleRadian = (float) ((2 * Math.PI) / mCircleCount);
        mSelectPosition = mCircleCount;
    }

    /**
     * 根据点击的坐标点获取是点击在哪个position的区域
     *
     * @param x
     * @param y
     * @return
     */
    public int getTouchPositionFromPoint(float x, float y) {
        double radians = getRadian(x, y);
        float cos = computeCos(x, y);
        System.out.println("===radians===" + radians);

        if (x < getWidth() / 2) { // 滑动超过180度
            mCurAngle = Math.PI * RADIAN + Math.acos(cos) * RADIAN;
        } else { // 没有超过180度
            mCurAngle = Math.PI * RADIAN - Math.acos(cos) * RADIAN;
        }
        if (radians <= mSingleRadian / 2
                || radians >= (2 * Math.PI) - mSingleRadian / 2) {
            // 顶上最中央的地方需要特殊处理。
            setSelection(mCircleCount);

            return mCircleCount;
        }

        // 最小与最大弧度边界
        double minRadianRange = 0;
        double maxRadianRange = 0;
        for (int i = 1; i <= mCircleCount; i++) {
            minRadianRange = mSingleRadian * (i - .5);
            maxRadianRange = mSingleRadian * (i + .5);
            if (radians > minRadianRange && radians <= maxRadianRange) {
                setSelection(i);
                return i;
            }
        }
        return mSelectPosition;
    }

    /**
     * 设置选中的位置
     *
     * @param position
     */
    public void setSelection(int position) {
        if (mSelectionChangeListener != null) {
            mSelectionChangeListener
                    .onPositionChange(position, mSelectPosition);
        }
        mSelectPosition = position;

        invalidate();

    }

    /**
     * 获取当前的弧度
     *
     * @param x
     * @param y
     * @return
     */
    private double getRadian(float x, float y) {
        float disX = x - mCenterPoint.x;
        float disY = mCenterPoint.y - y;
        double radians = 0;
        if (disX > 0 && disY > 0) {
            // 第一象限
            radians = Math.atan(disX / disY);
        } else if (disX > 0 && disY < 0) {
            // 第二象限
            radians = Math.atan(disY / -disX);
            radians += Math.PI / 2;
        } else if (disX < 0 && disY <= 0) {
            // 第三象限
            radians = Math.atan(-disX / -disY);
            radians += Math.PI;
        } else if (disX < 0 && disY >= 0) {
            // 第四象限
            radians = Math.atan(disY / -disX);
            radians += Math.PI * 3 / 2;
            // 以下是点击的坐标点在以圆心为坐标原点的坐标轴上的情况
        } else if (disX == 0 && disY > 0) {
            // 在Y正轴上
            radians = 0;
        } else if (disX == 0 && disY < 0) {
            // 在Y负轴上
            radians = Math.PI;
        } else if (disX > 0 && disY == 0) {
            // 在X正轴上
            radians = Math.PI / 2;
        } else if (disX < 0 && disY == 0) {
            // 在X负轴上
            radians = Math.PI * 2 / 3;
        }
        return radians;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        // 先画背景的圆
        // drawOutterCircle(canvas);
        // 画圆心
        //   drawCircleCenter(canvas);
        // 画指针
        drawPoint(canvas);
        // 画圆周的条目
        // drawCircleItem(canvas);
        drawCircleSelected(canvas);

    }

    /**
     * 画指针
     */
    private void drawPoint(Canvas canvas) {
        float x = 0;
        float y = 0;
        if (mSelectPosition != INVALID_POSITION) {
            mPaint.setColor(mCirclePointColor);
            mPaint.setStrokeWidth(mCirclePointWidth);
            x = mCenterPoint.x
                    + (int) (mCircleInnerRadius * Math.sin(mSingleRadian
                    * mSelectPosition));
            y = mCenterPoint.y
                    - (int) (mCircleInnerRadius * Math.cos(mSingleRadian
                    * mSelectPosition));
            //  canvas.drawLine(mCenterPoint.x, mCenterPoint.y, x, y, mPaint);
            //   mPaint.setColor(mCircleCenterColor);
            //   canvas.drawCircle(mCenterPoint.x, mCenterPoint.y,
            //     mCircleCenterRadius, mPaint);
            mPaint.setColor(mCircleTargetColor);
            mPaint.setStyle(Paint.Style.FILL);
            canvas.drawCircle(x, y, mCircleTargetRadius, mPaint);

        }
    }

    /**
     * 画圆周条目
     */
//    private void drawCircleItem(Canvas canvas) {
//        mPaint.setColor(Color.BLUE);
//        int txtSize = dip2px(getContext(), 15);
//        System.out.println("==txtSize=" + txtSize);
//        mPaint.setTextSize(txtSize);
//        float x = 0;
//        float y = 0;
//        // 绘制的item文字距离圆心的位置的长度
//        mPaint.setColor(Color.BLUE);
//        for (int i = 1; i <= mCircleCount; i++) {
//            String text = mAdapter == null ? ("" + i) : mAdapter
//                    .getNameByPosition(i);
//            float textW = mPaint.measureText(text);
//            x = (int) (mCenterPoint.x + mCircleInnerRadius
//                    * Math.sin(mSingleRadian * i) - textW / 2);
//            y = (int) (mCenterPoint.y - mCircleInnerRadius
//                    * Math.cos(mSingleRadian * i) + txtSize / 2);
//            canvas.drawText(text, x, y, mPaint);
//        }
//    }


    //根据已经选择的部分画出圆弧
    private void drawCircleSelected(Canvas canvas) {
        float left = getPaddingLeft() + (getWidth() - mCircleRadius * 2) / 2 + ((float) mCircleTargetRadius) * (float) 1.5 + 40;
        float top = getPaddingTop() + (getHeight() - mCircleRadius * 2) / 2 + ((float) mCircleTargetRadius) * (float) 1.5 + 40;
        float right = canvas.getWidth() - getPaddingRight() - (getWidth() - mCircleRadius * 2) / 2 - ((float) mCircleTargetRadius) * (float) 1.5 - 40;
        float bottom = canvas.getHeight() - getPaddingBottom() - (getHeight() - mCircleRadius * 2) / 2 - ((float) mCircleTargetRadius) * (float) 1.5 - 40;
        mPaint.setStrokeWidth(((float) mCircleTargetRadius) * (float) 1.2); //设置圆环的宽度
        mPaint.setColor(mCircleTargetColor);  //设置进度的颜色
        RectF oval = new RectF(left, top, right, bottom);  //用于定义的圆弧的形状和大小的界限
        mPaint.setStyle(Paint.Style.STROKE);
        canvas.drawArc(oval, -90, (float) mCurAngle, false, mPaint);


    }

    /**
     * 画圆心
     */
 /*   private void drawCircleCenter(Canvas canvas) {
        mPaint.setColor(mCircleCenterColor);
        canvas.drawCircle(mCenterPoint.x, mCenterPoint.y, mCircleCenterRadius,
                mPaint);
    }

    /**
     * 画背景的圆
     */
    private void drawOutterCircle(Canvas canvas) {
        mPaint.setColor(Color.GRAY);
        canvas.drawCircle(mCenterPoint.x, mCenterPoint.y, mCircleRadius, mPaint);
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    public interface OnSelectionChangeListener {

        void onPositionChange(int newPositoin, int oldPosition);


    }

    public interface TimeAdapter {
        int getCount();

        String getNameByPosition(int position);

    }


    //计算cos值
    private float computeCos(float x, float y) {
        float width = x - getWidth() / 2;
        float height = y - getHeight() / 2;
        float slope = (float) Math.sqrt(width * width + height * height);
        return height / slope;
    }


    //画出一条鱼
//    private void drawFish(Canvas canvas){
//        //bitMap= BitmapFactory.decodeResource(getResources(),R.drawable.test);
//        float left = getPaddingLeft() +(getWidth()-mCircleRadius*2)/2+((float)mCircleTargetRadius)*(float)1.5;
//        float top = getPaddingTop()+(getHeight()-mCircleRadius*2)/2+((float)mCircleTargetRadius)*(float)1.5;
//        float right = canvas.getWidth() - getPaddingRight()-(getWidth()-mCircleRadius*2)/2-((float)mCircleTargetRadius)*(float)1.5;
//        float bottom = canvas.getHeight() - getPaddingBottom()-(getHeight()-mCircleRadius*2)/2-((float)mCircleTargetRadius)*(float)1.5;
//        RectF rectf=new RectF(left,top,right,bottom);
//        Rect rect=new Rect((int)left,(int)top,(int)right,(int)bottom);
//        canvas.drawBitmap(bitMap,null,rectf,mPaint);
//        isDraw=true;
//    }


}
