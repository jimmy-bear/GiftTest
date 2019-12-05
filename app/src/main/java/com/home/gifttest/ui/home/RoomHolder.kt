package com.home.gifttest.ui.home

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.home.gifttest.GameRoomItem
import kotlinx.android.synthetic.main.item_room.view.*

class RoomHolder (view:View):RecyclerView.ViewHolder(view){
    val gameNameList= arrayListOf<String>("超達小","吃吃樂","遙遙去","猜猜樂","超級大","骰骰樂")
    val roomName=view.tx_room_roomName
    val roomMode=view.tx_room_gameMode
    val roomCountry=view.tx_room_country
    val roomCount=view.tx_room_count
    val roomNumber=view.tx_room_number
    val roomIcon=view.image_room_user
    val txApply=view.tx_apply
    val txApplyCount=view.tx_applyCount
    val imageLock=view.image_lock
    fun binto(item: GameRoomItem, countryName: Array<String>){
        roomName.text=item.roomName
        roomMode.text=gameNameList.get(item.gameMode)
        roomCount.text=item.count.toString()
        roomNumber.text=item.roomNumber.toString()
        roomCountry.text=countryName[item.country]
        txApplyCount.text=item.applyCount.toString()
        //imageLock.setImageResource(R.drawable.baseline_lock_black_18dp)
        Glide.with(itemView.context)
            .load(item.iconUri)
            .apply(RequestOptions().override(180))
            .into(roomIcon)
//        if(item.limitMode==1){
//            txApply.visibility=View.VISIBLE
//            txApplyCount.visibility=View.VISIBLE
//        }else{
//            txApply.visibility=View.GONE
//            txApplyCount.visibility=View.GONE
//        }
        //
        when(item.limitMode){
            1->{//申請制
                txApply.visibility=View.VISIBLE
                txApplyCount.visibility=View.VISIBLE
            }
            2->{//密碼制
                imageLock.visibility=View.VISIBLE
            }
            else->{
                txApply.visibility=View.GONE
                txApplyCount.visibility=View.GONE
                imageLock.visibility=View.GONE
            }
        }
    }
}