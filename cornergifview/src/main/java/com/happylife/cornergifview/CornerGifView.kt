package com.happylife.cornergifview

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import pl.droidsonroids.gif.GifImageView

class CornerGifView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : GifImageView(context, attrs, defStyle) {

    private var mCornerColor = Color.WHITE
    private var mPaint: Paint? = null
    private var mStrokePaint: Paint? = null
    private var mStrokeCorner: Float? = 0F
    private var mPath: Path? = null
    private var mFloats: FloatArray? = null
    private var mRectF: RectF? = null
    private var mStrokeWidth = 0F

    init {
        init(context, attrs)
    }

    private fun init(context: Context, attrs: AttributeSet?) {
        val a = context.obtainStyledAttributes(attrs, R.styleable.CornerGifView)
        val mLeftTopCorner = a.getDimension(R.styleable.CornerGifView_leftTopCorner, 0f)
        val mRightTopCorner = a.getDimension(R.styleable.CornerGifView_rightTopCorner, 0f)
        val mLeftBottomCorner = a.getDimension(R.styleable.CornerGifView_leftBottomCorner, 0f)
        val mRightBottomCorner = a.getDimension(R.styleable.CornerGifView_rightBottomCorner, 0f)

        mStrokeWidth = a.getDimension(R.styleable.CornerGifView_strokeWidth, 0f)
        val strokeColor = a.getColor(R.styleable.CornerGifView_strokeColor, Color.WHITE)
        val strokeCorner = a.getDimension(R.styleable.CornerGifView_strokeCorner, 0f)
        mStrokeCorner = if (strokeCorner > 0) {
            strokeCorner
        } else {
            mLeftTopCorner
        }
        mStrokePaint = Paint()
        mStrokePaint?.color = strokeColor
        mStrokePaint?.style = Paint.Style.STROKE
        mStrokePaint?.strokeWidth = mStrokeWidth
        mStrokePaint?.isAntiAlias = true

        mCornerColor = a.getColor(R.styleable.CornerGifView_cornerColor, Color.WHITE)
        a.recycle()
        mFloats = floatArrayOf(
            mLeftTopCorner, mLeftTopCorner, mRightTopCorner, mRightTopCorner,
            mLeftBottomCorner, mLeftBottomCorner, mRightBottomCorner, mRightBottomCorner
        )
        mPath = Path()
        mPath?.fillType = Path.FillType.INVERSE_EVEN_ODD
        mPath?.addRoundRect(
            RectF(0f, 0f, measuredWidth.toFloat(), measuredHeight.toFloat()),
            mFloats!!,
            Path.Direction.CCW
        )
        mPaint = Paint()
        mPaint?.style = if (mStrokeWidth > 0) {
            Paint.Style.FILL
        } else {
            Paint.Style.FILL_AND_STROKE
        }
        mPaint?.color = mCornerColor
        mPaint?.isAntiAlias = true
    }

    /**
     * 用于动态改变圆角颜色
     * 例如父布局的背景色由服务器下发的数据决定时
     * 可以调用此方法将圆角颜色设置为与父布局背景色一致
     */
    fun setCornerColor(cornerColor: Int) {
        mCornerColor = cornerColor
        mPaint?.color = mCornerColor
        invalidate()
    }

    override fun onSizeChanged(w: Int, h: Int, oldW: Int, oldH: Int) {
        super.onSizeChanged(w, h, oldW, oldH)
        mPath?.reset()
        mRectF = if (mStrokeWidth > 0) {
            RectF(
                0f + mStrokeWidth / 2,
                0f + mStrokeWidth / 2,
                w.toFloat() - mStrokeWidth / 2,
                h.toFloat() - mStrokeWidth / 2
            )
        } else {
            RectF(0f, 0f, w.toFloat(), h.toFloat())
        }
        mPath?.addRoundRect(mRectF!!, mFloats!!, Path.Direction.CCW)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.save()
        canvas.drawPath(mPath!!, mPaint!!)
        if (mStrokeWidth > 0) {
            canvas.drawRoundRect(mRectF!!, mStrokeCorner ?: 0F, mStrokeCorner ?: 0F, mStrokePaint!!)
        }
        canvas.restore()
    }
}