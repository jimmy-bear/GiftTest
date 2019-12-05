package com.home.gifttest.ui.home

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.home.gifttest.GameRoomItem

import com.home.gifttest.R
import com.home.gifttest.ui.notifications.NotificationsViewModel
import com.home.gifttest.ui.notifications.PersonItem

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_ROOMID = "roomid"

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [DetailFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [DetailFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class DetailFragment : DialogFragment() {
    private var roomid: String? = null

    private val TAG=DetailFragment::class.java.simpleName
    private lateinit var homeViewModel: HomeViewModel
    private lateinit var detailAdapter: DetailAdapter
    private lateinit var notificationsViewModel:NotificationsViewModel
    private var member=0
    private lateinit var roomItem:GameRoomItem
    private lateinit var personItem:PersonItem
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            roomid=it.getString(ARG_ROOMID)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        homeViewModel =
            ViewModelProviders.of(this).get(HomeViewModel::class.java)
        notificationsViewModel=
            ViewModelProviders.of(this).get(NotificationsViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_detail, container, false)
        val recycleDetail=root.findViewById<RecyclerView>(R.id.recycle_detail)
        val txNickname: TextView =root.findViewById(R.id.tx_detail_creat_nickname)
        val txSex: TextView =root.findViewById(R.id.tx_detail_creat_sex)
        val txCountry: TextView =root.findViewById(R.id.tx_detail_creat_country)
        val txAge: TextView =root.findViewById(R.id.tx_detail_creat_age)
        val txMemberCount:TextView=root.findViewById(R.id.tx_complete_member)
        val imageCreatIcon=root.findViewById<ImageView>(R.id.image_detail_creatIcon)
        val btnExit=root.findViewById<Button>(R.id.btn_detail_exit)
        val btnEnter=root.findViewById<Button>(R.id.btn_detail_enterName)
        //一開始給roomid可以
        recycleDetail.setHasFixedSize(true)
        recycleDetail.layoutManager=LinearLayoutManager(root.context)
        detailAdapter=DetailAdapter(mutableListOf())
        recycleDetail.adapter=detailAdapter

        notificationsViewModel.getPersonItems().observe(this, Observer {
            personItem=it
        })

        homeViewModel.getJoinDetailItems().observe(this, Observer {
            Log.d(TAG,"observe+${it.size}")
            member=it.size
            detailAdapter.items=it
            detailAdapter.notifyDataSetChanged()
        })
        homeViewModel.getCreatDetailItem().observe(this, Observer {
            txNickname.text=it.nickname
            txAge.text=it.age
            txSex.text=resources.getStringArray(R.array.sex).get(it.sexuality)
            txCountry.text=resources.getStringArray(R.array.country).get(it.country)
            txMemberCount.text=member.toString()
            Glide.with(root.context)
                .load(it.userIcon)
                .apply(RequestOptions().override(120))
                .into(imageCreatIcon)
        })
        homeViewModel.getDetailRoomItem().observe(this, Observer {
            roomItem=it
        })
        btnExit.setOnClickListener {
           dismiss()
        }
        btnEnter.setOnClickListener {
            val authUid=FirebaseAuth.getInstance().currentUser!!.uid
            var allCount=roomItem.count
            var applyCount=roomItem.applyCount
            //上傳房間資訊到我報名的房間，藉由funtion新增至join，*做審核或自由
            //審查制
            if(roomItem.limitMode==1){
                //建立apply清單
                FirebaseFirestore.getInstance().collection("rooms")
                    .document(roomid!!)
                    .collection("applicants").document(authUid)
                    .set(mapOf<String,String>("gameName" to "")).addOnSuccessListener {
                         FirebaseFirestore.getInstance().collection("rooms")
                             .document(roomid!!).update("applyCount",applyCount+1)
                             .addOnSuccessListener {
                                Toast.makeText(this.context, "等待開房者審核", Toast.LENGTH_LONG).show()
                                dismiss() }

                            }

            }
            else {//靠function新增
                 FirebaseFirestore.getInstance().collection("rooms")
                     .document(roomid!!)
                     .collection("joins").document(authUid)
                     .set(mapOf<String,String>("gameName" to "")).addOnSuccessListener {
                          FirebaseFirestore.getInstance().collection("rooms")
                              .document(roomid!!).update("count",allCount+1)
                              .addOnSuccessListener {
                                  Toast.makeText(this.context, "報名成功", Toast.LENGTH_LONG).show()
                                  dismiss() }
                            }
                    }

        }

        return root
    }

    override fun onResume() {
        super.onResume()
        homeViewModel.setRoomId(roomid!!)
    }
    inner class DetailAdapter(var items:List<PersonItem>):RecyclerView.Adapter<DetailHolder>(){
        private val countryList=resources.getStringArray(R.array.country)
        private val sexList=resources.getStringArray(R.array.sex)
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DetailHolder {
            return DetailHolder(LayoutInflater.from(parent.context)
                .inflate(R.layout.item_detail,parent,false))
        }

        override fun getItemCount(): Int {
            return items.size
        }

        override fun onBindViewHolder(holder: DetailHolder, position: Int) {
            holder.binto(items[position],countryList,sexList)
        }

    }

    companion object {
        @JvmStatic
        fun newInstance(roomid:String) =DetailFragment().apply {
            arguments = Bundle().apply {
                putString(ARG_ROOMID, roomid)
            }
        }
    }
}
