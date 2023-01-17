package com.example.cashmanager

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import okhttp3.*
import java.io.IOException

class Panier : AppCompatActivity() {
    @SuppressLint("WrongViewCast", "MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_panier)

        var str =  ""
        if (intent.hasExtra("montant")) {
            str = intent.getStringExtra("montant").toString()
            Log.v("EditText", "mmmmmmm")
        }

        var priceList : ArrayList<String> = ArrayList()
        if (intent.hasExtra("prix")) {
            priceList = intent.getSerializableExtra("prix") as ArrayList<String>
            Log.v("EditText", priceList.toString())
        }


        var moneytopay = findViewById<TextView>(R.id.textMoneyToPay)
        moneytopay.setText(str)

        val adapter = ArrayAdapter(this,
            R.layout.listview_item, priceList)

        val listView:ListView = findViewById(R.id.listview_1)
        listView.setAdapter(adapter)

        val buttonClickBack = findViewById<Button>(R.id.buttonBack)
        buttonClickBack.setOnClickListener {
            val intent = Intent(this, Home::class.java)
            startActivity(intent)
        }

        val buttonClickDelete = findViewById<Button>(R.id.buttonDelete)
        buttonClickDelete.setOnClickListener {
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
                                val intent = Intent(this@Panier, Home::class.java)
                                startActivity(intent)
                            }
                        }
                    }
                })
            } else {
                Log.d("tag", "Failed request")
            }
        }

        val buttonClickPay = findViewById<Button>(R.id.buttonPay)
        buttonClickPay.setOnClickListener {
            val intent = Intent(this, PaymentActivity ::class.java)
            intent.putExtra("montant", str)
            startActivity(intent)
        }
    }
}