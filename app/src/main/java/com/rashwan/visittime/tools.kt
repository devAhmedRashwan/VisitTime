package com.rashwan.visittime


import android.content.Context
import android.view.View
import android.view.animation.AnimationUtils
import android.view.animation.Interpolator
import java.time.*
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import java.util.*

class tools {

    companion object MyTools {


        fun epochToStr(e: Long): String {

            return LocalDateTime.ofInstant(Instant.ofEpochSecond(e), ZoneId.systemDefault())
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd EEEE"))
        }


        fun epochToStrWithoutEEE(e: Long): String {

            return LocalDateTime.ofInstant(Instant.ofEpochSecond(e), ZoneId.systemDefault())
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
        }

        fun strToEpoch(l: String): Long {
            var edited = l
            try {
                if (l.length != 10) {
                    edited = l.substring(0, 10)
                } else edited = l
            }catch(e:Exception){
                edited="2020-01-01"
            }

            return LocalDateTime.parse(edited + "T00:00:00.000").atZone(ZoneOffset.systemDefault())
                .toEpochSecond()

        }
        fun strToMilliEpoch(l: Date): Long {
            //   var edited=l.replace(" Sun","")


                     return l.getTime()

        }

        fun getDaysDif(fromDate: LocalDate, toDate: LocalDate): Long {
            return ChronoUnit.DAYS.between(fromDate, toDate)
        }

        fun getLocalDateFromString(d: String, format: String): LocalDate {
            return LocalDate.parse(d, DateTimeFormatter.ofPattern(format))
        }


    }

    internal class MyBounceInterpolator(
        amplitude: Double,
        frequency: Double
    ) :
        Interpolator {
        private var mAmplitude = 1.0
        private var mFrequency = 10.0
        override fun getInterpolation(time: Float): Float {
            return (-1 * Math.pow(Math.E, -time / mAmplitude) *
                    Math.cos(mFrequency * time) + 1).toFloat()
        }

        init {
            mAmplitude = amplitude
            mFrequency = frequency
        }
    }
    fun didTapButton(view: View,context:Context) {
//        val button: Button = findViewById(R.id.button) as Button
        val myAnim =
            AnimationUtils.loadAnimation(context,R.anim.fade_out)

        // Use bounce interpolator with amplitude 0.2 and frequency 20
        val interpolator = MyBounceInterpolator(0.2, 20.0)
        myAnim.interpolator = interpolator
        view.startAnimation(myAnim)
    }
}




