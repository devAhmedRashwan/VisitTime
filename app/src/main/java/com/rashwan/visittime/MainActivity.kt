package com.rashwan.visittime

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import android.view.MenuItem
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.header_layout.view.*


class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    var mRef: DatabaseReference? = null
    var mAuth: FirebaseAuth? = null
    var database: FirebaseDatabase = FirebaseDatabase.getInstance()


    override fun onCreate(savedInstanceState: Bundle?) {
        mRef = database.getReference("Config")
        mAuth = FirebaseAuth.getInstance()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //navbar
        setSupportActionBar(mytoolbar)
        supportActionBar?.title = getString(R.string.arapp_name)
        val actiontoggle = ActionBarDrawerToggle(
            this,
            mydrawer,
            mytoolbar,
            R.string.draw_open,
            R.string.draw_close
        )
        mydrawer.addDrawerListener(actiontoggle)
        actiontoggle.syncState()
        mNav.setNavigationItemSelectedListener(this)
//end nav bar


        getUserData()
        booknow.setOnClickListener() {
            ToolsVisit.btnanim(it)
            if(mAuth?.currentUser?.isEmailVerified!! || mAuth?.currentUser?.email.toString()=="demo@visittime.com"){
                startActivity(Intent(this, com.rashwan.visittime.bookvisit::class.java))
            }else{
                ToolsVisit.vtoast(
                    "يجب تأكيد البريد الالكتروني قبل البدء في الحجز",
                    1,
                    this,
                    layoutInflater
                )
            }
        }
        mybooks.setOnClickListener() {
            ToolsVisit.btnanim(it)
                startActivity(Intent(this, com.rashwan.visittime.AllReservation::class.java))
        }
        Manage.setOnClickListener() {
            ToolsVisit.btnanim(it)
            startActivity(Intent(this, com.rashwan.visittime.Manage::class.java))
        }
    }


    override fun onNavigationItemSelected(menuitem: MenuItem): Boolean {
        var id = menuitem.itemId
        var itemtitle = menuitem.title
        mydrawer.closeDrawer(GravityCompat.START)
        when {
            (id == R.id.exit) -> finishAffinity()
            (id == R.id.incoming) ->
                Toast.makeText(this, itemtitle, Toast.LENGTH_LONG).show()
            (id == R.id.myprofile) -> {

                Toast.makeText(this, itemtitle, Toast.LENGTH_LONG).show()
            }
            (id == R.id.logout) -> {
                FirebaseAuth.getInstance().signOut()
                startActivity(Intent(this, landing::class.java))
            }
                (id == R.id.history) ->
                Toast.makeText(this, itemtitle, Toast.LENGTH_LONG).show()


        }
//        super.onBackPressed()


        return true
    }

    override fun onBackPressed() {
       if (mydrawer.isDrawerOpen(GravityCompat.START)){
           mydrawer.closeDrawer(GravityCompat.START)

       }
        else
       {
           finishAffinity()
       }
    }
    fun getUserData() {
try {
    val headerView = mNav.getHeaderView(0)
    headerView.usemail.text = mAuth?.currentUser?.email.toString()
    headerView.usname.text = mAuth?.currentUser?.displayName
    headerView.usphone.text = mAuth?.currentUser?.phoneNumber.toString()

}catch (e:Exception){
    ToolsVisit.vtoast(
        "من فضلك تأكد من اتصالك بالانترنت ",
        2,
        this,
        layoutInflater
    )
}

    }
}