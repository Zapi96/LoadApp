package com.udacity

import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.view.animation.LinearInterpolator
import androidx.core.content.withStyledAttributes
import kotlin.math.cos
import kotlin.math.sin
import kotlin.properties.Delegates

private const val OFFSET = 50

class LoadingButton @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {


    // INITIALIZE VARIABLES
    private var widthSize = 0
    private var heightSize = 0
    private var animatedWidth : Float = 0.0f

    private var buttonNormalColor = 0
    private var buttonLoadingColor = 0
    private var textColor = 0

    private val valueAnimator = ValueAnimator()

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        textAlign = Paint.Align.CENTER
        textSize = 55.0f
        typeface = Typeface.create( "", Typeface.BOLD)
    }

    // OBSERVE BUTTON BEHAVIOUR
    var buttonState: ButtonState by Delegates.observable<ButtonState>(ButtonState.Completed) { p, old, new ->
        if (new == ButtonState.Loading){
            Log.d("SUCCESS","Observer")
            animatedWidthButton()
        }else{
            Log.d("EXITO","Cancel")
            animatedWidthButtonCancel()
        }

    }


    init {
        // EXTRACT VALUES OF COLORS
        context.withStyledAttributes(attrs, R.styleable.LoadingButton) {
            buttonNormalColor = getColor(R.styleable.LoadingButton_buttonColor1, 0)
            buttonLoadingColor = getColor(R.styleable.LoadingButton_buttonColor2, 0)
            textColor = getColor(R.styleable.LoadingButton_textcolor, 0)

        }

    }

    // DRAW
    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        val textHeight: Float = paint.descent() - paint.ascent()
        val textOffset: Float = textHeight / 2 - paint.descent()
        val startAngle = 0.0f
        val x = width*3/4f
        val y = height/4f
        val diameter = height/2f
        var oval = RectF()

        if (buttonState == ButtonState.Completed){
            paint.color = buttonNormalColor
            canvas?.drawRect(0f, 0f, width.toFloat(), height.toFloat(), paint)
            paint.color = Color.WHITE
            canvas?.drawText("DOWNLOAD",width.toFloat() / 2, height.toFloat() / 2 + textOffset, paint)
        }else{
            Log.d("EXITO","Entrada")
            paint.color = buttonNormalColor
            canvas?.drawRect(0f, 0f, width.toFloat(), height.toFloat(), paint)
            paint.color = buttonLoadingColor
            canvas?.drawRect(0f, 0f, animatedWidth, heightSize.toFloat(), paint)
            paint.color = textColor
            canvas?.drawText("LOADING...",width.toFloat() / 2, height.toFloat() / 2 + textOffset, paint)
            paint.color = textColor
            oval.set(x,y,x+diameter,y+diameter)
            canvas?.drawArc(oval,startAngle,animatedWidth/width*360f,true,paint)

        }

    }


    // ANIMATE_LOADING_BUTTON_BACKGROUND
     fun animatedWidthButton() {
        valueAnimator.apply {
            setFloatValues(0f, width.toFloat())
            duration = 1000
        }
        valueAnimator.addUpdateListener {
            animatedWidth = it.animatedValue as Float
            invalidate()
        }
        valueAnimator.start()
    }

    // CANCEL ANIMATED BUTTON
    fun animatedWidthButtonCancel(){
        valueAnimator.cancel()
        invalidate()
    }




}