package com.rashwan.visittime
import android.content.Intent
import android.content.res.TypedArray
import android.graphics.drawable.AnimationDrawable
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
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
//        val LGNemail = LGemailED.text.toString()
//        val LGNpassword = LGpassED.text.toString()
        mAuth = FirebaseAuth.getInstance()
        useremailET.addTextChangedListener(this)
        userpasslET.addTextChangedListener(this)
        signupbtn.setOnClickListener {
            ToolsVisit.btnanim(it)
            try {
                startActivity(Intent(this, SignUp::class.java))
            } catch (e: Exception) {
                Toast.makeText(this, e.message.toString(), Toast.LENGTH_SHORT).show()
            }
        }
        loginbtn.setOnClickListener {
            ToolsVisit.btnanim(it)
            if (useremailET.text.isNotEmpty() && userpasslET.text.isNotEmpty()) {
                mAuth?.signInWithEmailAndPassword(useremailET.text.toString(), userpasslET.text.toString())
                    ?.addOnCompleteListener {
                        if (it.isSuccessful) {
                            startActivity(Intent(this, MainActivity::class.java))
                            val ds=mAuth?.currentUser?.displayName.toString()
                            ToolsVisit.vtoast(
                                " تم تسجيل الدخول الى حسابك بنجاح ، مرحبا $ds",
                                1,
                                this,
                                layoutInflater
                            )
                        } else Toast.makeText(
                            this,
                            it.exception.toString(),
                            Toast.LENGTH_LONG
                        ).show()
                    }
                mAuth?.currentUser?.email.toString()

            }

        }

        btndemo.setOnClickListener {
            ToolsVisit.btnanim(it)
            mAuth?.signInWithEmailAndPassword("demo@VisitTime.com", "123456")
                ?.addOnCompleteListener {
                    if (it.isSuccessful) {
                        try {
                            startActivity(Intent(this, MainActivity::class.java))
                        } catch (e: Exception) {
                            Toast.makeText(this, e.message.toString(), Toast.LENGTH_SHORT).show()

                        }


                    } else Toast.makeText(
                        applicationContext,
                        it.exception.toString(),
                        Toast.LENGTH_LONG
                    ).show()


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
            if(cu=="Demo"){cu="مستخدم تجريبي"}
            ToolsVisit.vtoast(
                " تم تسجيل الدخول كـ $cu",
                1,
                this@landing,
                layoutInflater
            )
            try {
                startActivity(Intent(this, MainActivity::class.java))
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
    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

    }
    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

        val LGNemail = useremailET.text.toString()
        val LGNpassword = userpasslET.text.toString()
        loginbtn.isEnabled = LGNemail.trim().isNotEmpty() && LGNpassword.trim().isNotEmpty() &&LGNemail.contains("@")
        forgetpassbtn.isEnabled = LGNemail.trim().isNotEmpty() && LGNemail.contains("@")

//        btnlogin.isEnabled = LGNemail.trim().isNotEmpty() && LGNpassword.trim().isNotEmpty()

    }

}
