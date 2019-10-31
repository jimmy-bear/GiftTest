package com.home.gifttest.ui.dashboard

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore

class DashboardViewModel : ViewModel() {

//    private val _text = MutableLiveData<String>().apply {
//        value = "This is dashboard Fragment"
    private var items=MutableLiveData<List<GameItem>>()

    fun getItems():MutableLiveData<List<GameItem>>{
        FirebaseFirestore.getInstance()
            .collection("game")
            .addSnapshotListener { querySnapshot, exception ->
                if (querySnapshot!=null&&!querySnapshot.isEmpty){
                    items.value=querySnapshot.toObjects(GameItem::class.java)
                }
            }
        return items
    }
    //val text: LiveData<String> = _text
}