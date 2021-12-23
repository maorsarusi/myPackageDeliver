package com.example.mypackagedeliver.Entities

/**
 * class that represent a package
 */
data class Parcel
    (
 var pkg_id: Int,
 val owner : String,
 val owner_address : String,
 val type : String,
 val weight : String,
 val lng : String,
 val lat : String,
 val fragile : String,
 val phone : String)
