 package com.example.mypackagedeliver

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Toast
import com.example.mypackagedeliver.models.packeges
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

 class Home : AppCompatActivity() {
     override fun onCreate(savedInstanceState: Bundle?) {
         super.onCreate(savedInstanceState)
         setContentView(R.layout.activity_home)


         val button: Button = findViewById(R.id.storage)
         var count: Int = 1

         button.setOnClickListener {


             // Write a message to the database
             val database = Firebase.database
             val packeges_db = database.getReference("packages")

             val editText = findViewById<EditText>(R.id.editTextName)
             val owner = editText.text.toString()

             val editText2 = findViewById<EditText>(R.id.editTextName2)
             val type = editText2.text.toString()

             val editText3 = findViewById<EditText>(R.id.editTextName3)
             val weight = editText3.text.toString()

             val editText4 = findViewById<EditText>(R.id.editTextName4)
             val lng = editText4.text.toString()

             val editText5 = findViewById<EditText>(R.id.editTextName5)
             val lat = editText5.text.toString()

             val editText6 = findViewById<EditText>(R.id.editTextName6)
             val address = editText6.text.toString()

             val checkBox = findViewById<CheckBox>(R.id.checkBox)
             val answer = checkBox.isChecked
             val fragile : String

             fragile = if (answer) { "yes" }
                       else { "no" }


             val pack = insertIntoFireBase(count, owner, type, weight, lng, lat, address, fragile)

             packeges_db.push().setValue(pack)
             Toast.makeText(applicationContext, "the package with id $count is stored", Toast.LENGTH_LONG).show()
             count ++
         }

         }
     fun insertIntoFireBase(count: Int, owner: String, type: String, weight: String, lng: String, lat: String, address: String, fragile: String) :packeges {
         val pack: packeges = packeges()
         pack.pkg_id = count
         pack.owner = owner
         pack.type = type
         pack.weight = weight
         pack.lng = lng
         pack.lat = lat
         pack.owner_address = address
         pack.fragile = fragile
         return pack

     }


     }

