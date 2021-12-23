package com.example.mypackagedeliver.UI.MainActivity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import com.example.mypackagedeliver.R
import com.example.mypackagedeliver.Entities.*
import com.example.mypackagedeliver.UI.Login.LoginActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase


class Home : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        /*
        * binding the logout bouttn and the hello textview
        * */
        val user = intent.getStringExtra("user_id")
        val pass = intent.getStringExtra("password_id")
        val hellov: TextView = findViewById(R.id.helloView)
        hellov.text = "hello $user"
        val lobutton: Button = findViewById(R.id.logoutButton)
        lobutton.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }

        val button: Button = findViewById(R.id.storage)
        var count: Int = 1
        /// array for the spinner
        val typesArray = arrayOf("Envelop", "Big package", "Small package")
        val arrayAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, typesArray)

        val spinner: Spinner = findViewById<Spinner>(R.id.types)
        spinner.adapter = arrayAdapter


        var type: String = ""
        val typeText = findViewById<TextView>(R.id.typeTexteView)

        spinner.onItemSelectedListener = object :

            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {

                typeText.text = typesArray[position]
                type = typesArray[position]

            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                type = "Unknown"
            }

        }


        button.setOnClickListener {


            // Write a message to the database
            val database = Firebase.database
            val packeges_db = database.getReference("packages")

            val editText = findViewById<EditText>(R.id.editTextName)
            val owner = editText.text.toString()

            val editText2 = findViewById<EditText>(R.id.editTextName2)
            val weight = editText2.text.toString()
            if (isNumber(weight))
                Toast.makeText(
                    applicationContext, "must be a number" +
                            "", Toast.LENGTH_LONG
                ).show()

            val editText3 = findViewById<EditText>(R.id.editTextName3)
            val lng = editText3.text.toString()

            val editText4 = findViewById<EditText>(R.id.editTextName4)
            val lat = editText4.text.toString()

            val editText5 = findViewById<EditText>(R.id.editTextName5)
            val address = editText5.text.toString()


            val checkBox = findViewById<CheckBox>(R.id.checkBox)
            val answer = checkBox.isChecked


            val fragile: String = if (answer) {
                "yes"
            } else {
                "no"
            }


            val pack = insertIntoFireBase(count, owner, type, weight, lng, lat, address, fragile)

            packeges_db.push().setValue(pack)
            Toast.makeText(
                applicationContext,
                "the package with id $count is stored",
                Toast.LENGTH_LONG
            ).show()
            count++
        }

    }

    private fun insertIntoFireBase(
        count: Int,
        owner: String,
        type: String,
        weight: String,
        lng: String,
        lat: String,
        address: String,
        fragile: String
    ): Parcel {
        return Parcel(count, owner, address, type, weight, lng, lat, fragile)

    }

    private fun isNumber(s: String?): Boolean {
        return if (s.isNullOrEmpty()) false else s.all { Character.isDigit(it) }
    }

}

