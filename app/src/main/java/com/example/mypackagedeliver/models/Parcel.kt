package com.example.mypackagedeliver.models

/**
 * class that represent a package
 */
data class Parcel
    (val pkg_id: Int,
     val owner : String,
     val owner_address : String,
     val type : String,
     val weight : String,
     val lng : String,
     val lat : String,
     val fragile : String)
