package com.happylife.compositepicture

import android.graphics.*

object CompositeUtil {
    var xLabel: Int = 0 // 具体数值
    var yLabel: Int = 0

    var xLabelPer: Float = 0.0f // 百分比
    var yLabelPer: Float = 0.0f

    fun setXYLabel(x: Int, y: Int) {
        this.xLabel = x
        this.yLabel = y
    }

    fun setXYLabelPer(x: Float, y: Float) {
        this.xLabelPer = x
        this.yLabelPer = y
    }

    /**
     * 合成图片
     *
     * @param first  第一层图片
     * @param second 第二层图片
     * @return 合成后的图片
     */
    fun combineImages(first: Bitmap?, second: Bitmap?): Bitmap? {
        if (first == null || second == null) {
            return null
        }
        val bmp: Bitmap
        val newBmp: Bitmap
        val width = if (first.width > second.width) first.width else second.width
        val height = if (first.height > second.height) first.height else second.height
        bmp = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val paint = Paint()
        paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_OVER) // 覆盖原图

        val scaleWidth =
            if (xLabelPer * width + second.width > first.width) (first.width - xLabelPer * width).toInt() else second.width
        val scaleHeight =
            if (yLabelPer * height + second.height > first.height) (first.height - yLabelPer * height).toInt() else second.height

        newBmp = if (scaleWidth != second.width || scaleHeight != second.height) {
            scaleBitmap(second, scaleWidth, scaleHeight)
        } else {
            second
        }
        // 第一种：如果超出大小，贴边（放置的位置可能不是初衷的位置）
//        val leftPer = if (xLabelPer * width + second.width > first.width) (first.width - second.width).toFloat() else xLabelPer * width
//        val topPer = if (yLabelPer * height + second.height > first.height) (first.height - second.height).toFloat() else yLabelPer * height

        // 第二种：缩放图片（保留真实位置）
        val leftPer = xLabelPer * width
        val topPer = yLabelPer * height

        val canvas = Canvas(bmp)
        canvas.drawBitmap(first, 0f, 0f, null)
        canvas.drawBitmap(newBmp, leftPer, topPer, paint)
        return bmp
    }

    /**
     * 缩放图片
     *
     * @param bitmap    图片
     * @param newWidth  新宽度
     * @param newHeight 新高度
     * @return 缩放后的图片
     */
    private fun scaleBitmap(bitmap: Bitmap, newWidth: Int, newHeight: Int): Bitmap {
        // 获得图片的宽高
        val width = bitmap.width
        val height = bitmap.height
        // 计算缩放比例
        val scaleWidth = newWidth.toFloat() / width
        val scaleHeight = newHeight.toFloat() / height
        // 取得想要缩放的matrix参数
        val matrix = Matrix()
        matrix.postScale(scaleWidth, scaleHeight)
        // 得到新的图片
        return Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true)
    }
}