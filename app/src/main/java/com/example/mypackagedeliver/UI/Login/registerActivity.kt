package com.example.mypackagedeliver.UI.Login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.mypackagedeliver.R
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
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
        button.setOnClickListener {

            val userNameString = userName.text.toString().trim { it <= ' ' }
            val passwordString = password.text.toString().trim { it <= ' ' }
            val phoneNumString = phoneNum.text.toString().trim { it <= ' ' }
            val database = Firebase.database

            if (!((userNameString == "") || passwordString == "" || phoneNumString == "")) {
                FirebaseAuth.getInstance()
                    .createUserWithEmailAndPassword(userNameString, passwordString)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            val firebaseUser: FirebaseUser = task.result!!.user!!
                            Toast.makeText(
                                this,
                                "you are registed successfully",
                                Toast.LENGTH_SHORT
                            )
                                .show()
                            val intent = Intent(this, loginActivity::class.java)
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
                val users_db = database.getReference("users")

                val user = insertIntoFireBase(userNameString, phoneNumString, passwordString)
                users_db.child(phoneNumString).setValue(user)
                Toast.makeText(
                    applicationContext,
                    "The registration was successful",
                    Toast.LENGTH_LONG
                ).show()
                val intent = Intent(this, loginActivity::class.java)
                startActivity(intent)
            } else {
                Toast.makeText(applicationContext, "Registration failed", Toast.LENGTH_LONG).show()

            }

        }
    }

    private fun insertIntoFireBase(
        username: String,
        phoneNum: String,
        password: String
    ): User {
        return User(username, phoneNum, password)

    }


}
