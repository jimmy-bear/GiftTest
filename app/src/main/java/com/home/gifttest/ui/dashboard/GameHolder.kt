package com.home.gifttest.ui.dashboard

import android.util.Log
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.home.gifttest.R
import kotlinx.android.synthetic.main.fragment_dashboard.view.*
import kotlinx.android.synthetic.main.item_game.view.*

class GameHolder (view: View):RecyclerView.ViewHolder(view){
    val arrayIcon= arrayListOf<Int>(
        R.drawable.func_balance, R.drawable.func_contacts
        , R.drawable.func_finance, R.drawable.func_transaction, R.drawable.func_exit)
    val arrayname= arrayListOf<String>()
    var txGame=view.tx_game
    var imageGame=view.image_game
    fun binto(item: GameItem){
        txGame.setText(item.gameName)
        //Glide
        Glide.with(itemView.context)
            .load(item.imageUrl)
            //更改圖檔大小
            .apply(RequestOptions().override(120))
            .into(imageGame)
    }
}