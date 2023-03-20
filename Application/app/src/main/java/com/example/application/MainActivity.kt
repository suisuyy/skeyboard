package com.example.application;

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import adrt.ADRTLogCatReader

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ADRTLogCatReader.onContext(this@MainActivity, "com.aide.lts")
        setContentView(R.layout.activity_main)
    }
}
