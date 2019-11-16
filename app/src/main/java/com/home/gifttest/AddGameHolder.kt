package com.home.gifttest

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import kotlinx.android.synthetic.main.item_game.view.*

class AddGameHolder(view:View) :RecyclerView.ViewHolder(view){
    val gameIconURL= arrayListOf<String>("https://firebasestorage.googleapis.com/v0/b/giftchenge.appspot.com/o/func_balance.png?alt=media&token=c0caa462-b4d6-4a65-ab03-6ee0c86878f1",
        "https://firebasestorage.googleapis.com/v0/b/giftchenge.appspot.com/o/func_contacts.png?alt=media&token=85c41222-efeb-4f78-9397-1c5110554346",
        "https://firebasestorage.googleapis.com/v0/b/giftchenge.appspot.com/o/atm.png?alt=media&token=c6f27a36-f033-4770-ab54-a0df1fed2422",
        "https://firebasestorage.googleapis.com/v0/b/giftchenge.appspot.com/o/func_exit.png?alt=media&token=baf85851-6f28-4d2e-8d3f-12e71cc24a47",
        "https://firebasestorage.googleapis.com/v0/b/giftchenge.appspot.com/o/func_finance.png?alt=media&token=c446bb87-705c-4114-9097-653d584771b0",
        "https://firebasestorage.googleapis.com/v0/b/giftchenge.appspot.com/o/func_transaction.png?alt=media&token=2e6869c4-a22a-4818-8776-ff9ad352771c")
    val gameNameList= arrayListOf<String>("超達小","吃吃樂","遙遙去","猜猜樂","超級大","骰骰樂")

    val txGame=view.tx_game
    val imagGame=view.image_game

    fun binTo(position:Int){
        txGame.text=gameNameList.get(position)
        Glide.with(itemView.context)
            .load(gameIconURL.get(position))
            //更改圖檔大小
            .apply(RequestOptions().override(120))
            .into(imagGame)
    }
}