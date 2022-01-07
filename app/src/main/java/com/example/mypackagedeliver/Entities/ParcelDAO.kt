package com.example.mypackagedeliver.Entities

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface ParcelDAO {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertParcel(parcel: PackageParcel)

    //@Query(select * FROM parcel_data_table ORDER BY parcel_id ASC)
    suspend fun readAllData(): LiveData<List<PackageParcel>>
}