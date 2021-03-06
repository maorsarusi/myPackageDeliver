package com.example.mypackagedeliver.UI

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.example.mypackagedeliver.R
import com.example.mypackagedeliver.UI.Login.LoginActivity
import com.example.mypackagedeliver.UI.Login.RegisterActivity

// the first screen in the app
class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        supportActionBar?.hide()

        Handler().postDelayed({
          val intent = Intent (this@SplashActivity, LoginActivity::class.java)
          startActivity(intent)
            finish()
        },


            3000)
    }



}