package com.example.slidebacklayout

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.os.Build
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.ViewParent
import android.widget.FrameLayout
import androidx.annotation.RequiresApi
import kotlin.math.abs
import kotlin.math.sin

class SlideBackLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {
    private var mStartX: Float = 0.toFloat()
    private var mStartY: Float = 0.toFloat()
    private var mCurrentX: Float = 0.toFloat()
    private var mHasJudged = false
    private var progress: Float = 0.toFloat()
    private var mPaint: Paint? = null
    private var mPaintWhite: Paint? = null
    private var mPath: Path? = null
    private var mHalfScreenWidth: Int = 0
    private var mDrawBack = false
    private var mIsAccept = false
    private var mIsLeftStart = false
    private var mIsRightStart = false
    private var mSlideBackListener: OnSlideBackListener? = null
    private var mIsRightSlideEnable = true
    private var mIsLeftSlideEnable = true
    private val mStartMarginDP = 30
    private val mContext = context

    init {
        val array = mContext.obtainStyledAttributes(attrs, R.styleable.SlideBackLayout)
        val leftEnable = array.getBoolean(R.styleable.SlideBackLayout_left_swipe, true)
        val rightEnable = array.getBoolean(R.styleable.SlideBackLayout_right_swipe, true)
        array.recycle()
        setLeftSlideEnable(leftEnable)
        setRightSlideEnable(rightEnable)
        initDrawTool()
    }

    companion object {
        private const val TAG = "SlideBackLayout"
    }

    private fun initDrawTool() {
        mPaint = Paint()
        mPaint?.color = Color.BLACK
        mPaint?.strokeWidth = 8f
        mPaint?.isAntiAlias = true
        mPaint?.style = Paint.Style.FILL

        mPaintWhite = Paint()
        mPaintWhite?.color = Color.WHITE
        mPaintWhite?.strokeWidth = 4f
        mPaintWhite?.isAntiAlias = true
        mPaintWhite?.strokeCap = Paint.Cap.ROUND
        mPath = Path()
    }

    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
        when (ev.actionMasked) {
            MotionEvent.ACTION_DOWN -> {
                mHasJudged = false
                mStartX = ev.x
                mStartY = ev.y
                mIsLeftStart = isLeftStart(mStartX)
                mIsRightStart = isRightStart(mStartX)
            }
        }
        return mIsLeftStart && mIsLeftSlideEnable || mIsRightStart && mIsRightSlideEnable ||
                super.onInterceptTouchEvent(ev)
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    override fun onTouchEvent(event: MotionEvent): Boolean {
        disallowParentsInterceptTouchEvent(parent)
        when (event.action) {
            MotionEvent.ACTION_MOVE -> {
                mCurrentX = event.x
                if (!mHasJudged) {
                    val distanceX = Math.abs(mCurrentX - mStartX)
                    if (distanceX > 30) {
                        allowParentsInterceptTouchEvent(parent)
                        mHasJudged = true
                    }
                }
                // 大于30开始画图
                if (mHasJudged) {
                    postInvalidateDelayed(0)
                }
            }
            MotionEvent.ACTION_UP -> {
                progress = calculateProgress()
                if (progress > 0.9 * 0.665 && (mIsLeftStart || mIsRightStart)) {
                    Log.d(TAG, "onTouchEvent: action up")
                    mIsAccept = true
                    mSlideBackListener?.completeSwipeBack()
                }
                mDrawBack = true
                postInvalidateDelayed(0)
            }
        }
        return true
    }

    private fun isLeftStart(startX: Float): Boolean {
        return mIsLeftSlideEnable && startX < dip2px(mStartMarginDP.toFloat())
    }

    private fun isRightStart(startX: Float): Boolean {
        return mIsRightSlideEnable && startX > width - dip2px(mStartMarginDP.toFloat())
    }

    private fun disallowParentsInterceptTouchEvent(parent: ViewParent?) {
        if (parent == null) {
            return
        }
        parent.requestDisallowInterceptTouchEvent(true)
        disallowParentsInterceptTouchEvent(parent.parent)
    }

    private fun allowParentsInterceptTouchEvent(parent: ViewParent?) {
        if (parent == null) {
            return
        }
        parent.requestDisallowInterceptTouchEvent(false)
        allowParentsInterceptTouchEvent(parent.parent)
    }


