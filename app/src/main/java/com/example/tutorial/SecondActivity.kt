package com.example.tutorial

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.os.Bundle
import android.os.Handler
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import kotlin.random.Random

class SecondActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(GameView(this))
    }
}

class GameView : View {

    private val ballPaint = Paint().apply {
        color = Color.RED
        isAntiAlias = true
    }

    private val scorePaint = Paint().apply {
        color = Color.BLACK
        textSize = 50f
        isAntiAlias = true
    }

    private val rect = RectF()
    private var ballX = 0f
    private var ballY = 0f
    private var ballRadius = 0f
    private var score = 0
    private var gameHandler = Handler()
    private var gameRunnable: Runnable? = null

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)

    override fun onSizeChanged(width: Int, height: Int, oldWidth: Int, oldHeight: Int) {
        super.onSizeChanged(width, height, oldWidth, oldHeight)
        ballRadius = width / 10f
        resetBall()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        // ボールを描画する
        rect.set(ballX - ballRadius, ballY - ballRadius, ballX + ballRadius, ballY + ballRadius)
        canvas.drawOval(rect, ballPaint)

        // スコアを描画する
        canvas.drawText("Score: $score", 50f, 50f, scorePaint)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_DOWN) {
            val touchX = event.x
            val touchY = event.y

            // ボールをタップした場合、スコアを増やす
            if (Math.sqrt(Math.pow((touchX - ballX).toDouble(), 2.0) + Math.pow((touchY - ballY).toDouble(), 2.0)) < ballRadius) {
                score++
                resetBall()
            }
        }
        return true
    }

    private fun resetBall() {
        ballX = Random.nextInt(width).toFloat()
        ballY = Random.nextInt(height).toFloat()
        invalidate()

        // 一定時間後にボールを再配置する
        gameRunnable = Runnable { resetBall() }
        gameHandler.postDelayed(gameRunnable!!, 1000L)
    }
}