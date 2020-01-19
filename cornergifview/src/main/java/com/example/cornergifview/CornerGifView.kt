package com.example.cornergifview

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
    private var mPath: Path? = null
    private var mFloats: FloatArray? = null

    init {
        init(context, attrs)
    }

    private fun init(context: Context, attrs: AttributeSet?) {
        val a = context.obtainStyledAttributes(attrs, R.styleable.CornerGifView)
        val mLeftTopCorner = a.getDimension(R.styleable.CornerGifView_leftTopCorner, 0f)
        val mRightTopCorner = a.getDimension(R.styleable.CornerGifView_rightTopCorner, 0f)
        val mLeftBottomCorner = a.getDimension(R.styleable.CornerGifView_leftBottomCorner, 0f)
        val mRightBottomCorner = a.getDimension(R.styleable.CornerGifView_rightBottomCorner, 0f)
        mCornerColor = a.getColor(R.styleable.CornerGifView_cornerColor, Color.WHITE)
        a.recycle()
        mFloats = floatArrayOf(
            mLeftTopCorner,
            mLeftTopCorner,
            mRightTopCorner,
            mRightTopCorner,
            mLeftBottomCorner,
            mLeftBottomCorner,
            mRightBottomCorner,
            mRightBottomCorner
        )
        mPath = Path()
        mPath!!.fillType = Path.FillType.INVERSE_EVEN_ODD
        mPath!!.addRoundRect(
            RectF(0f, 0f, measuredWidth.toFloat(), measuredHeight.toFloat()),
            mFloats!!,
            Path.Direction.CCW
        )
        mPaint = Paint()
        mPaint!!.style = Paint.Style.FILL_AND_STROKE
        mPaint!!.color = mCornerColor
        mPaint!!.isAntiAlias = true
    }

    fun setCornerColor(cornerColor: Int) {
        mCornerColor = cornerColor
        mPaint!!.color = mCornerColor
        invalidate()
    }

    override fun onSizeChanged(w: Int, h: Int, oldW: Int, oldH: Int) {
        super.onSizeChanged(w, h, oldW, oldH)
        mPath!!.reset()
        val mRectF = RectF(0f, 0f, w.toFloat(), h.toFloat())
        mPath!!.addRoundRect(mRectF, mFloats!!, Path.Direction.CCW)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.save()
        canvas.drawPath(mPath!!, mPaint!!)
        canvas.restore()
    }
}