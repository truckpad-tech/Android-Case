package com.isa.oliveira.truckerapp.Activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.isa.oliveira.truckerapp.R

class SplashActivity : Activity() {


    private val SPLASH_TIME_OUT = 3000

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        setContentView(R.layout.activity_splash)

        Handler().postDelayed({

            var firebaseApp = FirebaseApp.initializeApp(this)
            var auth = FirebaseAuth(firebaseApp)

            if(auth.currentUser == null){
                val i = Intent(this@SplashActivity, LoginActivity::class.java)
                startActivity(i)
                finish()
            }else{
                val i = Intent(this@SplashActivity, MainActivity::class.java)
                startActivity(i)
                finish()
            }

        }, SPLASH_TIME_OUT.toLong())
    }

}

