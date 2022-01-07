package com.example.mypackagedeliver.Entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * class that represent a package
 */
@Entity(tableName = "parcel_package_table")
data class PackageParcel
    (
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "addressee")
    var addressee: String,
    @ColumnInfo(name = "fragile")
    val fragile: String,
    @ColumnInfo(name = "latitude")
    val latitude: String,
    @ColumnInfo(name = "longitude")
    val longitude: String,
    @ColumnInfo(name = "pktId")
    val pktId: String,
    @ColumnInfo(name = "phone")
    val phone: String,
    @ColumnInfo(name = "sender")
    val sender: String,
    @ColumnInfo(name = "type")
    val type: String,
    @ColumnInfo(name = "weight")
    val weight: String,
)
