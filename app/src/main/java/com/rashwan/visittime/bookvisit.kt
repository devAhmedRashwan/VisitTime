package com.rashwan.visittime

import android.app.Application
import android.app.DatePickerDialog
import android.content.DialogInterface
import android.media.ImageReader.newInstance
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

import kotlinx.android.synthetic.main.activity_bookvisit.*
import java.text.SimpleDateFormat
import java.util.*
import java.util.GregorianCalendar as GregorianCalendar1


class bookvisit : AppCompatActivity(){

    var mRef: DatabaseReference? = null
    var BookedList: ArrayList<Booked>? = null
    var mAuth: FirebaseAuth? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bookvisit)
        var database: FirebaseDatabase = FirebaseDatabase.getInstance()
        mRef = database.getReference("Booked")
        mAuth = FirebaseAuth.getInstance()

        visitdate.setOnClickListener() {
            pickDateValidation(visitdate)

            check.text = ToolsVisit.getmaxdate()


        }
        val maintimesarray = ToolsVisit.gettimes()

        getmain.setOnClickListener() {
            val textviewid = (R.id.tv)
            val maintimesadapter =
                ArrayAdapter(this@bookvisit, R.layout.spstyle, textviewid, maintimesarray)
            maintimes.adapter = maintimesadapter
        }


        availabletimes.setOnClickListener() {
            mRef?.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    try {
                        var vacant: MutableList<String> = mutableListOf()
                        vacant.addAll(maintimesarray)
                        var wantedvisit = "notexist"
                        for (n in dataSnapshot.children) {
                            val visita = n.getValue(Booked::class.java)

                            var visittime = visita?.time.toString()
                            var pickedvisitdate = visita?.date?.toLong()
                            if (pickedvisitdate == tools.strToEpoch(visitdate.text.toString())) {
                                wantedvisit = visita?.time.toString()
                            }

                            if (vacant.contains(wantedvisit)) {
                                vacant.remove(wantedvisit)
                            } else {
                                // vacant.add(visittime)


                            }


                        }
                        val adapter =
                            ArrayAdapter(this@bookvisit, R.layout.spstyle, (R.id.tv), vacant)
                        availabletimespinner.adapter = adapter
                    }
                    catch (e:Exception){
                                  Toast.makeText(application, e.message , Toast.LENGTH_LONG).show()

                    }
                }


                override fun onCancelled(databaseError: DatabaseError) {
                    println("loadPost:onCancelled ${databaseError.toException()}")

                }
            })
//            Toast.makeText(this, gotid , Toast.LENGTH_LONG).show()


        }
        fullbooked.setOnClickListener() {
            mRef?.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    var vacant: MutableList<String> = mutableListOf()
                    var bookedlist: MutableList<String> = mutableListOf()
                    vacant.addAll(maintimesarray)
                    var wantedvisit = "notexist"
                    for (n in dataSnapshot.children) {
                        val visita = n.getValue(Booked::class.java)
                        var pickedvisitdate = visita?.date?.toLong()
                        if (pickedvisitdate == tools.strToEpoch(visitdate.text.toString())) {
                            wantedvisit = visita?.time.toString()
                        }

                        if (vacant.contains(wantedvisit)) {
                            vacant.remove(wantedvisit)
                            bookedlist.add(wantedvisit)

                        } else {


                        }


                    }
                    val adapter =
                        ArrayAdapter(this@bookvisit, R.layout.spstyle, (R.id.tv), bookedlist)
                    fullbookedsp.adapter = adapter
                }


                override fun onCancelled(databaseError: DatabaseError) {
                    println("loadPost:onCancelled ${databaseError.toException()}")

                }
            })
//            Toast.makeText(this, gotid , Toast.LENGTH_LONG).show()


        }
        booknow.setOnClickListener() {
            val id = mRef!!.push().key!!
            try {
                val mybooking =
                    Booked(
                        id,
                        tools.strToEpoch(visitdate.text.toString()),
                        availabletimespinner.selectedItem.toString(),
                        0,
                        0,
                        0,
                        0,
                        "",
                        0,
                        "test"
                    )


                mRef!!.child(id).setValue(mybooking)

            } catch (e: Exception) {
                Toast.makeText(
                    this,
                    "لم يتم اختيار موعد",
                    Toast.LENGTH_SHORT
                ).show()

            }



            check.setOnClickListener() {


            }













        }



    }
    fun pickDateValidation(textview: TextView): Long {
        var orginalvalue = textview.text
        val c = Calendar.getInstance()
        //   val year = c.get(Calendar.YEAR)
        // var month = c.get(Calendar.MONTH)
        //var day = c.get(Calendar.DAY_OF_MONTH)
        //  var pure = year.toString() + month.toString() + day.toString()
        var result: Long = 0

        val dpd = DatePickerDialog(
            this,
            DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
                val myCalendar = GregorianCalendar1(year, month, dayOfMonth)


                var dayOfWeek = myCalendar.get(Calendar.DAY_OF_WEEK)
                var mydateformated =
                    (SimpleDateFormat(
                        "yyyy-MM-dd",
                        Locale.getDefault()
                    ).format(myCalendar.time))

                var timecaptured = myCalendar.time

                if (dayOfWeek > 5) {

                    Toast.makeText(
                        this,
                        "لا يمكن اختيار يوم عطلة ، يرجى التأكد من التاريخ والمحاولة ثانية!",
                        Toast.LENGTH_SHORT
                    ).show()

                } else {
                    //  textview.text = mydateformated

                    var a = myCalendar.timeInMillis.toString()
                    var b = a.substring(0, 10).toLong()
                    result = b
                    // day=result
                    //  textview.text = tools.epochToStr(b)
                    textview.text = tools.epochToStr(b)


                }

            },
            1,
            2,
            3
        )


        dpd.datePicker.firstDayOfWeek = Calendar.SUNDAY
        dpd.datePicker.minDate = (c.timeInMillis)
        //set 30/5/2020 as last day

        //   dpd.datePicker.maxDate = ToolsVisit.Vtools.getmaxdate().toLong()

        val map = HashMap<String, Booked>()

        for ((timez, time) in map) {
        }


        dpd.show()
        return result
    }

}