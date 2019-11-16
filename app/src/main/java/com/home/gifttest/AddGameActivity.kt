package com.home.gifttest

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Adapter
import android.widget.LinearLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import kotlinx.android.synthetic.main.activity_add_game.*

class AddGameActivity : AppCompatActivity() {
    val TAG=AddGameActivity::class.java.simpleName
    lateinit var personAdapter:gameAdapter
    var selectposition=0
    val gameWebURL= arrayListOf<String>("https://giftchenge.web.app/test1.html","https://giftchenge.web.app/test2.html",
        "https://giftchenge.web.app/test3.html","https://giftchenge.web.app/test4.html",
        "https://giftchenge.web.app/test5.html","https://giftchenge.web.app/404.html")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_game)
        web_game.settings.javaScriptEnabled=true
        web_game.loadUrl(gameWebURL.get(0))
        personAdapter=gameAdapter(gameWebURL)
        recyc_gametype.apply {
            setHasFixedSize(true)
            layoutManager=LinearLayoutManager(this@AddGameActivity,LinearLayoutManager.HORIZONTAL,false)
            adapter=personAdapter
        }
        btn_gmaecancel.setOnClickListener {
            finish()
        }
        btn_creatg.setOnClickListener {
            val intent=Intent(this,AddRoomActivity::class.java)
            intent.putExtra("selectPosition",selectposition)
            startActivity(intent)
            finish()
        }
    }
   inner class gameAdapter(val gameWebURL:List<String>):RecyclerView.Adapter<AddGameHolder>(){
       override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AddGameHolder {
           return AddGameHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_game,parent,false))
       }

       override fun getItemCount(): Int {
        return gameWebURL.size
        }

       override fun onBindViewHolder(holder: AddGameHolder, position: Int) {
           holder.binTo(position)
           holder.itemView.setOnClickListener {
               listener(position)
           }
       }

   }
    private fun listener(position: Int) {
        Log.d(TAG,"item  :${position}")
        web_game.loadUrl(gameWebURL.get(position))
        selectposition=position
    }

}
