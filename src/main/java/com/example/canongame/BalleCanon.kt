package com.example.canongame

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.PointF
import android.view.MotionEvent

class BalleCanon (var view: CanonView, val obstacle: Obstacle, /*val cible: Cible*/) {

    //val hauteurMax = view.screenWidth / 2f
    var canonball = PointF()
    var canonballVitesse = 0f
    var canonballVitesseX = 0f
    var canonballVitesseY = 0f
    var canonballOnScreen = true
    var canonballRadius = 0f
    var canonballPaint = Paint()

    init {
        canonballPaint.color = Color.BLACK
    }

    fun launch(angle: Double) {

        canonball.x = canonballRadius
        canonball.y = view.screenHeight / 8f
        canonballVitesseX = (canonballVitesse * Math.sin(angle)).toFloat()
        canonballVitesseY = 0f
        canonballOnScreen = true
    }

    /*override fun onTouchEvent(event: MotionEvent): Boolean {
        dinosourUp += 1

        if(dinosaureY>=((canvasHeight/100)*60) && event.action == MotionEvent.ACTION_DOWN)
        {
            touch = true
            dinosaureVitesse = -50

        }*/

    fun draw(canvas: Canvas) {
        canvas.drawCircle(
            canonball.x, canonball.y, canonballRadius,
            canonballPaint
        )
        /*int left, int top, int right, int bottom*/
    }

    fun resetCanonBall() {
        canonballOnScreen = false
    }

    fun update(interval: Double) {

        if (canonball.x > 350) {
                canonballVitesseX = (-500).toFloat()
                }

        if (canonballOnScreen) {
            canonball.x += (interval * canonballVitesseX).toFloat()
            canonball.y += (interval * canonballVitesseY).toFloat()

            /* Vérifions si la balle touche l'obstacle ou pas */
            if (canonball.x + canonballRadius > obstacle.obstacle.left
                && canonball.y + canonballRadius > obstacle.obstacle.top
                && canonball.y - canonballRadius < obstacle.obstacle.bottom
            ) {
                canonballVitesseX *= -1
                canonball.offset((5 * canonballVitesseX * interval).toFloat(), 0f)
                //view.reduceTimeLeft()
                //view.playObstacleSound()
            }
            // Si elle sorte de l'écran
            else if (canonball.x + canonballRadius >= view.screenWidth
                || canonball.x - canonballRadius < 0
            ) {
                canonballVitesseX = 0f
            }
            else if (canonball.y + canonballRadius > view.screenHeight
                || canonball.y - canonballRadius < 0) {
                canonballVitesseY = -canonballVitesseY
                }
//            else if (canonball.x + canonballRadius>cible.cible.left
//                && canonball.y + canonballRadius>cible.cible.top
//                && canonball.y - canonballRadius<cible.cible.bottom) {
//                cible.detectChoc(this)
//            }
        }
    }
}
