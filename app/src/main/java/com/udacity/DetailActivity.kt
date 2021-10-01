package com.udacity

import android.app.NotificationManager
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_detail.*
import kotlinx.android.synthetic.main.content_detail.*

class DetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        setSupportActionBar(toolbar)

        val notificationManager = ContextCompat.getSystemService(applicationContext,
            NotificationManager::class.java
        ) as NotificationManager
        notificationManager.cancel(0)

        val status = intent.getStringExtra("status")
        val url = intent.getStringExtra("name")

        textViewFileName.setText(url)
        textViewStatusFileName.setText(status)

        buttonOk.setOnClickListener {
            finish()
        }

    }



}
