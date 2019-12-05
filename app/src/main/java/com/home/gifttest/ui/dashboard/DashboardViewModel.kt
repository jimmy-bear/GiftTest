package com.home.gifttest.ui.dashboard

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.home.gifttest.GameRoomItem

class DashboardViewModel : ViewModel() {

    //    private val _text = MutableLiveData<String>().apply {
//        value = "This is dashboard Fragment"
    private var creatRoomItems = MutableLiveData<List<GameRoomItem>>()
    private val joinRoomLiveData = MyJoinLiveData()
    //Dash獲得創建資料
    fun getCreatRoomItems(): MutableLiveData<List<GameRoomItem>> {
        FirebaseFirestore.getInstance()
            .collection("rooms")
            .whereEqualTo("userID", FirebaseAuth.getInstance().currentUser!!.uid)
            .limit(5)
            .addSnapshotListener { querySnapshot, exception ->
                if (querySnapshot != null && !querySnapshot.isEmpty) {
                    val list= mutableListOf<GameRoomItem>()
                    for (doc in querySnapshot.documents){
                        val item=doc.toObject(GameRoomItem::class.java)?: GameRoomItem()
                        item.documentID=doc.id
                        list.add(item)
                    }
                    creatRoomItems.value = list
                }

            }
        return creatRoomItems
    }

    //Dash獲得加入的房間資訊
    fun getJoinRoomItems(): LiveData<List<GameRoomItem>> {
        return joinRoomLiveData
    }
    //(1=通過審核，0=未通過，預設顯示已通過)
    fun setVerify(isVerify:Boolean){
        joinRoomLiveData.setVerify(isVerify)
    }

    //for(報名房間){
//for(房間ID){讀房間資訊}}
}