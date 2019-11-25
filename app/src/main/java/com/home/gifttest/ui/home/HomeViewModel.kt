package com.home.gifttest.ui.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
import com.home.gifttest.GameRoomItem

class HomeViewModel : ViewModel() {
    private var items=MutableLiveData<List<GameRoomItem>>()
    private val list= mutableListOf<GameRoomItem>()
    fun getItems():MutableLiveData<List<GameRoomItem>>{
        FirebaseFirestore.getInstance()
            .collection("rooms")
            .addSnapshotListener { querySnapshot, exception ->
                if (querySnapshot!=null&&!querySnapshot.isEmpty){
                    for (doc in querySnapshot.documents){
                        //null就給空字串
                        val item=doc.toObject(GameRoomItem::class.java)?:GameRoomItem()
                        item.documentID=doc.id
                        list.add(item)
                    }
                    items.value=list
                    //items.value=querySnapshot.toObjects(GameRoomItem::class.java)
                }
            }
        return items
    }
}