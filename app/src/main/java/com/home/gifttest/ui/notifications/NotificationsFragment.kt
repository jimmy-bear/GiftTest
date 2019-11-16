package com.home.gifttest.ui.notifications

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.home.gifttest.R
import kotlinx.android.synthetic.main.fragment_notifications.*


class NotificationsFragment : Fragment() {

    private lateinit var notificationsViewModel: NotificationsViewModel
    private val TAG=NotificationsFragment::class.java.simpleName
    val userUID=FirebaseAuth.getInstance().currentUser!!.uid
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        notificationsViewModel =
            ViewModelProviders.of(this).get(NotificationsViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_notifications, container, false)
        val textView: TextView = root.findViewById(R.id.textView6)
        val editButton:ImageButton=root.findViewById(R.id.imageButton2)
        val checkButton:ImageButton=root.findViewById(R.id.btn_check)
        val cancelButton:ImageButton=root.findViewById(R.id.btn_cancel)
        val tx_nickname:TextView=root.findViewById(R.id.tx_nickname)
        val tx_sex:TextView=root.findViewById(R.id.tx_sex)
        val tx_country:TextView=root.findViewById(R.id.tx_country)
        val tx_area:TextView=root.findViewById(R.id.tx_area)
        val tx_school:TextView=root.findViewById(R.id.tx_school)

        val ed_nickname:EditText=root.findViewById(R.id.ed_nickname)
        val sp_sex:Spinner=root.findViewById(R.id.sp_sex)
        val sp_country:Spinner=root.findViewById(R.id.sp_country)
        val sp_ares:Spinner=root.findViewById(R.id.sp_area)
        val sp_school:Spinner=root.findViewById(R.id.sp_school)
        var sex_position=0
        var country_position=0
        sp_sex.adapter=ArrayAdapter.createFromResource(root.context,R.array.sex,android.R.layout.simple_spinner_item)
            .apply { setDropDownViewResource(android.R.layout.simple_dropdown_item_1line) }
        sp_country.adapter=ArrayAdapter.createFromResource(root.context,R.array.country,android.R.layout.simple_spinner_item)
            .apply { setDropDownViewResource(android.R.layout.simple_dropdown_item_1line) }
        readData(tx_nickname)


        notificationsViewModel.text.observe(this, Observer {
            textView.text = it
        })
        editButton.setOnClickListener {
            editMode(
                checkButton, cancelButton, editButton, ed_nickname,
                sp_sex, sp_country, sp_ares, sp_school,
                tx_nickname, tx_sex, tx_country, tx_area, tx_school
            )
            ed_nickname.setText(tx_nickname.text)

            sp_sex.onItemSelectedListener=object :AdapterView.OnItemSelectedListener{
                override fun onNothingSelected(parent: AdapterView<*>?) {
                    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                }

                override fun onItemSelected(
                    parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                    sex_position=position
                }

            }
            sp_country.onItemSelectedListener=object :AdapterView.OnItemSelectedListener{
                override fun onNothingSelected(parent: AdapterView<*>?) {
                    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                }

                override fun onItemSelected(
                    parent: AdapterView<*>?, view: View?, position: Int, id: Long
                ) {
                    country_position=position
                }

            }

        }
        cancelButton.setOnClickListener {
            viewMode(
                editButton, checkButton, cancelButton, ed_nickname,
                sp_sex, sp_country, sp_ares, sp_school,
                tx_nickname, tx_sex, tx_country, tx_area, tx_school
            )
        }
        checkButton.setOnClickListener {
            viewMode(
                editButton, checkButton, cancelButton, ed_nickname,
                sp_sex, sp_country, sp_ares, sp_school,
                tx_nickname, tx_sex, tx_country, tx_area, tx_school
            )

            FirebaseFirestore.getInstance()
                .collection("user")
                .document(userUID)
                .set(PersonItem(ed_nickname.text.toString(),sex_position,country_position))
            readData(tx_nickname)

        }
        return root
    }

    private fun readData(tx_nickname: TextView) {
        FirebaseFirestore.getInstance().collection("user").document(userUID).addSnapshotListener {
                Snapshot, exception ->
            if(Snapshot!=null){
                val array_sex=resources.getStringArray(R.array.sex)
                val array_country=resources.getStringArray(R.array.country)
                var data=Snapshot!!.toObject(PersonItem::class.java)
                tx_nickname.text=data!!.nickname
                tx_sex.text=array_sex.get(data!!.sexuality)
                tx_country.text=array_country.get(data!!.country)
                sp_sex.setSelection(data!!.sexuality)
                sp_country.setSelection(data!!.country)

            }
        }
    }

    private fun viewMode(
        editButton: ImageButton,
        checkButton: ImageButton,
        cancelButton: ImageButton,
        ed_nickname: EditText,
        sp_sex: Spinner,
        sp_county: Spinner,
        sp_ares: Spinner,
        sp_school: Spinner,
        tx_nickname: TextView,
        tx_sex: TextView,
        tx_county: TextView,
        tx_area: TextView,
        tx_school: TextView
    ) {
        editButton.visibility = View.VISIBLE
        checkButton.visibility = View.GONE
        cancelButton.visibility = View.GONE
        //
        ed_nickname.visibility = View.GONE
        sp_sex.visibility = View.GONE
        sp_county.visibility = View.GONE
        sp_ares.visibility = View.GONE
        sp_school.visibility = View.GONE
        //
        tx_nickname.visibility = View.VISIBLE
        tx_sex.visibility = View.VISIBLE
        tx_county.visibility = View.VISIBLE
        tx_area.visibility = View.VISIBLE
        tx_school.visibility = View.VISIBLE
    }

    private fun editMode(
        checkButton: ImageButton,
        cancelButton: ImageButton,
        editButton: ImageButton,
        ed_nickname: EditText,
        sp_sex: Spinner,
        sp_county: Spinner,
        sp_ares: Spinner,
        sp_school: Spinner,
        tx_nickname: TextView,
        tx_sex: TextView,
        tx_county: TextView,
        tx_area: TextView,
        tx_school: TextView
    ) {
        checkButton.visibility = View.VISIBLE
        cancelButton.visibility = View.VISIBLE
        editButton.visibility = View.GONE
        //
        ed_nickname.visibility = View.VISIBLE
        sp_sex.visibility = View.VISIBLE
        sp_county.visibility = View.VISIBLE
        sp_ares.visibility = View.VISIBLE
        sp_school.visibility = View.VISIBLE
        //
        tx_nickname.visibility = View.GONE
        tx_sex.visibility = View.GONE
        tx_county.visibility = View.GONE
        tx_area.visibility = View.GONE
        tx_school.visibility = View.GONE
    }


}