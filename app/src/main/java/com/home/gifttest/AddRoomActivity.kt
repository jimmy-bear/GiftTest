package com.home.gifttest

import android.app.Activity
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AlertDialog
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ServerTimestamp
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageMetadata
import kotlinx.android.synthetic.main.activity_add_room.*
import kotlinx.android.synthetic.main.activity_edit_user.*
import kotlinx.android.synthetic.main.item_room.*
import java.io.ByteArrayOutputStream
import java.util.*
import kotlin.random.Random

class AddRoomActivity : AppCompatActivity() {
    var countrySelect:Int=0
    var limitSelect:Int=0
    val REQUESTCODE:Int=100
    //private lateinit var bitmap:Bitmap
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
        image_upload_photo.setOnClickListener {
            val intent= Intent()
            intent.apply {
                setType("image/*")
                setAction(Intent.ACTION_GET_CONTENT)
            }
            startActivityForResult(intent,REQUESTCODE)
        }
        btn_game_ok.setOnClickListener {
            if(!ed_roomName.text.toString().equals("")) {
                if(limitSelect==2&&ed_roomPassword.text.toString().equals("")) {
                    AlertDialog.Builder(this).setMessage("請填寫密碼").setPositiveButton("OK",
                        DialogInterface.OnClickListener { dialog, which ->  }).show()
                }else {
                    uploadData()
                }
            }else{
                AlertDialog.Builder(this).setMessage("請填寫房間名稱").setPositiveButton("OK",
                    DialogInterface.OnClickListener { dialog, which ->  }).show()
            }
        }
        btn_gamec.setOnClickListener {
            finish()
        }

        rad_join.setOnCheckedChangeListener { group, checkedId ->
            tx_note.text=when(checkedId){
                R.id.rad_free->{
                    ed_roomPassword.visibility=View.GONE
                    ed_roomPassword.setText("")
                    limitSelect=0
                    "自由"
                }
                R.id.rad_apply->{
                    ed_roomPassword.visibility=View.GONE
                    ed_roomPassword.setText("")
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode==REQUESTCODE&&resultCode== Activity.RESULT_OK){
                val bitmapView = BitmapFactory.decodeStream(
                    this.contentResolver
                        .openInputStream(data?.data!!)
                )
                image_upload_photo
                    .setImageBitmap(bitmapView)

        }
    }

    private fun uploadData() {
        //上傳檔名
        val fileName = UUID.randomUUID().toString() + ".jpg"
        val baos = ByteArrayOutputStream()
        val bitmap = (image_upload_photo.drawable as BitmapDrawable).bitmap
        bitmap.compress(Bitmap.CompressFormat.PNG, 50, baos)
        val imageData = baos.toByteArray()
        val storagemeta=StorageMetadata.Builder().setCustomMetadata("Mykey","MyValue").build()
        val ref=FirebaseStorage.getInstance()
            .getReference()
            .child(FirebaseAuth.getInstance().currentUser!!.uid)
            .child("roomIcon")
            .child(fileName)
        val uploadTask=ref.putBytes(imageData,storagemeta)
        uploadTask.addOnSuccessListener {

        }.addOnCompleteListener{task->
            //修改
            ref.downloadUrl.addOnCompleteListener {
                val downloadUri=it.result.toString()
                FirebaseFirestore.getInstance()
                    .collection("rooms").document()
                    .set(GameRoomItem(intent.getIntExtra("selectPosition",0),
                        ed_roomName.text.toString()
                        ,countrySelect,limitSelect
                        ,FirebaseAuth.getInstance().currentUser!!.uid
                        , Random.nextInt(10000)
                        ,downloadUri,ref.path
                        ,ed_roomPassword.text.toString()
                        ,1))
                    .addOnCompleteListener { task ->
                        if(task.isSuccessful){
                            AlertDialog.Builder(this).setMessage("上傳成功").setPositiveButton("OK",
                                DialogInterface.OnClickListener { dialog, which -> finish() }).show()
                        }
                    }
            }

        }

    }

    private fun setGame(position:Int) {
        tx_gameMode.text=gameNameList.get(position)
        Glide.with(this)
            .load(gameIconURL.get(position))
            .apply(RequestOptions().override(100))
            .into(imag_gameicon)
    }
}
