package com.example.mypackagedeliver.UI.Login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatTextView
import com.example.mypackagedeliver.Entities.User
import com.example.mypackagedeliver.R
import com.example.mypackagedeliver.UI.MainActivity.Home
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.DatabaseError

import com.google.firebase.database.DataSnapshot

import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.getValue


class LoginActivity : AppCompatActivity() {
    private var firebaseAuth: FirebaseAuth? = null
    private var firebaseDatabase: FirebaseDatabase? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val email = findViewById<EditText>(R.id.textInputEditTextEmail)
        val password = findViewById<EditText>(R.id.textInputEditTextPassword)

        firebaseDatabase = FirebaseDatabase.getInstance()
        firebaseAuth = FirebaseAuth.getInstance()

        val buttonRegister: AppCompatTextView = findViewById(R.id.textViewLinkRegister)
        buttonRegister.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }


        val buttonLogin: AppCompatButton = findViewById(R.id.appCompatButtonLogin)
        buttonLogin.setOnClickListener {

            val emailString = email.text.toString().trim { it <= ' ' }
            val passwordString = password.text.toString().trim { it <= ' ' }

            when {
                emailString.isEmpty() -> {
                    email.error = "Please provide email id"
                    email.requestFocus()
                }
                passwordString.isEmpty() -> {
                    password.error = "Please provide password"
                    password.requestFocus()
                }
                else -> {
                    firebaseAuth!!.signInWithEmailAndPassword(emailString, passwordString)
                        .addOnCompleteListener { task ->
                            if (!task.isSuccessful) {
                                Toast.makeText(
                                    this,
                                    "Login Error ,Please try again",
                                    Toast.LENGTH_SHORT
                                ).show()
                            } else {
                                firebaseDatabase!!.getReference("users")
                                    .child(task.result!!.user!!.uid)
                                    .addValueEventListener(object : ValueEventListener {
                                        override fun onDataChange(dataSnapshot: DataSnapshot) {
                                            val userDetail = dataSnapshot.getValue<User>()
                                            val name: String =
                                                userDetail!!.first_name + " " + userDetail.last_name
                                            val intent =
                                                Intent(applicationContext, Home::class.java)
                                            Toast.makeText(
                                                applicationContext,
                                                "Login Successful",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                            intent.putExtra("name", name)
                                            intent.putExtra("email", userDetail.email)
                                            startActivity(intent)
                                            finish()
                                        }

                                        override fun onCancelled(error: DatabaseError) {}
                                    })
                            }
                        }

                }
            }

        }
    }
}