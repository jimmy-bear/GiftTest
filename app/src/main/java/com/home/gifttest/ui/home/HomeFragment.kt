package com.home.gifttest.ui.home

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.home.gifttest.GameRoomItem

import com.home.gifttest.R
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.view_password.view.*

class HomeFragment : Fragment(){

    private val RC_SIGN_IN: Int=150
    private lateinit var homeViewModel: HomeViewModel
    private lateinit var adapter:RoomAdapter
    private val TAG=HomeFragment::class.java.simpleName


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        homeViewModel =
            ViewModelProviders.of(this).get(HomeViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_home, container, false)
        val spFilterCountry=root.findViewById<Spinner>(R.id.sp_filter_country)
        val spGameType=root.findViewById<Spinner>(R.id.sp_gametype)
        val recycleRoom=root.findViewById<RecyclerView>(R.id.recycle_room)

        recycleRoom.setHasFixedSize(true)
        recycleRoom.layoutManager=LinearLayoutManager(root.context,LinearLayoutManager.VERTICAL,false)
        adapter=RoomAdapter(mutableListOf())
        recycleRoom.adapter=adapter
        homeViewModel.getRoomItems().observe(this, Observer {
            adapter.items=it
            adapter.notifyDataSetChanged()
        })
        spFilterCountry.adapter=ArrayAdapter.createFromResource(root.context
            ,R.array.country,android.R.layout.simple_spinner_item)
            .apply { setDropDownViewResource(android.R.layout.simple_dropdown_item_1line) }
        spGameType.adapter=ArrayAdapter.createFromResource(root.context
          ,R.array.gameType,android.R.layout.simple_spinner_item)
            .apply { setDropDownViewResource(android.R.layout.simple_dropdown_item_1line) }

        return root
    }
    inner class RoomAdapter(var items:List<GameRoomItem>) :RecyclerView.Adapter<RoomHolder>(){
        val arrayCountry=resources.getStringArray(R.array.country)

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RoomHolder {
            return RoomHolder(LayoutInflater.from(parent.context)
                .inflate(R.layout.item_room,parent,false))
        }

        override fun getItemCount(): Int {
            return items.size
            }

        override fun onBindViewHolder(holder: RoomHolder, position: Int) {
            holder.binto(items[position],arrayCountry)
            holder.itemView.setOnClickListener{
                Log.d(TAG,"count + ${items[position].documentID}")
                //homeViewModel.setRoomId(items[position].documentID)
                if(items[position].limitMode==2){
                    val alertView=LayoutInflater.from(context).inflate(R.layout.view_password,container)
                    val alertBulid=androidx.appcompat.app.AlertDialog.Builder(context!!)
                        .setView(alertView).setTitle("請輸入房間密碼").setNegativeButton("Cancel",
                            DialogInterface.OnClickListener { dialog, which -> })
                        alertBulid.setPositiveButton("OK",
                        DialogInterface.OnClickListener { dialog, which ->
                            if(alertView.ed_enterPassword.text.toString().equals(items[position].password)){
                                val newsFragment = DetailFragment.newInstance(items[position].documentID)
                                newsFragment.show(fragmentManager!!, "")
                            }
                            else{
                                AlertDialog.Builder(context).setMessage("密碼錯誤").setPositiveButton("OK",
                                    DialogInterface.OnClickListener { dialog, which ->  }).show()
                            }
                        }).show()
                }else {
                    val newsFragment = DetailFragment.newInstance(items[position].documentID)
                    newsFragment.show(fragmentManager!!, "")
                }
            }
        }

    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG,"Resume")
    }
}

