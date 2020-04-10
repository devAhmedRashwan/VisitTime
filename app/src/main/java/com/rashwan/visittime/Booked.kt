package com.rashwan.visittime


    class Booked {
        var id: String? = null
        var visitdate: Long? = null
        var regvisitdate: String? = null
        var modifdate: String? = null
        var time: String? = null
        var isbooked: Int? = null
        var isdeleted: Int? = null
        var ischanged: Int? = null
        var isconfirmed: Int? = null
        var name: String? = null
        var patemail: String? = null
        var age: Int? = null
        var notes: String? = null





        constructor()


        constructor(
            id: String, visitdate: Long, regvisitdate:String,modifdate:String,time: String, isbooked: Int,isdeleted:Int,ischanged:Int,isconfirmed:Int,name:String,patemail:String,age:Int,notes:String
        ) {

            this.id = id
            this.visitdate = visitdate
            this.regvisitdate = regvisitdate
            this.modifdate = modifdate
            this.time = time
            this.isbooked = isbooked
            this.isdeleted=isdeleted
            this.ischanged=ischanged
            this.isconfirmed=isconfirmed
            this.name=name
            this.patemail=patemail
            this.age=age
            this.notes=notes

          


        }

    }

