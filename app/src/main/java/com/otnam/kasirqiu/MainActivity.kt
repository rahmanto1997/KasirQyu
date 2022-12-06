package com.otnam.kasirqiu

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.view.MotionEvent
import android.view.View
import android.view.View.OnTouchListener
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.otnam.kasirqiu.helper.Constant
import com.otnam.kasirqiu.helper.PreferenceHelper

class MainActivity : AppCompatActivity() {
    var reg: TextView? = null
    var buttonLogin: Button? = null
    var email: EditText? = null
    var password: EditText? = null
    var passwordVisible = false
    var auth: FirebaseAuth? = null
    var loadingBar: ProgressDialog? = null

    lateinit var sharedPref: PreferenceHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar!!.hide()
        reg = findViewById(R.id.new_account)
        email = findViewById(R.id.field_email)
        password = findViewById(R.id.field_password)
        buttonLogin = findViewById(R.id.btn_login)
        auth = FirebaseAuth.getInstance()
        loadingBar = ProgressDialog(this)
        sharedPref = PreferenceHelper(this)

        password?.let {
            it.setOnTouchListener(OnTouchListener { v, event ->
                val Right = 2
                if (event.action == MotionEvent.ACTION_UP) {
                    if (event.rawX >= it.getRight() - it.getCompoundDrawables()[Right].bounds.width()) {
                        val selection = it.getSelectionEnd()
                        passwordVisible = if (passwordVisible) {
                            //set drawable image here
                            it.setCompoundDrawablesRelativeWithIntrinsicBounds(
                                0,
                                0,
                                R.drawable.ic_baseline_visibility_off_24,
                                0
                            )
                            //for hide password
                            it.setTransformationMethod(PasswordTransformationMethod.getInstance())
                            false
                        } else {
                            //set drawable image here
                            it.setCompoundDrawablesRelativeWithIntrinsicBounds(
                                0,
                                0,
                                R.drawable.ic_baseline_visibility_24,
                                0
                            )
                            //for show  password
                            it.setTransformationMethod(HideReturnsTransformationMethod.getInstance())
                            true
                        }
                        it.setSelection(selection)
                        return@OnTouchListener true
                    }
                }
                false
            })
        }
        buttonLogin?.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View) {
                val sEmail = email?.getText().toString()
                val sPassword = password?.getText().toString()
                loginMethode(sEmail, sPassword)
            }

            private fun loginMethode(sEmail: String, sPassword: String) {
                if (TextUtils.isEmpty(sEmail)) {
                    email?.setError("Email is required")
                } else if (TextUtils.isEmpty(sPassword)) {
                    password?.setError("Password is required")
                } else {
                    loadingBar!!.setTitle("Login Progress")
                    loadingBar!!.setMessage("Please Wait")
                    loadingBar!!.show()
                    auth!!.signInWithEmailAndPassword(sEmail, sPassword)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                sharedPref.put(Constant.PREF_EMAIL, sEmail)
                                sharedPref.put(Constant.PREF_PASSWORD, sPassword)
                                sharedPref.put(Constant.PREF_IS_LOGIN,true)
                                Toast.makeText(
                                    this@MainActivity,
                                    "Login Successfully",
                                    Toast.LENGTH_SHORT
                                ).show()
                                startActivity(Intent(this@MainActivity, HomeScreen::class.java))
                                finish()
                                loadingBar!!.dismiss()
                            } else {
                                Toast.makeText(
                                    this@MainActivity,
                                    "Login Failed",
                                    Toast.LENGTH_SHORT
                                ).show()
                                loadingBar!!.dismiss()
                            }
                        }
                }
            }
        })
        reg?.setOnClickListener(View.OnClickListener {
            startActivity(
                Intent(
                    this@MainActivity,RegisterActivity::class.java
                )
            )
        })
    }

    override fun onStart() {
        super.onStart()
        if (sharedPref.getBoolean(Constant.PREF_IS_LOGIN)){
            startActivity(Intent(this, HomeScreen::class.java))
            finish()
        }
    }
    override fun onBackPressed() {
        super.onBackPressed();
        finishAffinity();
    }
}