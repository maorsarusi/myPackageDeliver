package com.example.mypackagedeliver.UI

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.example.mypackagedeliver.R
import com.example.mypackagedeliver.UI.Login.RegisterActivity

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        supportActionBar?.hide()

        Handler().postDelayed({
          val intent = Intent (this@SplashActivity, RegisterActivity::class.java)
          startActivity(intent)
            finish()
        },


            3000)
    }



}