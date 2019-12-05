package com.home.gifttest

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.IdpResponse
import com.google.api.LogDescriptor
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.home.gifttest.ui.notifications.EditActivity
import com.home.gifttest.ui.notifications.PersonItem
import java.util.*

class MainActivity : AppCompatActivity() ,FirebaseAuth.AuthStateListener{


    private val RC_Information=150
    private val RC_SIGN_IN=100
    private val TAG=MainActivity::class.java.simpleName

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val navView: BottomNavigationView = findViewById(R.id.nav_view)

        val navController = findNavController(R.id.nav_host_fragment)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications
            )
        )

        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

    }

    override fun onAuthStateChanged(auth: FirebaseAuth) {
        val user=auth.currentUser
        if(user==null){
            startActivityForResult(
                AuthUI.getInstance().createSignInIntentBuilder()
                    .setAvailableProviders(
                        Arrays.asList(
                            AuthUI.IdpConfig.EmailBuilder().build(),
                            AuthUI.IdpConfig.GoogleBuilder().build()
                        ))
                    .setIsSmartLockEnabled(false)
                    .setLogo(R.drawable.happy)
                    .build(),
                RC_SIGN_IN
            )
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

//        if(requestCode==RC_SIGN_IN){
//            if (resultCode== Activity.RESULT_OK){
//                if(FirebaseAuth.getInstance().currentUser!=null) {
//                    FirebaseFirestore.getInstance().collection("user")
//                        .document(FirebaseAuth.getInstance().currentUser!!.uid)
//                        .get().addOnSuccessListener {
//                            val item = it.toObject(PersonItem::class.java) ?: PersonItem()
//                            if (item == PersonItem()) {
//                                startActivityForResult(
//                                    Intent(this, EditActivity::class.java),
//                                    RC_Information
//                                )
//                            }
//                        }
//                }
            }
 //       }
//        if(requestCode==RC_Information){
//            if (resultCode!= Activity.RESULT_OK) {
//                finish()
//            }
//        }
//        else finish()
 //   }

    override fun onStart() {
        super.onStart()
        FirebaseAuth.getInstance().addAuthStateListener(this)
    }

    override fun onStop() {
        super.onStop()
        FirebaseAuth.getInstance().removeAuthStateListener(this)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.signout_menu,menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId) {

            R.id.action_signout->{
                Log.d(TAG,"signout")
                FirebaseAuth.getInstance().signOut()
                true
            }
            else->super.onOptionsItemSelected(item)
        }

    }

}
