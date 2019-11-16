package com.home.gifttest.ui.dashboard

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.ui.NavigationUI.setupActionBarWithNavController
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.api.LogDescriptor
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.home.gifttest.AddGameActivity
import com.home.gifttest.MainActivity
import com.home.gifttest.R
import kotlinx.android.synthetic.main.fragment_dashboard.*

class DashboardFragment : Fragment() {

    private val TAG=DashboardFragment::class.java.simpleName
    private lateinit var dashboardViewModel: DashboardViewModel
    //private  lateinit var adapter:FirestoreRecyclerAdapter<GameItem,GameHolder>
    lateinit var adapter:ItemAdapter
    lateinit var web_rule:WebView
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val root = inflater.inflate(R.layout.fragment_dashboard, container, false)
        val recycGame=root.findViewById<RecyclerView>(R.id.recyc_game)
        web_rule=root.findViewById<WebView>(R.id.web_rule)
        val btnCreatGame=root.findViewById<Button>(R.id.btn_creatgame)
        btnCreatGame.setOnClickListener {
            startActivity(Intent(root.context,AddGameActivity::class.java))
        }
        //val textView: TextView = root.findViewById(R.id.text_dashboard)

        recycGame.setHasFixedSize(true)
        recycGame.layoutManager=LinearLayoutManager(this.context,LinearLayoutManager.HORIZONTAL,false)
        adapter=ItemAdapter(mutableListOf<GameItem>())
        recycGame.adapter=adapter
        dashboardViewModel =
            ViewModelProviders.of(this).get(DashboardViewModel::class.java)

        dashboardViewModel.getItems().observe(this, Observer {
            Log.d(TAG,"observer  :"+"${it.size}")
            adapter.items=it
            //adapter變更用
            adapter.notifyDataSetChanged()
        })

        return root
    }
    inner class ItemAdapter(var items:List<GameItem>):RecyclerView.Adapter<GameHolder>(){
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GameHolder {
            val view=LayoutInflater.from(parent.context).inflate(R.layout.item_game,parent,false)
            return GameHolder(view)
        }

        override fun getItemCount(): Int {
            return items.size
        }

        override fun onBindViewHolder(holder: GameHolder, position: Int) {
            var item=items.get(position)
            web_rule.loadUrl(items.get(0).ruleUrl)
            //傳入item物件
            holder.binto(item)
            holder.itemView.setOnClickListener {
                onListener(item,position)
            }
        }

    }

    private fun onListener(item: GameItem, position: Int) {
        Log.d(TAG,"onclick  : ${item.gameName}")
        web_rule.settings.javaScriptEnabled=true
        web_rule.loadUrl(item.ruleUrl)

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