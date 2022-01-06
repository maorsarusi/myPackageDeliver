package com.example.mypackagedeliver.Entities

import androidx.lifecycle.LiveData
import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface ParcelDAO {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertParcel(parcel: Parcel)

    //@Query(select * FROM parcel_data_table ORDER BY parcel_id ASC)
    suspend fun readAllData(): LiveData<List<Parcel>>
}