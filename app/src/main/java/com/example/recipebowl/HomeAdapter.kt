package com.example.recipebowl

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.widget.TextViewCompat
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Recycler
import androidx.recyclerview.widget.RecyclerView.ViewHolder

class HomeAdapter (private val modelArrayList : MutableList<HomeModel>) : RecyclerView.Adapter<HomeAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent : ViewGroup, viewType : Int) : ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val infalteView = inflater.inflate(R.layout.home_row_layout, parent, false)
        return ViewHolder(infalteView)
    }

    override fun onBindViewHolder(holder : ViewHolder, position : Int) {
        val info = modelArrayList[position]
        holder.recipeName.text = info.getName()
    }

    override fun getItemCount(): Int {
        return modelArrayList.size
    }

    inner class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {
        val recipeName = itemView.findViewById<View>(R.id.recipeName) as TextView
    }
}