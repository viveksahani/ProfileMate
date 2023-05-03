package com.example.profilemate

import android.app.DatePickerDialog
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Base64
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.graphics.drawable.toBitmap
import androidx.core.net.toUri
import org.w3c.dom.Text
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileWriter
import java.util.*

class Register : AppCompatActivity() {
    lateinit var filePath: Uri
    lateinit var imageUri: Uri
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        val dateBtn: Button = findViewById(R.id.date)
        val photoBtn: Button = findViewById(R.id.photo)
        val submitBtn: Button = findViewById(R.id.submit)
        val cancelBtn: Button = findViewById(R.id.cancel)
        val dob: TextView = findViewById(R.id.textView7)
        val regNo: EditText = findViewById(R.id.editTextTextPersonName)
        val name: EditText = findViewById(R.id.editTextTextPersonName2)
        val fatherName: EditText = findViewById(R.id.editTextTextPersonName5)
        val phoneNumber: EditText = findViewById(R.id.editTextTextPersonName3)
        val emailId: EditText = findViewById(R.id.editTextTextPersonName6)
        val address: EditText = findViewById(R.id.editTextTextPersonName4)
        val password:EditText = findViewById(R.id.editTextTextPersonName7)
        val photo: ImageView = findViewById(R.id.img)
        var gender = ""
        val radioGroup: RadioGroup = findViewById(R.id.radioGroup)
        var flg = BooleanArray(10)

        radioGroup.setOnCheckedChangeListener { group, checkedId ->
            val radioButton: RadioButton = findViewById(checkedId)
            gender = radioButton.text.trim().toString()
            flg[3] = true
        }
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
                    var fl = true
                    for (i in dataArray.indices) {
                        if (dataArray[i] == txt.trim().toString()) {
                            regNo.error = "already in use, use another"
                            flg[0] = false
                            fl = false
                            break
                        }
                    }
                    if (fl) {
                        regNo.error = null
                        flg[0] = true
                    }
                }
            }
        }
        name.setOnFocusChangeListener { v, hasFocus ->
            if (!hasFocus) {
                if (name.text.toString() == "") {
                    name.error = "enter name"
                    flg[2] = false
                } else {
                    name.error = null
                    flg[2] = true
                }
            }
        }
        fatherName.setOnFocusChangeListener { v, hasFocus ->
            if (!hasFocus) {
                if (fatherName.text.toString() == "") {
                    fatherName.error = "enter father name"
                    flg[5] = false
                } else {
                    fatherName.error = null
                    flg[5] = true
                }
            }
        }
        phoneNumber.setOnFocusChangeListener { v, hasFocus ->
            if (!hasFocus) {
                if (phoneNumber.text.toString() == "") {
                    phoneNumber.error = "enter phone number"
                    flg[6] = false
                } else if (phoneNumber.text.length < 10 || phoneNumber.text.length > 10) {
                    phoneNumber.error = "enter valid 10 digit phone no"
                    flg[6] = false
                } else {
                    phoneNumber.error = null
                    flg[6] = true
                }
            }
        }
        emailId.setOnFocusChangeListener { v, hasFocus ->
            if (!hasFocus) {
                if (emailId.text.toString() == "") {
                    emailId.error = "enter email id"
                    flg[7] = false
                } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(emailId.text.toString())
                        .matches()
                ) {
                    emailId.error = "enter a valid email id"
                    flg[7] = false
                } else {
                    emailId.error = null
                    flg[7] = true
                }
            }
        }
        address.setOnFocusChangeListener { v, hasFocus ->
            if (!hasFocus) {
                if (address.text.toString() == "") {
                    address.error = "enter address"
                    flg[8] = false
                } else {
                    address.error = null
                    flg[8] = true
                }
            }
        }

        password.setOnFocusChangeListener { v, hasFocus ->
            if (!hasFocus) {
                if (password.text.toString() == "") {
                    password.error = "enter password"
                    flg[1] = false
                } else {
                    password.error = null
                    flg[1] = true
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
                    flg[4] = true
                },
                myear, mmonth, mday
            )
            datePickerDialog.show()
        }

        val ip = registerForActivityResult(ActivityResultContracts.GetContent()) {
            filePath = it!!
            if (it != null) {
                imageUri = filePath
                photo.setImageURI(it)
                flg[9] = true
            }
        }
        photoBtn.setOnClickListener {
            if (password.text.toString() == "") {
                password.error = "enter password"
                flg[1] = false
            } else {
                password.error = null
                flg[1] = true
            }
            photoBtn.background = getDrawable(R.drawable.text_style)
            Handler().postDelayed({
                photoBtn.background = getDrawable(R.drawable.btn_style)
            }, 100)
            ip.launch("image/*")
        }

        submitBtn.setOnClickListener {
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
                val file2 = File(fileDir, "${regNo.text}photo.txt")
                if (photo.drawable != null && (photo.drawable as BitmapDrawable).bitmap != null) {
                    val boas = ByteArrayOutputStream()
                    val bitmap = photo.drawable.toBitmap()
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, boas)
                    val encodedImage = Base64.encodeToString(boas.toByteArray(), Base64.DEFAULT)
                    file2.writeText(encodedImage)
                }
                val file3 = File(fileDir, "${regNo.text}.txt")
                val data = regNo.text.trim().toString() + "\n" + password.text.trim().toString() + "\n" + name.text.trim()
                    .toString() + "\n" + gender + "\n" + dob.text.toString() + "\n" + fatherName.text.trim()
                    .toString() + "\n" + phoneNumber.text.trim()
                    .toString() + "\n" + emailId.text.trim().toString() + "\n" + address.text.trim()
                    .toString() + "\n"
                file3.writeText(data)
                Toast.makeText(this, "registered successfully", Toast.LENGTH_SHORT).show()
                val writer = FileWriter(file, true)
                writer.write("${regNo.text.trim()}\n")
                writer.close()
                val file4 = File(fileDir, "${regNo.text}rating.txt")
                file4.writeText("5")
                onBackPressed()
            } else {
                val builder = AlertDialog.Builder(this)
                builder.setTitle("All fields are required !")
                builder.setMessage("Please fill all data and don't forget to select your profile picture.")
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