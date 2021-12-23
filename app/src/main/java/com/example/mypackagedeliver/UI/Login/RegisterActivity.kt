package com.example.mypackagedeliver.UI.Login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.mypackagedeliver.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class RegisterActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        val buttonLogin: Button = findViewById(R.id.loginButton)
        buttonLogin.setOnClickListener {
            onBackPressed()
        }

        val email = findViewById<EditText>(R.id.email)
        val phoneNum = findViewById<EditText>(R.id.phoneNumber)
        val password = findViewById<EditText>(R.id.password)

        val button: Button = findViewById(R.id.register)
        button.setOnClickListener {

            val emailString = email.text.toString().trim { it <= ' ' }
            val passwordString = password.text.toString().trim { it <= ' ' }
            val phoneNumString = phoneNum.text.toString().trim { it <= ' ' }
            val database = Firebase.database

            if (!((emailString == "") || passwordString == "" || phoneNumString == "")) {
                FirebaseAuth.getInstance()
                    .createUserWithEmailAndPassword(emailString, passwordString)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            val firebaseUser: FirebaseUser = task.result!!.user!!
                            Toast.makeText(
                                this,
                                "you are registed successfully",
                                Toast.LENGTH_SHORT
                            )
                                .show()
                            val intent = Intent(this, LoginActivity::class.java)
                            intent.putExtra("user_id", firebaseUser.uid)
                            intent.putExtra("password_id", passwordString)
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
