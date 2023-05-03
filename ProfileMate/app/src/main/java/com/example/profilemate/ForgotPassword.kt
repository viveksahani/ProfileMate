package com.example.profilemate

import android.app.DatePickerDialog
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Base64
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.graphics.drawable.toBitmap
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileWriter
import java.util.*

class ForgotPassword : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)
        val cancelBtn = findViewById<Button>(R.id.cancel)
        val dateBtn = findViewById<Button>(R.id.date)
        val submitBtn = findViewById<Button>(R.id.submit)
        val regNo: EditText = findViewById(R.id.editTextTextPersonName6)
        val dob: TextView = findViewById(R.id.textView15)
        val password:EditText = findViewById(R.id.editTextTextPersonName7)
        var flg = BooleanArray(3)

        val fileDir = getExternalFilesDir(null)
        val file = File(fileDir, "users.txt")
        val dataArray: Array<String>
        val separator = "\n"
        if (file.exists()) {
            val fileData = file.readText()
            dataArray = fileData.split(separator).toTypedArray()
        } else {
            val writer = FileWriter(file, true)
            writer.write("")
            writer.close()
            val fileData = file.readText()
            dataArray = fileData.split(separator).toTypedArray()
        }
        regNo.setOnFocusChangeListener { v, hasFocus ->
            if (!hasFocus) {
                val txt = regNo.text
                if (txt.length < 5 || (txt.length in 6..7) || txt.length > 8) {
                    flg[0] = false
                    regNo.error = "Enter a valid ID"
                } else {
                    var fl = false
                    for (i in dataArray.indices) {
                        if (dataArray[i] == txt.trim().toString()) {
                            regNo.error = null
                            flg[0] = true
                            fl = true
                            break
                        }
                    }
                    if (!fl) {
                        regNo.error = "registration number not found in database"
                        flg[0] = false
                    }
                }
            }
        }

        dateBtn.setOnClickListener {
            dateBtn.background = getDrawable(R.drawable.text_style)
            Handler().postDelayed({
                dateBtn.background = getDrawable(R.drawable.btn_style)
            }, 100)
            val c = Calendar.getInstance()
            val myear = c[Calendar.YEAR]
            val mmonth = c[Calendar.MONTH]
            val mday = c[Calendar.DAY_OF_MONTH]
            val datePickerDialog = DatePickerDialog(
                this,
                { view, year, monthOfYear, dayOfMonth
                    ->
                    dob.setText(dayOfMonth.toString() + "/" + (monthOfYear + 1) + "/" + year.toString())
                    flg[1] = true
                },
                myear, mmonth, mday
            )
            datePickerDialog.show()
        }

        password.setOnFocusChangeListener { v, hasFocus ->
            if (!hasFocus) {
                if (password.text.toString() == "") {
                    password.error = "enter password"
                    flg[2] = false
                } else {
                    password.error = null
                    flg[2] = true
                }
            }
        }

        submitBtn.setOnClickListener {
            if (password.text.toString() == "") {
                password.error = "enter password"
                flg[2] = false
            } else {
                password.error = null
                flg[2] = true
            }
            submitBtn.background = getDrawable(R.drawable.text_style)
            Handler().postDelayed({
                submitBtn.background = getDrawable(R.drawable.btn_style)
            }, 100)

            var finalFlg = true
            for (i in flg.indices) {
                if (flg[i] == false) {
                    finalFlg = false
                }
            }

            if (finalFlg) {
                val file2 = File(fileDir, "${regNo.text}.txt")
                val oldData = file2.readText()
                val oldDataArray = oldData.split(separator).toTypedArray()
                if(oldDataArray[4] == dob.text.toString()) {
                    var newData = regNo.text.trim().toString() + "\n" + password.text.trim().toString() + "\n"
                    for(i in 2 until oldDataArray.size) {
                        newData += oldDataArray[i] + "\n"
                    }
                    file2.writeText(newData)
                    Toast.makeText(this, "password reset successfully", Toast.LENGTH_SHORT).show()
                    onBackPressed()
                }
                else {
                    val builder = AlertDialog.Builder(this)
                    builder.setTitle("Date of birth is wrong !")
                    builder.setMessage("Please fill the data carefully")
                    builder.setView(null)
                    builder.setCancelable(true)
                    builder.setPositiveButton("Ok") { x, which ->
                    }
                    builder.create().show()
                }
            } else {
                val builder = AlertDialog.Builder(this)
                builder.setTitle("All fields are required !")
                builder.setMessage("Please fill all data carefully")
                builder.setView(null)
                builder.setCancelable(true)
                builder.setPositiveButton("Ok") { x, which ->
                }
                builder.create().show()
            }
        }

        cancelBtn.setOnClickListener {
            cancelBtn.background = getDrawable(R.drawable.text_style)
            Handler().postDelayed({
                cancelBtn.background = getDrawable(R.drawable.btn_style)
            }, 100)
            onBackPressed()
        }
    }
}