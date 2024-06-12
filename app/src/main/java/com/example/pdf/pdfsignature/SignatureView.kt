package com.example.pdf.pdfsignature

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View



class SignatureView (context : Context, attrs : AttributeSet) : View(context,attrs) {

    private val paint = Paint()
    private val path = Path()

    init {
        paint.isAntiAlias = true
        paint.style = Paint.Style.STROKE
        paint.color = Color.BLACK
        paint.strokeJoin = Paint.Join.ROUND
        paint.strokeWidth = 5f
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawPath(path,paint)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if(event != null){
            val x = event.x
            val y = event.y

            when(event.action){
                MotionEvent.ACTION_DOWN -> path.moveTo(x,y)
                MotionEvent.ACTION_MOVE -> path.lineTo(x,y)
                MotionEvent.ACTION_UP -> {}
            }

        }
        invalidate()
        return true
    }

    fun clear()
    {
        path.reset()
        invalidate()
    }

    fun getSignatureBitmap() : Bitmap{
        val bitmap = Bitmap.createBitmap(this.width,this.height,Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        this.draw(canvas)
        return bitmap
    }

}