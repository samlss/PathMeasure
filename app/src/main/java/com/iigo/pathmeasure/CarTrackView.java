package com.iigo.pathmeasure;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

public class CarTrackView extends View {
    private static final String TAG = "CarTrackView";
    private final static float SPEED_RATIO = 0.006f; //控制速度

    private Bitmap carBitmap; //小车bitmap
    private Paint contourPaint; //轮廓画笔
    private float distanceRatio = 0; //距离比例
    private Paint carPaint; //画小车的画笔

    public CarTrackView(Context context) {
        super(context);

        init();
    }

    public CarTrackView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        init();
    }

    public CarTrackView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init();
    }

    private void init(){
        carBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.icon_car);

        contourPaint = new Paint();
        contourPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        contourPaint.setStyle(Paint.Style.STROKE);
        contourPaint.setStrokeWidth(5);

        carPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawColor(Color.WHITE);

        int width = canvas.getWidth();
        int height = canvas.getHeight();

        canvas.translate(width / 2, height / 2); //移动canvas坐标系

        Path path = new Path();  //第一段为直线，第二段为曲线，第三段为直线


        path.moveTo(- width / 2, 0);
        path.lineTo(0, 0);

        path.cubicTo(0, 0, 0, -width / 2 / 2, width / 2 / 2, -width / 2 / 2); //画条三阶贝塞尔曲线

        path.lineTo(width / 2, -width / 2 / 2);

        distanceRatio += SPEED_RATIO;
        if(distanceRatio >=1){
            distanceRatio = 0;
        }

        PathMeasure pathMeasure = new PathMeasure(path, false);
        float distance = pathMeasure.getLength() * distanceRatio;

        Matrix carMatrix = new Matrix();
        pathMeasure.getMatrix(distance, carMatrix, PathMeasure.TANGENT_MATRIX_FLAG | PathMeasure.POSITION_MATRIX_FLAG); //获取距离的坐标和旋转角度
        carMatrix.preTranslate(-carBitmap.getWidth() / 2, -carBitmap.getHeight() / 2);//这里要将设置到小车的中心点

        canvas.drawPath(path, contourPaint); //先画轨迹
        canvas.drawBitmap(carBitmap, carMatrix, carPaint);

        invalidate();
    }
}
