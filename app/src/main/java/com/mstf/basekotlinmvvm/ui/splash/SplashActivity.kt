package com.mstf.basekotlinmvvm.ui.splash

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.mstf.basekotlinmvvm.ui.main.MainActivity


class SplashActivity : AppCompatActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    // Start home activity
    startActivity(Intent(this, MainActivity::class.java))
    // close splash activity
    finish()
  }
}