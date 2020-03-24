package com.rashwan.visittime


import android.content.DialogInterface
import android.icu.util.Calendar
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog
import kotlinx.android.synthetic.main.activity_manage.*


class Manage : AppCompatActivity(),DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {
    var mRef: DatabaseReference? = null
    var mAuth: FirebaseAuth? = null
    var Year: Int=2020
    var Month: Int=3
    var Day: Int=20



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_manage)
        var database: FirebaseDatabase = FirebaseDatabase.getInstance()
        mRef = database.getReference("Times")
        mAuth = FirebaseAuth.getInstance()
        val maintimesarray = ToolsVisit.gettimes()
        val textviewid = (R.id.tv)
        val maintimesadapter =
            ArrayAdapter(this, R.layout.spstyle, textviewid, maintimesarray)
        showtimesspinner.adapter = maintimesadapter

        showtimes.setOnClickListener() {
            // ToolsVisit.gettimes()
            val textviewid = (R.id.tv)
            val maintimesadapter =
                ArrayAdapter(this, R.layout.spstyle, textviewid, maintimesarray)
            showtimesspinner.adapter = maintimesadapter
        }
        add.setOnClickListener() {
                        try {
                mRef = database.getReference("Times")
                mRef!!.child(textView.text.toString()).setValue(1)

            } catch (e: Exception) {
                Toast.makeText(
                    this,
                    "لم يتم اضافة المواعيد",
                    Toast.LENGTH_SHORT
                ).show()

            }


        }
        deletetime.setOnClickListener() {

            try {
                //mRef = database.getReference("Times")
                mRef!!.child(showtimesspinner.selectedItem.toString()).removeValue()

                val maintimesadapter =
                    ArrayAdapter(this, R.layout.spstyle, textviewid, maintimesarray)

                showtimesspinner.adapter = maintimesadapter
            } catch (e: Exception) {
                Toast.makeText(
                    this,
                    e.message,
                    Toast.LENGTH_SHORT
                ).show()

            }


        }
/*
        pick.setOnClickListener() {


                val cal = Calendar.getInstance()
                val timeSetListener = TimePickerDialog.OnTimeSetListener { timePicker, hour, minute ->
                    cal.set(Calendar.HOUR_OF_DAY, hour)
                    cal.set(Calendar.MINUTE, minute)
                    textView.text = SimpleDateFormat("h:mm a").format(cal.time)
                }
                TimePickerDialog(this, timeSetListener, cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), false).show()
            }
*/


        post.setOnClickListener() {
            val datePickerDialog: DatePickerDialog

            var Hour: Int=16
            var Minute:Int= 20
            var calendar: Calendar
                datePickerDialog =
                    DatePickerDialog.newInstance(
                        this@Manage,
                        Year,
                        Month,
                        Day
                    )
                datePickerDialog.setThemeDark(false)
                datePickerDialog.showYearPickerFirst(false)
                datePickerDialog.setTitle("Date Picker")
                datePickerDialog.setOnCancelListener(DialogInterface.OnCancelListener {
                    Toast.makeText(this@Manage, "Datepicker Canceled", Toast.LENGTH_SHORT)
                        .show()
                })

            val newFragment = DialogFragment()
//             datePickerDialog.show((this.getSupportFragmentManager()), "TAG")
//             var test:FragmentManager= getSupportFragmentManager()
            datePickerDialog.show(supportFragmentManager,"aa")
            }






        }

    override fun onDateSet(view: DatePickerDialog?, year: Int, monthOfYear: Int, dayOfMonth: Int) {
        val date = "Date: " + Day + "/" + (Month + 1) + "/" + Year

        Toast.makeText(this@Manage, date, Toast.LENGTH_LONG).show()

        post.text = date

    }

    override fun onTimeSet(view: TimePickerDialog?, hourOfDay: Int, minute: Int, second: Int) {

        val time =
            "Time: " + hourOfDay.toString() + "h" + minute.toString() + "m" + second

        Toast.makeText(this@Manage, time, Toast.LENGTH_LONG).show()


        post.text = time

    }


}



