package com.happylife.compositepicture

import android.graphics.*

object CompositeUtil {
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
        val width = if (first.width > second.width) first.width else second.width
        val height = if (first.height > second.height) first.height else second.height
        bmp = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val paint = Paint()
        paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_OVER)

        val canvas = Canvas(bmp)
        canvas.drawBitmap(first, 0f, 0f, null)
        canvas.drawBitmap(second, 0f, 0f, paint)
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