package com.home.gifttest.ui.home

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.home.gifttest.R
import com.home.gifttest.ui.notifications.PersonItem

class DetailHolder(view :View) :RecyclerView.ViewHolder(view){
    val txJoinNickName=view.findViewById<TextView>(R.id.tx_detail_join_nickname)
    val txJoinAge=view.findViewById<TextView>(R.id.tx_detail_join_age)
    val txJoinSex=view.findViewById<TextView>(R.id.tx_detail_join_sex)
    val txJoinCountry=view.findViewById<TextView>(R.id.tx_detail_join_country)
    val imageJoin=view.findViewById<ImageView>(R.id.image_detail_joinIcon)
    fun binto(
        item: PersonItem,
        countryName:Array<String>,
        sexList: Array<String>
    ){
        txJoinNickName.text=item.nickname
        txJoinAge.text=item.age
        txJoinCountry.text=countryName[item.country]
        txJoinSex.text=sexList[item.sexuality]
        Glide.with(itemView.context)
            .load(item.userIcon)
            .apply(RequestOptions().override(120))
            .into(imageJoin)
    }
}