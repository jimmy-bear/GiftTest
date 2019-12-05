package com.home.gifttest.ui.dashboard

import androidx.lifecycle.LiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.*
import com.home.gifttest.GameRoomItem

class MyJoinLiveData :LiveData<List<GameRoomItem>>(), EventListener<QuerySnapshot> {
    private lateinit var registration: ListenerRegistration
    var isRegistration = false
    var query = FirebaseFirestore.getInstance().collection("user")
        .document(FirebaseAuth.getInstance().currentUser!!.uid)
        .collection("joinRoom")
        .whereEqualTo("accepted", 1)

    override fun onActive() {
        super.onActive()
        registration = query.addSnapshotListener(this)
        isRegistration = true
    }

    override fun onInactive() {
        super.onInactive()
        if (isRegistration) {
            registration.remove()
        }
    }

    override fun onEvent(querySnapshot: QuerySnapshot?, exception: FirebaseFirestoreException?) {
        if (querySnapshot != null && !querySnapshot.isEmpty) {
            val roomDataList = mutableListOf<GameRoomItem>()
            var loadCount = 0
            for (doc in querySnapshot.documents) {
                FirebaseFirestore.getInstance()
                    .collection("rooms")
                    .document(doc.id)
                    .get().addOnSuccessListener {
                        val item = it.toObject(GameRoomItem::class.java) ?: GameRoomItem()
                        item.documentID=doc.id
                        roomDataList.add(item)
                        loadCount++
                        //收集滿再送上去value
                        if(loadCount>=querySnapshot.documents.size) {
                            value=roomDataList
                        }
                    }
            }

        }
        else{
            val roomDataList = mutableListOf<GameRoomItem>()
            value=roomDataList
        }
    }
    fun setVerify(isVerify: Boolean) {
        if (isRegistration) {
            registration.remove()
        }
        if(isVerify){
            //已驗證
            query = FirebaseFirestore.getInstance().collection("user")
                .document(FirebaseAuth.getInstance().currentUser!!.uid)
                .collection("joinRoom")
                .whereEqualTo("accepted", 1)
        }else{
            //未驗證
            query=FirebaseFirestore.getInstance().collection("user")
                .document(FirebaseAuth.getInstance().currentUser!!.uid)
                .collection("joinRoom")
                .whereEqualTo("accepted", 0)
        }
        query.addSnapshotListener(this)
        isRegistration=true
    }

}
//做進入房間,判斷是否已被關閉