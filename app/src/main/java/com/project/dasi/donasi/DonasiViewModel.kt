package com.project.dasi.donasi

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore

class DonasiViewModel : ViewModel() {

    private val donateList = MutableLiveData<ArrayList<DonasiModel>>()
    private val listItems = ArrayList<DonasiModel>()
    private val TAG = DonasiViewModel::class.java.simpleName

    fun setListDonate() {
        listItems.clear()


        try {
            FirebaseFirestore.getInstance().collection("alarm")
                .get()
                .addOnSuccessListener { documents ->
                    for (document in documents) {
                        val model = DonasiModel()
                        model.uid = document.data["uid"].toString()
                        model.name = document.data["name"].toString()
                        model.dateStart = document.data["dateStart"] as Long?
                        model.dateEnd = document.data["dateEnd"] as Long?
                        model.description = document.data["description"].toString()
                        model.image = document.data["image"].toString()
                        model.owner = document.data["owner"].toString()
                        model.ownerId = document.data["ownerId"].toString()
                        model.donateValue = document.data["donateValue"] as Long?
                        model.nominal = document.data["nominal"] as Long?

                        listItems.add(model)
                    }
                    donateList.postValue(listItems)
                }
                .addOnFailureListener { exception ->
                    Log.w(TAG, "Error getting documents: ", exception)
                }
        } catch (error: Exception) {
            error.printStackTrace()
        }
    }

    fun getDonateList() : LiveData<ArrayList<DonasiModel>> {
        return donateList
    }

}