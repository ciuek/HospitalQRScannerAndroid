package com.example.hospitalscannerandroid

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.google.zxing.integration.android.IntentIntegrator


class ScannedBarcodeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.scanned_code)
        if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.CAMERA),
                PERMISSION_REQUEST_CAMERA
            )
        } else {
            initQRCodeScanner()
        }
        val cat = Cat(
            name = "Kot",
            age = 5,
            pesel = "0002265",
            medical_history = listOf(
                MedicalEvent("1999-02-01T11:13:43", "cos tam sie stalo xds"),
                MedicalEvent("2000-07-06T21:37:00", "cos gorszego"),
                MedicalEvent("2000-07-06T21:37:00", "cos gorszego"),
                MedicalEvent("2000-07-06T21:37:00", "cos gorszego"),
                MedicalEvent("2000-07-06T21:37:00", "cos gorszego"),
                MedicalEvent("2000-07-06T21:37:00", "cos gorszego"),
                MedicalEvent("2000-07-06T21:37:00", "cos gorszego"),
                MedicalEvent("2000-07-06T21:37:00", "cos gorszego"),
                MedicalEvent("2000-07-06T21:37:00", "cos gorszego"),
                MedicalEvent("2000-07-06T21:37:00", "cos gorszego"),
                MedicalEvent("2000-07-06T21:37:00", "cos gorszego"),
                MedicalEvent("2000-07-06T21:37:00", "cos gorszego"),
                MedicalEvent("2000-07-06T21:37:00", "cos gorszego"),
                MedicalEvent("2000-07-06T21:37:00", "cos gorszego"),
                MedicalEvent("2000-07-06T21:37:00", "cos gorszego"),
            )
        )

        // Ustawienie danych w widokach
        val tvName = findViewById<TextView>(R.id.tvName)
        val tvAge = findViewById<TextView>(R.id.tvAge)
        val tvPesel = findViewById<TextView>(R.id.tvPesel)
        val lvMedicalHistory = findViewById<ListView>(R.id.lvMedicalHistory)

        tvName.text = "Imię i nazwisko: " + cat.name
        tvAge.text = "Wiek: " + cat.age.toString()
        tvPesel.text = "PESEL: " + cat.pesel

        // Utworzenie adaptera dla ListView
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_2, android.R.id.text1, cat.medical_history.map {
            "${it.event_date} - ${it.event_description}"
        })
        lvMedicalHistory.adapter = adapter
    }

    data class MedicalEvent(
        val event_date: String,
        val event_description: String
    )

    data class Cat(
        val name: String,
        val age: Int,
        val pesel: String,
        val medical_history: List<MedicalEvent>
    )

    private fun initQRCodeScanner() {
        val integrator = IntentIntegrator(this)
        integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE)
        integrator.setPrompt("Scan a QR code")
        integrator.initiateScan()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSION_REQUEST_CAMERA) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                initQRCodeScanner()
            } else {
                Toast.makeText(this, "Camera permission is required", Toast.LENGTH_LONG).show()
                finish()
            }
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
        if (result != null) {
            if (result.contents == null) {
                Toast.makeText(this, "Scan cancelled", Toast.LENGTH_LONG).show()
                finish()
            } else {
                Toast.makeText(this, "Scanned: " + result.contents, Toast.LENGTH_LONG).show()
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    companion object {
        private const val PERMISSION_REQUEST_CAMERA = 1
    }
}