package com.rashwan.visittime

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        var readData=ReadData()
        var database:FirebaseDatabase=FirebaseDatabase.getInstance()
        var ref:DatabaseReference=database.getReference("ClinicName")

        ref.addValueEventListener(ReadData())
booknow.setOnClickListener(){
    startActivity(Intent(this, bookvisit::class.java))

}
        Manage.setOnClickListener(){
            startActivity(Intent(this,com.rashwan.visittime.Manage::class.java))

        }
    }

inner class ReadData:ValueEventListener {

    override fun onDataChange(p0: DataSnapshot) {
var clinicname=p0.getValue(String::class.java)!!

            Cname.text=clinicname.toString()


    }
    override fun onCancelled(p0: DatabaseError) {
    }
}


}
