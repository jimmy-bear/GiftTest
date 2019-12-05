package com.home.gifttest.ui.home

import androidx.lifecycle.LiveData
import com.google.firebase.firestore.FirebaseFirestore
import com.home.gifttest.GameRoomItem
import com.home.gifttest.ui.notifications.PersonItem

class DetailCreatLiveData :LiveData<PersonItem>(){
//    var query=FirebaseFirestore.getInstance()
//        .collection("empty")
//        .document("000empty")


    override fun onActive() {
        super.onActive()
//        query.get().addOnSuccessListener{
//            value=it.toObject(PersonItem::class.java)
//        }
    }

    override fun onInactive() {
        super.onInactive()
    }

    fun setRoomid(setroom: String) {
        //需修改
        FirebaseFirestore.getInstance()
            .collection("rooms")
            .document(setroom).get().addOnSuccessListener {snap->
                FirebaseFirestore.getInstance()
                    .collection("user")
                    .document(snap.toObject(GameRoomItem::class.java)!!.userID)
                    .get().addOnSuccessListener {
                        value=it.toObject(PersonItem::class.java)
                    }
            }
    }
}