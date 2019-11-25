package com.home.gifttest.ui.home

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
        recycleRoom.layoutManager=LinearLayoutManager(this.context,LinearLayoutManager.VERTICAL,false)
        adapter=RoomAdapter(mutableListOf())
        recycleRoom.adapter=adapter
        homeViewModel.getItems().observe(this, Observer {
            adapter.items=it
            adapter.notifyDataSetChanged()
        })
        spFilterCountry.adapter=ArrayAdapter.createFromResource(root.context
            ,R.array.country,android.R.layout.simple_spinner_item)
            .apply { setDropDownViewResource(android.R.layout.simple_dropdown_item_1line) }
        spGameType.adapter=ArrayAdapter.createFromResource(root.context
          ,R.array.gameName,android.R.layout.simple_spinner_item)
            .apply { setDropDownViewResource(android.R.layout.simple_dropdown_item_1line) }
        return root
    }
    inner class RoomAdapter(var items:List<GameRoomItem>) :RecyclerView.Adapter<RoomHolder>(){
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RoomHolder {
            return RoomHolder(LayoutInflater.from(parent.context)
                .inflate(R.layout.item_room,parent,false))
        }

        override fun getItemCount(): Int {
            return items.size
            }

        override fun onBindViewHolder(holder: RoomHolder, position: Int) {

            val listCountry=resources.getStringArray(R.array.country)
            holder.binto(items[position],listCountry)
            holder.itemView.setOnClickListener{
                val newsFragment=DetailFragment.newInstance()
                newsFragment.show(fragmentManager!!,"")
            }
        }

    }


}

