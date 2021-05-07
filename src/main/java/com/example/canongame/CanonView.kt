package com.example.canongame

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Point
import android.os.Bundle
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.SurfaceHolder
import android.view.SurfaceView
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentActivity

class CanonView @JvmOverloads constructor (context: Context, attributes: AttributeSet? = null, defStyleAttr: Int = 0): SurfaceView(context, attributes,defStyleAttr), SurfaceHolder.Callback, Runnable {

    lateinit var canvas: Canvas

    val activity = context as FragmentActivity

        val backgroundPaint = Paint()
        val textPaint = Paint()
        var screenWidth = 0f
        var screenHeight = 0f
        var drawing = false

//    val soundPool: SoundPool
//    val soundMap: SparseIntArray

    lateinit var thread: Thread


    val obstacle1 = Obstacle(0f, 0f, 0f, 0f, 0f, this)
    val obstacle2 = Obstacle(0f, 0f, 0f, 0f, 0f, this)
    val balle = BalleCanon(this, obstacle1, /*cible*/)

    var shotsFired = 0
    var timeLeft = 0.0
    val MISS_PENALTY = 2
    val HIT_REWARD = 3
    var gameOver = false
    var totalElapsedTime = 0.0



    init {
        backgroundPaint.color = Color.WHITE
        textPaint.textSize= screenWidth/20
        textPaint.color = Color.BLACK
        timeLeft = 10.0

    }

    fun pause() {
        drawing = false
        thread.join()
    }

    fun resume() {
        drawing = true
        thread = Thread(this)
        thread.start()
    }

    override fun run() {
        var previousFrameTime = System.currentTimeMillis()
        while (drawing) {
            val currentTime = System.currentTimeMillis()
            val elapsedTimeMS:Double=(currentTime-previousFrameTime).toDouble()
            totalElapsedTime += elapsedTimeMS / 1000.0
            updatePositions(elapsedTimeMS)
            draw()
            previousFrameTime = currentTime

        }
    }

    override fun onSizeChanged(w:Int, h:Int, oldw:Int, oldh:Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        screenWidth = w.toFloat()
        screenHeight = h.toFloat()


        balle.canonballRadius= (w / 18f)
        balle.canonballVitesse = (w * 3 / 4f)
        balle.launch(0.0)

        obstacle1.obstacleDistance = (0f)
        obstacle1.obstacleDebut = (h*1f)
        obstacle1.obstacleFin = (h * 1f - 100f)
        obstacle1.width = (w / 6f)
        obstacle1.initialObstacleVitesse = (-h / 5f)
        obstacle1.setRect()

        obstacle2.obstacleDistance = (0f)
        obstacle2.obstacleDebut = (h * 2 / 3f + 100)
        obstacle2.obstacleFin = (h * 2 / 3f)
        obstacle2.width = (w / 3f)
        obstacle2.initialObstacleVitesse = (-h * 0f)
        obstacle2.setRect()

        textPaint.setTextSize(w / 20f)
        textPaint.isAntiAlias = true

        newGame()
    }

    fun draw() {
        if (holder.surface.isValid) {
            canvas = holder.lockCanvas()
            canvas.drawRect(0f, 0f, canvas.width.toFloat(),
                canvas.height.toFloat(), backgroundPaint)
            val formatted = String.format("%.2f", timeLeft)
            canvas.drawText("Il reste $formatted secondes. ",
                30f, 50f, textPaint)
            if (balle.canonballOnScreen)
                balle.draw(canvas)
            obstacle1.draw(canvas)
            obstacle2.draw(canvas)
            holder.unlockCanvasAndPost(canvas)
        }
    }

    fun updatePositions(elapsedTimeMS: Double) {
        val interval = elapsedTimeMS / 1000.0
        obstacle1.update(interval)
        obstacle2.update(interval)
        balle.update(interval)
        timeLeft -= interval

//        if (timeLeft <= 0.0) {
//            timeLeft = 0.0
//            gameOver = true
//            drawing = false
//            showGameOverDialog(R.string.lose)
//        }
    }

    fun gameOver() {
        drawing = false
        showGameOverDialog(R.string.win)
        gameOver = true
    }

    fun showGameOverDialog(messageId: Int) {
        class GameResult: DialogFragment() {
            override fun onCreateDialog(bundle: Bundle?): Dialog {
                val builder = AlertDialog.Builder(getActivity())
                builder.setTitle(resources.getString(messageId))
                builder.setMessage(
                    resources.getString(
                        R.string.results_format, shotsFired, totalElapsedTime
                    )
                )
                builder.setPositiveButton(R.string.reset_game,
                    DialogInterface.OnClickListener { _, _->newGame()}
                )
                return builder.create()
            }
        }

        activity.runOnUiThread(
            Runnable {
                val ft = activity.supportFragmentManager.beginTransaction()
                val prev =
                    activity.supportFragmentManager.findFragmentByTag("dialog")
                if (prev != null) {
                    ft.remove(prev)
                }
                ft.addToBackStack(null)
                val gameResult = GameResult()
                gameResult.setCancelable(false)
                gameResult.show(ft,"dialog")
            }
        )
    }

    fun newGame() {

        //obstacle1.resetObstacle()
        //obstacle2.resetObstacle()
        timeLeft = 10.0
        balle.resetCanonBall()
        shotsFired = 0
        totalElapsedTime = 0.0
        drawing = true
        if (gameOver) {
            gameOver = false
            thread = Thread(this)
            thread.start()
        }
    }

     override fun onTouchEvent(e: MotionEvent): Boolean {
        val action = e.action
        if (action == MotionEvent.ACTION_DOWN
            || action == MotionEvent.ACTION_MOVE) {
            fireCanonball(e)
        }
        return true
    }

    fun fireCanonball(event: MotionEvent) {

        if (!balle.canonballOnScreen || balle.canonballVitesseX == 0f) {
            val angle = alignCanon(event)
            balle.launch(angle)
            ++shotsFired

        }
    }

    fun alignCanon(event: MotionEvent): Double {
        val touchPoint = Point(event.x.toInt(), event.y.toInt())
        val centerMinusY = screenHeight / 2 - touchPoint.y
        var angle = 0.0
        if (centerMinusY != 0.0f)
            angle = Math.atan((touchPoint.x).toDouble()/ centerMinusY)
        if (touchPoint.y > screenHeight / 2)
            angle += Math.PI
        //canon.align(angle)
        return angle
    }

    fun reduceTimeLeft() {
        timeLeft -= MISS_PENALTY
    }

    fun increaseTimeLeft() {
        timeLeft += HIT_REWARD
    }


    override fun surfaceChanged(holder: SurfaceHolder, format: Int,
                                width: Int, height: Int) {}

    override fun surfaceCreated(holder: SurfaceHolder) {}

    override fun surfaceDestroyed(holder: SurfaceHolder) {}
}