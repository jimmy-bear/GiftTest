package com.home.gifttest.ui.notifications

import android.app.Activity
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import androidx.lifecycle.Observer
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageMetadata
import com.home.gifttest.R
import kotlinx.android.synthetic.main.activity_edit_user.*
import java.io.ByteArrayOutputStream

class EditActivity : AppCompatActivity() {

    private val REQUESTCODE: Int=150
    private lateinit var downloadUri: String
    private lateinit var notificationsViewModel: NotificationsViewModel
    private var sexPosition=0
    private var countryPosition=0
    private var isChengePicture=false
    private val userUid = FirebaseAuth.getInstance().currentUser!!.uid

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_user)
        notificationsViewModel =
            ViewModelProviders.of(this).get(NotificationsViewModel::class.java)
        //sp
        sp_sex.adapter= ArrayAdapter.createFromResource(this,R.array.sex,android.R.layout.simple_spinner_item)
            .apply { setDropDownViewResource(android.R.layout.simple_dropdown_item_1line) }
        sp_country.adapter= ArrayAdapter.createFromResource(this,R.array.country,android.R.layout.simple_spinner_item)
            .apply { setDropDownViewResource(android.R.layout.simple_dropdown_item_1line) }
        notificationsViewModel.getPersonItems().observe(this, Observer {
            if(it!=null){
                ed_nickname.setText(it.nickname)
                ed_age.setText(it.age)
                sp_sex.setSelection(it.sexuality)
                sp_country.setSelection(it.country)
                if(it.userIcon!="") {
                    downloadUri = it.userIcon
                    Glide.with(this)
                        .load(downloadUri)
                        .apply(RequestOptions().override(240))
                        .into(image_eduser)
                }
            }

        })

        //ChooseImage
        btn_picture.setOnClickListener {
            val intent = Intent()
            intent.apply {
                //setType()
                type="image/*"
                action=Intent.ACTION_GET_CONTENT
                //setAction()
            }
            startActivityForResult(intent, REQUESTCODE)
        }
        btn_personOK.setOnClickListener {
            //上傳
            if(isChengePicture) {
                val bitmap = (image_eduser.drawable as BitmapDrawable).bitmap
                val baos = ByteArrayOutputStream()
                bitmap.compress(Bitmap.CompressFormat.PNG, 50, baos)
                val storagemeta =
                    StorageMetadata.Builder().setCustomMetadata("Mykey", "MyValue").build()

                val ref = FirebaseStorage.getInstance()
                    .reference
                    .child(userUid)
                    .child("userIcon")
                    .child("headShot.jpg")
                val uploadTask = ref.putBytes(baos.toByteArray(), storagemeta)
                uploadTask.addOnSuccessListener {
                    ref.downloadUrl.addOnCompleteListener {
                        downloadUri = it.result.toString()
                        uploadData()
                    }
                }
            }
            else{
                uploadData()
            }
        }
        btn_personCancel.setOnClickListener {
            finish()
        }
        //
        sp_sex.onItemSelectedListener=object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(
                parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                sexPosition=position
            }

        }
        sp_country.onItemSelectedListener=object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(
                parent: AdapterView<*>?, view: View?, position: Int, id: Long
            ) {
                countryPosition=position
            }
        }

    }

    private fun uploadData() {
        if(ed_nickname.text.toString()!=""&&ed_age.text.toString()!="") {
            FirebaseFirestore.getInstance()
                .collection("user")
                .document(userUid)
                .set(
                    PersonItem(
                        ed_nickname.text.toString()
                        , sexPosition
                        , countryPosition
                        , ed_age.text.toString()
                        , downloadUri
                    )
                )
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        setResult(Activity.RESULT_OK)
                        AlertDialog.Builder(this).setMessage("上傳成功")
                            .setPositiveButton("OK",
                                DialogInterface.OnClickListener { dialog, which ->
                                    finish()
                                })
                            .show()
                    }
                }
        }
        else
            AlertDialog.Builder(this).setMessage("資料未輸入完成")
                .setPositiveButton("OK",null)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode==REQUESTCODE&&resultCode== Activity.RESULT_OK){
            isChengePicture=true
            val bitmapView = BitmapFactory.decodeStream(
                this.contentResolver
                    .openInputStream(data?.data!!)
            )
            image_eduser
                .setImageBitmap(bitmapView)
        }

    }
}
