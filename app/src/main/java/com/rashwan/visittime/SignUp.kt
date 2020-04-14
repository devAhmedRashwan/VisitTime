package com.rashwan.visittime

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_sign_up.*


class SignUp : AppCompatActivity() {
    var mAuth: FirebaseAuth? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()

        setContentView(R.layout.activity_sign_up)



        btnreg.setOnClickListener {
            var email = signupemail.text.toString()
            var password = signuppass.text.toString()
            var cpassword = csignuppass.text.toString()
            if (email.isNotEmpty() && password.isNotEmpty() && password == cpassword) {
                mAuth = FirebaseAuth.getInstance()

                mAuth?.createUserWithEmailAndPassword(email, password)

                    ?.addOnCompleteListener {
                        if (it.isSuccessful) {
//                            val user = FirebaseAuth.getInstance().currentUser
//
//                            val profileUpdates =
//                                UserProfileChangeRequest.Builder()
//                                    .setDisplayName(mName).build()
//                            user!!.updateProfile(profileUpdates)
                            Toast.makeText(applicationContext, "Successfull", Toast.LENGTH_LONG)
                                .show()
                        } else

                            Toast.makeText(
                                applicationContext,
                                it.exception.toString(),
                                Toast.LENGTH_LONG
                            ).show()

                    }

            } else {
                Toast.makeText(applicationContext, "complete fields first", Toast.LENGTH_LONG)

            }

        }
    }
}
