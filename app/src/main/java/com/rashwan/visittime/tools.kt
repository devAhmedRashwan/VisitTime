package com.rashwan.visittime


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
            //   var edited=l.replace(" Sun","")
            var edited = l
            if (l.length != 10) {
                edited = l.substring(0, 10)
            } else edited = l

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
}




