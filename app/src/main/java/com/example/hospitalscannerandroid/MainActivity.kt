package com.example.hospitalscannerandroid

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.ComponentActivity
import com.google.gson.Gson
import okhttp3.*
import okio.IOException


class MainActivity : ComponentActivity() {
    private var token: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initViews()
    }

    data class TokenResponse(val access_token: String)

    private fun initViews() {
        val btnScanBarcode = findViewById<Button>(R.id.btnScanBarcode)
        val btnLogin = findViewById<Button>(R.id.btnLogin)
        val etUsername = findViewById<EditText>(R.id.etUsername)
        val etPassword = findViewById<EditText>(R.id.etPassword)

        btnScanBarcode.setOnClickListener {
            if (token != null) {
                val intent = Intent(this@MainActivity, ScannedBarcodeActivity::class.java)
                intent.putExtra("token", token)
                startActivity(intent)
            } else {
                Toast.makeText(this@MainActivity, "Proszę najpierw się zalogować", Toast.LENGTH_LONG).show()
            }
        }

        btnLogin.setOnClickListener {
            // Pobranie wartości z pól EditText
            val username = etUsername.text.toString()
            val password = etPassword.text.toString()

            // Utworzenie klienta OkHttp
            val client = OkHttpClient()

            // Utworzenie żądania POST
            val requestBody = FormBody.Builder()
                .add("username", username)
                .add("password", password)
                .build()

            val request = Request.Builder()
                .url("http://192.168.1.100:8000/token")
                .post(requestBody)
                .build()

            client.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    e.printStackTrace()
                    runOnUiThread {
                        Toast.makeText(this@MainActivity, "Logowanie nie powiodło się", Toast.LENGTH_LONG).show()
                    }
                }

                override fun onResponse(call: Call, response: Response) {
                    response.use {
                        if (!response.isSuccessful) {
                            runOnUiThread {
                                Toast.makeText(
                                    this@MainActivity,
                                    "Logowanie nie powiodło się",
                                    Toast.LENGTH_LONG
                                ).show()
                            }
                            throw IOException("Unexpected code $response")
                        }

                        // Pobranie tokenu z odpowiedzi
                        val responseBody = response.body?.string()
                        val gson = Gson()
                        val tokenResponse = gson.fromJson(responseBody, TokenResponse::class.java)

                        // Przechowanie tokenu
                        token = tokenResponse.access_token

                        runOnUiThread {
                            Toast.makeText(this@MainActivity, "Zalogowano pomyślnie", Toast.LENGTH_LONG).show()
                            etUsername.visibility = View.GONE
                            etPassword.visibility = View.GONE
                            btnLogin.visibility = View.GONE
                        }
                    }
                }
            })
        }
    }
}
