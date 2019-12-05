package com.home.gifttest.ui.dashboard

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.home.gifttest.AddGameActivity
import com.home.gifttest.GameRoomItem
import com.home.gifttest.R
import com.home.gifttest.ui.home.RoomHolder

class DashboardFragment : Fragment() {

    private val TAG=DashboardFragment::class.java.simpleName
    private lateinit var dashboardViewModel: DashboardViewModel
    //private  lateinit var adapter:FirestoreRecyclerAdapter<GameItem,GameHolder>
    lateinit var creatAdapter:craetRoomAdapter
    lateinit var joinAdapter:joinRoomAdapter
    lateinit var web_rule:WebView
    var verifyPosition=0
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val root = inflater.inflate(R.layout.fragment_dashboard, container, false)
        val recycleCreat=root.findViewById<RecyclerView>(R.id.recycle_myRoom)
        val recycleJoin=root.findViewById<RecyclerView>(R.id.recycle_joinRoom)
        val btnCreatGame=root.findViewById<Button>(R.id.btn_creatgame)
        val spVerify=root.findViewById<Spinner>(R.id.sp_verify)
        dashboardViewModel =
            ViewModelProviders.of(this).get(DashboardViewModel::class.java)
        creatAdapter=craetRoomAdapter(mutableListOf())
        joinAdapter=joinRoomAdapter(mutableListOf())
        recycleCreat.apply {
            setHasFixedSize(true)
            layoutManager=LinearLayoutManager(this.context,LinearLayoutManager.VERTICAL,false)
            adapter=creatAdapter
        }
        recycleJoin.apply {
            setHasFixedSize(true)
            layoutManager=LinearLayoutManager(this.context,LinearLayoutManager.VERTICAL,false)
            adapter=joinAdapter
        }

        dashboardViewModel.getCreatRoomItems().observe(this, Observer {
            Log.d(TAG,"observer  :"+"${it.size}")
            creatAdapter.creatItems=it
            //adapter變更用
            creatAdapter.notifyDataSetChanged()
        })
        dashboardViewModel.getJoinRoomItems().observe(this, Observer {
            joinAdapter.joinItems=it
            joinAdapter.notifyDataSetChanged()
        })
        spVerify.adapter=ArrayAdapter.createFromResource(root.context,
            R.array.verify,android.R.layout.simple_spinner_item).apply {
            setDropDownViewResource(android.R.layout.simple_dropdown_item_1line)
        }
        spVerify.onItemSelectedListener=object :AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                when(position){
                    //初始0顯示已報名
                    0-> {
                        verifyPosition=0
                        dashboardViewModel.setVerify(true)
                    }
                    1->{
                        verifyPosition=1
                        dashboardViewModel.setVerify(false)
                    }
                }
            }

        }
        btnCreatGame.setOnClickListener {
            if(creatAdapter.creatItems.size<5) {
                startActivity(Intent(root.context, AddGameActivity::class.java))
            }
            else
                AlertDialog.Builder(this.context!!).setMessage("已達到開啟房間上限").setPositiveButton("OK",
                    DialogInterface.OnClickListener { dialog, which ->  }).show()
        }
        return root
    }
    inner class craetRoomAdapter(var creatItems:List<GameRoomItem>):RecyclerView.Adapter<RoomHolder>(){
        val arrayCountry=resources.getStringArray(R.array.country)
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RoomHolder {
            val view=LayoutInflater.from(parent.context).inflate(R.layout.item_room,parent,false)
            return RoomHolder(view)
        }

        override fun getItemCount(): Int {
            return creatItems.size
        }

        override fun onBindViewHolder(holder: RoomHolder, position: Int) {
            //var item=creatItems.get(position)
//            web_rule.loadUrl(items.get(0).ruleUrl)
            //傳入item物件
            holder.binto(creatItems[position],arrayCountry)
            holder.itemView.setOnClickListener {  }
            holder.itemView.setOnLongClickListener {
                AlertDialog.Builder(this@DashboardFragment.context!!).setTitle("房名:\t${creatItems[position].roomName}").setItems(R.array.myRoomEdit,
                    DialogInterface.OnClickListener { dialog, which ->
                        when(which){
                            0->Log.d(TAG,"0")
                            1->{
                                //刪除房間
                                FirebaseFirestore.getInstance()
                                    .collection("rooms")
                                    .document(creatItems[position].documentID).delete().addOnSuccessListener {
                                        Toast.makeText(this@DashboardFragment.context
                                            ,"刪除成功"
                                            ,Toast.LENGTH_LONG )
                                            .show()
                                   }
                            }
                        }
                    }).show()
                return@setOnLongClickListener true
            }
        }
    }
    inner class joinRoomAdapter(var joinItems:List<GameRoomItem>):RecyclerView.Adapter<MyJoinRoomHolder>(){
        val arrayCountry=resources.getStringArray(R.array.country)
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyJoinRoomHolder {
            return MyJoinRoomHolder(
                LayoutInflater.from(parent.context).inflate(R.layout.item_myroom,parent,false))
        }

        override fun getItemCount(): Int {
            return joinItems.size
        }

        override fun onBindViewHolder(holder: MyJoinRoomHolder, position: Int) {
            holder.binto(joinItems[position],arrayCountry)
            holder.itemView.setOnClickListener {  }
            holder.itemView.setOnLongClickListener {
                AlertDialog.Builder(this@DashboardFragment.context!!).setTitle("房名:\t${joinItems[position].roomName}").setItems(R.array.myRoomEdit,
                    DialogInterface.OnClickListener { dialog, which ->
                        when(which){
                            0->Log.d(TAG,"0")
                            1->{
                                if (verifyPosition==0) {
                                    //刪除房間
                                    FirebaseFirestore.getInstance()
                                        .collection("rooms")
                                        .document(joinItems[position].documentID)
                                        .collection("joins")
                                        .document(FirebaseAuth.getInstance().currentUser!!.uid)
                                        .delete()
                                        .addOnSuccessListener {
                                            Toast.makeText(
                                                this@DashboardFragment.context
                                                , "刪除成功"
                                                , Toast.LENGTH_LONG
                                            )
                                                .show()
                                        }
                                }
                                else{
                                    FirebaseFirestore.getInstance()
                                        .collection("rooms")
                                        .document(joinItems[position].documentID)
                                        .collection("applicants")
                                        .document(FirebaseAuth.getInstance().currentUser!!.uid)
                                        .delete()
                                        .addOnSuccessListener {
                                            Toast.makeText(
                                                this@DashboardFragment.context
                                                , "刪除成功"
                                                , Toast.LENGTH_LONG
                                            )
                                                .show()
                                        }
                                }
                            }
                        }
                    }).show()
                return@setOnLongClickListener true
            }
        }

    }
//更新報名這數據

}