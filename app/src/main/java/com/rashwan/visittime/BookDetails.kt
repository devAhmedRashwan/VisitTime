package com.rashwan.visittime

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.ArrayAdapter
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_edit_book.view.*
import kotlinx.android.synthetic.main.book_operations.*
import kotlinx.android.synthetic.main.bookdetails.*
import kotlinx.android.synthetic.main.bookdetails.view.*
import kotlinx.android.synthetic.main.rowstyledetailed.*
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

class BookDetails : AppCompatActivity() {
    var mNotelist: ArrayList<Booked>? = null
    var mRef: DatabaseReference? = null
    var BookedList: ArrayList<Booked>? = null
    var mAuth: FirebaseAuth? = null
    val maintimesarray = ToolsVisit.gettimes()
    var GFA = mutableListOf<String>()
    var database: FirebaseDatabase = FirebaseDatabase.getInstance()
    val FirstDate = ToolsVisit.Dates(0)
    val LastDate = ToolsVisit.Dates(1)
    var timeset= mutableListOf<String>()
    var DaysInNumbersArr: MutableList<Int> = mutableListOf()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.bookdetails)
        DaysInNumbersArr = ToolsVisit.DaysInNumbers()
        val id = intent.extras!!.getString("id")
        mRef = database.getReference("Booked")
        mAuth = FirebaseAuth.getInstance()
        mNotelist = ArrayList()
        dremain.text = intent.extras!!.getString("remain")

        val sortedList = mNotelist?.sortedWith(compareBy { it.visitdate })?.toList()
        mRef?.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
            }

            override fun onDataChange(p0: DataSnapshot) {
                val mbook = p0.child(id!!).getValue(Booked::class.java)!!
                dpatname.text = mbook.patname
                dpatemail.text = mbook.patemail
                dpatphone.text = mbook.patphone
                dpatage.text = mbook.age.toString()
                dvisitdate.text = tools.epochToStr(mbook.visitdate!!.toLong())
                dtime.text = mbook.time
                dvnumber.text = mbook.vnumber.toString()
                dpatnotes.text = mbook.notes.toString()
                dcreator.text = mbook.user
                sugdate.text = tools.epochToStr(mbook.sugvisitdate!!)
                sugtime.text = mbook.sugtime
                if (mbook.sugvisitdate == 0.toLong()) {
                    sugdate.visibility = View.GONE
                    sugtime.visibility = View.GONE
                    sugdateV.visibility = View.GONE
                    sugtimeV.visibility = View.GONE
                } else {
                    sugdate.visibility = View.VISIBLE
                    sugtime.visibility = View.VISIBLE
                    sugdateV.visibility = View.VISIBLE
                    sugtimeV.visibility = View.VISIBLE
                }

                when (mbook.sex) {
                    1 -> dsex.text = getString(R.string.male)
                    2 -> dsex.text = getString(R.string.female)
                    0 -> dsex.text = getString(R.string.undefined)
                }
                when (mbook.isconfirmed) {
                    0 -> {
                        dstatus.text = "انتظار"
                        confirmbtn.isEnabled = true
                    }
                    1 -> dstatus.text = "مؤكدة"
                    2 -> dstatus.text = "تمت"
                    3 -> dstatus.text = "فائتة"
                    4 -> dstatus.text = "ملغية"
                    5 -> {
                        dstatus.text = "طلب تعديل موعد"
                        accepttimechangebtn.isEnabled = true

                    }
                    6 -> {
                        dstatus.text = "طلب الغاء"
                        acceptcancellationbtn.isEnabled = true

                    }
                }

            }
        })


        req_change.setOnClickListener() {
            ToolsVisit.btnanim(it)
            pickDateValidation(it as TextView)
        }
        req_full_change.setOnClickListener() {
            val alertBuilder = AlertDialog.Builder(this)
            var BE_view = layoutInflater.inflate(R.layout.activity_edit_book, null)
            val alertDialog = alertBuilder.create()
            alertDialog.setView(BE_view)
            val textviewid = (R.id.tv)
            val patsexadapter =
                ArrayAdapter(this, R.layout.spstyle, textviewid, arrayOf("غير محدد", "ذكر", "انثى"))
            BE_view.patsex.adapter = patsexadapter
            BE_view.patname.setText(dpatname.text.toString())
            BE_view.patphone.setText(dpatphone.text.toString())
            BE_view.patage.setText(dpatage.text.toString())
            BE_view.patemail.setText(dpatemail.text.toString())
            BE_view.patnote.setText(dpatnotes.text.toString())
            when (dsex.text) {
                "غير محدد" -> BE_view.patsex.setSelection(0)
                "ذكر" -> BE_view.patsex.setSelection(1)
                "انثى" -> BE_view.patsex.setSelection(2)
            }

            BE_view.confirmedit.setOnClickListener() {
                val id = intent.extras!!.getString("id")
                mRef = database.getReference("Booked")
                mRef?.child(id!!)?.child("patname")?.setValue(BE_view.patname.text.toString())
                mRef?.child(id!!)?.child("patphone")?.setValue(BE_view.patphone.text.toString())
                mRef?.child(id!!)?.child("patemail")?.setValue(BE_view.patemail.text.toString())
                mRef?.child(id!!)?.child("patage")?.setValue(BE_view.patage.text.toString().toInt())
                mRef?.child(id!!)?.child("sex")
                    ?.setValue(BE_view.patsex.selectedItemPosition.toInt())
                mRef?.child(id!!)?.child("patnotes")?.setValue(BE_view.patnote.text.toString())
                mRef?.child(id!!)?.child("ischanged")?.setValue(1)
                    ?.addOnSuccessListener {
                        ToolsVisit.vtoast(
                            " تم التعديل بنجاح",
                            1,
                            this@BookDetails,
                            layoutInflater
                        )
                    }
                alertDialog.dismiss()
            }
            alertDialog.show()

        }
        confirmbtn.setOnClickListener() {
            mRef?.child(id!!)?.child("isconfirmed")?.setValue(1)
                ?.addOnCompleteListener {
                    ToolsVisit.vtoast(
                        " تم تأكيد الموعد ",
                        1,
                        this@BookDetails,
                        layoutInflater
                    )
                    confirmbtn.isEnabled = false

                }
        }

        accepttimechangebtn.setOnClickListener() {
            mRef = database.getReference("Booked")
            mRef?.child(id!!)?.child("visitdate")
                ?.setValue(tools.strToEpoch(sugdate.text.toString()))
            mRef?.child(id!!)?.child("time")?.setValue(sugtime.text.toString())
            mRef?.child(id!!)?.child("sugvisitdate")?.setValue(0)
            mRef?.child(id!!)?.child("sugtime")?.setValue("")
            mRef?.child(id!!)?.child("isconfirmed")?.setValue(1)
                ?.addOnCompleteListener {
                    ToolsVisit.vtoast(
                        " تمت الموافقة على الموعد المقترح ",
                        1,
                        this@BookDetails,
                        layoutInflater
                    )
                    accepttimechangebtn.isEnabled = false

                }
        }
        acceptcancellationbtn.setOnClickListener() {
            mRef?.child(id!!)?.child("isconfirmed")?.setValue(4)
                ?.addOnCompleteListener {
                    ToolsVisit.vtoast(
                        " تم قبول الغاء الموعد ",
                        1,
                        this@BookDetails,
                        layoutInflater
                    )
                    acceptcancellationbtn.isEnabled = false
                }
        }
        req_cancelbtn.setOnClickListener() {
            mRef?.child(id!!)?.child("isconfirmed")?.setValue(6)
                ?.addOnCompleteListener {
                    ToolsVisit.vtoast(
                        " تم تقديم طلب الغاء الموعد ",
                        1,
                        this@BookDetails,
                        layoutInflater
                    )
                    acceptcancellationbtn.isEnabled = true
                }
        }

    }

        fun pickDateValidation(textview: TextView): Long {
            var orginalvalue = textview.text
            val c = Calendar.getInstance()
            var result: Long = 0
            val dpd = DatePickerDialog(
                this,
                DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
                    val myCalendar = GregorianCalendar(year, month, dayOfMonth)
                    val dayOfWeek = myCalendar.get(Calendar.DAY_OF_WEEK)
                    if (dayOfWeek !in DaysInNumbersArr) {
                        ToolsVisit.vtoast(
                            "عفوا ، لا يوجد مواعيد متاحه بهذا اليوم حيث انه يوم عطلة اسبوعية بالعيادة ".toString(),
//                        DaysInNumbersArr.toString(),
                            3,
                            this,
                            layoutInflater
                        )
                        pickDateValidation(textview)
                    } else {
                        val a = myCalendar.timeInMillis.toString()
                        result = myCalendar.timeInMillis.toString().substring(0, 10).toLong()
                        sugdate.text = tools.epochToStr(result)
                        AvailableDialoge(result, sugtime, textview)
                        sugtime.text = getString(R.string.sugdate)
                        sugtime.visibility = View.VISIBLE
                        sugdate.visibility = View.VISIBLE
                    }
                },
                1,
                2,
                3
            )
            try {
                dpd.datePicker.firstDayOfWeek = Calendar.SUNDAY
//        dpd.datePicker.minDate = (c.timeInMillis)  //today's date in millisec
                dpd.datePicker.minDate = (FirstDate[0] + "000").toLong()
                dpd.datePicker.maxDate = (LastDate[0] + "000").toLong()

                dpd.show()
            } catch (e: Exception) {
                ToolsVisit.vtoast(
                    "من فضلك تأكد من اتصالك بالانترنت ",
                    2,
                    this,
                    layoutInflater
                )
            }
            return result
        }

        fun AvailableDialoge(Date: Long, TextView: TextView, DateTextView: TextView): String {
            var result = "null val"
            val BRef = database.getReference("Booked")
            BRef.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onCancelled(databaseError: DatabaseError) {
                }

                override fun onDataChange(dataSnapshot: DataSnapshot) = try {
                    timeset.clear()
                    timeset.addAll(maintimesarray)
                    var wantedvisit = ""
                    for (n in dataSnapshot.children) {
                        val visita = n.getValue(Booked::class.java)
                        if (Date == visita?.visitdate?.toLong()) {
                            wantedvisit = visita.time.toString()
                        }
                        if (timeset.contains(wantedvisit)) {
                            timeset.remove(wantedvisit)
                        }
                    }
                    val mBuilder = AlertDialog.Builder(
                        this@BookDetails,
                        AlertDialog.THEME_DEVICE_DEFAULT_LIGHT
                    )
                    mBuilder.setTitle(" المواعيد المتوفرة في يوم: ${sugdate.text.toString()} ")
                    mBuilder.setIcon(R.drawable.icon_time_set)
                    mBuilder.setSingleChoiceItems(
                        timeset.toTypedArray(),
                        0
                    ) { dialogInterface, i ->
                    }
                    mBuilder.setPositiveButton(getString(R.string.choose)) { dialog, which ->
                        val position = (dialog as AlertDialog).listView.checkedItemPosition
                        if (position != -1 && timeset.size != 0) {
                            TextView.text = timeset[position]
                            result = timeset[position]
                            if (sugtime.text.isNotEmpty()) {
                                val id = intent.extras!!.getString("id")
                                mRef = database.getReference("Booked")
                                mRef?.child(id!!)?.child("sugvisitdate")
                                    ?.setValue(tools.strToEpoch(sugdate.text.toString()))
                                mRef?.child(id!!)?.child("sugtime")?.setValue(sugtime.text)
                                mRef?.child(id!!)?.child("isconfirmed")?.setValue(5)
                                    ?.addOnSuccessListener {
                                        ToolsVisit.vtoast(
                                            " تم ارسال طلب تعديل الموعد برجاء انتظار رسالة التأكيد",
                                            1,
                                            this@BookDetails,
                                            layoutInflater
                                        )
                                    }
                            }
                        }
                    }
                    mBuilder.setNeutralButton(getString(R.string.cancel)) { dialog, which ->
                        if (!TextView.text.contains(":")) {
                            TextView.text = getString(R.string.time)
                            DateTextView.text = getString(R.string.visitdate)
                        }
                        dialog.cancel()
                    }
                    val mDialog: AlertDialog = mBuilder.create()
                    if (Date.toString().length > 10 && timeset.size == 0) {
                        ToolsVisit.vtoast(
                            "عفوا ، لا يوجد مواعيد متاحة في هذا اليوم",
                            2,
                            this@BookDetails,
                            layoutInflater
                        )
                    } else if (Date.toString().length < 10) {
                        ToolsVisit.vtoast(
                            "اختر اليوم اولا",
                            2,
                            this@BookDetails,
                            layoutInflater
                        )
                    } else {
                        mDialog.show()
                    }
                } catch (e: Exception) {
                    timeset.clear()
                }
            })
            return result
        }
    }



