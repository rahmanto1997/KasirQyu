package com.otnam.kasirqiu

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.otnam.kasirqiu.helper.PreferenceHelper

class Setting : AppCompatActivity() {
    var auth: FirebaseAuth? = null
    var user: FirebaseUser? = null
    var logout: Button? = null

    lateinit var sharedPref: PreferenceHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)
        supportActionBar!!.hide()

        sharedPref = PreferenceHelper(this)
        logout = findViewById(R.id.btn_logout)
        auth = FirebaseAuth.getInstance()
        user = auth!!.currentUser

        logout?.setOnClickListener(View.OnClickListener {
            auth!!.signOut()
            sharedPref.clear()
            Toast.makeText(this, "Berhasil Logout", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this@Setting, MainActivity::class.java))
            finish()
        })
    }
}