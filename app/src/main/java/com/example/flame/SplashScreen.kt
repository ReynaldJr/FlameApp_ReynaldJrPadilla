package com.example.flame

import android.app.ActivityOptions
import android.app.TaskStackBuilder.create
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.WindowManager
import android.view.animation.AnimationUtils
import kotlinx.android.synthetic.main.activity_splash_screen.*
import java.net.URI.create
import android.util.Pair as UtilPair

class SplashScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        supportActionBar?.hide()

        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        val logoAnimation = AnimationUtils.loadAnimation(this, R.anim.top_logo)
        val textAnimation = AnimationUtils.loadAnimation(this, R.anim.bottom_logo)
        logoImage.startAnimation(logoAnimation)
        splashTitleFlame.startAnimation(textAnimation)

        Handler(Looper.getMainLooper()).postDelayed({
            val intent = Intent(this, MainActivity::class.java)
            val pair = UtilPair.create<View, String>(splashTitleFlame, "title_image")
            val makeSceneTransitionAnimation = ActivityOptions.makeSceneTransitionAnimation(this, pair)
            startActivity(intent, makeSceneTransitionAnimation.toBundle())
        }, 3000)

    }
}