package com.rashwan.visittime

import android.app.AlertDialog
import android.app.AlertDialog.THEME_DEVICE_DEFAULT_LIGHT
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.core.view.isVisible
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.header_layout.view.*
import java.util.*

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    var mRef: DatabaseReference? = null
    var mAuth: FirebaseAuth? = null
    var database: FirebaseDatabase = FirebaseDatabase.getInstance()
//    val disablMgrsarray = ToolsVisit.Getmgrs()


    override fun onCreate(savedInstanceState: Bundle?) {
//        val disablMgrsarray = ToolsVisit.Getmgrs()

        mRef = database.getReference("Config")
        mAuth = FirebaseAuth.getInstance()


        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(mytoolbar)
        supportActionBar?.title = getString(R.string.arapp_name)
        ToolsVisit.get_Cinfo(c_name, c_address, c_phone, c_web,null)
        ToolsVisit.get_Cinfo(set_c_name, set_c_address,set_c_phone,set_c_web,null)
         mAuth= FirebaseAuth.getInstance()
//        val disablMgrsarray = ToolsVisit.Getmgrs()
        val currentlogged=mAuth?.currentUser?.email.toString()


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
        getUserData()
        getClinicData()
//        if (currentlogged in disablMgrsarray )  Manage.text="مدير"
        ToolsVisit.isAdmin(set_clinic_btn,null)
        ToolsVisit.isAdmin(Mngmntbtn,mybooks)
        ToolsVisit.isAdmin(allbooks,null)

tst.setOnClickListener(){
//    startActivity(Intent(this, com.rashwan.visittime.testslider::class.java))

}


        set_clinic_btn.setOnClickListener(){
            ToolsVisit.btnanim(it)
            if (clinic_info_show.isShown) {
                set_clinic_btn.text="تم"
                clinic_info_show.visibility = View.GONE
                clinic_info_set.visibility = View.VISIBLE
            }else{
                set_clinic_btn.text="تعديل"
                clinic_info_show.visibility = View.VISIBLE
                clinic_info_set.visibility = View.GONE
                ToolsVisit.set_Cinfo(set_c_name, set_c_address,set_c_phone,set_c_web,this,layoutInflater)
                c_name.text=set_c_name.text
                c_address.text=set_c_address.text
                 c_phone.text=set_c_phone.text
                 c_web.text==set_c_web.text
            }
        }
        booknow.setOnClickListener() {
            ToolsVisit.btnanim(it)
            if(mAuth?.currentUser?.isEmailVerified!! || mAuth?.currentUser?.email.toString()=="demo@visittime.com" || mAuth?.currentUser?.email.toString()=="demoadmin@visittime.com"){
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
                startActivity(Intent(this, com.rashwan.visittime.MyReservation::class.java))
        }
        allbooks.setOnClickListener() {
            ToolsVisit.btnanim(it)
            startActivity(Intent(this, com.rashwan.visittime.AllReservation::class.java))
        }

        Mngmntbtn.setOnClickListener() {
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
    fun getClinicData(){


    }

}