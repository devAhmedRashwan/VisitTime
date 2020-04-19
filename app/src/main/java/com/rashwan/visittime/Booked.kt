package com.rashwan.visittime


    class Booked {
        var id: String? = null
        var vnumber: Long? = null
        var visitdate: Long? = null
        var regvisitdate: String? = null
        var modifdate: String? = null
        var time: String? = null
        var isbooked: Int? = null
        var isdeleted: Int? = null
        var ischanged: Int? = null
        var isconfirmed: Int? = null
        var patname: String? = null
        var patemail: String? = null
        var patphone: String? = null
        var age: Int? = null
        var sex: Int? = null
        var notes: String? = null
        var paymentvalue: Double? = null
        var paymentref: String? = null
        var user: String? = null





        constructor()


        constructor(
            id: String,vnumber:Long, visitdate: Long, regvisitdate:String,modifdate:String,time: String, isbooked: Int,isdeleted:Int,ischanged:Int,isconfirmed:Int,patname:String,patemail:String,patphone:String,age:Int,sex:Int,notes:String,paymentvalue:Double,paymentref:String,user:String
        ) {

            this.id = id
            this.vnumber = vnumber
            this.visitdate = visitdate
            this.regvisitdate = regvisitdate
            this.modifdate = modifdate
            this.time = time
            this.isbooked = isbooked
            this.isdeleted=isdeleted
            this.ischanged=ischanged
            this.isconfirmed=isconfirmed
            this.patname=patname
            this.patemail=patemail
            this.patphone=patphone
            this.age=age
            this.sex=sex
            this.notes=notes
            this.paymentvalue=paymentvalue
            this.paymentref=paymentref
            this.user=user
        }

    }

