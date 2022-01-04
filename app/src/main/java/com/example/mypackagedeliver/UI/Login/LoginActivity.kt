package com.example.mypackagedeliver.UI.Login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatTextView
import com.example.mypackagedeliver.R
import com.example.mypackagedeliver.UI.MainActivity.Home
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val buttonRegister: AppCompatTextView = findViewById(R.id.textViewLinkRegister)
        buttonRegister.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }


        val email = findViewById<EditText>(R.id.textInputEditTextEmail)
        val password = findViewById<EditText>(R.id.textInputEditTextPassword)
        val buttonLogin: AppCompatButton = findViewById(R.id.appCompatButtonLogin)
        buttonLogin.setOnClickListener {

            val emailString = email.text.toString().trim { it <= ' ' }
            val passwordString = password.text.toString().trim { it <= ' ' }
            val database = Firebase.database

            if (!((emailString.isBlank()) || passwordString.isBlank())) {
                FirebaseAuth.getInstance()
                    .signInWithEmailAndPassword(emailString, passwordString)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            val firebaseUser: FirebaseUser = task.result!!.user!!
                            Toast.makeText(
                                this,
                                "you are login successfully",
                                Toast.LENGTH_SHORT
                            )
                                .show()
                            val intent = Intent(this, Home::class.java)
                            intent.putExtra("user", FirebaseAuth.getInstance().currentUser)
                            startActivity(intent)
                            finish()
                        } else {
                            Toast.makeText(
                                this,
                                task.exception!!.message.toString(),
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }

            }
        }

    }
}