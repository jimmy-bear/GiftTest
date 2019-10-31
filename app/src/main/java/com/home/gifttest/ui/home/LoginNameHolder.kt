package com.home.gifttest.ui.home

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_user.view.*

class LoginNameHolder(view: View) :RecyclerView.ViewHolder(view){
    var name_activity=view.tx_activityName
    var image_activity=view.image_name
    fun binto(item: LoginItem){
        name_activity.setText(item.name)
        //Glide

    }
}