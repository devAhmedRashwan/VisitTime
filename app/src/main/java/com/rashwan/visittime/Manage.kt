package com.rashwan.visittime


import android.app.AlertDialog
import android.app.TimePickerDialog
import android.content.DialogInterface
import android.content.Intent
import android.icu.util.Calendar
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_manage.*
import kotlinx.android.synthetic.main.deldal.view.*
import java.text.SimpleDateFormat
import java.util.*


class Manage : AppCompatActivity() {
    var mRef: DatabaseReference? = null
    var mAuth: FirebaseAuth? = null
    var Year: Int = 2020
    var Month: Int = 3
    var Day: Int = 20


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
        allreservations.setOnClickListener(){
            startActivity(Intent(this, AllReservation::class.java))

        }
        showtimes.setOnClickListener() {
            // ToolsVisit.gettimes()
            val textviewid = (R.id.tv)
            val maintimesadapter =
                ArrayAdapter(this, R.layout.spstyle, textviewid, maintimesarray)
            showtimesspinner.adapter = maintimesadapter
        }
        add.setOnClickListener() {
            try {
                var alert=AlertDialog.Builder(this)
                var inflater=layoutInflater
                val view =inflater.inflate(R.layout.deldal,null)
                alert.setView(view)
                val alertdailoge=alert.create()
                alertdailoge.show()

                view.delbtn.setOnClickListener(){
                    mRef = database.getReference("Times")
                    mRef!!.child(picked.text.toString()).setValue(1)
                    alertdailoge.dismiss()
                }

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



        picked.setOnClickListener() {


            val cal = Calendar.getInstance()
            val timeSetListener = TimePickerDialog.OnTimeSetListener { timePicker, hour, minute ->
                cal.set(Calendar.HOUR_OF_DAY, hour)
                cal.set(Calendar.MINUTE, minute)
                picked.text = SimpleDateFormat("h:mm a",Locale.getDefault()).format(cal.time)
            }
            TimePickerDialog(this, timeSetListener, cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), false).show()
        }


testbtn.setOnClickListener(){
    val final = Collections.unmodifiableList(maintimesarray)

   var lista= mfun(maintimesarray.toList())
    val textviewid = (R.id.tv)

    val chosedadapter= ArrayAdapter(this, R.layout.spstyle, textviewid, lista)

    afterdeletespinner.adapter=chosedadapter
    }

}
    fun mfun (e:List<String>):MutableList<String>{
//    var mcolors= arrayOf("red","blue","green")
     var   mcolors= e.toTypedArray()
        var selectedItems= mutableListOf<String>()
        val final = Collections.unmodifiableList(selectedItems)

        val builder = AlertDialog.Builder(this)
        builder.setTitle("Pick color")

            .setMultiChoiceItems(mcolors, null,
                DialogInterface.OnMultiChoiceClickListener { dialog, which, isChecked ->
                    if (isChecked) {
                        // If the user checked the item, add it to the selected items
                        selectedItems.add(mcolors[which])
                    } else if (selectedItems.contains(mcolors[which])) {
                        // Else, if the item is already in the array, remove it
                        selectedItems.remove(mcolors[which])

                    }
                })

            // Set the action buttons
            .setPositiveButton("OK",
                DialogInterface.OnClickListener { dialog, id ->
                    // User clicked OK, so save the selectedItems results somewhere
                    // or return them to the component that opened the dialog
//                    var final=selectedItems.toTypedArray()

                    Toast.makeText(this,"you choose $final",Toast.LENGTH_LONG).show()
                })
            .setNegativeButton("Cancel",
                DialogInterface.OnClickListener { dialog, id ->

                })
        builder.create()

        builder.show()
        return final

//        return selectedItems.toTypedArray()
//return arrayOf("Ahmed","Ali")
    }

    }

//Toast.makeText(this,"you choose $mcolors[which]",Toast.LENGTH_LONG).show()







