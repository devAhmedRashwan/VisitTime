package com.rashwan.visittime

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.os.Bundle
import android.view.animation.AnimationUtils
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.bookdetails.*
import kotlinx.android.synthetic.main.bookdetails.view.*
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

class BookDetails : AppCompatActivity() {
    var mRef: DatabaseReference? = null
    var mNotelist: ArrayList<Booked>? = null
    var database: FirebaseDatabase = FirebaseDatabase.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.bookdetails)

        dpatname.text = intent.extras!!.getString("patname")
        dpatemail.text = intent.extras!!.getString("patemail")
        dpatphone.text = intent.extras!!.getString("patphone")
        dpatage.text = intent.extras!!.getString("patage")
        dvisitdate.text = intent.extras!!.getString("visitdate")
        dtime.text = intent.extras!!.getString("time")
        dvnumber.text = intent.extras!!.getString("vnumber")
        dremain.text = intent.extras!!.getString("remain")
        dcreator.text = intent.extras!!.getString("creator")
        //sex
        if (intent.extras!!.getInt("sex") == 1) {
            dsex.text = getString(R.string.male)
        }
        if (intent.extras!!.getInt("sex") == 2) {
            dsex.text = getString(R.string.female)
        }
        if (intent.extras!!.getInt("sex") == 0) {
            dsex.text = getString(R.string.undefined)
        }
//confirmed
        if (intent.extras!!.getInt("isconfirmed") == 0) {
            dstatus.text = getString(R.string.pending)
        }
        if (intent.extras!!.getInt("isconfirmed") == 1) {
            dstatus.text = getString(R.string.confirmed)
        }
        if (intent.extras!!.getInt("isconfirmed") == 2) {
            dstatus.text = getString(R.string.visitdone)
        }
        if (intent.extras!!.getInt("isconfirmed") == 3) {
            dstatus.text = getString(R.string.missed)
        }
        if (intent.extras!!.getInt("isconfirmed") == 4) {
            dstatus.text = getString(R.string.visitcanceled)
        }


    }
}


