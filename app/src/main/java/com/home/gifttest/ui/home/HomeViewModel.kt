package com.home.gifttest.ui.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.home.gifttest.GameRoomItem
import com.home.gifttest.ui.notifications.PersonItem

class HomeViewModel : ViewModel() {
    private var roomItems=MutableLiveData<List<GameRoomItem>>()
    private var joinLiveData=DetailJoinQueryLiveData()
    private var creatLiveData=DetailCreatLiveData()
    private var detailRoomLiveData=DetailRoomLiveData()
    private val TAG=HomeViewModel::class.java.simpleName
    //var roomID=MutableLiveData<String>()
    //private var joinItems=MutableLiveData<List<PersonItem>>()
    //private var creatItem=MutableLiveData<PersonItem>()

    //用在homeFragment顯示房間資訊
    fun getRoomItems():MutableLiveData<List<GameRoomItem>> {
//        FirebaseFirestore.getInstance()
//            .collection("rooms")
//            .addSnapshotListener { querySnapshot, exception ->
//                if (querySnapshot!=null&&!querySnapshot.isEmpty){
//                    val roomList= mutableListOf<GameRoomItem>()
//                    for (doc in querySnapshot.documents){
//                        //null就給空字串
//                        val item=doc.toObject(GameRoomItem::class.java)?:GameRoomItem()
//                        item.documentID=doc.id
//                        roomList.add(item)
//                    }
//                    roomItems.value=roomList
//                }
//            }
        loadRoomData()
        return roomItems
    }
    fun updateRoom(){
        loadRoomData()
    }
    private fun loadRoomData() {
        val userUid=FirebaseAuth.getInstance().currentUser?.uid
        FirebaseFirestore.getInstance()
            .collection("rooms")
            .get().addOnSuccessListener { roomSnapshot ->
                if (roomSnapshot != null && !roomSnapshot.isEmpty) {
                    val roomList = mutableListOf<GameRoomItem>()
                    FirebaseFirestore.getInstance()
                        .collection("user")
                        .document(userUid!!)
                        .collection("joinRoom")
                        .get().addOnSuccessListener { joinSanpshot ->
                            if (joinSanpshot != null) {
                                for (roomdoc in roomSnapshot.documents) {

                                    //檢查是否已經報名過
                                    var isRepeat = false
                                    for (joindoc in joinSanpshot.documents) {
                                        if (roomdoc.id == joindoc.id)
                                            isRepeat = true
                                    }
                                    if (!isRepeat) {
                                        val item = roomdoc.toObject(GameRoomItem::class.java)
                                            ?: GameRoomItem()
                                        item.documentID = roomdoc.id
                                        //只顯示別人開創的房間
                                        if(item.userID!=userUid)
                                        roomList.add(item)
                                    }
                                }
                                roomItems.value = roomList
                            }
                        }
                }
            }
    }


    //為了在detailFragment取得Room資訊
    fun getDetailRoomItem():LiveData<GameRoomItem>{
        return detailRoomLiveData
    }
    //用在detailFrgment顯示開房者資訊
    fun getCreatDetailItem():LiveData<PersonItem>{
//        FirebaseFirestore.getInstance()
//            .collection("user")
//            .document("n7qVbjdJpBTBrKy8eV9dV27kq7g1")
//            .get().addOnSuccessListener {
//                creatItem.value=it.toObject(PersonItem::class.java)
//            }
        return creatLiveData
    }

    //HomeFragment設定點選的房間ID
    fun setRoomId(setroom:String){
        joinLiveData.setRoomid(setroom)
        creatLiveData.setRoomid(setroom)
        detailRoomLiveData.setRoomid(setroom)
    }

    //用在detailFrgment顯示參加者資訊
    fun getJoinDetailItems(): LiveData<List<PersonItem>> {
//        FirebaseFirestore.getInstance()
//            .collection("rooms")
//            .document(roomID.value!!)
//            .collection("joins")
//            .addSnapshotListener { querySnapshot, exception ->
//                if (querySnapshot!=null&&!querySnapshot.isEmpty) {
//                    joinItems.value = querySnapshot.toObjects(PersonItem::class.java)
//                }
//            }
//        return joinItems
        return joinLiveData
    }

}