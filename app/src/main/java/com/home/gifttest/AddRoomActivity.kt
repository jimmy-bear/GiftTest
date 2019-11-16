package com.home.gifttest

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_add_room.*

class AddRoomActivity : AppCompatActivity() {
    var countrySelect:Int=0
    var limitSelect:Int=0
    val TAG=AddRoomActivity::class.java.simpleName
    val gameIconURL= arrayListOf<String>("https://firebasestorage.googleapis.com/v0/b/giftchenge.appspot.com/o/func_balance.png?alt=media&token=c0caa462-b4d6-4a65-ab03-6ee0c86878f1",
        "https://firebasestorage.googleapis.com/v0/b/giftchenge.appspot.com/o/func_contacts.png?alt=media&token=85c41222-efeb-4f78-9397-1c5110554346",
        "https://firebasestorage.googleapis.com/v0/b/giftchenge.appspot.com/o/atm.png?alt=media&token=c6f27a36-f033-4770-ab54-a0df1fed2422",
        "https://firebasestorage.googleapis.com/v0/b/giftchenge.appspot.com/o/func_exit.png?alt=media&token=baf85851-6f28-4d2e-8d3f-12e71cc24a47",
        "https://firebasestorage.googleapis.com/v0/b/giftchenge.appspot.com/o/func_finance.png?alt=media&token=c446bb87-705c-4114-9097-653d584771b0",
        "https://firebasestorage.googleapis.com/v0/b/giftchenge.appspot.com/o/func_transaction.png?alt=media&token=2e6869c4-a22a-4818-8776-ff9ad352771c")
    val gameNameList= arrayListOf<String>("超達小","吃吃樂","遙遙去","猜猜樂","超級大","骰骰樂")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_room)
        setGame(intent.getIntExtra("selectPosition",0))
        btn_game_ok.setOnClickListener {
            uploadData()
            finish()
        }
        btn_gamec.setOnClickListener {
            finish()
        }

        rad_join.setOnCheckedChangeListener { group, checkedId ->
            tx_note.text=when(checkedId){
                R.id.rad_free->{
                    ed_roomPassword.visibility=View.GONE
                    limitSelect=0
                    "自由"
                }
                R.id.rad_apply->{
                    ed_roomPassword.visibility=View.GONE
                    limitSelect=1
                    "申請"
                }
                R.id.rad_password->{
                    ed_roomPassword.visibility=View.VISIBLE
                    limitSelect=2
                    "密碼制"

                }
                else->""
            }
        }
        sp_position.adapter=ArrayAdapter.createFromResource(this,R.array.country,android.R.layout.simple_spinner_item)
            .apply { setDropDownViewResource(android.R.layout.simple_dropdown_item_1line) }
        sp_position.onItemSelectedListener=object :AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onItemSelected(
                parent: AdapterView<*>?, view: View?, position: Int, id: Long
            ) {
                countrySelect=position
            }

        }

    }

    private fun uploadData() {
        FirebaseFirestore.getInstance()
            .collection("rooms").document()
            .set(GameRoomItem(intent.getIntExtra("selectPosition",0),
                ed_roomName.text.toString(),countrySelect,limitSelect))
    }

    private fun setGame(position:Int) {
        tx_gameMode.text=gameNameList.get(position)
        Glide.with(this)
            .load(gameIconURL.get(position))
            .apply(RequestOptions().override(240))
            .into(imag_gameicon)
    }
}