    override fun dispatchDraw(canvas: Canvas) {
        super.dispatchDraw(canvas)
        drawSinLine(canvas)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        mHalfScreenWidth = measuredWidth / 2
    }

    private fun drawSinLine(canvas: Canvas) {
        if (!mIsRightStart && !mIsLeftStart) {
            resetAll()
            return
        }
        if (!mDrawBack) {
            progress = calculateProgress()
        } else if (mIsAccept) {
            resetAll()
        } else {
            progress -= 0.1f
            if (progress < 0) {
                resetAll()
            }
        }
        val initAmplitude = dip2px(12f)
        val amplitude = initAmplitude.toFloat() * progress * 1.5f
        val height = initAmplitude.toFloat() * 2f * progress * 1.5f
        val sineWidth = dip2px(135f).toFloat()
        var sineIndex = 0f
        val sineTheta = 30f
        var valueSineStart = 0f
        val sineLineStartY = mStartY - 1.9f / 3f * sineWidth
        if (mIsRightStart) {
            valueSineStart += width.toFloat()
        }
        mPath?.reset()
        mPath?.moveTo(valueSineStart, sineLineStartY)
        var valueSine: Float
        while (sineIndex <= sineWidth * 4 / 3) {
            valueSine = (sin((sineIndex / sineWidth).toDouble() * 1.5 * Math.PI + sineTheta) * amplitude + height - amplitude).toFloat()
            if (mIsRightStart) {
                valueSine *= -1f
                valueSine += width.toFloat()
            }
            mPath?.lineTo(valueSine, sineLineStartY + sineIndex)
            sineIndex++
        }
        mPath?.lineTo(valueSineStart, sineLineStartY)
        mPath?.close()
        mPaint?.alpha = (190f * progress * 1.5f).toInt()

        mPaint?.let { paint ->
            mPath?.let { path ->
                canvas.drawPath(path, paint)
            }
        }

        var midBackX = amplitude * 1.25f
        val midBackY = mStartY

        mPaintWhite?.alpha = (255f * progress * 1.5f).toInt()
        val lineLength = dip2px(5f).toFloat() * progress * 1.5f

        if (mIsRightStart) {
            midBackX *= -1f
            midBackX += width + lineLength
            mPaintWhite?.let{
                canvas.drawLine(midBackX - lineLength, midBackY, midBackX, midBackY - lineLength, it)
                canvas.drawLine(midBackX - lineLength, midBackY, midBackX, midBackY + lineLength, it)
            }
        } else {
            mPaintWhite?.let{
                canvas.drawLine(midBackX - lineLength, midBackY, midBackX, midBackY + lineLength, it)
                canvas.drawLine(midBackX - lineLength, midBackY, midBackX, midBackY - lineLength, it)
            }
        }

        // 慢回弹
        if (mDrawBack) {
            postInvalidateDelayed(0)
        }
    }

    private fun resetAll() {
        mCurrentX = 0f
        mHasJudged = false
        mStartY = 0f
        mStartX = 0f
        mDrawBack = false
        progress = 0f
        allowParentsInterceptTouchEvent(parent)
    }

    private fun calculateProgress(): Float {
        val distance = abs(mCurrentX - mStartX)
        val swipeMaximum = 0.6665f
        if (distance > mHalfScreenWidth) {
            return swipeMaximum
        }
        val temp = distance / mHalfScreenWidth
        return if (temp > swipeMaximum) {
            swipeMaximum
        } else temp
    }

    private fun dip2px(dipValue: Float): Int {
        val scale = mContext.resources.displayMetrics.scaledDensity
        return (dipValue * scale + 0.5f).toInt()
    }

    private fun setRightSlideEnable(enable: Boolean) {
        this.mIsRightSlideEnable = enable
    }

    private fun setLeftSlideEnable(enable: Boolean) {
        this.mIsLeftSlideEnable = enable
    }

    fun setSwipeBackListener(onSlideBackListener: OnSlideBackListener) {
        this.mSlideBackListener = onSlideBackListener
    }

    interface OnSlideBackListener {
        fun completeSwipeBack()
    }
}