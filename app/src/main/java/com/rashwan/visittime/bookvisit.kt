package com.rashwan.visittime

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.ProgressDialog.show
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_bookvisit.*
import kotlinx.android.synthetic.main.confirmed.view.*
import java.lang.Double.parseDouble
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import java.util.concurrent.TimeUnit
import java.util.GregorianCalendar as GregorianCalendar1


 class bookvisit : AppCompatActivity() {

    var mRef: DatabaseReference? = null
    var BookedList: ArrayList<Booked>? = null
    var mAuth: FirebaseAuth? = null
    val maintimesarray = ToolsVisit.gettimes()
    var database: FirebaseDatabase = FirebaseDatabase.getInstance()
    var availabletimes: MutableList<String> = mutableListOf()
    var DaysInNumbersArr: MutableList<Int> = mutableListOf()
     val FirstDate = ToolsVisit.Dates(0)
     val LastDate = ToolsVisit.Dates(1)



     override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bookvisit)
        mRef = database.getReference("Booked")
        mAuth = FirebaseAuth.getInstance()
         DaysInNumbersArr= ToolsVisit.DaysInNumbers()
        pickday.setOnClickListener() {
            pickDateValidation(pickday)
            mRef?.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    try {
                        availabletimes.clear()
                        availabletimes.addAll(maintimesarray)
                        var wantedvisit = ""
                        for (n in dataSnapshot.children) {
                            val visita = n.getValue(Booked::class.java)
                            val pickedvisitdate = visita?.visitdate?.toLong()
                            if (pickedvisitdate == tools.strToEpoch(pickday.text.toString())) {
                                wantedvisit = visita.time.toString()
                            }
                            if (availabletimes.contains(wantedvisit)) {
                                availabletimes.remove(wantedvisit)

                            }

                        }



                    } catch (e: Exception) {

                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    ToolsVisit.vtoast(
                        " حدث خطأ في الاتصال بالانترنت",
                        3,
                        this@bookvisit,
                        layoutInflater)
                }


            })

        }

        popuptime.setOnClickListener() {
            /*
            mRef?.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    try {
                        availabletimes.clear()
                        availabletimes.addAll(maintimesarray)
                        var wantedvisit = ""
                        for (n in dataSnapshot.children) {
                            val visita = n.getValue(Booked::class.java)
                            val pickedvisitdate = visita?.visitdate?.toLong()
                            if (pickedvisitdate == tools.strToEpoch(pickday.text.toString())) {
                                wantedvisit = visita.time.toString()
                            }
                            if (availabletimes.contains(wantedvisit)) {
                                availabletimes.remove(wantedvisit)

                            }

                        }



                    } catch (e: Exception) {

                        ToolsVisit.vtoast("اختر اليوم اولا", 2, this@bookvisit, layoutInflater)
                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {}


            })

*/
            val mBuilder = AlertDialog.Builder(
                this@bookvisit,
                AlertDialog.THEME_DEVICE_DEFAULT_LIGHT
            )
            mBuilder.setTitle(" المواعيد المتوفرة في يوم ${pickday.text.toString()} ")
            mBuilder.setSingleChoiceItems(
                availabletimes.toTypedArray(),
                0
            ) { dialogInterface, i ->
            }
            // Set the neutral/cancel button click listener
            // alert dialog positive button
            mBuilder.setPositiveButton("اختيار") { dialog, which ->
                val position = (dialog as AlertDialog).listView.checkedItemPosition
                if (position != -1 && availabletimes.size !=0) {
                    popuptime.text = availabletimes[position]
                }
            }
            mBuilder.setNeutralButton("الغاء") { dialog, which ->
                // Do something when click the neutral button
                dialog.cancel()
            }

            val mDialog = mBuilder.create()


            if (pickday.text.toString().length >10  && availabletimes.size==0 ){
                ToolsVisit.vtoast(
                    "عفوا ، لا يوجد مواعيد متاحة في هذا اليوم",
                    2,
                    this@bookvisit,
                    layoutInflater)
            }else if( pickday.text.toString().length <10){
                ToolsVisit.vtoast(
                    "اختر اليوم اولا",
                    2,
                    this@bookvisit,
                    layoutInflater)
            }else{
                mDialog.show()
            }
        }




        getmain.setOnClickListener() {
            val textviewid = (R.id.tv)
            val maintimesadapter =
                ArrayAdapter(this@bookvisit, R.layout.spstyle, textviewid, maintimesarray)
            maintimes.adapter = maintimesadapter
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
                        var pickedvisitdate = visita?.visitdate?.toLong()
                        if (pickedvisitdate == tools.strToEpoch(pickday.text.toString())) {
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
                    ToolsVisit.vtoast(
                        "حدث خطأ",
                        3,
                        this@bookvisit,
                        layoutInflater)
                    println("loadPost:onCancelled ${databaseError.toException()}")

                }
            })


        }
        booknow.setOnClickListener() {
            booknow()
        }
        backtomain.setOnClickListener() {
            startActivity(Intent(this, MainActivity::class.java))
        }
    }

    fun booknow() {


        if (pickday.text.toString().length >10 && patname.text.isNotEmpty() && patphone.text.length > 7 && patage.text.isNotEmpty()
        ) {


            val id = mRef!!.push().key!!
            try {
                val mybooking =
                    Booked(
                        id,
                        tools.strToEpoch(pickday.text.toString()),
                        pickday.text.toString(),
                        (LocalDateTime.now().format(
                            DateTimeFormatter.ofPattern("yyyy-MM-dd EEEE HH-mm")
                        )),
                        popuptime.text.toString(),
                        1,
                        0,
                        0,
                        0,
                        patname.text.toString(),
                        patemail.text.toString(),
                        patphone.text.toString(),
                        0, 1, "notes", "currentuser"
                    )
                mRef!!.child(id).setValue(mybooking)
                    .addOnSuccessListener {
                        ToolsVisit.vtoast(
                            " تم ارسال طلب الحجز برجاء انتظار رسالة التأكيد",
                            1,
                            this@bookvisit,
                            layoutInflater
                        )
                    }
                    .addOnFailureListener {
                        ToolsVisit.vtoast(
                            "لم تتم اضافة الموعد" + it.message!!,
                            3,
                            this@bookvisit,
                            layoutInflater
                        )
                    }
                startActivity(Intent(this, MainActivity::class.java))


            } catch (e: Exception) {
                ToolsVisit.vtoast(e.message!!, 2, this@bookvisit, layoutInflater)
            }
        }else{
            ToolsVisit.vtoast("تأكد من اكمال جميع البيانات ", 2, this@bookvisit, layoutInflater)

        }
    }
    fun getavailabletimes(): MutableList<String> {
        var vacant: MutableList<String> = mutableListOf()

        mRef?.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                try {
                    vacant.addAll(maintimesarray)
                    var wantedvisit = "notexist"
                    for (n in dataSnapshot.children) {
                        val visita = n.getValue(Booked::class.java)

                        var visittime = visita?.time.toString()
                        var pickedvisitdate = visita?.visitdate?.toLong()
                        if (pickedvisitdate == tools.strToEpoch(pickday.text.toString())) {
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
//                    myspinner.adapter = adapter
                } catch (e: Exception) {
                    ToolsVisit.vtoast(e.message.toString(), 3, this@bookvisit, layoutInflater)


                }
            }


            override fun onCancelled(databaseError: DatabaseError) {
                ToolsVisit.vtoast(
                     "حدث خطأ",
                    3,
                    this@bookvisit,
                    layoutInflater)
                println("loadPost:onCancelled ${databaseError.toException()}")

            }
        })
        return vacant
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

                if (dayOfWeek  !in DaysInNumbersArr) {

                    ToolsVisit.vtoast(
                        "عفوا ، لا يوجد مواعيد متاحه بهذا اليوم حيث انه يوم عطلة اسبوعية بالعيادة ".toString(),
//                        DaysInNumbersArr.toString(),
                        3,
                        this@bookvisit,
                        layoutInflater
                    )

                } else {
                    //  textview.text = mydateformated

                    val a = myCalendar.timeInMillis.toString()
                    val b = a.substring(0, 10).toLong()
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
//        dpd.datePicker.minDate = (c.timeInMillis)  //today's date in millisec
        dpd.datePicker.minDate = (FirstDate[0]+"000").toLong()
        dpd.datePicker.maxDate = (LastDate[0]+"000").toLong()

        //set 30/5/2020 as last day

        //   dpd.datePicker.maxDate = ToolsVisit.Vtools.getmaxdate().toLong()

        val map = HashMap<String, Booked>()

        for ((timez, time) in map) {
        }


        dpd.show()
//        if(result.toString().contains("1")){

//        }
        popuptime.visibility=View.VISIBLE
        return result
    }


}