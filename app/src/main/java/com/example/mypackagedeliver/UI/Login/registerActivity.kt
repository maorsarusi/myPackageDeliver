package com.example.mypackagedeliver.UI.Login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.mypackagedeliver.R
import com.example.mypackagedeliver.Entities.User
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class registerActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        val userName = findViewById<EditText>(R.id.username)
        val phoneNum = findViewById<EditText>(R.id.phoneNumber)
        val password = findViewById<EditText>(R.id.password)

        val button: Button = findViewById(R.id.register)
        button.setOnClickListener{
            val database = Firebase.database
            if(!((userName.text.toString() == "")|| password.text.toString() == "" || phoneNum.text.toString() == "")){
                val users_db = database.getReference("users")
                val userNameString = userName.text.toString()
                val passwordString=password.text.toString()
                val phoneNumString=phoneNum.text.toString()
                val user = insertIntoFireBase(userNameString,phoneNumString,passwordString)
                users_db.child(phoneNumString).setValue(user)
                Toast.makeText(applicationContext, "The registration was successful", Toast.LENGTH_LONG).show()
                val intent = Intent(this, loginActivity::class.java)
                startActivity(intent)
            }
            else{
                Toast.makeText(applicationContext, "Registration failed", Toast.LENGTH_LONG).show()

            }

        }
    }
    private fun insertIntoFireBase(
        username:String,
        phoneNum:String,
        password:String
    ): User {
        return User(username,phoneNum,password)

    }
    private fun isNumber(s: String?): Boolean {
        return if (s.isNullOrEmpty()) false else s.all { Character.isDigit(it) }
    }

}
