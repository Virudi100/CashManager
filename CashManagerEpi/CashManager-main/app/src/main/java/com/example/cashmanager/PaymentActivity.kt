package com.example.cashmanager

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException


class PaymentActivity : AppCompatActivity() {


    @SuppressLint("WrongViewCast", "SuspiciousIndentation", "MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payment)

        val buttonClickBack = findViewById<Button>(R.id.buttonBack)
        buttonClickBack.setOnClickListener {
            val intent = Intent(this, Home::class.java)
            startActivity(intent)
        }

        var str =  ""
        if (intent.hasExtra("montant")) {
            str = intent.getStringExtra("montant").toString()
            Log.v("EditText", "mmmmmmm")
            Log.v("EditText", str)

        }
        val textPayment = findViewById<EditText>(R.id.editTextPayment)

        textPayment.setOnClickListener(
            object : View.OnClickListener {
                override fun onClick(view: View?) {
                    Log.v("EditText", textPayment.getText().toString())
                }
            }
        )

        val buttonClickLogin = findViewById<Button>(R.id.buttonPayment)
        buttonClickLogin.setOnClickListener {

            val URL = "http://192.168.68.114:3000/api/bank/payment/"+textPayment.getText()
            Log.e("Http error", textPayment.getText().toString())
            Log.e("Http error", URL)

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
                            if (response.body?.string() == "null") {
                                Log.e("Http error", "Something went wrong")
                            } else {
                                val body = response.body?.string()
                                if (body != null) {
                                    Log.e("Http error", body)
                                }
                                val response = JSONObject(body)
                                var montant = response.getString("montant")
                                Log.e("Http error", montant)
                                if(montant.toInt() >= str.toInt()){
                                    Log.e("Http error", montant)
                                    val URL = "http://192.168.68.114:3000/api/bank/"+textPayment.getText()
                                    Log.e("Http error", URL)

                                    if (URL.isNotEmpty()) {
                                        val client = OkHttpClient().newBuilder()
                                            .build();
                                        montant = (montant.toInt() - str.toInt()).toString()
                                        val json =
                                            "{\"montant\" : \"" + montant + "\"}"
                                        Log.e("Http error", json)

                                        val body = RequestBody.create(
                                            "application/json; charset=utf-8".toMediaTypeOrNull(),
                                            json
                                        )

                                        val request = Request.Builder()
                                            .url(URL)
                                            .method("PUT", body)
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
                                                        if (body != null) {
                                                            Log.e("Http error", body)
                                                        }
                                                        val response = JSONObject(body)
                                                        val montant = response.getString("montant")
                                                        Log.e("Http error", montant)
                                                        val URL = "http://192.168.68.114:3000/api/panier/deletePanier"

                                                        if (URL.isNotEmpty()) {
                                                            val client = OkHttpClient().newBuilder()
                                                                .build();

                                                            val request = Request.Builder()
                                                                .url(URL)
                                                                .delete()
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
                                                                            Log.e("Http error", "mmmmmmmmmmmmmmmmmmm")
                                                                            val intent = Intent(this@PaymentActivity, Home::class.java)
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
                                        })
                                    } else {
                                        Log.d("tag", "Failed request")
                                    }
                                } else {
                                Log.e("Http error", "pas assez d'argent")
                                    val intent = Intent(this@PaymentActivity, Panier::class.java)
                                    startActivity(intent)
                                }
                            }
                        }
                    }
                })
            }
        }
    }
