package com.rashwan.visittime

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.provider.Settings.Global.getString
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.ArrayAdapter
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getSystemService
import kotlinx.android.synthetic.main.bookdetails.view.*
import kotlinx.android.synthetic.main.rowstyle.view.*
import kotlinx.android.synthetic.main.rowstyle.view.patphone
import kotlinx.android.synthetic.main.rowstyle.view.remain
import kotlinx.android.synthetic.main.rowstyle.view.rowstyle
import kotlinx.android.synthetic.main.rowstyle.view.status
import java.time.LocalDate

class visitadapter(context: Context, caseList: List<Booked>) :
    ArrayAdapter<Booked>(context, 0, caseList) {
    @SuppressLint("ViewHolder")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = View.inflate(context, R.layout.rowstyle, null);
        val book: Booked? = getItem(position)
        view.vnumber.text = book?.vnumber.toString()
        view.name.text = book?.patname.toString()
        view.patphone.text = book?.patphone
        view.time.text = book?.time
        view.Vdate.text = tools.epochToStr(book?.visitdate!!.toLong())
        when {
            (book.isconfirmed == 0) -> view.status.text= "انتظار"
            (book.isconfirmed == 1) -> view.status.text = "مؤكدة"
            (book.isconfirmed == 2) -> view.status.text = "تمت"
            (book.isconfirmed == 3) -> view.status.text = "فائتة"
            (book.isconfirmed == 4) -> view.status.text = "ملغية"
            (book.isconfirmed == 5) -> view.status.text = "طلب تعديل موعد"
            (book.isconfirmed == 6) -> view.status.text = "طلب الغاء"
        }
        //remaining
        var diffDays =
            ((book.visitdate!!.toLong() - tools.strToEpoch(LocalDate.now().toString())) / (86400))
        var daycount = "ايام"
        if (diffDays.toInt() > 11) {
            daycount = "يوما"
        }
        when {
            (diffDays.toInt()== 0) -> view.remain.text= "موعد اليوم!"
            (diffDays.toInt()== 1) -> view.remain.text = "موعد باكر!"
            (diffDays.toInt()== 2) -> view.remain.text = "موعد بعد باكر!"
            (diffDays.toInt()== 3) -> view.remain.text = "متبقي يومان"
            (diffDays.toInt() > 0) -> view.remain.text = "متبقي( $diffDays ) $daycount"
            (diffDays.toInt() < -10) -> {
                daycount = "يوما"
                diffDays *= -1
                view.remain.text = "مضى( " + diffDays.toString() + " )$daycount"
            }
            (diffDays.toInt() == -1)-> view.remain.text = "موعد الأمس"
            (diffDays.toInt() == -2) -> view.remain.text = "مضى يومان"
            (diffDays.toInt() < -2) -> {
                diffDays= -diffDays
                view.remain.text = "مضى( " + diffDays.toString() + " )$daycount"
        }}
        view.remain.text = view.remain.text.toString() + " ⏳"
        if (book.isdeleted == 1) {
            view.rowstyle.background =
                ContextCompat.getDrawable(context, R.drawable.background_rowstyle_deleted)
//            view.startAnimation(AnimationUtils.loadAnimation(context, R.anim.fade_in))
        } else {
//            view.startAnimation(AnimationUtils.loadAnimation(context, R.anim.scale))
        }

        //remaining
        return view
    }
}
