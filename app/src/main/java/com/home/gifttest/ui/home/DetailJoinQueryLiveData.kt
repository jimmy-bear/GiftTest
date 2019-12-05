package com.home.gifttest.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.*
import com.home.gifttest.ui.notifications.PersonItem

class DetailJoinQueryLiveData :LiveData<List<PersonItem>>(), EventListener<QuerySnapshot> {
    lateinit var registration: ListenerRegistration
    var isRegistration=false
//    var query:Query= FirebaseFirestore.getInstance()
//        .collection("rooms")
//        .document("000empty")
//        .collection("joins")

    override fun onActive() {
//        registration=query.addSnapshotListener(this)
//        isRegistration=true
    }

    override fun onInactive() {
        super.onInactive()
        if(isRegistration){
            registration.remove()
        }
    }

    override fun onEvent(querySnapshot: QuerySnapshot?, exception: FirebaseFirestoreException?) {
        if (querySnapshot!=null&&!querySnapshot.isEmpty) {
            value = querySnapshot.toObjects(PersonItem::class.java)
        }
    }

    fun setRoomid(room: String) {
        if(isRegistration){
            registration.remove()
            isRegistration=false
        }
        var query= FirebaseFirestore.getInstance()
            .collection("rooms")
            .document(room)
            .collection("joins")
        registration=query.addSnapshotListener(this)
        isRegistration=true
    }

}



