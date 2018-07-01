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

public class Loading1View extends View {
    private final static float RADIUS = 200; //圆的半径
    private final static float SPEED_RATIO = 0.006f; //控制速度

    private Bitmap carBitmap;

    private float[] pos = new float[2]; //记录位置
    private float[] tan = new float[2]; //记录切点值xy

    private Path path;
    private PathMeasure pathMeasure; //路径计算
    private float distanceRatio = 0;
    private Paint circlePaint; //画圆圈的画笔
    private Paint carPaint; //画小车的画笔
    private Matrix carMatrix; //针对car bitmap图片操作的矩阵


    public Loading1View(Context context) {
        super(context);

        init();
    }

    public Loading1View(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        init();
    }

    public Loading1View(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init();
    }

    private void init(){
        carBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.icon_car);

        path = new Path();
        path.addCircle(0, 0, RADIUS, Path.Direction.CW);

        pathMeasure = new PathMeasure(path, false);

        circlePaint = new Paint();
        circlePaint.setStrokeWidth(5);
        circlePaint.setStyle(Paint.Style.STROKE);
        circlePaint.setAntiAlias(true);
        circlePaint.setColor(Color.BLACK);

        carPaint = new Paint();
        carPaint.setColor(Color.DKGRAY);
        carPaint.setStrokeWidth(2);
        carPaint.setStyle(Paint.Style.STROKE);

        carMatrix = new Matrix();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawColor(Color.WHITE);
        int width = canvas.getWidth();
        int height = canvas.getHeight();

        //移动canvas坐标系到中心
        canvas.translate(width / 2, height / 2);
        carMatrix.reset();

        distanceRatio += SPEED_RATIO;
        if(distanceRatio >=1){
            distanceRatio = 0;
        }

        float distance = pathMeasure.getLength() * distanceRatio;


        {
            //使用getPosTan方法
            pathMeasure.getPosTan(distance, pos, tan);
            float degree = (float) (Math.atan2(tan[1], tan[0]) * 180 / Math.PI); //计算小车本身要旋转的角度
            carMatrix.postRotate(degree, carBitmap.getWidth() / 2, carBitmap.getHeight() / 2); //设置旋转角度和旋转中心

            //这里要将设置到小车的中心点
            carMatrix.postTranslate(pos[0] - carBitmap.getWidth() / 2, pos[1] - carBitmap.getHeight() / 2);


            //使用getMatrix方法
            //再次重新计算坐标和正切值
            //该方法内部已帮我们实现实现上面的计算,包括坐标点和正切值获得的角度
//            pathMeasure.getMatrix(distance, carMatrix, PathMeasure.TANGENT_MATRIX_FLAG | PathMeasure.POSITION_MATRIX_FLAG); //获取
//            carMatrix.preTranslate(-carBitmap.getWidth() / 2, -carBitmap.getHeight() / 2);//这里要将设置到小车的中心点

            /*
            这里我们讲一下postTranslate和preTranslate的差别，
            Postconcats the matrix with the specified translation. M' = T(dx, dy) * M  代表指定平移之后再进行矩阵拼接
            Preconcats the matrix with the specified translation. M' = M * T(dx, dy) 代表指定平移之前就要进行矩阵拼接

            我们这里，使用getPosTan方法时，调用的是postTranslate：
            这是因为，在调用postRotate接口的时候，已经指定了旋转中心，然后再调用postTranslate进行平移，若调用
            preTranslate的话，则会导致旋转的时候由于平移使旋转中心发生改变，导致小车旋转角度不正确。

            而在调用getMatrix方法时，调用的是preTranslate，这是由于，
            getMatrix将坐标和角度信息拼接到矩阵carMatrix的时候并没有指定旋转中心，因此这里需要先使用preTranslate进行移动和矩阵拼接，
            然后getMatrix内部对carMatrix矩阵赋值进行角度旋转的时候会以移动后的位置为中心
            */
        }


        canvas.drawPath(path, circlePaint);
        canvas.drawBitmap(carBitmap, carMatrix, carPaint);

        invalidate();
    }
}

