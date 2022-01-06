package com.example.mypackagedeliver.Entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * class that represent a package
 */
@Entity(tableName = "parcel_data_table")
data class Parcel
    (
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "parcel_id")
    var pkg_id: Int,
    @ColumnInfo(name = "owner")
    val owner: String,
    @ColumnInfo(name = "owner_address")
    val owner_address: String,
    @ColumnInfo(name = "type")
    val type: String,
    @ColumnInfo(name = "weight")
    val weight: String,
    @ColumnInfo(name = "lng")
    val lng: String,
    @ColumnInfo(name = "lat")
    val lat: String,
    @ColumnInfo(name = "fragile")
    val fragile: String,
    @ColumnInfo(name = "phone")
    val phone: String
)
