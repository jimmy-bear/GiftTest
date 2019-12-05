package com.home.gifttest.ui.notifications

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.item_prop.view.*

class PropHolder(view:View) :RecyclerView.ViewHolder(view){
    var propName=view.tx_propName
    var propIcon=view.image_propIcon
    var propNumber=view.tx_propNumber
    fun binto(item: PropItem){
        propNumber.text=item.number.toString()
        propName.text=item.name
        Glide.with(itemView.context)
            .load(item.iconUri)
            .apply(RequestOptions().override(100))
            .into(propIcon)
    }
}