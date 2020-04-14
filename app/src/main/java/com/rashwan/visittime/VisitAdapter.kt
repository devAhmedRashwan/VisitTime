package com.rashwan.visittime
import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.ArrayAdapter
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getSystemService
import kotlinx.android.synthetic.main.rowstyle.view.*
import java.time.LocalDate

class visitadapter(context: Context, caseList: List<Booked>) :
    ArrayAdapter<Booked>(context, 0, caseList)

{

    @SuppressLint("ViewHolder")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
      var  view = View.inflate(context,R.layout.rowstyle,null);
        val book: Booked? =getItem(position)
        view.name.text=book?.patname.toString()
        view.patphone.text=book?.patphone
        view.time.text=book?.time
        view.date.text= tools.epochToStr(book?.visitdate!!.toLong())
        if (book.isconfirmed==1) {
        view.status.text="مؤكدة"
        }
        else{
            view.status.text="انتظار"

        }
        //remaining

        var diffDays =
            ((book.visitdate!!.toLong() - tools.strToEpoch(LocalDate.now().toString())) / (86400))
        var daycount = "ايام"
        if (diffDays.toInt() > 11) {
            daycount = "يوما"
        }

        if (diffDays.toInt() == 0) {
            view.remain.text = "جلسة اليوم!"
        } else if (diffDays.toInt() == 1) {
            view.remain.text = "جلسة باكر!"
        } else if (diffDays.toInt() == 2) {
            view.remain.text = "جلسة بعد باكر!"
        } else if (diffDays.toInt() == 3) {
            view.remain.text = "متبقي يومان"
        } else if (diffDays.toInt() > 0) {
            diffDays = diffDays - 1

            view.remain.text = "متبقي( " + diffDays.toString() + " ) $daycount"

        } else {

            if (diffDays.toInt() < -10) {
                daycount = "يوما"
                diffDays *= -1
                view.remain.text = "مضى( " + diffDays.toString() + " )$daycount"
            } else if (diffDays.toInt() == -1) {
                view.remain.text = "جلسة الأمس"


            } else if (diffDays.toInt() == -2) {
                view.remain.text = "مضى يومان"

            } else {
                diffDays *= -1
                view.remain.text = "مضى( " + diffDays.toString() + " )$daycount"

            }
        }
        view.remain.text=view.remain.text.toString()+" ⏳"
        if (book.isdeleted == 1) {
            view.rowstyle.background =
                ContextCompat.getDrawable(context, R.drawable.background_rowstyle_deleted)
//            view.startAnimation(AnimationUtils.loadAnimation(context, R.anim.fade_in))
        }else{
//            view.startAnimation(AnimationUtils.loadAnimation(context, R.anim.scale))
        }




        //remaining
        return view
    }
}

