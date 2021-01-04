package com.example.imageloadmoreview

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.transition.TransitionManager
import android.view.View
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet

class ProfileActivity : AppCompatActivity() {

    private var isOpen = false
    private var layout1: ConstraintSet? = null
    private var layout2: ConstraintSet? = null
    private var constraintLayout: ConstraintLayout? = null
    private var imageViewPhoto: ImageView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        layout1 = ConstraintSet()
        layout2 = ConstraintSet()
        imageViewPhoto = findViewById(R.id.cover_image)
        constraintLayout = findViewById(R.id.constraint_Layout)
        layout2!!.clone(this, R.layout.activity_profile_expanded)
        layout1!!.clone(constraintLayout)
        imageViewPhoto?.setOnClickListener(View.OnClickListener {
            isOpen = if (!isOpen) {
                TransitionManager.beginDelayedTransition(constraintLayout)
                layout2!!.applyTo(constraintLayout)
                !isOpen
            } else {
                TransitionManager.beginDelayedTransition(constraintLayout)
                layout1!!.applyTo(constraintLayout)
                !isOpen
            }
        })
    }
}