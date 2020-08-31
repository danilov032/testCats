package com.example.mytestproject

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.mytestproject.DB.ObjectURL
import kotlinx.android.synthetic.main.recycler_item.view.*

class CustomAdapter(val catsList: ArrayList<String>) : RecyclerView.Adapter<CustomAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
            .inflate(R.layout.recycler_item, parent, false)
        return ViewHolder(inflater)
    }

    override fun onBindViewHolder(holder: CustomAdapter.ViewHolder, position: Int) {
        holder.bindItems(catsList[position])
    }

    override fun getItemCount(): Int {
        return catsList.size
    }

    class ViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {
        val imageView = view.itemIm
        fun bindItems(cat: String) {

            imageView.setOnClickListener {
                Toast.makeText(view.context, cat, Toast.LENGTH_SHORT).show()
                ObjectURL.url = cat
            }
            Glide
                .with(view.context)
                .load(cat)
                .fitCenter()
                .into(imageView)

        }
    }
}