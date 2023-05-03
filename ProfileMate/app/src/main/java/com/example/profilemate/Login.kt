package com.example.profilemate

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.provider.ContactsContract.Data
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import java.io.File

class Login : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        val forgotPass: TextView = findViewById(R.id.textView2)
        val register: TextView = findViewById(R.id.textView4)
        val login_btn: Button = findViewById(R.id.button)
        val userId: EditText = findViewById(R.id.editTextNumber)
        val password: EditText = findViewById(R.id.editTextTextPassword)

        forgotPass.setOnClickListener {
            forgotPass.setTextColor(Color.RED)
            Handler().postDelayed({
                forgotPass.setTextColor(Color.YELLOW)
            }, 100)
            val i = Intent(this, ForgotPassword::class.java)
            startActivity(i)
        }

        register.setOnClickListener {
            register.setTextColor(Color.RED)
            Handler().postDelayed({
                register.setTextColor(Color.YELLOW)
            }, 100)
            val i = Intent(this, Register::class.java)
            startActivity(i)
        }

        userId.setOnFocusChangeListener { v, hasFocus ->
            if (!hasFocus) {
                val txt = userId.text
                if (txt.length < 5 || (txt.length in 6..7) || txt.length > 8) {
                    userId.error = "Enter a valid ID"
                } else {
                    userId.error = null
                }
            }
        }
        password.setOnFocusChangeListener { v, hasFocus ->
            if (!hasFocus) {
                if (password.text.toString() == "") {
                    password.error = "please enter password"
                } else {
                    password.error = null
                }
            }
        }
        login_btn.setOnClickListener {
            login_btn.background = getDrawable(R.drawable.text_style)
            Handler().postDelayed({
                login_btn.background = getDrawable(R.drawable.btn_style)
            }, 100)
            val fileDir = getExternalFilesDir(null)
            val file = File(fileDir, "${userId.text}.txt")
            val separator = "\n"
            if (file.exists()) {
                val fileData = file.readText()
                val dataArray = fileData.split(separator).toTypedArray()
                if (dataArray[0] == userId.text.trim()
                        .toString() && dataArray[1] == password.text.trim().toString()
                ) {
                    val i = Intent(this, UserProfile::class.java)
                    i.putExtra("UserId", userId.text.trim().toString())
                    startActivity(i)
                    finish()
                } else if (dataArray[0] == userId.text.trim()
                        .toString() && dataArray[1] != password.text.trim().toString()
                ) {
                    password.error = "incorrect password"
                    val builder = AlertDialog.Builder(this)
                    builder.setTitle("Incorrect password")
                    builder.setMessage("Password entered by you is incorrect.\nYou can reset your password using your user id and DOB.")
                    builder.setView(null)
                    builder.setCancelable(true)
                    builder.setNeutralButton("Cancel") { x, which ->
                    }
                    builder.setPositiveButton("Reset password") { x, which ->
                        val i = Intent(this, ForgotPassword::class.java)
                        startActivity(i)
                    }
                    builder.create().show()
                }
            } else {
                val dialogView = layoutInflater.inflate(R.layout.user_not_found, null)
                val builder = AlertDialog.Builder(this)
                val btn: Button = dialogView.findViewById(R.id.button2)
                btn.setOnClickListener {
                    val i = Intent(this, Register::class.java)
                    startActivity(i)
                }
                builder.setTitle("User Not Found")
                builder.setMessage("According to the details given by you, either you have entered wrong user id or user is not present in the database.\nYou can make new register using given below button.")
                builder.setView(dialogView)
                builder.setCancelable(true)
                builder.setNeutralButton("Cancel") { x, which ->
                }
                builder.setPositiveButton("Ok") { x, which ->
                }
                builder.create().show()
            }
        }

    }
}