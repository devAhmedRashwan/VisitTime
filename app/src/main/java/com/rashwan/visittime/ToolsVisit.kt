package com.rashwan.visittime

import android.app.Application
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.SpinnerAdapter
import com.google.api.Context
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import java.security.AccessControlContext


class ToolsVisit {

    companion object Vtools{

        fun gettimes():MutableList<String>{
        var vacant: MutableList<String> = mutableListOf()
    var mRef: DatabaseReference? = null
    var mAuth: FirebaseAuth? = null
    var database: FirebaseDatabase = FirebaseDatabase.getInstance()
    mRef = database.getReference("Times")
    mAuth = FirebaseAuth.getInstance()

        mRef?.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {

                vacant.clear()
                for (n in dataSnapshot.children) {
                    //  val tindex = n.getValue(Long.toString()::class.java)
                    val tindex =n.key.toString()



                    vacant.add(tindex)

                }


            }
})
    return vacant
    }
        fun advancedgettimes(spinner: Spinner){
            var vacant: MutableList<String> = mutableListOf()
            var mRef: DatabaseReference? = null
            var mAuth: FirebaseAuth? = null
            var database: FirebaseDatabase = FirebaseDatabase.getInstance()
            mRef = database.getReference("Times")
            mAuth = FirebaseAuth.getInstance()

            mRef?.addValueEventListener(object : ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {
                }

                override fun onDataChange(dataSnapshot: DataSnapshot) {

                    vacant.clear()
                    for (n in dataSnapshot.children) {
                        //  val tindex = n.getValue(Long.toString()::class.java)
                        val tindex =n.key.toString()



                        vacant.add(tindex)

                    }


                }
            })
            val array= ArrayList(vacant)
            val textviewid = (R.id.tv)

            val maintimesadapter =
                ArrayAdapter(spinner.context, R.layout.spstyle, textviewid, array)
            spinner.adapter = maintimesadapter


        }



            fun getmaxdate():String {
            var mRef: DatabaseReference? = null
            var mAuth: FirebaseAuth? = null
            var database: FirebaseDatabase = FirebaseDatabase.getInstance()
            var ref:DatabaseReference=database.getReference("MaxDate")
            mAuth = FirebaseAuth.getInstance()
                mRef?.addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onCancelled(p0: DatabaseError) {
                    }

                    override fun onDataChange(p0: DataSnapshot) {
                        var   value="Test"
                        value=p0.getValue(String::class.java)!!
                    }
                })
                var   value="Test"
            mRef?.addValueEventListener(object : ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {
                }

                override fun onDataChange(p0: DataSnapshot){


                       value=p0.getValue(String::class.java)!!
                }
            }

            )
                return value

    }

    }
    }


interface MyCallback {
    fun onCallback(value: String?)
}



