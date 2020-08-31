package com.example.mytestproject.View

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mytestproject.CustomAdapter
import com.example.mytestproject.DB.ObjectURL
import com.example.mytestproject.R
import kotlinx.android.synthetic.main.activity_favourites.*

class activity_favourites : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favourites)

        val catsAdapter = CustomAdapter(ObjectURL.list)
        //catsAdapter.clearAndNotClearItem(ObjectURL.list)
        recyclerViewFavourites.apply {
            layoutManager = LinearLayoutManager(this@activity_favourites)
            adapter = catsAdapter
        }

    }
}