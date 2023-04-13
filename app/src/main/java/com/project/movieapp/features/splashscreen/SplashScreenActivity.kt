package com.project.movieapp.features.splashscreen

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.project.movieapp.R
import com.project.movieapp.features.movielist.MovieListActivity
import com.project.movieapp.utils.UIUtils

class SplashScreenActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        UIUtils.setLightIconStatusBar(window)

        val handler = Handler(Looper.getMainLooper())
        handler.postDelayed({
            startActivity(Intent(this, MovieListActivity::class.java))
            finish()
        },3000)
    }
}
