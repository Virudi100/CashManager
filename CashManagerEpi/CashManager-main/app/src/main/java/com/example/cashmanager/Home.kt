package com.example.cashmanager

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageButton
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException

class Home : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        val buttonClickBack = findViewById<Button>(R.id.buttonBack)
        buttonClickBack.setOnClickListener {
            val intent = Intent(this, Login::class.java)
            startActivity(intent)
        }

        val buttonClickCamera = findViewById<ImageButton>(R.id.imageButtonCamera)
        buttonClickCamera.setOnClickListener {
            val intent = Intent(this, ScanningCamera::class.java)
            startActivity(intent)
        }

        val buttonClickPanier = findViewById<ImageButton>(R.id.imageButtonPanier)
        buttonClickPanier.setOnClickListener {
            val URL = "http://192.168.68.114:3000/api/panier/getAllPanier"

            if (URL.isNotEmpty()) {
                val client = OkHttpClient().newBuilder()
                    .build();

                val request = Request.Builder()
                    .url(URL)
                    .addHeader("Content-Type", "application/json")
                    .build()

                client.newCall(request).enqueue(object : Callback {
                    override fun onFailure(call: Call, e: IOException) {
                        e.printStackTrace()
                    }

                    override fun onResponse(call: Call, response: Response) {

                        response.use {
                            if (!response.isSuccessful) {
                                Log.e("Http error", "Something went wrong")
                            } else {
                                val body = response.body?.string()
                                val jsonArray = JSONArray(body)
                                var montant = 0
                                var itemconcat = ""
                                var price: ArrayList<String> = ArrayList()
                                var name: ArrayList<String> = ArrayList()
                                for (i in 0 until jsonArray.length()) {
                                    val item = jsonArray?.get(i).toString()
                                    val response = JSONObject(item)
                                    val itemName = response.getString("panierItemName")
                                    val itemPrice = response.getString("panierItemPrice")
                                    montant = montant + itemPrice.toInt()
                                    itemconcat = itemName +"               "+ itemPrice
                                    price.add(itemconcat)
                                }
                                println(montant)
                                val intent = Intent(this@Home, Panier::class.java)
                                intent.putExtra("montant", montant.toString())
                                intent.putExtra("prix", price)
                                startActivity(intent)
                            }
                        }
                    }
                })
            } else {
                Log.d("tag", "Failed request")
            }

        }
    }
}