package com.example.canongame

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF

class Obstacle (var obstacleDistance: Float, var obstacleDebut: Float, var obstacleFin: Float, var initialObstacleVitesse: Float, var width: Float, var view: CanonView) {
    val obstacle = RectF(
        obstacleDistance, obstacleDebut,
        obstacleDistance + width, obstacleFin
    )
    val obstaclePaint = Paint()
    var obstacleVitesse = initialObstacleVitesse



    fun setRect() {
        obstacle.set(
            obstacleDistance, obstacleDebut,
            obstacleDistance + width, obstacleFin
        )
        obstacleVitesse = initialObstacleVitesse
    }

    fun draw(canvas: Canvas) {
        obstaclePaint.color = Color.RED
        canvas.drawRect(obstacle, obstaclePaint)
    }


    fun update(interval: Double) {
        var up = (interval * obstacleVitesse).toFloat()
        obstacle.offset(0f, up)
        if (obstacle.top < 0 || obstacle.bottom > view.screenHeight) {
            obstacle.resetObstacle()
        }
    }

    private fun RectF.resetObstacle() {
//        screenWidth = w.toFloat()
//        screenHeight = h.toFloat()



        obstacleVitesse = initialObstacleVitesse
        obstacleDistance = 0f
        obstacleDebut = 1200f
        obstacleFin = 1000f
        obstacle.set(
            obstacleDistance, obstacleDebut,
            obstacleDistance + width, obstacleFin
        )
    }


//    private fun RectF.resetObstacle() {
//        obstacleVitesse = initialObstacleVitesse
//        obstacle.set(
//            obstacleDistance, obstacleDebut,
//            obstacleDistance + width, obstacleFin
//        )
//    }
}