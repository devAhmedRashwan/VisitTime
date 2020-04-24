package com.rashwan.visittime

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import kotlinx.android.synthetic.main.activity_edit_book.*

class EditBook : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_book)
        val textviewid = (R.id.tv)
        val patsexadapter= ArrayAdapter(this, R.layout.spstyle, textviewid, arrayOf("غير محدد","ذكر","انثى"))
        patsex.adapter=patsexadapter
    }
}
