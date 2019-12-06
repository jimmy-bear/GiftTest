package com.home.gifttest.ui.dashboard

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.home.gifttest.GameRoomItem
import kotlinx.android.synthetic.main.item_myroom.view.*
import kotlinx.android.synthetic.main.item_room.view.*

class MyJoinRoomHolder(view:View) :RecyclerView.ViewHolder(view){
    val gameNameList= arrayListOf<String>("超達小","吃吃樂","遙遙去","猜猜樂","超級大","骰骰樂")
    val roomName=view.tx_myRoom_roomnName
    val roomMode=view.tx_myRoom_gameType
    val roomCountry=view.tx_myRoom_country
    val roomCount=view.tx_myRoom_count//總報名人數
    val roomNumber=view.tx_myRoom_number//房號
    val roomIcon=view.image_myRoom_user
    //val imageWarning=view.image_applyWarning//是否通過申請，無警告等於通過
    fun binto(item:GameRoomItem, countryName: Array<String>){
        roomName.text=item.roomName
        roomMode.text=gameNameList.get(item.gameMode)
        if(item.count>500){
            roomCount.text=""
        }
        else {
            roomCount.text = item.count.toString()
        }
        roomNumber.text=item.roomNumber.toString()
        roomCountry.text=countryName[item.country]
        Glide.with(itemView.context)
            .load(item.iconUri)
            .apply(RequestOptions().override(180))
            .into(roomIcon)
//        if(item.limitMode==0){
//            imageWarning.visibility=View.VISIBLE
//        }
//        else{
//            imageWarning.visibility=View.GONE
//        }
    }

}