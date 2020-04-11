package com.rashwan.visittime

import android.app.Application
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_landing.*

class landing : AppCompatActivity() {
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
        btnsignup.setOnClickListener {
try{
            startActivity(Intent(this, SignUp::class.java))}
catch (e:Exception){
    Toast.makeText(this,e.message.toString(),Toast.LENGTH_SHORT).show()

}
        }
        mAuth = FirebaseAuth.getInstance()

        btnlogin.setOnClickListener {
            var email = edID.text.toString()
            var password = edpass.text.toString()
            if (email.isNotEmpty() && password.isNotEmpty()) {

                mAuth?.signInWithEmailAndPassword(email, password)

                    ?.addOnCompleteListener {
                        if (it.isSuccessful) {
                            startActivity(Intent(this, MainActivity::class.java))
//                            Toast.makeText(applicationContext,
//                                mAuth?.currentUser?.displayName.toString(),
//                                Toast.LENGTH_LONG).show()
                        } else Toast.makeText(
                            applicationContext,
                            it.exception.toString(),
                            Toast.LENGTH_LONG
                        ).show()


                    }
                mAuth?.currentUser?.email.toString()

            }
            if (email.isEmpty() && password.isEmpty()) {
                startActivity(Intent(this, SignUp::class.java))

            }
        }

        btndemo.setOnClickListener {
            mAuth?.signInWithEmailAndPassword("demo@supercase.com", "123456")
                ?.addOnCompleteListener {
                    if (it.isSuccessful) {

                        try {
                            startActivity(Intent(this, MainActivity::class.java))
                        }
                        catch (e:Exception){
                            Toast.makeText(this,e.message.toString(),Toast.LENGTH_SHORT).show()

                        }



                    } else Toast.makeText(
                        applicationContext,
                        it.exception.toString(),
                        Toast.LENGTH_LONG
                    ).show()


                }
        }

    }

    override fun onStart() {
        super.onStart()
        if (mAuth?.currentUser != null) {
            var cu = mAuth?.currentUser?.email.toString()
            Toast.makeText(
                applicationContext,
                "Already Login  $cu",
                Toast.LENGTH_LONG
            ).show()
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
        var id = item.itemId
        if (id == R.id.logout) {
            FirebaseAuth.getInstance().signOut()
            startActivity(Intent(this, landing::class.java))
        }
        return true
    }


}

