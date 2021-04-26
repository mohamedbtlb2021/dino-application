package com.example.dino

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button

class MainActivity : AppCompatActivity(){
    lateinit var drawingView: DrawingView
    lateinit var boutonStart: Button
    lateinit var resultat: View
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //drawingView = findViewById(R.id.vMain)
        boutonStart = findViewById(R.id.startok)
        //resultat= findViewById(R.id.resultat)
        boutonStart.setOnClickListener {
            //resultat.setBackgroundColor(Color.RED) ?????  pq ca fait buger
            boutonStart.setText("LET'S GOOO!!!!")
           // drawingView.setBackgroundColor(Color.GREEN)
        }
    }
     /*fun onClickResultat ( v: View) {
        boutonStart.setText("RUINA!!!!")
        drawingView.setBackgroundColor(Color.RED)
    }*/
}


