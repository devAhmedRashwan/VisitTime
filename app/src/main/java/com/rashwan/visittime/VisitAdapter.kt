package com.rashwan.visittime
import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import kotlinx.android.synthetic.main.rowstyle.view.*

class visitadapter(context: Context, caseList: List<Booked>) :
    ArrayAdapter<Booked>(context, 0, caseList)

{

    @SuppressLint("ViewHolder")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {

        val view = LayoutInflater.from(context).inflate(R.layout.rowstyle,parent,true)


        val case: Booked? =getItem(position)
        view.name.text=""
        view.age.text = case?.age.toString()
        view.patphone.text=case?.patphone
        view.sex.text=case?.sex.toString()
        view.email.text=case?.patemail
        view.date.text= tools.epochToStr(case?.visitdate!!.toLong())


        return view
    }
}

