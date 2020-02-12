package com.happylife.draglayout

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.app.Activity
import android.content.Context
import android.graphics.Rect
import android.os.Handler
import android.os.Message
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.Window.ID_ANDROID_CONTENT
import android.widget.FrameLayout
import android.widget.LinearLayout
import androidx.recyclerview.widget.RecyclerView
import java.lang.ref.WeakReference


class DragLayout @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {
    companion object {
        private const val MSG_TYPE_SHOW_VIRTUAL_VIEW = 1
        private const val DELAY_MILLIS = 1000
        private const val DURATION_300 = 300
        private const val TRANSLATION_X = "translationX"
        private const val TRANSLATION_Y = "translationY"
        private const val MAX_MOVE = 50
        private const val DURATION_500 = 500
        private const val ALPHA = "alpha"
    }

    private val mContext = context
    private var mVirtualLayout: LinearLayout? = null
    private var mRealLayout: LinearLayout? = null
    private var mContentView: View? = null
    private var handler: CountDownHandler? = null
    private var mDownX: Int = 0
    private var mDownY: Int = 0
    private var originalL: Int = 0
    private var originalT: Int = 0
    private var mViewCreateListener: IonVirtualViewCreateListener? = null

    init {
        mRealLayout = LinearLayout(mContext)
        mRealLayout!!.layoutParams = LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.MATCH_PARENT
        )
        mVirtualLayout = LinearLayout(mContext)
        mVirtualLayout!!.layoutParams = LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.MATCH_PARENT
        )
        mVirtualLayout?.alpha = 0.5f
        addView(mRealLayout)
        addView(mVirtualLayout)
    }

    fun setOnVirtualViewCreatedListener(listener: IonVirtualViewCreateListener) {
        mViewCreateListener = listener
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        if (childCount > 3) {
            throw Exception("DragLayout contains more than 1 child")
        }
        mContentView = getChildAt(2)
        if (mContentView == null || mContentView !is ViewGroup) {
            throw Exception("content view must be ViewGroup or RecyclerView")
        }
        removeView(mContentView)
        mRealLayout?.addView(mContentView)
        //为了获取event事件
        mContentView?.setOnClickListener {}
    }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        ev?.let {
            when (ev.action) {
                MotionEvent.ACTION_DOWN -> {
                    mDownX = ev.rawX.toInt()
                    mDownY = ev.rawY.toInt()
                    originalL = 0
                    originalT = 0
                    if (handler == null) {
                        handler = CountDownHandler(
                            mContext,
                            mContentView,
                            mVirtualLayout,
                            mViewCreateListener
                        )
                    }
                    handler?.setPoint(mDownX.toFloat(), mDownY.toFloat())
                    handler?.sendEmptyMessageDelayed(
                        MSG_TYPE_SHOW_VIRTUAL_VIEW,
                        DELAY_MILLIS.toLong()
                    )
                }
                MotionEvent.ACTION_MOVE -> {
                    val x = ev.rawX.toInt()
                    val y = ev.rawY.toInt()
                    val dx = x - mDownX
                    val dy = y - mDownY
                    if (getItemView() != null) {
                        val itemView = getItemView()
                        if (originalT == 0) {
                            originalL = itemView?.left ?: 0
                            originalT = itemView?.top ?: 0
                        }
                        val l = itemView?.left ?: 0
                        val r = itemView?.right ?: 0
                        val t = itemView?.top ?: 0
                        val b = itemView?.bottom ?: 0
                        itemView?.layout(l + dx, t + dy, r + dx, b + dy)
                        mDownX = ev.rawX.toInt()
                        mDownY = ev.rawY.toInt()
                        return false
                    } else {
                        if (dx > MAX_MOVE || dy > MAX_MOVE) {
                            removeView()
                            return false
                        } else {
                            //无逻辑
                        }
                    }
                }
                MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                    removeView()
                }
                else -> {
                    //无逻辑
                }
            }
        }
        return super.dispatchTouchEvent(ev)
    }

    private class CountDownHandler internal constructor(
        context: Context,
        contentView: View?,
        llContainer: LinearLayout?,
        listener: IonVirtualViewCreateListener?
    ) :
        Handler() {
        private var weakReference: WeakReference<Activity>? = null
        private var mContentView: View? = null
        private var x: Float = 0.toFloat()
        private var y: Float = 0.toFloat()
        private var llContainer: LinearLayout? = null
        private var mListener: IonVirtualViewCreateListener? = null

        init {
            if (context is Activity) {
                weakReference = WeakReference(context)
                this.mContentView = contentView
                this.llContainer = llContainer
                this.mListener = listener
            }
        }

        internal fun setPoint(x: Float, y: Float) {
            this.x = x
            this.y = y
        }

        override fun dispatchMessage(msg: Message) {
            super.dispatchMessage(msg)
            if (msg.what == 1) {
                val activity = weakReference?.get()
                if (activity != null
                    && !activity.isFinishing
                    && !activity.isDestroyed
                ) {
                    var virtualView: View? = null
                    if (mContentView is RecyclerView) {
                        val targetView = (mContentView as RecyclerView).findChildViewUnder(x, y)
                        targetView?.let {
                            val position =
                                (mContentView as RecyclerView).getChildAdapterPosition(targetView)
                            val viewHolder =
                                (mContentView as RecyclerView).findViewHolderForAdapterPosition(
                                    position
                                )
                            virtualView = viewHolder?.let { vh ->
                                mListener?.onCreateVirtualRecyclerItemView(
                                    targetView,
                                    position,
                                    vh
                                )
                            }
                        }
                    } else {
                        virtualView = mListener?.onCreateVirtualView()
                    }
                    try {
                        llContainer?.removeAllViews()
                        val linearLayout = LinearLayout(activity)
                        val originalLocation = IntArray(2)
                        mContentView?.getLocationInWindow(originalLocation)
                        val x = originalLocation[0]
                        val y = originalLocation[1]
                        val layoutParams = LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.WRAP_CONTENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT
                        )
                        val frame = Rect()
                        activity.window.decorView.getWindowVisibleDisplayFrame(frame)
                        val statusBarHeight = frame.top
                        val contentTop =
                            (activity.window.findViewById(ID_ANDROID_CONTENT) as FrameLayout).top
                        layoutParams.leftMargin = x
                        layoutParams.topMargin = y - statusBarHeight - contentTop
                        linearLayout.layoutParams = layoutParams

                        linearLayout.addView(virtualView)
                        llContainer?.addView(linearLayout)
                        val animator = ObjectAnimator.ofFloat(linearLayout, ALPHA, 0f, 1f)
                        animator.duration = DURATION_500.toLong()
                        animator.start()
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }
        }
    }

    private fun removeView() {
        handler?.removeMessages(MSG_TYPE_SHOW_VIRTUAL_VIEW)
        if (getItemView() != null) {
            val itemView = getItemView()
            val l = itemView?.left ?: 0
            val t = itemView?.top ?: 0
            val floatL = (originalL - l).toFloat()
            val floatT = (originalT - t).toFloat()
            val animator = ObjectAnimator.ofFloat(itemView, TRANSLATION_X, floatL)
            animator.duration = DURATION_300.toLong()
            val animator1 = ObjectAnimator.ofFloat(itemView, TRANSLATION_Y, floatT)
            animator1.duration = DURATION_300.toLong()
            animator.start()
            animator1.start()
            animator.addListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    super.onAnimationEnd(animation)
                    mVirtualLayout?.removeAllViews()
                }
            })
        }
    }


    private fun getItemView(): View? {
        return mVirtualLayout?.getChildAt(0)
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        if (handler != null) {
            handler?.removeCallbacksAndMessages(null)
            handler = null
        }
    }
}