package com.example.mypackagedeliver.UI.MainActivity


import android.Manifest
import android.annotation.SuppressLint
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.InputType
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.app.ActivityCompat
import com.example.mypackagedeliver.R
import com.example.mypackagedeliver.Entities.*
import com.example.mypackagedeliver.UI.Login.LoginActivity
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.getValue


class Home : AppCompatActivity() {
    private var firebaseAuth: FirebaseAuth? = null
    private var firebaseDatabase: FirebaseDatabase? = null
    private var currentEmail: String = ""
    private var currentName: String = ""
    private var pktType: String = ""
    private var pktWeight: String = ""
    private var pktOwnerName: String = ""
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var myLocation: Location
    private  var  lng : String = (-1.0).toString()
    private   var lat : String = (-1.0).toString()

    @SuppressLint("UseSwitchCompatOrMaterialCode", "SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)


        currentName = intent.getStringExtra("name").toString()
        currentEmail = intent.getStringExtra("email").toString()
        firebaseAuth = FirebaseAuth.getInstance()
        firebaseDatabase = FirebaseDatabase.getInstance()
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        getLocation()

        val helloView: TextView = findViewById(R.id.helloView)
        helloView.text = "Hello $currentName!"

        val buttonLogout: AppCompatTextView = findViewById(R.id.appCompatTextViewLogoutLink)
        buttonLogout.setOnClickListener {
            firebaseAuth!!.signOut()
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        val dialog  = AlertDialog.Builder(this)
        dialog.setTitle("Change Location")
        //     val lng = myLocation.longitude.toString()
        dialog.setMessage("Pay attention!, the LNG and LAT has been filled automatic,do you want to change them?")
        dialog.setPositiveButton("yes", DialogInterface.OnClickListener { dialog, id -> setLngLat() })
        dialog.setNegativeButton("no",DialogInterface.OnClickListener{
                dialog, id -> dialog.cancel()
        })
        dialog.show()

        typeSpinner()
        weightSpinner()
        getNamesPackages(object : GetNameOfPkt {
            override fun onGet(value: Array<String>?) {
                if (value != null) {
                    addresseeSpinner(value)
                }
            }
        })


        val buttonStore: AppCompatButton = findViewById(R.id.appCompatButtonStore)
        buttonStore.setOnClickListener {
            val phoneNumberET = findViewById<EditText>(R.id.textInputEditTextPhone)
            val phoneNumberString = phoneNumberET.text.toString()

            val fragileSwitch = findViewById<Switch>(R.id.pkt_fragile_switch)
            val isFragile = fragileSwitch.isChecked.toString()

            val typeSpinner = findViewById<Spinner>(R.id.pkt_type_spinner)
            val weighSpinner = findViewById<Spinner>(R.id.pkt_weight_spinner)
            val addresseeSpinner = findViewById<Spinner>(R.id.pkt_addressee_spinner)


            when {
                weighSpinner.selectedItemPosition == 0 -> {
                    Toast.makeText(
                        applicationContext,
                        "Please select the package weight",
                        Toast.LENGTH_LONG
                    ).show()
                }
                typeSpinner.selectedItemPosition == 0 -> {
                    Toast.makeText(
                        applicationContext,
                        "Please select the package type",
                        Toast.LENGTH_LONG
                    ).show()
                }
                addresseeSpinner.selectedItemPosition == 0 -> {
                    Toast.makeText(
                        applicationContext,
                        "Please select who the package is for",
                        Toast.LENGTH_LONG
                    ).show()
                }
                else -> {
                    if(lng == "-1.0" && lat == "-1.0")
                    {
                        lng = myLocation.longitude.toString()
                        lat = myLocation.latitude.toString()
                    }


                    val pktId = firebaseDatabase!!.getReference("packages").push().key.toString()
                    val pack =
                        Parcel(
                            pktWeight,
                            pktType,
                            pktOwnerName,
                            phoneNumberString,
                            isFragile,
                            lng,
                            lat,
                            currentName,
                            pktId
                        )

                    firebaseDatabase!!.getReference("packages")
                        .child(pktId)
                        .setValue(pack)
                        .addOnSuccessListener {
                            Toast.makeText(
                                this@Home,
                                "You have send the package",
                                Toast.LENGTH_SHORT
                            ).show()
                        }

                    typeSpinner.setSelection(0)
                    weighSpinner.setSelection(0)
                    addresseeSpinner.setSelection(0)
                    fragileSwitch.isChecked = false
                    phoneNumberET.setText("")
                }
            }
        }

    }

    private fun setLngLat() {
        val dialog  = AlertDialog.Builder(this)
        dialog.setTitle("Change Location")
        val layout : LinearLayout = LinearLayout(this)
        layout.orientation = LinearLayout.VERTICAL

        //     val lng = myLocation.longitude.toString()
        dialog.setMessage("insert lng and lat")
        val inputLng = EditText(this)
        inputLng.hint = "Enter lng"
        layout.addView(inputLng)


        val inputLat = EditText(this)
        inputLat.hint = "Enter lat"
        inputLat.inputType = InputType.TYPE_CLASS_TEXT
        layout.addView(inputLat)
        dialog.setView(layout)
        dialog.setPositiveButton("finish", DialogInterface.OnClickListener {
                dialog, which -> lng = inputLng.text.toString()
                lat =inputLat.text.toString()
        })
        dialog.setNegativeButton("cancel", DialogInterface.OnClickListener { dialog, which -> dialog.cancel() })
        dialog.show()

    }

    private fun typeSpinner() {
        val systemTypes: MutableList<String> = mutableListOf("")
        for (curType in Types.values())
            systemTypes.add(curType.type)
        val arrayAdapter =
            ArrayAdapter(this, android.R.layout.simple_spinner_item, systemTypes)
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
                    1 -> {
                        pktType = Types.Envelop.type
                    }
                    2 -> {
                        pktType = Types.Small.type
                    }
                    3 -> {
                        pktType = Types.Big.type
                    }
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                pktType = "Unknown"
            }
        }
    }

    private fun weightSpinner() {
        val systemWeights: MutableList<String> = mutableListOf("")
        for (curWeight in Weights.values())
            systemWeights.add(curWeight.weight)
        val arrayAdapter =
            ArrayAdapter(this, android.R.layout.simple_spinner_item, systemWeights)
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
                    1 -> {
                        pktWeight = Weights.HalfKg.weight
                    }
                    2 -> {
                        pktWeight = Weights.Kg.weight
                    }
                    3 -> {
                        pktWeight = Weights.FiveKg.weight
                    }
                    4 -> {
                        pktWeight = Weights.TwentyKg.weight
                    }
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                pktWeight = "Unknown"
            }
        }
    }

    private fun addresseeSpinner(systemUsers: Array<String>) {
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
                if (position != 0) {
                    pktOwnerName = systemUsers[position]
                    addSpinner.setSelection(position)
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                pktOwnerName = "Unknown"
            }
        }
    }

    private fun getNamesPackages(getNameOfPkt: GetNameOfPkt) {
        val pktListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val systemUsers: MutableList<String> = mutableListOf("")
                for (curUser in dataSnapshot.children) {
                    val user = curUser.getValue<User>()
                    systemUsers.add(user!!.first_name + " " + user.last_name)
                }
                getNameOfPkt.onGet(systemUsers.toTypedArray())
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        }
        firebaseDatabase!!.getReference("users").addValueEventListener(pktListener)
    }

    private fun getLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                1
            )
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                2
            )
            return
        }
        fusedLocationClient.lastLocation
            .addOnSuccessListener { location: Location? ->
                myLocation = location!!
            }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            1 -> {
                // If request is cancelled, the result arrays are empty.
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    getLocation()
                }
            }
            2 -> {
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    getLocation()
                } else {
                    Toast.makeText(this, "Location request was denied", Toast.LENGTH_LONG).show()
                }
            }
        }
    }
}

interface GetNameOfPkt {
    fun onGet(value: Array<String>?)
}

