package com.example.canongame

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.PointF
import android.view.MotionEvent

class BalleCanon (var view: CanonView, val obstacle: Obstacle) {

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


    fun draw(canvas: Canvas) {
        canvas.drawCircle(
            canonball.x, canonball.y, canonballRadius,
            canonballPaint
        )
    }

    fun resetCanonBall() {
        canonballOnScreen = false
    }

    fun update(interval: Double) {

        if (canonball.x > 350) {
                canonballVitesseX = (-300).toFloat()
                }

        if (canonballOnScreen) {
            canonball.x += (interval * canonballVitesseX).toFloat()
            canonball.y += (interval * canonballVitesseY).toFloat()

            /* Vérifions si la balle touche l'obstacle ou pas */
            if (canonball.y + canonballRadius > obstacle.obstacle.top
            ) {
                canonballVitesseX = -canonballVitesseX
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
//            else if (canonball.y + canonballRadius > view.screenHeight
//                || canonball.y - canonballRadius < 0) {
//                canonballVitesseY = -canonballVitesseY
//                }
//            else if (canonball.x + canonballRadius>cible.cible.left
//                && canonball.y + canonballRadius>cible.cible.top
//                && canonball.y - canonballRadius<cible.cible.bottom) {
//                cible.detectChoc(this)
//            }
        }
    }
}
