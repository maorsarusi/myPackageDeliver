package com.example.mypackagedeliver.Data.RegisteredParcels
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mypackagedeliver.Entities.UserParcel

class PackagesViewModel: ViewModel() {

    private val users: MutableLiveData<List<UserParcel>> by lazy {
        MutableLiveData<List<UserParcel>>().also {
            loadUsers()
        }
    }

    fun getUsers(): LiveData<List<UserParcel>> {
        return users
    }

    private fun loadUsers() {
        // Do an asynchronous operation to fetch users.
    }
}