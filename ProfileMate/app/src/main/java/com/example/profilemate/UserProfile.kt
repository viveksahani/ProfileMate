package com.example.profilemate

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Base64
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import de.hdodenhof.circleimageview.CircleImageView
import org.w3c.dom.Text
import java.io.File

class UserProfile : AppCompatActivity() {
    lateinit var toggle: ActionBarDrawerToggle
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_profile)
        val drawerLayout: DrawerLayout = findViewById(R.id.drawerId)
        val navView: NavigationView = findViewById(R.id.nav_view)
        toggle = ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        val userId = intent.getStringExtra("UserId")
        val fileDir = getExternalFilesDir(null)
        val separator = "\n"
        val file = File(fileDir, "${userId.toString()}.txt")
        val file2 = File(fileDir, "${userId.toString()}photo.txt")
        val fileData = file.readText()
        val fileDataArray = fileData.split(separator).toTypedArray()
        val navigationView: NavigationView = findViewById(R.id.nav_view)
        val headerView = navigationView.getHeaderView(0)
        val profilePic: ImageView = headerView.findViewById(R.id.imageView)
        val name: TextView = headerView.findViewById(R.id.textView17)
        val email: TextView = headerView.findViewById(R.id.textView18)
        name.text = fileDataArray[2].trim()
        email.text = fileDataArray[7].trim()
        val fileData2 = file2.readText()
        if (fileData2 != "") {
            val imageBytes = Base64.decode(fileData2, Base64.DEFAULT)
            val decodedImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
            profilePic.setImageBitmap(decodedImage)
        }

        // sent user id to fragment
        val bundle = Bundle()
        bundle.putString("UserId", userId.toString())

        val fragment = Profile()
        fragment.arguments = bundle
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentId, fragment)
            .addToBackStack(null)
            .commit()


        navView.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.home -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragmentId, fragment)
                        .addToBackStack(null)
                        .commit()
                }
                R.id.editProfile -> {
                    Toast.makeText(this, "This feature will available soon", Toast.LENGTH_SHORT).show()
                }
                R.id.search -> {
                    val fragment2 = Search()
                    fragment2.arguments = bundle
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragmentId, fragment2)
                        .addToBackStack(null)
                        .commit()
                }
                R.id.logout -> {
                    Toast.makeText(this, "logout", Toast.LENGTH_SHORT).show()
                    val i = Intent(this, Login::class.java)
                    startActivity(i)
                    finish()
                }
                R.id.call -> {
                    Toast.makeText(this, "call", Toast.LENGTH_SHORT).show()
                    if (ContextCompat.checkSelfPermission(
                            this,
                            Manifest.permission.CALL_PHONE
                        ) != PackageManager.PERMISSION_GRANTED
                    ) {
                        ActivityCompat.requestPermissions(
                            this,
                            arrayOf(android.Manifest.permission.CALL_PHONE),
                            1
                        )
                    } else {
                        var call: String = "tel:+916387026109";
                        startActivity(Intent(Intent.ACTION_CALL, Uri.parse(call)));
                    }
                }
                R.id.feedback -> {

                    val inflat = LayoutInflater.from(this).inflate(R.layout.activity_feedback, null)
                    var alert = AlertDialog.Builder(this)
                        .setTitle("Feedback")
                        .setMessage("please give feedback")
                        .setView(inflat)
                        .setCancelable(true)
                        .setPositiveButton("submit") { x, which ->
                            Toast.makeText(this, "feedback submitted", Toast.LENGTH_SHORT).show()
                        }
                        .setNeutralButton("cancel") { x, which ->
                        }
                        .create().show();
                }
            }
            true
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (toggle.onOptionsItemSelected(item)) {
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        val inflat = LayoutInflater.from(this).inflate(R.layout.activity_feedback, null)
        var alert = AlertDialog.Builder(this)
            .setTitle("Feedback")
            .setMessage("please give feedback")
            .setView(inflat)
            .setCancelable(true)
            .setPositiveButton("submit") { x, which ->
                Toast.makeText(this, "feedback submitted", Toast.LENGTH_SHORT).show()
                val i = Intent(this, Login::class.java)
                startActivity(i)
                finish()
            }
            .setNeutralButton("cancel") { x, which ->
            }
            .create().show();
    }
}