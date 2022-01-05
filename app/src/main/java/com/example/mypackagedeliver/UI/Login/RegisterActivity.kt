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
import com.google.firebase.database.FirebaseDatabase

import android.widget.TextView
import com.example.mypackagedeliver.Entities.User


class RegisterActivity : AppCompatActivity() {

    var mFirebaseAuth: FirebaseAuth? = null
    var firebaseDatabase: FirebaseDatabase? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        val firstName = findViewById<EditText>(R.id.textInputEditTextFirstName)
        val lastName = findViewById<EditText>(R.id.textInputEditTextLastName)
        val email = findViewById<EditText>(R.id.textInputEditTextEmail)
        val address = findViewById<EditText>(R.id.textInputEditTextAddress)
        val idNum = findViewById<EditText>(R.id.textInputEditTextID)
        val password = findViewById<EditText>(R.id.textInputEditTextPassword)
        val confirmPassword = findViewById<EditText>(R.id.textInputEditTextConfirmPassword)

        mFirebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();

        val buttonLogin: AppCompatTextView = findViewById(R.id.appCompatTextViewLoginLink)
        buttonLogin.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        val regButton: AppCompatButton = findViewById(R.id.appCompatButtonRegister)
        regButton.setOnClickListener {

            val firstNameString = firstName.text.toString().trim { it <= ' ' }
            val lastNameString = lastName.text.toString().trim { it <= ' ' }
            val emailString = email.text.toString().trim { it <= ' ' }
            val addressString = address.text.toString().trim { it <= ' ' }
            val idNumString = idNum.text.toString().trim { it <= ' ' }
            val passwordString = password.text.toString().trim { it <= ' ' }
            val confirmPasswordString = confirmPassword.text.toString().trim { it <= ' ' }

            when {
                emailString.isEmpty() -> {
                    email.error = "Please provide email id";
                    email.requestFocus();
                }
                firstNameString.isEmpty() -> {
                    firstName.error = "Please provide your first name";
                    firstName.requestFocus();
                }
                lastNameString.isEmpty() -> {
                    lastName.error = "Please provide your last name";
                    lastName.requestFocus();
                }
                addressString.isEmpty() -> {
                    address.error = "Please provide your address";
                    address.requestFocus();
                }
                idNumString.isEmpty() -> {
                    idNum.error = "Please provide your id";
                    idNum.requestFocus();
                }
                idNumString.length != 9 -> {
                    idNum.error = "Please provide 9-digit id";
                    idNum.requestFocus();
                }
                passwordString.isEmpty() -> {
                    password.error = "Please provide password";
                    password.requestFocus();
                }
                confirmPasswordString.isEmpty() -> {
                    confirmPassword.error = "Please provide confirm password";
                    confirmPassword.requestFocus();
                }
                passwordString != confirmPasswordString -> {
                    confirmPassword.error = "Please provide identical Passwords";
                    confirmPassword.requestFocus();
                }
                else -> {
                    mFirebaseAuth!!.createUserWithEmailAndPassword(emailString, passwordString)
                        .addOnCompleteListener { task ->
                            if (!task.isSuccessful) {
                                Toast.makeText(
                                    this,
                                    "SignUp Unsuccessful, please Try Again!" +
                                            task.exception!!.message.toString(),
                                    Toast.LENGTH_SHORT
                                ).show()
                            } else {
                                val currentUser = User(
                                    addressString,
                                    emailString,
                                    firstNameString,
                                    lastNameString,
                                    idNumString.toInt(),
                                )
                                val uid = task.result!!.user!!.uid
                                firebaseDatabase!!.getReference("users").child(uid).setValue(currentUser)
                                    .addOnSuccessListener {
                                        if (task.isSuccessful) {
                                            Toast.makeText(
                                                this,
                                                "You have successfully registered",
                                                Toast.LENGTH_SHORT
                                            ).show()

                                            val intent = Intent(this, LoginActivity::class.java)
                                            finish()
                                        }
                                    }
                            }
                        }
                }
            }
        }
    }
}
