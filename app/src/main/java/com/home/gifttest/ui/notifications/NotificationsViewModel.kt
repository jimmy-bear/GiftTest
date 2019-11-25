package com.home.gifttest.ui.notifications

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class NotificationsViewModel : ViewModel() {
    private var propItems=MutableLiveData<List<PropItem>>()
    private var personItem=MutableLiveData<PersonItem>()
    fun getPropItems() :MutableLiveData<List<PropItem>>{
        FirebaseFirestore.getInstance()
            .collection("user")
            .document(FirebaseAuth.getInstance().currentUser!!.uid)
            .collection("prop")
            .addSnapshotListener { querySnapshot, Exception ->
                if (querySnapshot!=null&&!querySnapshot.isEmpty){
                    propItems.value=querySnapshot.toObjects(PropItem::class.java)
                }
            }
        return propItems
    }
    fun getPersonItems():MutableLiveData<PersonItem>{
        FirebaseFirestore.getInstance()
            .collection("user")
            .document(FirebaseAuth.getInstance().currentUser!!.uid)
            .get().addOnSuccessListener {
                personItem.value=it.toObject(PersonItem::class.java)
            }
        return personItem
    }
}