package com.home.gifttest.ui.home

import androidx.lifecycle.LiveData
import com.google.firebase.firestore.FirebaseFirestore
import com.home.gifttest.GameRoomItem

class DetailRoomLiveData :LiveData<GameRoomItem>(){
    override fun onActive() {
        super.onActive()
    }
    override fun onInactive() {
        super.onInactive()
    }

    fun setRoomid(setroom: String) {
        FirebaseFirestore.getInstance()
            .collection("rooms")
            .document(setroom)
            .get()
            .addOnSuccessListener {
                value=it.toObject(GameRoomItem::class.java)
            }
    }


}