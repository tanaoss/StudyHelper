package com.hfut.studyhelper;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

public class RoundImageView extends View {
    private int mBorderColor = Color.TRANSPARENT;
    //要显示的图像
    private Drawable mDrawable;

    //图像的宽
    private int mBitmapWidth;
    //图像的高
    private int mBitmapHeight;
    //画圆形图像时，半径的位置
    private float mDrawableRadius;
    //画边框时，半径的位置
    private float mBorderRadius;
    //边框的宽度
    private float mBorderStrokeWidth=1f;

    //着色器，这是画出圆形图像的关键
    private BitmapShader mBitmapShader;

    //用于画出圆形图像的画笔
    private Paint mBitmapPaint;
    //用于画出图像边界面的画笔
    private Paint mBorderPaint;


    public RoundImageView(Context context) {
        super(context);
        init(null, 0);
    }

    public RoundImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public RoundImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }

    //初始化
    private void init(AttributeSet attrs, int defStyle) {
        //准备获取自定义属性的值
        final TypedArray a = getContext().obtainStyledAttributes(
                attrs, R.styleable.RoundImageView, defStyle, 0);

        //获取文本的颜色
        mBorderColor = a.getColor(
                R.styleable.RoundImageView_borderColor,
                mBorderColor);
        //获取边框的宽度（像素）
        mBorderStrokeWidth = a.getDimension(
                R.styleable.RoundImageView_borderWidth,
                mBorderStrokeWidth);

        if (a.hasValue(R.styleable.RoundImageView_drawable)) {
            //获取图像
            mDrawable = a.getDrawable(R.styleable.RoundImageView_drawable);
        }

        //不再需要从attrs获取属性值了，及时释放一些资源
        a.recycle();

        if(mDrawable != null) {
            //从Drawable对象获取Bitmap对象。用于创建BitmapShader
            //Drawable只是Android SDK对于可绘制对象的封装，
            //底层的图像绘制使用的是Bitmap（位图或光栅图）
            Bitmap bitmap = getBitmapFromDrawable(mDrawable);
            if(bitmap==null){
                return;
            }

            //保留下图像的宽和高
            mBitmapWidth = bitmap.getWidth();
            mBitmapHeight = bitmap.getHeight();

            //创建着色器，第二和第三个参数指明了图像的平铺模式，可以参考Windows背景的平铺模式
            //这设置成不平铺
            mBitmapShader = new BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);

            //创建画位图的画笔
            mBitmapPaint = new Paint();
            //把着色器设置给画笔
            mBitmapPaint.setShader(mBitmapShader);

            //创建画边框的画笔
            mBorderPaint = new Paint();
            //只画线不填充
            mBorderPaint.setStyle(Paint.Style.STROKE);
            //画边框需要平滑效果
            mBorderPaint.setFlags(Paint.ANTI_ALIAS_FLAG);
            //设置边框的颜色
            mBorderPaint.setColor(mBorderColor);
            //设置边框的线条粗细
            mBorderPaint.setStrokeWidth(mBorderStrokeWidth);
        }
    }


    @Override
    protected void onDraw(Canvas canvas) {
        Log.i("SLogin","Sda");
        //super.onDraw(canvas);
        if(mDrawable == null){
            return;
        }

        //画背景
        canvas.drawCircle(getWidth() / 2f,
                getHeight() / 2f, mBorderRadius, mBorderPaint);
        //画图像
        canvas.drawCircle(getWidth() / 2f,
                getHeight() / 2f, mDrawableRadius, mBitmapPaint);
    }


    public Drawable getDrawable() {
        return mDrawable;
    }

    public void setDrawable(Drawable drawable){
        this.mDrawable= drawable;
        //从Drawable对象获取Bitmap对象。用于创建BitmapShader
        Bitmap bitmap = getBitmapFromDrawable(mDrawable);
        //保留下图像的宽和高
        mBitmapWidth = bitmap.getWidth();
        mBitmapHeight =bitmap.getHeight();
//创建着色器，第二和第三个参数指明了图像的平铺模式，可以参考Windows背景的平铺模式
        //这设置成不平铺
        mBitmapShader = new BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);

        //创建画位图的画笔
        mBitmapPaint = new Paint();
        //把着色器设置给画笔
        mBitmapPaint.setShader(mBitmapShader);

        //创建画边框的画笔
        mBorderPaint = new Paint();
        //只画线不填充
        mBorderPaint.setStyle(Paint.Style.STROKE);
        //画边框需要平滑效果
        mBorderPaint.setFlags(Paint.ANTI_ALIAS_FLAG);
        //设置边框的颜色
        mBorderPaint.setColor(mBorderColor);
        //设置边框的线条粗细
        mBorderPaint.setStrokeWidth(mBorderStrokeWidth);
        //重新计算位图着色器的变换矩阵
        updateShaderMatrix();
        //发出通知，强制系统重新绘制控件（图像都变了，当然要重新绘制了）
        invalidate();
    }

    //从Drawable获取Bitmap
    private Bitmap getBitmapFromDrawable(Drawable drawable) {
        if (drawable == null) {
            return null;
        }

        if (drawable instanceof BitmapDrawable) {
            //判断对象类型，如果传入的是一个位图Drawable，直接获取位图并返回
            return ((BitmapDrawable) drawable).getBitmap();
        }

        //如果不是位图图像（参考res/drawable下的各种资源），处理就复杂一点
        try {
            Bitmap bitmap;

            if (drawable instanceof ColorDrawable) {
                //如果是一个颜色，则创建一个宽和高都是一个像素的Bitmap，
                //指定其颜色空间是ARGB四通道，每个通道占8个字节。
                bitmap = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888);
            } else {
                //如果是其它类型的Drawable，则创建一个与它同样大小的位图
                bitmap = Bitmap.createBitmap(
                        drawable.getIntrinsicWidth(),
                        drawable.getIntrinsicHeight(),
                        Bitmap.Config.ARGB_8888);
            }

            //位图中必须要有drawable中的图案，所以用位图创建画布，
            //把drawable画到画布上，实际上就画到了位图上
            Canvas canvas = new Canvas(bitmap);
            //设置绘画的区域，绘制不会超过这个区域
            drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
            drawable.draw(canvas);
            return bitmap;
        } catch (OutOfMemoryError e) {
            //如果内存不够用，返回null
            return null;
        }
    }

    //计算位图的变换矩阵
    private void updateShaderMatrix() {
        //获取控件的上下左右padding，用于计算内容所处的区域
        int paddingLeft = getPaddingLeft();
        int paddingTop = getPaddingTop();
        int paddingRight = getPaddingRight();
        int paddingBottom = getPaddingBottom();

        //计算边框的半径，边框是按控件的最外围来画的
        mBorderRadius = Math.min((getHeight() - mBorderStrokeWidth) / 2f,
                (getWidth() - mBorderStrokeWidth) / 2f);

        //位图所在的外围框，位图不能超出这个矩形，
        //这个矩形应在控件的边框内，同时还要考虑padding的大小
        RectF drawableRect = new RectF(mBorderStrokeWidth+paddingLeft,
                mBorderStrokeWidth+paddingTop,
                getWidth() - mBorderStrokeWidth-paddingRight,
                getHeight() - mBorderStrokeWidth-paddingBottom);
        //计算画圆形位图所用的半径
        mDrawableRadius = Math.min(drawableRect.height() / 2f,
                drawableRect.width() / 2f);


        float scale;
        float dx = 0;//图像在x轴上开始的位置
        float dy = 0;//图像在y轴上开始的位置

        //三维变换矩阵，用于计算图像的缩放和位移
        Matrix mShaderMatrix = new Matrix();
        mShaderMatrix.set(null);
        //计算图像需要缩放的比例，我们要保证图像跟据其外围框的大小和长宽比进行按比例缩放
        if (mBitmapWidth * drawableRect.height() < drawableRect.width() * mBitmapHeight) {
            //如果图像的宽大于外围框的宽，则图像缩放后的高度变成跟外围框高度相同，
            //然后按比例计算图像缩放后的宽度
            scale = drawableRect.height() / (float) mBitmapHeight;
            //因图像比外围框窄，所以计算X轴上图像的开始位置
            dx = (drawableRect.width() - mBitmapWidth * scale) * 0.5f;
        } else {
            //如果图像的宽小于外围框的宽，则图像缩放后的宽度变成跟外围框宽度相同，
            //然后按比例计算图像缩放后的高度
            scale = drawableRect.width() / (float) mBitmapWidth;
            //因图像比外围框宽，所以计算y轴上图像的开始位置
            dy = (drawableRect.height() - mBitmapHeight * scale) * 0.5f;
        }

        //设置位图在X轴和y轴的缩放比例
        mShaderMatrix.setScale(scale, scale);
        //设置位图在x轴和y轴上的位移，以保证图像居中
        mShaderMatrix.postTranslate(
                (int) (dx + 0.5f) + mBorderStrokeWidth,
                (int) (dy + 0.5f) + mBorderStrokeWidth);

        //将变换矩阵设置给着色器
        mBitmapShader.setLocalMatrix(mShaderMatrix);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        updateShaderMatrix();
    }
}
