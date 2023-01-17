package com.example.cashmanager

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import java.io.IOException
import org.json.JSONObject


class Login : AppCompatActivity() {

    val positiveButton= { dialog: DialogInterface, which: Int ->
        Toast.makeText(applicationContext,
            android.R.string.no, Toast.LENGTH_SHORT).show()
    }
    fun showDialogEmptyMail(view: View?){
        val builder= AlertDialog.Builder(this)
        builder.setTitle("Alert")
        builder.setMessage("Veuillez entrer un email")
        builder.setPositiveButton("Ok",DialogInterface.OnClickListener(function = positiveButton))
        builder.show()
    }
    fun showDialogEmptyPass(view: View?){
        val builder= AlertDialog.Builder(this)
        builder.setTitle("Alert")
        builder.setMessage("Entrer le mot passe associé à votre mail")
        builder.setPositiveButton("Ok",DialogInterface.OnClickListener(function = positiveButton))
        builder.show()
    }
    fun showDialogError(view: View?){
        val builder= AlertDialog.Builder(this)
        builder.setTitle("Alert")
        builder.setMessage("email ou mot de passe invalide")
        builder.setPositiveButton("Ok",DialogInterface.OnClickListener(function = positiveButton))
        builder.show()
    }

    @SuppressLint("WrongViewCast", "SuspiciousIndentation", "MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val buttonClickBack = findViewById<Button>(R.id.buttonBack)
        buttonClickBack.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }


        val textEmail = findViewById<EditText>(R.id.editTextPayment)
        val textPassword = findViewById<EditText>(R.id.editTextTextEmailAddress2)

        textEmail.setOnClickListener(
            object : View.OnClickListener {
                override fun onClick(view: View?) {
                    Log.v("EditText", textEmail.getText().toString())
                }
            }
        )
        textPassword.setOnClickListener(
            object : View.OnClickListener {
                override fun onClick(view: View?) {
                    Log.v("EditText", textPassword.getText().toString())
                }
            }
        )



        val buttonClickLogin = findViewById<Button>(R.id.buttonPayment)
        buttonClickLogin.setOnClickListener {
            if( textEmail.getText().toString()==""){
                showDialogEmptyMail(null)
            }
            if(textPassword.getText().toString()==""){
                showDialogEmptyPass(null)
            }

            val URL = "http://192.168.68.121:3000/api/user/login"

                    if (URL.isNotEmpty()) {
                        val client = OkHttpClient().newBuilder()
                            .build();
                        val json =
                            "{\"email\" : \"" + textEmail.getText() + "\" , \"password\" : \"" + textPassword.getText() + "\"}"

                        val body = RequestBody.create(
                            "application/json; charset=utf-8".toMediaTypeOrNull(),
                            json
                        )

                        val request = Request.Builder()
                            .url(URL)
                            .method("POST", body)
                            .addHeader("Content-Type", "application/json")
                            .build()

                        client.newCall(request).enqueue(object : Callback {
                            override fun onFailure(call: Call, e: IOException) {
                                e.printStackTrace()
                            }


                            override fun onResponse(call: Call, response: Response) {

                                response.use {
                                    if (!response.isSuccessful) {
                                        println("invalide")
                                        Log.e("Http error", "Something went wrong")

                                    } else {
                                        val body = response.body?.string()
                                        val response = JSONObject(body)
                                        val email = response.getJSONObject("user").getString("email")
                                        Log.e("Http error", email)
                                        val intent = Intent(this@Login, Home::class.java)
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




