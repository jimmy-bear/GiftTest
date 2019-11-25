package com.home.gifttest.ui.home

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.home.gifttest.GameRoomItem
import kotlinx.android.synthetic.main.item_room.view.*

class RoomHolder (view:View):RecyclerView.ViewHolder(view){
    val gameNameList= arrayListOf<String>("超達小","吃吃樂","遙遙去","猜猜樂","超級大","骰骰樂")
    var roomName=view.tx_room_roomName
    var roomMode=view.tx_room_gameMode
    var roomCountry=view.tx_room_country
    var roomCount=view.tx_room_count
    var roomNumber=view.tx_room_number
    var roomIcon=view.image_room
    fun binto(item: GameRoomItem, Countrylist: Array<String>){
        roomName.text=item.roomName
        roomMode.text=gameNameList.get(item.gameMode)
        roomCount.text=item.count.toString()
        roomNumber.text=item.roomNumber.toString()
        roomCountry.text=Countrylist.get(item.country)
        Glide.with(itemView.context)
            .load(item.iconUri)
            .apply(RequestOptions().override(240))
            .into(roomIcon)
    }
}