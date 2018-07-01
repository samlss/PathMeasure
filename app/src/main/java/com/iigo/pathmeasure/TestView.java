package com.iigo.pathmeasure;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

public class TestView extends View {
    private final String TAG = "TestView";
    private Paint paint;
    private Path path;

    public TestView(Context context) {
        super(context);
        init();
    }

    public TestView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public TestView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init(){
        //初始化画笔
        paint = new Paint();
        paint.setStrokeWidth(6);
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(Color.RED);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawColor(Color.WHITE); //设置背景为白色

        int centerX = canvas.getWidth() / 2;
        int centerY = canvas.getHeight() / 2;

        //画坐标系
        canvas.save();
        Paint coorPaint = new Paint();
        coorPaint.setStyle(Paint.Style.STROKE);
        coorPaint.setColor(Color.BLACK);
        coorPaint.setStrokeWidth(1);
        canvas.drawLine(0, centerY, canvas.getWidth(), centerY, coorPaint);
        canvas.drawLine(centerX, 0, centerX, canvas.getHeight(), coorPaint);
        canvas.restore();
        //=========================================

        //测试闭合/不闭合效果
//        canvas.save();
//        canvas.translate(centerX, centerY); //移动canvas坐标系到中心点
//
//        path = new Path();
//        path.lineTo( - 200, - 200); //画一条线
//        path.lineTo(-200, 200); //画第二条线
//
//        PathMeasure pathMeasure1 = new PathMeasure(path, false); //不闭合测量
//        PathMeasure pathMeasure2 = new PathMeasure(path, true); //闭合测量
//
//        float length1 = pathMeasure1.getLength(); //获取轮廓长度
//        float length2 = pathMeasure2.getLength(); //获取轮廓长度
//
//        Log.e(TAG, "pathMeasure1 length: " + length1);
//        Log.e(TAG, "pathMeasure2 length: " + length2);
//
//        //        path.close(); //代表path本身闭合
//        canvas.drawPath(path, paint); //绘制原path轮廓
//
//        //画不闭合
////        for (float dis = 0; dis < length1; dis++){
////            float pos[] = new float[2];
////            float tan[] = new float[2];
////
////            pathMeasure1.getPosTan(dis, pos, tan);
////            canvas.drawPoint(pos[0], pos[1], paint);
////        }
//
//        //画闭合
//        for (float dis = 0; dis < length2; dis++){
//            float pos[] = new float[2];
//            float tan[] = new float[2];
//
//            pathMeasure2.getPosTan(dis, pos, tan);
//            canvas.drawPoint(pos[0], pos[1], paint);
//        }
//
//
//        canvas.restore();
        //=============================================




        //画两个矩形测试path轮廓的添加顺序对nextContour的影响
//        canvas.save();
//        canvas.translate(centerX, centerY); //移动canvas坐标系到中心点
//
//        path = new Path();
//        path.addRect(-100, -100, 100, 100, Path.Direction.CW);  // 添加周长为800的第一个矩形
//        canvas.drawPath(path, paint); //先画一个矩形
//
//        path.addRect(-200, -200, 200, 200, Path.Direction.CW);  // 添加周长为1600的第二个矩形
//        canvas.drawPath(path, paint); //再画一个矩形
//        canvas.restore();
//
//        //不闭合
//        PathMeasure pathMeasure = new PathMeasure(path, false);
//
//        //我们这里添加了两个轮廓，因此打印了两次
//        while(pathMeasure.nextContour()){
//            Log.e(TAG, "getLength: "+ pathMeasure.getLength());
//        }
        //==========================================================================================
    }
}
