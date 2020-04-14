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


class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //navbar
        setSupportActionBar(mytoolbar)
        supportActionBar?.title=getString(R.string.arapp_name)
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


        var readData = ReadData()
        var database: FirebaseDatabase = FirebaseDatabase.getInstance()
        var ref: DatabaseReference = database.getReference("ClinicName")

        ref.addValueEventListener(ReadData())
        booknow.setOnClickListener() {
            startActivity(Intent(this, bookvisit::class.java))

        }

        Manage.setOnClickListener() {
            startActivity(Intent(this, com.rashwan.visittime.Manage::class.java))

        }
    }

    inner class ReadData : ValueEventListener {

        override fun onDataChange(p0: DataSnapshot) {
            var clinicname = p0.getValue(String::class.java)!!

            Cname.text = clinicname.toString()


        }

        override fun onCancelled(p0: DatabaseError) {
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
            (id == R.id.myprofile) ->
                Toast.makeText(this, itemtitle, Toast.LENGTH_LONG).show()
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
           super.onBackPressed()
       }
    }


}
