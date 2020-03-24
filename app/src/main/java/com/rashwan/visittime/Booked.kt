package com.rashwan.visittime


    class Booked {
        var id: String? = null
        var date: Long? = null
        var time: String? = null
        var isbooked: Int? = null
        var isdeleted: Int? = null
        var ischanged: Int? = null
        var isconfirmed: Int? = null
        var name: String? = null
        var age: Int? = null
        var extra: String? = null





        constructor()


        constructor(
            id: String, date: Long, time: String, isbooked: Int,isdeleted:Int,ischanged:Int,isconfirmed:Int,name:String,age:Int,extra:String
        ) {

            this.id = id
            this.date = date
            this.time = time
            this.isbooked = isbooked
            this.isdeleted=isdeleted
            this.ischanged=ischanged
            this.isconfirmed=isconfirmed
            this.name=name
            this.age=age
            this.extra=extra

          


        }

    }

