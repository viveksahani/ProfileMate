package com.example.profilemate

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.appcompat.app.AlertDialog
import java.io.File

class Search : Fragment() {
    lateinit var searchBox: SearchView
    lateinit var v:View
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        v =  inflater.inflate(R.layout.fragment_search, container, false)
        init(container)
        return v
    }
    fun init(v1:ViewGroup?) {
        searchBox = v.findViewById(R.id.searchBox)
        val fileDir = requireContext().getExternalFilesDir(null)
        var userId = ""
        searchBox.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                userId = query
                if(userId.length < 5 || userId.length in 6..7 || userId.length > 8) {
                    AlertDialog.Builder(requireContext())
                        .setTitle("Search method")
                        .setMessage("Search User Id length must be of 5 or 8")
                        .setCancelable(true)
                        .setPositiveButton("Ok") {x, which ->}
                        .create().show()
                } else {
                    val file = File(fileDir, "${userId.trim()}.txt")
                    val file2 = File(fileDir, "${userId.trim()}photo.txt")
                    if(file.exists() && file2.exists()) {
                        val fragment = SearchProfile()
                        val bundle = Bundle()
                        bundle.putString("UserId", userId)
                        fragment.arguments = bundle
                        childFragmentManager.beginTransaction()
                            .replace(R.id.fragmentId, fragment)
                            .addToBackStack(null)
                            .commit()
                    } else {
                        childFragmentManager.beginTransaction()
                            .replace(R.id.fragmentId, BlankFragment())
                            .addToBackStack(null)
                            .commit()
                    }
                }
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                return true
            }
        })
    }
}