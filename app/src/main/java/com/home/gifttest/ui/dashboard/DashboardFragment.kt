package com.home.gifttest.ui.dashboard

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.home.gifttest.AddGameActivity
import com.home.gifttest.GameRoomItem
import com.home.gifttest.R
import com.home.gifttest.ui.home.RoomHolder

class DashboardFragment : Fragment() {

    private val TAG=DashboardFragment::class.java.simpleName
    private lateinit var dashboardViewModel: DashboardViewModel
    //private  lateinit var adapter:FirestoreRecyclerAdapter<GameItem,GameHolder>
    lateinit var adapter:myRoomAdapter
    lateinit var web_rule:WebView
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val root = inflater.inflate(R.layout.fragment_dashboard, container, false)
        val recycGame=root.findViewById<RecyclerView>(R.id.recycle_myRoom)
        val btnCreatGame=root.findViewById<Button>(R.id.btn_creatgame)
        btnCreatGame.setOnClickListener {
            startActivity(Intent(root.context,AddGameActivity::class.java))
        }
        //val textView: TextView = root.findViewById(R.id.text_dashboard)

        recycGame.setHasFixedSize(true)
        recycGame.layoutManager=LinearLayoutManager(this.context,LinearLayoutManager.VERTICAL,false)

        dashboardViewModel =
            ViewModelProviders.of(this).get(DashboardViewModel::class.java)
        adapter=myRoomAdapter(mutableListOf<GameRoomItem>())
        recycGame.adapter=adapter
        dashboardViewModel.getMyRoomItems().observe(this, Observer {
            Log.d(TAG,"observer  :"+"${it.size}")
            adapter.items=it
            //adapter變更用
            adapter.notifyDataSetChanged()
        })

        return root
    }
    inner class myRoomAdapter(var items:List<GameRoomItem>):RecyclerView.Adapter<RoomHolder>(){
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RoomHolder {
            val view=LayoutInflater.from(parent.context).inflate(R.layout.item_room,parent,false)
            return RoomHolder(view)
        }

        override fun getItemCount(): Int {
            return items.size
        }

        override fun onBindViewHolder(holder: RoomHolder, position: Int) {
            var item=items.get(position)
//            web_rule.loadUrl(items.get(0).ruleUrl)
            //傳入item物件
            holder.binto(item,resources.getStringArray(R.array.country))

            holder.itemView.setOnClickListener {
                onListener(item,position)
            }
        }
    }

    private fun onListener(item: GameRoomItem, position: Int) {
        Log.d(TAG,"MyRoom  +${position}")
//        Log.d(TAG,"onclick  : ${item.gameName}")
//        web_rule.settings.javaScriptEnabled=true
//        web_rule.loadUrl(item.ruleUrl)

    }

    override fun onStart() {
        super.onStart()
        //adapter.startListening()
    }

    override fun onStop() {
        super.onStop()
        //adapter.stopListening()
    }
}