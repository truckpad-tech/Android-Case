package com.isa.oliveira.truckerapp.Activity

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.tasks.Task
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.isa.oliveira.truckerapp.R

class LoginActivity : AppCompatActivity() {

    private val firebase: FirebaseAuth = FirebaseAuth(FirebaseApp.getInstance())
    private lateinit var email : EditText
    private lateinit var senha : EditText
    private lateinit var entrar : Button
    private lateinit var semLogin : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        email = findViewById(R.id.edt_email)
        senha = findViewById(R.id.edt_senha)
        entrar = findViewById(R.id.btn_proximo)
        semLogin = findViewById(R.id.btn_entrar)

        entrar.setOnClickListener {
            if(!email.text.isNullOrEmpty() && !senha.text.isNullOrEmpty()) {
                val fire: Task<AuthResult> =
                    firebase.createUserWithEmailAndPassword(
                        email.getText().toString(),
                        senha.getText().toString()
                    )

                fire.addOnSuccessListener {
                    finish()
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)

                }.addOnFailureListener {
                  var fireb =  firebase.signInWithEmailAndPassword(
                        email.getText().toString(),
                        senha.getText().toString()
                    )

                    fireb.addOnSuccessListener {
                        finish()
                        val intent = Intent(this, MainActivity::class.java)
                        startActivity(intent)
                    }.addOnFailureListener {
                        Toast.makeText(this, "Tente novamente mais tarde!", Toast.LENGTH_SHORT).show()

                    }
                }

            }else{
                Toast.makeText(this, "Preencha todos os campos", Toast.LENGTH_SHORT).show()
            }

        }

        semLogin.setOnClickListener {
            finish()
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

    }
}
