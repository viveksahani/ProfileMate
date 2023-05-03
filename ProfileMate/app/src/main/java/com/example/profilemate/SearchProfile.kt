package com.example.profilemate

import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Base64
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import java.io.File
import java.io.FileWriter

class SearchProfile : Fragment() {
    lateinit var v: View
    var userId = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        v =  inflater.inflate(R.layout.fragment_search_profile, container, false)
        val id = arguments?.getString("UserId")
        userId = id.toString()
        init(container)
        return v
    }
    private fun init(v1:ViewGroup?) {
        val image: ImageView = v.findViewById(R.id.image)
        val name: TextView = v.findViewById(R.id.name)
        val regNo: TextView = v.findViewById(R.id.regNo)
        val fatherName: TextView = v.findViewById(R.id.fatherName)
        val dob: TextView = v.findViewById(R.id.dob)
        val mobNumber: TextView = v.findViewById(R.id.mobNumber)
        val email: TextView = v.findViewById(R.id.emailId)
        val address: TextView = v.findViewById(R.id.address)
        val ratingBar: RatingBar = v.findViewById(R.id.ratingbar)
        val ratingBtn:Button = v.findViewById(R.id.rating_btn)
        var ratingBar2:RatingBar = v.findViewById(R.id.ratingbar2)
        val fileDir = requireContext().getExternalFilesDir(null)
        val file = File(fileDir, "${userId}.txt")
        val file2 = File(fileDir, "${userId}photo.txt")
        val file3 = File(fileDir, "${userId}rating.txt")
        val separator = "\n"
        if(file.exists()) {
            val fileData = file.readText()
            val fileDataArray = fileData.split(separator).toTypedArray()
            regNo.text = fileDataArray[0].trim()
            name.text = fileDataArray[2].trim()
            fatherName.text = fileDataArray[5].trim()
            dob.text = fileDataArray[4].trim()
            mobNumber.text = fileDataArray[6].trim()
            email.text = fileDataArray[7].trim()
            address.text = fileDataArray[8].trim()
        }
        if(file2.exists()) {
            val fileData = file2.readText()
            if (fileData != "") {
                val imageBytes = Base64.decode(fileData, Base64.DEFAULT)
                val decodedImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
                image.setImageBitmap(decodedImage)
            }
        }
        if(file3.exists()) {
            val fileData = file3.readText()
            val fileDataArray = fileData.split(separator).toTypedArray()
            var sum = 0f
            for(i in fileDataArray.indices) {
                sum += fileDataArray[i].toFloat()
            }
            val ratingVal = sum/fileDataArray.size
            ratingBar.rating = ratingVal
        }
        ratingBtn.setOnClickListener {
            val rateVal = ratingBar2.rating.toString()
            val file4 = File(fileDir, "${regNo.text}rating.txt")
            val writer = FileWriter(file4, true)
            writer.write("\n${rateVal}")
            writer.close()

            val fileData = file3.readText()
            val fileDataArray = fileData.split(separator).toTypedArray()
            var sum = 0f
            for(i in fileDataArray.indices) {
                sum += fileDataArray[i].toFloat()
            }
            val ratingVal = sum/fileDataArray.size
            ratingBar.rating = ratingVal
        }
    }
}