package com.home.gifttest.ui.notifications

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.firebase.auth.FirebaseAuth
import com.home.gifttest.R


class NotificationsFragment : Fragment() {


    private lateinit var notificationsViewModel: NotificationsViewModel
    private lateinit var pAdapter:PropAdapter
    private val TAG=NotificationsFragment::class.java.simpleName
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        notificationsViewModel =
            ViewModelProviders.of(this).get(NotificationsViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_notifications, container, false)
        val spPropGameType=root.findViewById<Spinner>(R.id.sp_prop_gametype)
        val recycleProp=root.findViewById<RecyclerView>(R.id.recycle_prop)
        val txNickname:TextView=root.findViewById(R.id.tx_nickname)
        val txSex:TextView=root.findViewById(R.id.tx_sex)
        val txCountry:TextView=root.findViewById(R.id.tx_country)
        val txAge:TextView=root.findViewById(R.id.tx_age)
        val imageUserIcon=root.findViewById<ImageView>(R.id.image_userIcon)
        val imageBEdit=root.findViewById<ImageButton>(R.id.image_B_edit)

        pAdapter= PropAdapter(mutableListOf<PropItem>())

        spPropGameType.adapter=ArrayAdapter
            .createFromResource(root.context,R.array.gameName,android.R.layout.simple_spinner_item).apply {
                setDropDownViewResource(android.R.layout.simple_dropdown_item_1line) }

        notificationsViewModel.getPropItems().observe(this, Observer {
            pAdapter.items=it
            pAdapter.notifyDataSetChanged()
        })
        recycleProp.apply {
            setHasFixedSize(true)
            layoutManager=GridLayoutManager(root.context,2)
            adapter=pAdapter
        }
        notificationsViewModel.getPersonItems().observe(this, Observer {
            txNickname.text=it.nickname
            txAge.text=it.age
            txSex.text=resources.getStringArray(R.array.sex).get(it.sexuality)
            txCountry.text=resources.getStringArray(R.array.country).get(it.country)
            Glide.with(root.context)
                .load(it.userIcon)
                .apply(RequestOptions().override(250))
                .into(imageUserIcon)
        })
        imageBEdit.setOnClickListener {
            startActivity(Intent(root.context,EditActivity::class.java))
        }

        return root

    }


    inner class PropAdapter(var items:List<PropItem>):RecyclerView.Adapter<PropHolder>(){
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PropHolder {
            return PropHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_prop,parent,false))
        }

        override fun getItemCount(): Int {
            return items.size
        }

        override fun onBindViewHolder(holder: PropHolder, position: Int) {
            holder.binto(items[position])
        }

    }





}