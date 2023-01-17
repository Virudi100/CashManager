package com.example.cashmanager

import android.app.PendingIntent
import android.content.Intent
import android.content.IntentFilter
import android.nfc.NfcAdapter
import android.nfc.Tag
import android.nfc.tech.NfcA
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log

class NFCReaderActivity : AppCompatActivity() {
    private var nfcAdapter: NfcAdapter? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nfcreader)

        this.nfcAdapter = NfcAdapter.getDefaultAdapter(this)?.let { it }
    }
    override fun onNewIntent(intent: Intent?)
    {
        super.onNewIntent(intent)
        Log.e("ans", this.nfcAdapter.toString())
        Log.e("ans", NfcAdapter.EXTRA_TAG)
        Log.e("ans", intent.toString())
        if (intent != null) {
            Log.e("ans", intent.hasExtra("NFCReaderActivity").toString())
        }
        var tagFromIntent: Tag? = intent?.getParcelableExtra(NfcAdapter.EXTRA_TAG)
        Log.e("ans", tagFromIntent.toString())
        val nfc = NfcA.get(tagFromIntent)

        val atqa: ByteArray = nfc.getAtqa()
        val sak: Short = nfc.getSak()

        nfc.connect()
        val isConnected= nfc.isConnected()

        if(isConnected)
        {
            val receivedData:ByteArray = nfc.transceive(NFC_READ_COMMAND())

            //code to handle the received data
            // Received data would be in the form of a byte array that can be converted to string
            //NFC_READ_COMMAND would be the custom command you would have to send to your NFC Tag in order to read it

        }
        else{
            Log.e("ans", "Not connected")
        }
    }

    private fun NFC_READ_COMMAND(): ByteArray? {
        return null
    }

    private fun enableForegroundDispatch(activity: AppCompatActivity, adapter: NfcAdapter?) {

        val intent = Intent(activity.applicationContext, activity.javaClass)
        intent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP

        val pendingIntent = PendingIntent.getActivity(activity.applicationContext, 0, intent, PendingIntent.FLAG_IMMUTABLE)

        val filters = arrayOfNulls<IntentFilter>(1)

        val techList = arrayOf<Array<String>>()
        filters[0] = IntentFilter()

        with(filters[0]) {
            this?.addAction(NfcAdapter.ACTION_NDEF_DISCOVERED)
            this?.addCategory(Intent.CATEGORY_DEFAULT)
            try {
                this?.addDataType("text/plain")
            } catch (ex: IntentFilter.MalformedMimeTypeException) {
                throw RuntimeException("ERROR")
            }
        }
        adapter?.enableForegroundDispatch(activity, pendingIntent, filters, techList)
    }

    override fun onResume() {
        super.onResume()
        enableForegroundDispatch(this, this.nfcAdapter)
    }
}
