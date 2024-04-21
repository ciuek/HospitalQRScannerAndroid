package com.example.hospitalscannerandroid

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.activity.ComponentActivity


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initViews()
    }

    private fun initViews() {
        val btnScanBarcode = findViewById<Button>(R.id.btnScanBarcode)
        btnScanBarcode.setOnClickListener {
            startActivity(Intent(this@MainActivity, ScannedBarcodeActivity::class.java))
        }
    }
}
