package com.example.cashmanager

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.media.MediaPlayer
import android.os.Bundle
import android.os.Vibrator
import android.util.Log
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.zxing.Result
import me.dm7.barcodescanner.zxing.ZXingScannerView
import me.dm7.barcodescanner.zxing.ZXingScannerView.ResultHandler
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import org.json.JSONObject
import java.io.IOException


class ScanningCamera : AppCompatActivity(), ResultHandler
{

    private val REQUESTCAMERA = 1
    private  var scannerView : ZXingScannerView? = null

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        scannerView = ZXingScannerView(this)
        setContentView(scannerView)

        if(!checkPermission())
            requestPermission()
    }

    private fun checkPermission() : Boolean
    {
        return ContextCompat.checkSelfPermission(this@ScanningCamera,android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
    }

    private fun requestPermission()
    {
        ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.CAMERA),REQUESTCAMERA)
    }

    override fun onResume()
    {
        super.onResume()
        if(checkPermission())
        {
            if(scannerView == null)
            {
                scannerView = ZXingScannerView(this)
                setContentView(scannerView)
            }
            scannerView?.setResultHandler(this)
            scannerView?.startCamera()
        }
    }

    override fun onDestroy()
    {
        super.onDestroy()
        scannerView?.stopCamera()
    }

    override fun handleResult(p0: Result?)
    {
        val result = p0?.text
        val vibrator = applicationContext.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        vibrator.vibrate(1000)
        val MediaPlayer = MediaPlayer.create(this@ScanningCamera, R.raw.bip)
        MediaPlayer.start()
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Result")

        builder.setPositiveButton("OK") { dialog, which ->
            scannerView?.resumeCameraPreview (this@ScanningCamera)
            val URL = "http://192.168.68.114:3000/api/item/"+result
            Log.e("Http error", URL)

            if (URL.isNotEmpty()) {
                val client = OkHttpClient().newBuilder()
                    .build()

                val request = Request.Builder()
                    .url(URL)
                    .addHeader("Content-Type", "application/json")
                    .build()
                Log.e("Http error", request.method)

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
                                val response_item = JSONObject(body)
                                Log.e("Http error", response_item.toString())
                                val product_name = response_item.getString("itemName")
                                Log.e("Http error", product_name)
                                val product_price = response_item.getString("itemPrice")
                                Log.e("Http error", product_price)
                                val URL = "http://192.168.68.114:3000/api/panier/createPanier"

                                if (URL.isNotEmpty()) {
                                    val client = OkHttpClient().newBuilder()
                                        .build();
                                    val json =
                                        "{\"panierItemName\" : \"" + product_name + "\" , \"panierItemPrice\" : \"" + product_price + "\"}"
                                    Log.e("Http error", json)

                                    val body_panier = RequestBody.create(
                                        "application/json; charset=utf-8".toMediaTypeOrNull(),
                                        json
                                    )
                                    Log.e("Http error", body_panier.toString())

                                    val request = Request.Builder()
                                        .url(URL)
                                        .method("POST", body_panier)
                                        .addHeader("Content-Type", "application/json")
                                        .build()

                                    client.newCall(request).enqueue(object : Callback {
                                        override fun onFailure(call: Call, e: IOException) {
                                            e.printStackTrace()
                                        }

                                        override fun onResponse(call: Call, response: Response) {

                                            response.use {
                                                if (!response.isSuccessful) {
                                                    Log.e("Http error", "item non ajouté au panier")
                                                } else {
                                                    val body = response.body?.string()
                                                    val response = JSONObject(body)
                                                    Log.e("Http error", "item crée")
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
            startActivity(intent)
        }

        builder.setMessage(result)
        val alert = builder.create()
        alert.show()
        // call api
    }
}