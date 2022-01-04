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
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class RegisterActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        val buttonLogin: AppCompatTextView = findViewById(R.id.appCompatTextViewLoginLink)
        buttonLogin.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }


        val firstName = findViewById<EditText>(R.id.textInputEditTextFirstName)
        val lastName = findViewById<EditText>(R.id.textInputEditTextLastName)
        val email = findViewById<EditText>(R.id.textInputEditTextEmail)
        val address = findViewById<EditText>(R.id.textInputEditTextAddress)
        val idNum = findViewById<EditText>(R.id.textInputEditTextID)
        val password = findViewById<EditText>(R.id.textInputEditTextPassword)
        val confirmPassword = findViewById<EditText>(R.id.textInputEditTextConfirmPassword)

        val regButton: AppCompatButton = findViewById(R.id.appCompatButtonRegister)
        regButton.setOnClickListener {

            val firstNameString = firstName.text.toString().trim { it <= ' ' }
            val lastNameString = lastName.text.toString().trim { it <= ' ' }
            val emailString = email.text.toString().trim { it <= ' ' }
            val addressString = address.text.toString().trim { it <= ' ' }
            val idNumString = idNum.text.toString().trim { it <= ' ' }
            val passwordString = password.text.toString().trim { it <= ' ' }
            val confirmPasswordString = confirmPassword.text.toString().trim { it <= ' ' }
            val database = Firebase.database

            if (!((firstNameString.isBlank()) || (lastNameString.isBlank()) || emailString.isBlank()
                        || addressString.isBlank() || idNumString.isBlank() || passwordString == ""
                        || confirmPasswordString != passwordString)
            ) {
                FirebaseAuth.getInstance()
                    .createUserWithEmailAndPassword(emailString, passwordString)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            val firebaseUser: FirebaseUser = task.result!!.user!!
                            Toast.makeText(
                                this,
                                "You have successfully registered",
                                Toast.LENGTH_SHORT
                            )
                                .show()
                            val intent = Intent(this, LoginActivity::class.java)
                            intent.putExtra("user_id", firebaseUser.uid)
                            intent.putExtra("password_id", passwordString)

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
