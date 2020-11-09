package com.github.aplikacjakardiologiczna.heart

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.view.View
import androidx.core.content.ContextCompat
import com.github.aplikacjakardiologiczna.R


@SuppressLint("ResourceAsColor")
class CustomProgressBar(context: Context?) : View(context) {
    companion object {
        const val IMG_SIZE = 32
    }

    private var progressPercent = 0
    private var barWidth = 0

    private val barPaint = Paint()
    private val outlinePaint = Paint()
    private val backgroundPaint = Paint()

    private val heartOutline: Bitmap
    private val heartBackground: Bitmap

    init {
        val barColor = ContextCompat.getColor(context!!, R.color.colorPrimary)
        val colorOutline = ContextCompat.getColor(context, R.color.colorPrimaryDark)
        val backgroundColor = ContextCompat.getColor(context, R.color.background)

        barPaint.color = barColor
        val filterOutline: ColorFilter = LightingColorFilter(colorOutline, 0)
        outlinePaint.colorFilter = filterOutline
        val filterBackground: ColorFilter = LightingColorFilter(backgroundColor, 0)
        backgroundPaint.colorFilter = filterBackground

        heartOutline = Bitmap.createScaledBitmap(
                BitmapFactory.decodeResource(resources, R.drawable.heart_outline), IMG_SIZE, IMG_SIZE,false)
        heartBackground = Bitmap.createScaledBitmap(
                BitmapFactory.decodeResource(resources, R.drawable.heart_background), IMG_SIZE, IMG_SIZE,false)
    }

    fun setProgressPercent(percent: Int) {
        progressPercent = percent
        invalidate()
    }

    @SuppressLint("DrawAllocation")
    override fun onDraw(canvas: Canvas) {
        val numberOfImages = barWidth / IMG_SIZE
        val fixedBarWidth = (IMG_SIZE * numberOfImages).toFloat()
        val newX = (barWidth - fixedBarWidth) / 2
        val end = newX + fixedBarWidth * progressPercent / 100

        canvas.drawRect(newX, 0F, end, IMG_SIZE.toFloat(), barPaint)

        for (i in 0 until numberOfImages) {
            canvas.drawBitmap(heartBackground, newX + i * IMG_SIZE, 0f, backgroundPaint)
            canvas.drawBitmap(heartOutline, newX + i * IMG_SIZE, 0f, outlinePaint)
        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        barWidth = w
    }
}
