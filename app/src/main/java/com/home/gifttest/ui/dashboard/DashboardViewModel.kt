package com.home.gifttest.ui.dashboard

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.home.gifttest.GameRoomItem

class DashboardViewModel : ViewModel() {

//    private val _text = MutableLiveData<String>().apply {
//        value = "This is dashboard Fragment"
    private var myRoomItems=MutableLiveData<List<GameRoomItem>>()
    private val joinRoomItems=MutableLiveData<List<GameRoomItem>>()
    fun getMyRoomItems():MutableLiveData<List<GameRoomItem>>{
        FirebaseFirestore.getInstance()
            .collection("rooms")
            .whereEqualTo("userID",FirebaseAuth.getInstance().currentUser!!.uid)
            .limit(3)
            .addSnapshotListener { querySnapshot, exception ->
                if (querySnapshot!=null&&!querySnapshot.isEmpty){
                    myRoomItems.value=querySnapshot.toObjects(GameRoomItem::class.java)
                }

            }
        return myRoomItems
    }
    //val text: LiveData<String> = _text
}