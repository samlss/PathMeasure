package com.iigo.pathmeasure;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.DecelerateInterpolator;

public class Loading2View extends View {
    private final static float RADIUS = 150; //圆的半径

    private Path path;
    private PathMeasure pathMeasure; //路径计算
    private Paint paint; //画笔
    private float pathDistanceRatio; //路径长度的比值 (0 - 1)

    public Loading2View(Context context) {
        super(context);
        init();
    }

    public Loading2View(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public Loading2View(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init(){
        path = new Path();
        path.addCircle(0, 0, RADIUS, Path.Direction.CW);

        pathMeasure = new PathMeasure(path, false);

        paint = new Paint();
        paint.setStrokeWidth(10);
        paint.setStyle(Paint.Style.STROKE);
        paint.setAntiAlias(true);
        paint.setColor(Color.BLACK);

        //计算比例的动画
        ValueAnimator ratioAnimator = ValueAnimator.ofFloat(0, 1);
        ratioAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                pathDistanceRatio = (float) valueAnimator.getAnimatedValue();
                invalidate();
            }
        });
        ratioAnimator.setDuration(1500);
        ratioAnimator.setRepeatMode(ValueAnimator.RESTART);
        ratioAnimator.setRepeatCount(ValueAnimator.INFINITE);
        ratioAnimator.setInterpolator(new DecelerateInterpolator());
        ratioAnimator.start();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawColor(Color.WHITE);
        int width = canvas.getWidth();
        int height = canvas.getHeight();

        canvas.translate(width / 2, height / 2); //移动canvas坐标系到中心点

        float stopD = pathMeasure.getLength() * pathDistanceRatio; //当前截取的结束点
        float startD = (float) (stopD - ((0.5 - Math.abs(pathDistanceRatio - 0.5)) * pathMeasure.getLength())); //当前截取的开始点
        Path dst = new Path();
        dst.moveTo(RADIUS, 0); //移动起始点

        pathMeasure.getSegment(startD, stopD, dst, true);
        canvas.drawPath(dst, paint);
//        testGetSegment(canvas, true);
    }

    /**
     * 测试getSegment函数
     *
     * @param canvas 画布
     * @param startWithMoveTo 起始点是否使用moveTo的点
     */
    private void testGetSegment(Canvas canvas, boolean startWithMoveTo) {
        Path path = new Path();
        //添加一个觉醒
        path.addRect(-200, -200, 200, 200, Path.Direction.CW);

        Path dst = new Path();
        //原本存在的轮廓
        dst.lineTo(-300, -300);

        // 将Path 与 PathMeasure 关联
        PathMeasure measure = new PathMeasure(path, false);

        // 截取一部分轮廓存入dst中，并设置moveTo保持截取得到的 Path 第一个点的位置是否不变，取决于startWithMoveTo
        measure.getSegment(300, 600, dst, startWithMoveTo);

        //原图画笔
        Paint srcPaint = new Paint();
        srcPaint.setStyle(Paint.Style.STROKE);
        srcPaint.setStrokeWidth(5);
        srcPaint.setColor(Color.RED);

        //截取后的图的画笔
        Paint dstPaint = new Paint();
        dstPaint.setStyle(Paint.Style.STROKE);
        dstPaint.setStrokeWidth(5);
        dstPaint.setColor(Color.GREEN);

        //绘制原图
        canvas.drawPath(path,srcPaint);
        // 绘制 dst
        canvas.drawPath(dst, dstPaint);
    }
}
