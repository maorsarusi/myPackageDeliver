package com.example.mypackagedeliver.UI.MainActivity


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatTextView
import com.example.mypackagedeliver.R
import com.example.mypackagedeliver.Entities.*
import com.example.mypackagedeliver.UI.Login.LoginActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.getValue
import java.lang.Package


class Home : AppCompatActivity() {
    var firebaseAuth: FirebaseAuth? = null
    var firebaseDatabase: FirebaseDatabase? = null
    var currentEmail: String? = ""
    var currentName: String? = ""
    var pkt_type: String? = ""
    var pkt_weight: String? = ""
    var pkt_owner_name: String? = ""
    var pkt_lat: String? = ""
    var pkt_lon: String? = ""

    var pkt_id: String? = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)


        currentName = intent.getStringExtra("name")
        currentEmail = intent.getStringExtra("email")
        firebaseAuth = FirebaseAuth.getInstance()
        firebaseDatabase = FirebaseDatabase.getInstance()

        val helloView: TextView = findViewById(R.id.helloView)
        helloView.text = "Hello $currentName!"

        val buttonLogout: AppCompatTextView = findViewById(R.id.appCompatTextViewLogoutLink)
        buttonLogout.setOnClickListener {
            firebaseAuth!!.signOut()
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }

        typeSpinner()
        weightSpinner()
        addresseeSpinner()

        val buttonStore: AppCompatButton = findViewById(R.id.appCompatButtonStore)
        buttonStore.setOnClickListener {
            // Write a pkt to the database
            val packagesDB = firebaseDatabase!!.getReference("packages")

            val phoneNumberET = findViewById<EditText>(R.id.textInputEditTextPhone)
            val phoneNumberString = phoneNumberET.text.toString()

            val fragileSwitch = findViewById<Switch>(R.id.pkt_fragile_switch)
            val isFragile = fragileSwitch.isChecked.toString()

            val currentPkt = Package(
                pkt_weight,
                pkt_type,
                pkt_owner_name,
                phoneNumberString,
                isFragile,
                pkt_lon,
                pkt_lat,
                currentName
            )

            countPackages(object : MyCallback {
                override fun onCallback(value: Int?) {
                    firebaseDatabase!!.getReference("packages").child(value.toString())
                        .setValue(currentPkt)
                        .addOnSuccessListener {
                            Toast.makeText(
                                this@Home,
                                "You have send the package",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                }
            })


//
//            val pack =
//                insertIntoFireBase(
//                    count,
//                    owner,
//                    pkt_type,
//                    weight,
//                    lng,
//                    lat,
//                    address,
//                    fragile,
//                    phone
//                )
//
//            packagesDB.child(count.toString()).setValue(pack)
//            Toast.makeText(
//                applicationContext,
//                "the package with id $count is stored",
//                Toast.LENGTH_LONG
//            ).show()
//            count++
        }

    }

    private fun typeSpinner() {
        val arrayAdapter =
            ArrayAdapter(this, android.R.layout.simple_spinner_item, Types.values())
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        val typeSpinner = findViewById<Spinner>(R.id.pkt_type_spinner)
        typeSpinner.prompt = "Select a package type"
        typeSpinner.setSelection(0, false)
        typeSpinner.adapter = arrayAdapter

        typeSpinner.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                when (position) {
                    0 -> {
                        pkt_type = Types.Envelop.type
                    }
                    1 -> {
                        pkt_type = Types.Small.type
                    }
                    2 -> {
                        pkt_type = Types.Big.type
                    }
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                pkt_type = "Unknown"
            }
        }
    }

    private fun weightSpinner() {
        val arrayAdapter =
            ArrayAdapter(this, android.R.layout.simple_spinner_item, Weights.values())
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        val weightSpinner = findViewById<Spinner>(R.id.pkt_weight_spinner)
        weightSpinner.prompt = "Select the weight of the package"
        weightSpinner.setSelection(0, false)
        weightSpinner.adapter = arrayAdapter

        weightSpinner.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                when (position) {
                    0 -> {
                        pkt_weight = Weights.HalfKg.weight
                    }
                    1 -> {
                        pkt_weight = Weights.Kg.weight
                    }
                    2 -> {
                        pkt_weight = Weights.FiveKg.weight
                    }
                    3 -> {
                        pkt_weight = Weights.TwentyKg.weight
                    }
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                pkt_weight = "Unknown"
            }
        }
    }

    private fun addresseeSpinner() {
        val systemUsers: MutableList<String> = mutableListOf()

        val userListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (curUser in dataSnapshot.children) {
                    val user = curUser.getValue<User>()
                    systemUsers.add(user!!.first_name + " " + user.last_name)
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        }
        firebaseDatabase!!.getReference("users").addValueEventListener(userListener)

        val arrayAdapter =
            ArrayAdapter(this, android.R.layout.simple_spinner_item, systemUsers)
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        val addSpinner = findViewById<Spinner>(R.id.pkt_addressee_spinner)
        addSpinner.prompt = "Select who the package is for"
        addSpinner.setSelection(0, false)
        addSpinner.adapter = arrayAdapter

        addSpinner.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                pkt_owner_name = systemUsers[position]
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                pkt_owner_name = "Unknown"
            }
        }
    }

    private fun countPackages(myCallback: MyCallback) {
        val pktListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                myCallback.onCallback(dataSnapshot.children.count())
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        }
        firebaseDatabase!!.getReference("users").addValueEventListener(pktListener)
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

}

interface MyCallback {
    fun onCallback(value: Int?)
}

