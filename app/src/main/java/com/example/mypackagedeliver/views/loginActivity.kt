package com.example.mypackagedeliver.views

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.mypackagedeliver.R
import com.example.mypackagedeliver.models.User
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class loginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        val userName = findViewById<EditText>(R.id.username)
        val password = findViewById<EditText>(R.id.password)

        val button: Button = findViewById(R.id.login)
        button.setOnClickListener {
            val userNameString = userName.text.toString()
            val passwordString = password.text.toString()
            val database = Firebase.database
            val users_db = database.getReference("users")
            if (true) {

                button.isEnabled=true
                Toast.makeText(applicationContext, "The login was successful", Toast.LENGTH_LONG)
                    .show()

            } else {
                Toast.makeText(applicationContext, "for register click on \"register now\"", Toast.LENGTH_LONG).show()

            }
            val intent = Intent(this, Home::class.java)
            startActivity(intent)
        }
        val buttonRegister: Button = findViewById(R.id.openRegisterActivity)
        button.setOnClickListener {
            val intent = Intent(this, registerActivity::class.java)
            startActivity(intent)
        }
    }

}