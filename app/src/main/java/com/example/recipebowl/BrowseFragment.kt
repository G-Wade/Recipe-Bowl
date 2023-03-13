package com.example.recipebowl

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot

class BrowseFragment : AppCompatActivity(R.layout.fragment_browse) {

    private lateinit var db : FirebaseFirestore

    override fun onStart() {
        super.onStart()

        db = FirebaseFirestore.getInstance()

        db.collection("Recipe").get().addOnCompleteListener {
            if (it.isSuccessful) {
                populateHome(it.result)
            }
        }
    }

    private fun populateHome(collection : QuerySnapshot) {
        val list = ArrayList<HomeModel>()
        val nameList = ArrayList<String>()

        for (document in collection) {
            nameList.add(document.get("name").toString())
        }

        for (i in 0 .. nameList.size-1) {
            val model = HomeModel()
            model.setName(nameList[i])
            list.add(model)
        }

        list.sortBy { list -> list.modelName}

        val recyclerView = findViewById<RecyclerView>(R.id.homeRecycler)
        val layoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = layoutManager
        val adapter = HomeAdapter(list)
        recyclerView.adapter = adapter
    }

}