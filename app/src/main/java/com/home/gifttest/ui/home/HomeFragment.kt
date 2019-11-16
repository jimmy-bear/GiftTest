package com.home.gifttest.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders


import com.home.gifttest.R

class HomeFragment : Fragment(){

    private val RC_SIGN_IN: Int=150
    private lateinit var homeViewModel: HomeViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        homeViewModel =
            ViewModelProviders.of(this).get(HomeViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_home, container, false)
        val textView: TextView = root.findViewById(R.id.text_home)
        val spFilter_country=root.findViewById<Spinner>(R.id.sp_filter_country)
        val spGmaeType=root.findViewById<Spinner>(R.id.sp_gametype)
        homeViewModel.text.observe(this, Observer {
            textView.text = it
        })
        spFilter_country.adapter=ArrayAdapter.createFromResource(root.context
            ,R.array.country,android.R.layout.simple_spinner_item)
            .apply { setDropDownViewResource(android.R.layout.simple_dropdown_item_1line) }
//      sp_gmaetype.adapter=ArrayAdapter.createFromResource(root.context
////////            ,R.array.school,android.R.layout.simple_spinner_item)
////////            .apply { setDropDownViewResource(android.R.layout.simple_dropdown_item_1line) }
        return root
    }
//    override fun onAuthStateChanged(p0: FirebaseAuth) {
//
//    }


}