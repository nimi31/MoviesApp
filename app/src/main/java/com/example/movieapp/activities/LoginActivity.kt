package com.example.movieapp.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.movieapp.R

class LoginActivity : AppCompatActivity() {
    private lateinit var userEdt: EditText
    private lateinit var passEdt: EditText
    private lateinit var loginbtn: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        initView()
    }

    private fun initView() {
        userEdt = findViewById(R.id.editTexttext)
        passEdt = findViewById(R.id.editTextpassword)
        loginbtn = findViewById(R.id.buttonLogin)

        loginbtn.setOnClickListener {
            if (userEdt.text.toString().isEmpty() || passEdt.text.toString().isEmpty()) {
                Toast.makeText(this, "Please fill username and password", Toast.LENGTH_SHORT).show()
            } else if (userEdt.text.toString() == "test" && passEdt.text.toString() == "test") {
                startActivity(Intent(this, MainActivity::class.java))
            } else {
                Toast.makeText(this, "Your password and username are not correct", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
