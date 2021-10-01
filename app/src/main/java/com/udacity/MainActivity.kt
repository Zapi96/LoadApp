package com.udacity

import android.app.DownloadManager
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.RadioButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*


class MainActivity : AppCompatActivity() {

    private var downloadID: Long = 0

    private lateinit var notificationManager: NotificationManager
    private lateinit var action: NotificationCompat.Action

    lateinit var glideButton: Button
    lateinit var retrofitButton: Button
    lateinit var loadAppButton: Button

    private var url: String? = null
    private var name: String? = null



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        glideButton = findViewById(R.id.glideButton)
        retrofitButton = findViewById(R.id.retrofitButton)
        loadAppButton = findViewById(R.id.loadappButton)

        glideButton.setOnClickListener {
            url = URL_glide
            name = glideButton.text.toString()
        }

        retrofitButton.setOnClickListener {
            url = URL_retrofit
            name = retrofitButton.text.toString()
        }

        loadAppButton.setOnClickListener {
            url = URL_loadApp
            name = loadAppButton.text.toString()
        }



        registerReceiver(receiver, IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE))

        custom_button.setOnClickListener {
            Log.d("SUCCESS","Button")
            download(url)

        }

    }


    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent?) {


            val id = intent?.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1)
            var status: Int? = null
            val downloadManager = getSystemService(DOWNLOAD_SERVICE) as DownloadManager
            val query = id?.let { DownloadManager.Query().setFilterById(it) }
            val cursor = downloadManager.query(query)
            notificationManager = getSystemService( NotificationManager::class.java) as NotificationManager


            if (cursor.moveToFirst()) {
                status = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS))
                if (status ==  DownloadManager.STATUS_SUCCESSFUL){
                    Log.d("SUCCESS","Downloaded")

                    custom_button.buttonState = ButtonState.Completed
                    notificationManager.sendNotification("Downloaded", context,CHANNEL_ID,"Success",name)

                }else if (status ==  DownloadManager.STATUS_FAILED){
                    Log.d("SUCCESS","Not Downloaded")

                    custom_button.buttonState = ButtonState.Completed
                    notificationManager.sendNotification("Not Downloaded", context,CHANNEL_ID,"Fail",name)
                }

            }

        }
    }

    private fun download(url:String?) {
        if (url!=null) {
            custom_button.buttonState = ButtonState.Loading

            Log.d("SUCCESS",url)
            val request =
                DownloadManager.Request(Uri.parse(url))
                    .setTitle(getString(R.string.app_name))
                    .setDescription(getString(R.string.app_description))
                    .setRequiresCharging(false)
                    .setAllowedOverMetered(true)
                    .setAllowedOverRoaming(true)

            val downloadManager = getSystemService(DOWNLOAD_SERVICE) as DownloadManager
            downloadID =
                downloadManager.enqueue(request)// enqueue puts the download request in the queue.
            Log.d("SUCCESS",downloadID.toString())
        }else{
            Log.d("SUCCESS","Not downloading")
            custom_button.buttonState = ButtonState.Completed
            Toast.makeText(this,"Select a file",Toast.LENGTH_SHORT).show()
        }

    }

    companion object {
        private const val URL_glide =
            "https://github.com/bumptech/glide/archive/refs/heads/master.zip"
        private const val URL_retrofit =
            "https://github.com/udacity/nd940-c3-advanced-android-programming-project-starter/archive/refs/heads/master.zip"
        private const val URL_loadApp =
            "https://github.com/square/retrofit/archive/refs/heads/master.zip"
        private const val CHANNEL_ID = "channelId"
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(receiver)
    }
}
