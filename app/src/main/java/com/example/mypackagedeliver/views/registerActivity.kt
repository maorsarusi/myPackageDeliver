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

class registerActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        val userName = findViewById<EditText>(R.id.username)
        val password = findViewById<EditText>(R.id.password)

        val button: Button = findViewById(R.id.register)
        button.setOnClickListener{
            val database = Firebase.database
            if(!(userName.text.equals(""))||password.text.equals("")){
                val users_db = database.getReference("users")
                val userNameString = userName.text.toString()
                val passwordString=password.text.toString()
                val user = insertIntoFireBase(userNameString,passwordString)
                users_db.child(user.username).setValue(user)
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
        password:String
    ): User {
        return User(username,password)

    }
    private fun isNumber(s: String?): Boolean {
        return if (s.isNullOrEmpty()) false else s.all { Character.isDigit(it) }
    }

}
