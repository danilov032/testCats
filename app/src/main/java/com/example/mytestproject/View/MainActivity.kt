package com.example.mytestproject.View

import android.annotation.SuppressLint
import android.app.DownloadManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mytestproject.*
import com.example.mytestproject.DB.ObjectURL
import com.example.mytestproject.DB.ObjectURL.list
import com.example.mytestproject.Model.ModelCatFavourites
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private val STORAGE_PERMISSION_CODE: Int = 1000
    @SuppressLint("CheckResult")
    override fun onCreate(savedInstanceState: Bundle?) {
        ObjectURL.url = ""
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val database = dbAbstract.getDatabase(applicationContext)  // Создать или получить базу данных
        val dao = database?.catsDao()

        val apiService =
            ApiService.create()
        val repository = SearchRepository(apiService)
        repository.searchUsers(10)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe ({
                    result ->
                Log.d("Result", "There are  Java developers in Lagos")
                val cats = ArrayList<String>()
                for (element in result)
                {
                    cats.add(element.url)
                }
                val catsAdapter = CustomAdapter(cats)
                recyclerView.apply {
                    layoutManager = LinearLayoutManager(this@MainActivity)
                    adapter = catsAdapter
                }
            }, { error ->
                error.printStackTrace()
            })
        buttonFavourites.setOnClickListener {
            list.clear()
            Observable.fromCallable {
                with(dao){
                    this?.getCats()
                }
            }.map { it.map { list.add(it.url) } }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe()
            val intent = Intent(this, activity_favourites::class.java)
            startActivity(intent)
        }

        buttonDownload.setOnClickListener {
            if(!ObjectURL.url.equals("")) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) ==
                        PackageManager.PERMISSION_DENIED
                    ) {

                        requestPermissions(
                            arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE),
                            STORAGE_PERMISSION_CODE
                        )
                    } else {
                        startDownloading()
                    }
                } else {
                    startDownloading()
                }
            }
            else{
                Toast.makeText(applicationContext, "Выберите Изображение!!!", Toast.LENGTH_SHORT).show()
            }
        }
        inFavorites.setOnClickListener {
            if(!ObjectURL.url.equals("")) {
                Observable.fromCallable {
                    var cat = ModelCatFavourites(url = ObjectURL.url)

                    with(dao) {
                        this?.addReadout(cat)
                    }
                }.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe()
            }
            else
            {
                Toast.makeText(applicationContext, "Выберите Изображение!!!", Toast.LENGTH_SHORT).show()
            }

        }
    }
    private fun startDownloading() {
        val url = ObjectURL.url
        if(url.isNotEmpty()) {
            val request = DownloadManager.Request(Uri.parse(url))
            request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI or DownloadManager.Request.NETWORK_MOBILE)
            request.setTitle("Download")
            request.setDescription("the file...")

            request.allowScanningByMediaScanner()
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
            request.setDestinationInExternalPublicDir(
                Environment.DIRECTORY_DOWNLOADS,
                "${System.currentTimeMillis()}"
            )

            val manager = getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
            manager.enqueue(request)
        }
        else{
            Toast.makeText(applicationContext, "выбери", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            STORAGE_PERMISSION_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] ==
                    PackageManager.PERMISSION_GRANTED
                ) {
                    startDownloading()
                } else {
                    Toast.makeText(this, "Permission", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

}