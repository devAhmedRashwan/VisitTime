package com.rashwan.visittime


import android.app.AlertDialog
import android.app.AlertDialog.THEME_DEVICE_DEFAULT_LIGHT
import android.app.TimePickerDialog
import android.content.DialogInterface
import android.content.Intent
import android.icu.util.Calendar
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_manage.*
import kotlinx.android.synthetic.main.addnewtime.view.*
import kotlinx.android.synthetic.main.addnewtime.view.addtimenow
import kotlinx.android.synthetic.main.addnewtime.view.closeit
import kotlinx.android.synthetic.main.addnewtime.view.pickthetime
import kotlinx.android.synthetic.main.disabletimes.view.disabletimenow
import kotlinx.android.synthetic.main.set_dates.view.*
import java.text.SimpleDateFormat
import java.util.*


class Manage : AppCompatActivity() {
    var mRef: DatabaseReference? = null
    var mAuth: FirebaseAuth? = null
    var Year: Int = 2020
    var Month: Int = 3
    var Day: Int = 20
    val FirstDate = ToolsVisit.Dates(0)
    val LastDate = ToolsVisit.Dates(1)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_manage)
        var database: FirebaseDatabase = FirebaseDatabase.getInstance()
        mRef = database.getReference("Times")
        mAuth = FirebaseAuth.getInstance()
        val maintimesarray = ToolsVisit.gettimes()
        val DaysInarray = ToolsVisit.DaysIn()
        val disabledtimesarray = ToolsVisit.disabledtimes()
        val textviewid = (R.id.tv)
        allreservations.setOnClickListener() {
            startActivity(Intent(this, AllReservation::class.java))

        }
        addtimebtn.setOnClickListener() {

            var alert = AlertDialog.Builder(this)
            var inflater = layoutInflater
            val view = inflater.inflate(R.layout.addnewtime, null)
            alert.setView(view)
            val alertdailoge = alert.create()
            alertdailoge.show()
            view.closeit.setOnClickListener() {
                alertdailoge.dismiss()
                startActivity(Intent(this, MainActivity::class.java))

            }
            view.pickthetime.setOnClickListener() {


                val cal = Calendar.getInstance()
                val timeSetListener =
                    TimePickerDialog.OnTimeSetListener { timePicker, hour, minute ->
                        cal.set(Calendar.HOUR_OF_DAY, hour)
                        cal.set(Calendar.MINUTE, minute)
                        view.pickthetime.text =
                            SimpleDateFormat("h:mm a", Locale.getDefault()).format(cal.time)
                        if (view.pickthetime.text.toString().contains("M")) {
                            view.enableonceadded.visibility = View.VISIBLE
                            view.addtimenow.visibility = View.VISIBLE
                        }

                    }
                TimePickerDialog(
                    this,
                    timeSetListener,
                    cal.get(Calendar.HOUR_OF_DAY),
                    cal.get(Calendar.MINUTE),
                    false
                ).show()
            }
            view.addtimenow.setOnClickListener() {

                try {
                    var timestat = 0
                    if (view.enableonceadded.isChecked) {
                        timestat = 1
                    }
                    mRef = database.getReference("Times")
                    mRef!!.child(view.pickthetime.text.toString()).setValue(timestat)

                        .addOnSuccessListener {
                            ToolsVisit.vtoast(
                                "تم اضافة الموعد بنجاح",
                                1,
                                this@Manage,
                                layoutInflater
                            )

                            view.addtimenow.visibility = View.GONE
                            view.enableonceadded.visibility = View.GONE
                            view.pickthetime.text = "اضافة موعد اخر ؟"
                        }
                        .addOnFailureListener {
                            ToolsVisit.vtoast(
                                "لم تتم اضافة الموعد" + it.message,
                                1,
                                this@Manage,
                                layoutInflater
                            )


                        }


                } catch (e: Exception) {
                    ToolsVisit.vtoast(
                        "لم تتم اضافة الموعد" + e.message,
                        1,
                        this@Manage,
                        layoutInflater
                    )

                }

            }
            view.closeit.setOnClickListener() {
                alertdailoge.dismiss()
            }


        }
        disabletimebtn.setOnClickListener() {
            if (maintimesarray.isEmpty()) {
                ToolsVisit.vtoast(
                    "لا يوجد اي مواعيد نشطة يمكن ايقافها",
                    2,
                    this@Manage,
                    layoutInflater
                )
            } else {
                val alert = AlertDialog.Builder(this)
                val inflater = layoutInflater
                val view = inflater.inflate(R.layout.disabletimes, null)
                alert.setView(view)
                val alertdailoge = alert.create()
                alertdailoge.show()
                view.closeit.setOnClickListener() {
                    alertdailoge.dismiss()
                    startActivity(Intent(this, MainActivity::class.java))
                }
                view.pickthetime.setOnClickListener() {
                    var selectedItems = mutableListOf<String>()
                    val final = Collections.unmodifiableList(selectedItems)
//                    val builder = AlertDialog.Builder(this,R.style.MyDialogTheme)
                    val builder = AlertDialog.Builder(this, THEME_DEVICE_DEFAULT_LIGHT)
                    builder.setIcon(R.drawable.ico_clock)

                    builder.setTitle("اختر الموعد المراد ايقافه")
                        .setMultiChoiceItems(maintimesarray.toTypedArray(), null,
                            DialogInterface.OnMultiChoiceClickListener { dialog, which, isChecked ->
                                if (isChecked) {

                                    selectedItems.add(maintimesarray.toTypedArray()[which])
                                } else if (selectedItems.contains(maintimesarray.toTypedArray()[which])) {
                                    selectedItems.remove(maintimesarray.toTypedArray()[which])
                                }
                            })

                        .setPositiveButton("تم",
                            DialogInterface.OnClickListener { dialog, id ->
//                            Toast.makeText(this,"you choose $final",Toast.LENGTH_LONG).show()
                                if (final.toString() != "[]") {
                                    view.pickthetime.text = final.toString()
                                    view.disabletimenow.visibility = View.VISIBLE
                                }

                            })
                        .setNegativeButton("الغاء",
                            DialogInterface.OnClickListener { dialog, id ->
                            })

                    builder.create()
                    builder.show()


                }


//end


                view.disabletimenow.setOnClickListener() {

                    var joinedarry =
                        view.pickthetime.text.toString().replace("[", "").replace("]", "")
                            .split(",").toTypedArray()
                    for (item in joinedarry) {
                        try {
                            var firstchar = item.substring(0, 1)
                            var edited = item
                            if (firstchar == " ") {
                                edited = item.removeRange(0, 1)
                            }
                            mRef = database.getReference("Times")
                            mRef!!.child(edited).setValue(0)
                                .addOnSuccessListener {
                                    ToolsVisit.vtoast(
                                        " تم الايقاف بنجاح   ",
                                        1,
                                        this@Manage,
                                        layoutInflater
                                    )

                                    view.disabletimenow.visibility = View.GONE
                                    view.pickthetime.text = "ايقاف موعد اخر ؟"

                                }
                                .addOnFailureListener {
                                    ToolsVisit.vtoast(
                                        "   لم يتم الايقاف   ",
                                        3,
                                        this@Manage,
                                        layoutInflater
                                    )

                                }


                        } catch (e: Exception) {

                            ToolsVisit.vtoast(
                                "   لم يتم الايقاف   " + e.message,
                                3,
                                this@Manage,
                                layoutInflater
                            )


                        }

                    }
                    view.disabletimenow.visibility = View.GONE
                }
                view.closeit.setOnClickListener() {
                    alertdailoge.dismiss()
                }


            }

        }
        enabletimesbtn.setOnClickListener() {
            if (disabledtimesarray.isEmpty()) {
                ToolsVisit.vtoast(
                    "لا يوجد اي مواعيد موقوفة يمكن تفعيلها",
                    2,
                    this@Manage,
                    layoutInflater
                )


            } else {

                var alert = AlertDialog.Builder(this)
                var inflater = layoutInflater
                val view = inflater.inflate(R.layout.enabletimes, null)
                alert.setView(view)
                val alertdailoge = alert.create()
                alertdailoge.show()
                view.closeit.setOnClickListener() {
                    alertdailoge.dismiss()
                    startActivity(Intent(this, MainActivity::class.java))

                }
                view.pickthetime.setOnClickListener() {


//srat


                    var selectedItems = mutableListOf<String>()
                    val final = Collections.unmodifiableList(selectedItems)
                    val builder = AlertDialog.Builder(this)
                    builder.setTitle("اختر الموعد المراد تفعيله")
                        .setMultiChoiceItems(disabledtimesarray.toTypedArray(), null,
                            DialogInterface.OnMultiChoiceClickListener { dialog, which, isChecked ->
                                if (isChecked) {
                                    selectedItems.add(disabledtimesarray.toTypedArray()[which])
                                } else if (selectedItems.contains(disabledtimesarray.toTypedArray()[which])) {
                                    selectedItems.remove(disabledtimesarray.toTypedArray()[which])
                                }
                            })
                        .setPositiveButton("OK",
                            DialogInterface.OnClickListener { dialog, id ->
//                            Toast.makeText(this,"you choose $final",Toast.LENGTH_LONG).show()
                                if (final.toString() != "[]") {
                                    view.pickthetime.text = final.toString()
                                    view.disabletimenow.visibility = View.VISIBLE
                                }
                            })
                        .setNegativeButton("Cancel",
                            DialogInterface.OnClickListener { dialog, id ->
                            })
                    builder.create()
                    builder.show()


                }

                view.disabletimenow.setOnClickListener() {

                    var joinedarry =
                        view.pickthetime.text.toString().replace("[", "").replace("]", "")
                            .split(",").toTypedArray()
                    for (item in joinedarry) {
                        try {
                            var firstchar = item.substring(0, 1)
                            var edited = item
                            if (firstchar == " ") {
                                edited = item.removeRange(0, 1)
                            }
                            mRef = database.getReference("Times")
                            mRef!!.child(edited).setValue(1)
                                .addOnSuccessListener {
                                    ToolsVisit.vtoast(
                                        " تم التفعيل بنجاح",
                                        1,
                                        this@Manage,
                                        layoutInflater
                                    )
                                    view.disabletimenow.visibility = View.GONE
                                    view.pickthetime.text = "تفعيل موعد اخر ؟"

                                }
                                .addOnFailureListener {
                                    ToolsVisit.vtoast(
                                        " لم يتم التفعيل " + it.message,
                                        3,
                                        this@Manage,
                                        layoutInflater
                                    )


                                }


                        } catch (e: Exception) {
                            ToolsVisit.vtoast(
                                " لم يتم التفعيل " + e.message,
                                3,
                                this@Manage,
                                layoutInflater
                            )


                        }

                    }
                    view.disabletimenow.visibility = View.GONE
                }
                view.closeit.setOnClickListener() {
                    alertdailoge.dismiss()
                }


            }

        }
        addfirstdaybtn.setOnClickListener() {

            var alert = AlertDialog.Builder(this)
            var inflater = layoutInflater
            val view = inflater.inflate(R.layout.addfirstdate, null)
            try {
                //  view.pickthetime.text= ToolsVisit.getdates(0,view.pickthetime, this, layoutInflater)[0]
                view.pickthetime.text = tools.epochToStr(FirstDate[0].toLong())
            } catch (e: Exception) {
                ToolsVisit.vtoast(e.message!!, 1, this@Manage, layoutInflater)
            }
            alert.setView(view)
            val alertdailoge = alert.create()
            alertdailoge.show()
            view.closeit.setOnClickListener() {
                alertdailoge.dismiss()
            }
            view.pickthetime.setOnClickListener() {
                ToolsVisit.PickLongDate(this, view.pickthetime, layoutInflater)
                if (view.pickthetime.text.toString().contains("-")) {
                    view.addtimenow.visibility = View.VISIBLE
                }

            }


            view.addtimenow.setOnClickListener() {

                try {

                    mRef = database.getReference("Dates")
                    mRef!!.child("FirstDate")
                        .setValue(tools.strToEpoch(view.pickthetime.text.toString()))

                        .addOnSuccessListener {
                            ToolsVisit.vtoast(
                                "تم اضافة بداية الفترة بنجاح",
                                1,
                                this@Manage,
                                layoutInflater
                            )

                        }
                        .addOnFailureListener {
                            ToolsVisit.vtoast(
                                "لم تتم اضافة بداية الفترة" + it.message,
                                1,
                                this@Manage,
                                layoutInflater
                            )


                        }


                } catch (e: Exception) {
                    ToolsVisit.vtoast(
                        "لم تتم اضافة بداية الفترة" + e.message,
                        1,
                        this@Manage,
                        layoutInflater
                    )

                }

            }
            view.closeit.setOnClickListener() {
                alertdailoge.dismiss()
            }


        }
        addlastdaybtn.setOnClickListener() {

            var alert = AlertDialog.Builder(this)
            var inflater = layoutInflater
            val view = inflater.inflate(R.layout.addlastdate, null)
            try {
                //  view.pickthetime.text= ToolsVisit.getdates(0,view.pickthetime, this, layoutInflater)[0]
                view.pickthetime.text = tools.epochToStr(LastDate[0].toLong())
            } catch (e: Exception) {
                ToolsVisit.vtoast(e.message!!, 1, this@Manage, layoutInflater)
            }
            alert.setView(view)
            val alertdailoge = alert.create()
            alertdailoge.show()
            view.closeit.setOnClickListener() {
                alertdailoge.dismiss()
            }
            view.pickthetime.setOnClickListener() {
                ToolsVisit.PickLongDate(this, view.pickthetime, layoutInflater)
                if (view.pickthetime.text.toString().contains("-")) {
                    view.addtimenow.visibility = View.VISIBLE
                }

            }


            view.addtimenow.setOnClickListener() {

                try {

                    mRef = database.getReference("Dates")
                    mRef!!.child("LastDate")
                        .setValue(tools.strToEpoch(view.pickthetime.text.toString()))

                        .addOnSuccessListener {
                            ToolsVisit.vtoast(
                                "تم اضافة نهاية الفترة بنجاح",
                                1,
                                this@Manage,
                                layoutInflater
                            )

                        }
                        .addOnFailureListener {
                            ToolsVisit.vtoast(
                                "لم تتم اضافة نهاية الفترة" + it.message,
                                1,
                                this@Manage,
                                layoutInflater
                            )


                        }


                } catch (e: Exception) {
                    ToolsVisit.vtoast(
                        "لم تتم اضافة نهاية الفترة" + e.message,
                        1,
                        this@Manage,
                        layoutInflater
                    )

                }

            }
            view.closeit.setOnClickListener() {
                alertdailoge.dismiss()
            }


        }
        setdatesbtn.setOnClickListener() {

            var alert = AlertDialog.Builder(this)
            var inflater = layoutInflater
            val view = inflater.inflate(R.layout.set_dates, null)
            try {
                view.setFdate.text = tools.epochToStr(FirstDate[0].toLong())
                view.setLdate.text = tools.epochToStr(LastDate[0].toLong())

            } catch (e: Exception) {
                ToolsVisit.vtoast(e.message!!, 1, this@Manage, layoutInflater)
            }
            alert.setView(view)
            val alertdailoge = alert.create()
            alertdailoge.show()
            view.closeit.setOnClickListener() {
                alertdailoge.dismiss()
            }
            view.setFdate.setOnClickListener() {
                ToolsVisit.PickLongDate(this, view.setFdate, layoutInflater)
            }
            view.setLdate.setOnClickListener() {
                ToolsVisit.PickLongDate(this, view.setLdate, layoutInflater)
            }


            view.confirmsettings.setOnClickListener() {

                try {

                    mRef = database.getReference("Dates")
                    mRef!!.child("FirstDate")
                        .setValue(tools.strToEpoch(view.setFdate.text.toString()))
                    mRef!!.child("LastDate")
                        .setValue(tools.strToEpoch(view.setLdate.text.toString()))

                        .addOnSuccessListener {
                            ToolsVisit.vtoast("تم ضبط الفترة بنجاح", 1, this@Manage, layoutInflater)

                        }
                        .addOnFailureListener {
                            ToolsVisit.vtoast(
                                "لم يتم ضبط الفترة" + it.message,
                                1,
                                this@Manage,
                                layoutInflater
                            )


                        }


                } catch (e: Exception) {
                    ToolsVisit.vtoast(
                        "لم يتم  ضبط الفترة" + e.message,
                        1,
                        this@Manage,
                        layoutInflater
                    )

                }

            }
            view.closeit.setOnClickListener() {
                alertdailoge.dismiss()
            }


        }
        DaysInbtn.setOnClickListener() {
            var checkeditems = mutableListOf<String>()
            var uncheckeditems = mutableListOf<String>()
            var uncheckeddays: String = "initauncheck"
            var checkeddays: String = "initacheck"
            val BArray = ToolsVisit.DaysInBL()
            val final = Collections.unmodifiableList(checkeditems)
            val builder = AlertDialog.Builder(this, THEME_DEVICE_DEFAULT_LIGHT)
            builder.setIcon(R.drawable.ico_clock)
            builder.setTitle("اختر الايام التي سيكون بها الحجز متاحا")
                .setMultiChoiceItems(DaysInarray.toTypedArray(), BArray,
                    DialogInterface.OnMultiChoiceClickListener { _, which, isChecked ->
                        if (isChecked) {
                            checkeditems.add(DaysInarray.toTypedArray()[which])
                        } else if (checkeditems.contains(DaysInarray.toTypedArray()[which])) {
                            checkeditems.remove(DaysInarray.toTypedArray()[which])
                        }
                        checkeddays =
                            checkeditems.toString().replace("[", "").replace("]", "")
                                .split(",").toTypedArray().toString()


                        if (!isChecked) {
                            uncheckeditems.add(DaysInarray.toTypedArray()[which])
                        } else if (uncheckeditems.contains(DaysInarray.toTypedArray()[which])) {
                            uncheckeditems.remove(DaysInarray.toTypedArray()[which])
                        }

                        uncheckeddays =
                            uncheckeditems.toString().replace("[", "").replace("]", "")
                                .split(",").toTypedArray().toString()


                    }

                )
                .setPositiveButton("تم",
                    DialogInterface.OnClickListener { dialog, id ->
                        try {
                            if (checkeditems.toString() != "") {
                                var lastitemindex = checkeditems.size
                                for (item in checkeditems) {
                                    val firstchar = item.substring(0, 1)
                                    checkeddays = item
                                    if (firstchar == " ") {
                                        checkeddays = item.removeRange(0, 1)
                                    }

                                    try {
                                        if (!checkeddays.contains("[")) {
                                            mRef = database.getReference("DaysIn")
                                            mRef!!.child(checkeddays).setValue(1)
                                                .addOnSuccessListener {
                                                    if (item == checkeditems[lastitemindex - 1] && uncheckeditems.size == 0) {
                                                        ToolsVisit.vtoast(
                                                            " تم تعديل ايام العمل بنجاح   ",
                                                            1,
                                                            this@Manage,
                                                            layoutInflater
                                                        )
                                                    }


                                                }
                                                .addOnFailureListener {
                                                    ToolsVisit.vtoast(
                                                        "   لم يتم التعديل خطأ في قاعدة البيانات   ",
                                                        3,
                                                        this@Manage,
                                                        layoutInflater
                                                    )
                                                }
                                        }
                                    } catch (e: Exception) {
                                        ToolsVisit.vtoast(
                                            "   لم يتم التعديل - خطا في البرنامج   " + e.message,
                                            3,
                                            this@Manage,
                                            layoutInflater
                                        )

                                    }


                                }
                            }







                            if (uncheckeditems.toString() != "") {
                                for (item in uncheckeditems) {
                                    var lastitemindex = uncheckeditems.size
                                    val firstchar = item.substring(0, 1)
                                    uncheckeddays = item
                                    if (firstchar == " ") {
                                        uncheckeddays = item.removeRange(0, 1)
                                    }
                                    try {
                                        if (!uncheckeddays.contains("[")) {
                                            mRef = database.getReference("DaysIn")
                                            mRef!!.child(uncheckeddays).setValue(0)
                                                .addOnSuccessListener {
                                                    if (item == uncheckeditems[lastitemindex - 1]) {
                                                        ToolsVisit.vtoast(
                                                            " تم تعديل ايام العمل بنجاح   ",
                                                            1,
                                                            this@Manage,
                                                            layoutInflater
                                                        )
                                                    }

                                                }
                                                .addOnFailureListener {
                                                    ToolsVisit.vtoast(
                                                        "   لم يتم الايقاف   ",
                                                        3,
                                                        this@Manage,
                                                        layoutInflater
                                                    )
                                                }
                                        }
                                    } catch (e: Exception) {
                                        ToolsVisit.vtoast(
                                            "   لم يتم الايقاف   " + e.message,
                                            3,
                                            this@Manage,
                                            layoutInflater
                                        )
                                    }
                                }
                            }

                        } catch (e: Exception) {
                            ToolsVisit.vtoast(
                                "   لم يتم الايقاف   " + e.message,
                                3,
                                this@Manage,
                                layoutInflater
                            )
                        }


                    })
                .setNegativeButton("الغاء",
                    DialogInterface.OnClickListener { dialog, id ->
                    })
            builder.create()
            builder.show()
        }


    }

}










