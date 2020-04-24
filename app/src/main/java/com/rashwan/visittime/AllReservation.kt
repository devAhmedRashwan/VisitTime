package com.rashwan.visittime

import android.app.AlertDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_allreservation.*
import kotlinx.android.synthetic.main.book_operations.view.*
import kotlinx.android.synthetic.main.rowstyle.*
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.temporal.TemporalAdjusters

class AllReservation : AppCompatActivity() {
    var mNotelist: ArrayList<Booked>? = null
    var mAuth: FirebaseAuth? = null
    var mRef: DatabaseReference? = null
    var database: FirebaseDatabase = FirebaseDatabase.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_allreservation)
        mRef = database.getReference("Booked")
        mAuth = FirebaseAuth.getInstance()
        mNotelist = ArrayList()
        res_LV.onItemClickListener =
            AdapterView.OnItemClickListener { _, _, position, _ ->
                val sortedList = mNotelist?.sortedWith(compareBy { it.visitdate })?.toList()
                val vBook = sortedList?.get(position)!!
                val BdCard = Intent(this, BookDetails::class.java)

                BdCard.putExtra("id", vBook.id)
                BdCard.putExtra("patname", vBook.patname)
                BdCard.putExtra("vnumber", vBook.vnumber.toString())
                BdCard.putExtra("patphone", vBook.patphone)
                BdCard.putExtra("patemail", vBook.patemail)
                BdCard.putExtra("patage", vBook.age.toString())
                BdCard.putExtra("visitdate", tools.epochToStr(vBook.visitdate!!.toLong()))
                BdCard.putExtra("time", vBook.time)
                BdCard.putExtra("sex", vBook.sex)
                BdCard.putExtra("isconfirmed", vBook.isconfirmed)
                BdCard.putExtra("remain", remain.text.toString())
                BdCard.putExtra("creator", vBook.user)


                startActivity(BdCard)
            }

        res_LV.onItemLongClickListener=
            AdapterView.OnItemLongClickListener { _, _, p2, _ ->
                val bookdbid = mNotelist?.get(p2)!!
                val alertBuilder = AlertDialog.Builder(this)
                var update_book_view = layoutInflater.inflate(R.layout.book_operations, null)
                val alertDialog = alertBuilder.create()
                alertDialog.setView(update_book_view)
                alertDialog.show()
                update_book_view.confirm.setOnClickListener {
                    var childRef = mRef?.child(bookdbid.id!!)
                    childRef?.child("isbooked")?.setValue(1)
                    Toast.makeText(this, "تم التعديل", Toast.LENGTH_LONG).show()
                    alertDialog.dismiss()
                }


                true
            }





    }
    fun LVA(view: View) {
        mNotelist?.clear()
//        Pbar.isVisible=true

        if (btnswitch.isChecked == true) {
            val searchtext = searchtext.text.toString()
//            TTitle2.text ="نتيجة البحث عن " + searchtext +"."
//Toast.makeText("نتيجة البحث عن " + searchtext +".")
        } else {
            //  TTitle2.isVisible = false

        }
        val today = LocalDate.now()
        var firstdayofweek = today
        while (firstdayofweek.dayOfWeek !== DayOfWeek.SATURDAY) {
            firstdayofweek = firstdayofweek.minusDays(1)
        }

        var lastdayofweek = today
        while (lastdayofweek.dayOfWeek !== DayOfWeek.FRIDAY) {
            lastdayofweek = lastdayofweek.plusDays(1)
        }
        var firstdayofmonth = today
        while (firstdayofmonth.dayOfMonth != 1) {
            firstdayofmonth = firstdayofmonth.minusDays(1)
        }
        var    lastdayofmonth =firstdayofmonth.with(TemporalAdjusters.lastDayOfMonth())
        var lastdayofnextmonth =firstdayofmonth.plusMonths(1).with(TemporalAdjusters.lastDayOfMonth())

        var firstDayOfWeek = tools.strToEpoch(firstdayofweek.toString())
        var lastDayOfWeek = tools.strToEpoch(lastdayofweek.toString())
        var delStat=false
        when {
            view.id == (R.id.allbtn) -> {
                firstDayOfWeek = "0".toLong()
                lastDayOfWeek = "999999999999".toLong()

            }
            view.id == (R.id.cwbtn) -> {

                firstDayOfWeek = tools.strToEpoch(firstdayofweek.toString())
                lastDayOfWeek = tools.strToEpoch(lastdayofweek.toString())

            }
            view.id == (R.id.nwbtn) -> {
                firstDayOfWeek = tools.strToEpoch(firstdayofweek.toString())+ 604800
                lastDayOfWeek = tools.strToEpoch(lastdayofweek.toString())+ 604800

            }
            view.id == (R.id.anwbtn) -> {
                firstDayOfWeek = tools.strToEpoch(firstdayofweek.toString()) + 1209600
                lastDayOfWeek = tools.strToEpoch(lastdayofweek.toString()) + 1209600

            }
            view.id == (R.id.cm) -> {
                firstDayOfWeek = tools.strToEpoch(firstdayofmonth.toString())
                lastDayOfWeek = tools.strToEpoch(lastdayofmonth.toString())

            }


            view.id == (R.id.nm) -> {
                firstDayOfWeek = tools.strToEpoch(lastdayofmonth.toString())+86400
                lastDayOfWeek = tools.strToEpoch(lastdayofnextmonth.toString())

            }
            view.id == R.id.delbtn -> {
                firstDayOfWeek = "0".toLong()
                lastDayOfWeek = "999999999999".toLong()
                delStat=true

            }


        }

        mRef?.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
            }

            override fun onDataChange(p0: DataSnapshot) {


              mNotelist?.clear()
                try {
                    for (n in p0.children) {
                        val case = n.getValue(Booked::class.java)!!
                        val checkDeleted = n.child("isdeleted").value
                        val visitdate = n.child("visitdate").value.toString().toLong()
                        val casenumsearch =  n.child("patname").value.toString() + n.child("patphone").value.toString()
                        var searchtxt = searchtext.text.toString()
//                        Toast.makeText(this@AllReservation,casenumsearch,Toast.LENGTH_SHORT).show()
                        mNotelist!!.add(0, case)
                        if (btnswitch.isChecked == false) {
                            searchtxt = ""
                        }
                        if (delStat == false) {

                            if (checkDeleted.toString() == "0" && (visitdate >= firstDayOfWeek) && (visitdate <= lastDayOfWeek) && searchtxt in casenumsearch) {
//                                Toast.makeText(this@AllReservation,casenumsearch,Toast.LENGTH_SHORT).show()

                                mNotelist!!.add(0, case)



                            }
                        } else {
                            if (checkDeleted.toString() == "1" && (visitdate >= firstDayOfWeek) && (visitdate <= lastDayOfWeek) && searchtxt in casenumsearch) {
                                mNotelist!!.add(0, case!!)


                            }

                        }
//
                        var sortedList =
                            mNotelist?.sortedWith(compareBy({ it.visitdate }))?.toList()

                        val noteadapter = visitadapter(application, sortedList!!)
//                        Pbar.isVisible=false
                        res_LV.adapter = noteadapter


                    }

                }catch  (e : Exception){
                    Toast.makeText(this@AllReservation,e.message.toString(),Toast.LENGTH_SHORT).show()
//                    Pbar.isVisible = false
                }
                if (mNotelist?.count() == 0) {
                    if (btnswitch.isChecked == true && searchtext.text.isNotEmpty() == true) {
//                        Pbar.isVisible = false
                        TTitle.text =
                            " عفوا لا يوجد نتائج للبحث عن ' " + searchtext.text + "  ' في " + "" + view.contentDescription
                    } else {
//                        Pbar.isVisible = false
                        TTitle.text = " عفوا لا يوجد قضايا في " + view.contentDescription
                    }


                } else {

                    val casecount = " ( " + mNotelist?.count().toString() + " )"

                    if (btnswitch.isChecked == true && searchtext.text.isNotEmpty() == true) {
                        var hint =
                            " نتيجة البحث عن ' " + searchtext.text + "  ' في " + "" + view.contentDescription + casecount

                        TTitle.text = hint

                    } else {
                        TTitle.text = "" + view.contentDescription + casecount
                    }


                }
            }
        })
    }

}
