package com.example.mypackagedeliver.UI.Login

import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.mypackagedeliver.Entities.User
import com.example.mypackagedeliver.R
import com.example.mypackagedeliver.UI.MainActivity.Home
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase

class loginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        val userName = findViewById<EditText>(R.id.username)
        val phoneNum = findViewById<EditText>(R.id.phoneNumber)
        val password = findViewById<EditText>(R.id.password)


        val button: Button = findViewById(R.id.login)
        button.setOnClickListener {
            val userNameString = userName.text.toString()
            val passwordString = password.text.toString()
            val phoneNumString = phoneNum.text.toString()

            val database = FirebaseDatabase.getInstance()
            val myRef = database.getReference("users")



            button.isEnabled = true
            Toast.makeText(applicationContext, "The login was successful", Toast.LENGTH_LONG)
                .show()


            Toast.makeText(
                applicationContext,
                "for register click on \"register now\"",
                Toast.LENGTH_LONG
            ).show()

        }
        val intent = Intent(this, Home::class.java)
        startActivity(intent)
        val buttonRegister: Button = findViewById(R.id.openRegisterActivity)
        buttonRegister.setOnClickListener {
            val intent = Intent(this, registerActivity::class.java)
            startActivity(intent)
        }
    }
}