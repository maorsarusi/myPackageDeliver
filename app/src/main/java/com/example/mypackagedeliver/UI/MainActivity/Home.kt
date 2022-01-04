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
        val user = intent.getStringExtra("user")
        val pass = intent.getStringExtra("password_id")
        val x = FirebaseAuth.getInstance().currentUser
        val name = x?.email
        val hellov: TextView = findViewById(R.id.helloView)
        hellov.text = "hello $name"
        val lobutton: Button = findViewById(R.id.logoutButton)
        lobutton.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }

        val button: Button = findViewById(R.id.storage)
        var count: Int = 1

        val arrayAdapter = ArrayAdapter<Types>(this, android.R.layout.simple_spinner_item, Types.values())

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
                when (position) {
                    0 -> {typeText.text = Types.Envelop.type
                          type = Types.Envelop.type}
                    1 -> { typeText.text = Types.Small.type
                           type = Types.Small.type}
                    2 -> {typeText.text = Types.Big.type
                             type = Types.Big.type}
                }
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

            val editText6 = findViewById<EditText>(R.id.editTextPhone)

            val phone = editText6.text.toString()


            val checkBox = findViewById<CheckBox>(R.id.checkBox)
            val answer = checkBox.isChecked


            val fragile: String = if (answer) {
                "yes"
            } else {
                "no"
            }


            val pack = insertIntoFireBase(count, owner, type, weight, lng, lat, address, fragile, phone)

            packeges_db.child(count.toString()).setValue(pack)
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
        fragile: String,
        phone: String
    ): Parcel {
        return Parcel(count, owner, address, type, weight, lng, lat, fragile, phone)

    }

    private fun isNumber(s: String?): Boolean {
        return if (s.isNullOrEmpty()) false else s.all { Character.isDigit(it) }
    }

}

