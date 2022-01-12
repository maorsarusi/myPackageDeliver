package com.example.mypackagedeliver.Data.RegisteredParcels
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mypackagedeliver.Entities.Parcel

class PackagesViewModel: ViewModel() {

    private val users: MutableLiveData<List<Parcel>> by lazy {
        MutableLiveData<List<Parcel>>().also {
            loadUsers()
        }
    }

    fun getUsers(): LiveData<List<Parcel>> {
        return users
    }

    private fun loadUsers() {
        // Do an asynchronous operation to fetch users.
    }
}