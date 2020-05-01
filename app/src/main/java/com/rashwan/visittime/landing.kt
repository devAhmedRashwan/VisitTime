package com.rashwan.visittime
import android.app.AlertDialog.THEME_DEVICE_DEFAULT_LIGHT
import android.app.Application
import android.content.DialogInterface
import android.content.Intent
import android.content.res.TypedArray
import android.graphics.drawable.AnimationDrawable
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Patterns
import android.view.*
import android.view.animation.AnimationUtils
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import kotlinx.android.synthetic.main.activity_landing.*
class landing : AppCompatActivity(),TextWatcher {
    var mAuth: FirebaseAuth? = null
    override fun onBackPressed() {
        var i = Intent()
        i.action = Intent.ACTION_MAIN
        i.addCategory(Intent.CATEGORY_HOME)
        this.startActivity(i)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        supportActionBar?.hide()
        setContentView(R.layout.activity_landing)
        mAuth = FirebaseAuth.getInstance()
        useremailET.addTextChangedListener(this)
        userpasslET.addTextChangedListener(this)
        signupbtn.setOnClickListener {
          ToolsVisit.btnanim(it)

if ( LL.layoutParams.height!=(0)) {
    LL.layoutParams.height = (0)
    signupbtn.text = "العودة الى شاشة الدخول"
    regbtnL.gravity = Gravity.END
    RL.layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT
}else{
    LL.layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT
    signupbtn.text = "التسجيل"
    regbtnL.gravity = Gravity.CENTER
    RL.layoutParams.height = (0)
}
        }
        SUregister.setOnClickListener(){


        }

        SUregister.setOnClickListener {
            ToolsVisit.btnanim(it)
            val email = SUemail.text.toString()
            val password = SUpass.text.toString()
            val cpassword = SUcpass.text.toString()
            val displayname = SUdisplayname.text.toString()
            (Patterns.EMAIL_ADDRESS.matcher(email).matches())

            if (email.isNotEmpty() && (Patterns.EMAIL_ADDRESS.matcher(email)
                    .matches()) && password.length > 5 && password == cpassword
            ) {
                mAuth = FirebaseAuth.getInstance()
                mAuth?.createUserWithEmailAndPassword(email, password)
                    ?.addOnCompleteListener {
                        if (it.isSuccessful) {
                            val user = FirebaseAuth.getInstance().currentUser
                            val profileUpdates =
                                UserProfileChangeRequest.Builder()
                                    .setDisplayName(displayname).build()
                            user!!.updateProfile(profileUpdates)
                            user.sendEmailVerification()
                            ToolsVisit.vtoast(
                                "تم التسجيل بنجاح و ارسال رسالة تأكيد الى بريدك الالكتروني ، يجب تأكيد الحساب حتى تتمكن من الحجز",
                                2,
                                this,
                                layoutInflater
                            )
                            startActivity(Intent(this, MainActivity::class.java))
                        } else
                            ToolsVisit.vtoast(
                                it.exception.toString(),
                                2,
                                this,
                                layoutInflater
                            )
                    }
            } else {
                if (password.length < 5 && password.isNotEmpty()) {
                    ToolsVisit.vtoast(
                        " لابد ان تكون كلمة المرور من ستة ارقام او حروف ",
                        2,
                        this,
                        layoutInflater
                    )
                }

            ToolsVisit.vtoast(
                " سجل البيانات اولا ، الاسم والبريد الالكتروني وكلمة المرور",
                2,
                this,
                layoutInflater
            )

            }
        }
        loginbtn.setOnClickListener {
            ToolsVisit.btnanim(it)
            if ((Patterns.EMAIL_ADDRESS.matcher(useremailET.text)
                    .matches()) && userpasslET.text.isNotEmpty()) {
                mAuth?.signInWithEmailAndPassword(useremailET.text.toString(), userpasslET.text.toString())
                    ?.addOnCompleteListener { it ->
                        if (it.isSuccessful) {
                            startActivity(Intent(this, MainActivity::class.java))
                            val ds=mAuth?.currentUser?.displayName.toString()
                            ToolsVisit.vtoast(
                                " تم تسجيل الدخول الى حسابك بنجاح ، مرحبا $ds",
                                1,
                                this,
                                layoutInflater
                            )
                        } else {
                            ToolsVisit.vtoast(
                                "هذا الحساب غير مسجل لدينا ، يمكنك التسجيل الان",
                                1,
                                this,
                                layoutInflater
                            )


                                val builder = AlertDialog.Builder(this, THEME_DEVICE_DEFAULT_LIGHT)
                                builder.setIcon(R.drawable.icon_time_off)
                                builder.setTitle("لا يوجد حساب مسجل لهذا البريد الالكتروني ، هل ترغب بتسجيل حساب؟")
                                    .setPositiveButton("تسجيل حساب جديد",
                                        DialogInterface.OnClickListener { dialog, id ->
                                            LL.layoutParams.height = (0)
                                            signupbtn.text = "العودة الى شاشة الدخول"
                                            regbtnL.gravity = Gravity.END
                                            RL.layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT
                                        })
                                            .setNegativeButton(" بدء كمستخدم تجريبي ",
                                            DialogInterface.OnClickListener { dialog, id ->
                                                mAuth?.signInWithEmailAndPassword("demo@VisitTime.com", "123456")
                                                startActivity(Intent(this, MainActivity::class.java))
                                            })
                                            builder.create()
                                            builder.show()
                        }
                    }
                mAuth?.currentUser?.email.toString()
            }
        }
        btndemo.setOnClickListener {
            ToolsVisit.btnanim(it)
            mAuth?.signInWithEmailAndPassword("demo@VisitTime.com", "123456")
                ?.addOnCompleteListener { it ->
                    if (it.isSuccessful) {
                        startActivity(Intent(this, MainActivity::class.java))
                        val ds=mAuth?.currentUser?.displayName.toString()
                        ToolsVisit.vtoast(
                            " تم تسجيل الدخول كعميل تجريبي ، مرحبا",
                            1,
                            this,
                            layoutInflater
                        )
                    }
                }
        }
        btndemoadmin.setOnClickListener {
            ToolsVisit.btnanim(it)
            mAuth?.signInWithEmailAndPassword("demoadmin@VisitTime.com", "123456")
                ?.addOnCompleteListener { it ->
                    if (it.isSuccessful) {
                        startActivity(Intent(this, MainActivity::class.java))
                        val ds=mAuth?.currentUser?.displayName.toString()
                        ToolsVisit.vtoast(
                            " تم تسجيل الدخول كمدير تجريبي ، مرحبا",
                            1,
                            this,
                            layoutInflater
                        )
                    }
                }
        }

        forgetpassbtn.setOnClickListener() {

            ToolsVisit.btnanim(it)
            if (useremailET.text.isNotEmpty() &&userpasslET.text.contains("@")){
            mAuth!!.sendPasswordResetEmail(useremailET.text.toString())
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        // successful!
                        ToolsVisit.vtoast(
                            "تم ارسال رسالة اعادة ضبط كلمة السر الى بريدك الالكتروني",
                            1,
                            this,
                            layoutInflater
                        )
                    } else {
                        // failed!
                        ToolsVisit.vtoast(
                            "لم يتم ارسال رسالة  الضبط ",
                            1,
                            this,
                            layoutInflater
                        )
                    }
                }
        }
            else{
                ToolsVisit.vtoast(
                    "ادخل البريد الالكتروني كي نتمكن من ارسال رسالة اعادة الضبط الخاصة بكلمة المرور",
                    1,
                    this,
                    layoutInflater
                )
            }
        }
    }
    override fun onStart() {
        super.onStart()
        if (mAuth?.currentUser != null) {
            var cu = mAuth?.currentUser?.displayName.toString()
            var cmail = mAuth?.currentUser?.email.toString()
            if(cu=="Demo"){
                cu="عميل تجريبي"
                ToolsVisit.vtoast(
                    " تم تسجيل الدخول كعميل تجريبي",
                    1,
                    this@landing,
                    layoutInflater
                )}else if(cmail=="demoadmin@visittime.com"){
                cu="مدير تجريبي"
                ToolsVisit.vtoast(
                    " تم تسجيل الدخول كمدير تجريبي",
                    1,
                    this@landing,
                    layoutInflater
                )}

            else{

            ToolsVisit.vtoast(
                " تم تسجيل الدخول كـ $cu",
                1,
                this@landing,
                layoutInflater
            )}
            try {
                startActivity(Intent(this, MainActivity::class.java))
//                           view.startAnimation(AnimationUtils.loadAnimation(this, R.anim.scale))

            }
            catch (e:Exception){
                Toast.makeText(this,e.message.toString(),Toast.LENGTH_SHORT).show()
            }        }
    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.navigation_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if (id == R.id.logout) {
            FirebaseAuth.getInstance().signOut()
            startActivity(Intent(this, landing::class.java))
        }
        return true
    }

    override fun afterTextChanged(p0: Editable?) {}
    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

        val LGNemail = useremailET.text.toString()
        val LGNpassword = userpasslET.text.toString()
        loginbtn.isEnabled = Patterns.EMAIL_ADDRESS.matcher(useremailET.text).matches()&& LGNpassword.trim().isNotEmpty() &&LGNemail.contains("@")
        forgetpassbtn.isEnabled = Patterns.EMAIL_ADDRESS.matcher(useremailET.text).matches()

//        btnlogin.isEnabled = LGNemail.trim().isNotEmpty() && LGNpassword.trim().isNotEmpty()

    }

}
