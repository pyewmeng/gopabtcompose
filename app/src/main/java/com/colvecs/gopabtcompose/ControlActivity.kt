package com.colvecs.gopabtcompose

import android.Manifest
import android.app.ProgressDialog
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.Context
import android.os.AsyncTask
import android.os.Bundle
import java.util.*
import android.bluetooth.BluetoothSocket
import android.content.ContentValues.TAG
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import androidx.activity.ComponentActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.colvecs.gopabtcompose.ControlActivity.Companion.m_bluetoothSocket
import com.colvecs.gopabtcompose.ControlActivity.Companion.m_isConnected
import com.colvecs.gopabtcompose.MainActivity.Companion.EXTRA_ADDRESS
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream


class ControlActivity: ComponentActivity(){

    private lateinit var stopvalue: EditText
    private lateinit var forwardvalue: EditText

    companion object {
        var m_myUUID: UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB")
        var m_bluetoothSocket: BluetoothSocket? = null
        lateinit var m_progress: ProgressDialog
        lateinit var m_bluetoothAdapter: BluetoothAdapter
        var m_isConnected: Boolean = false
        lateinit var m_address: String
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.control_layout)

        // Fetching the stored data from the SharedPreference
        val sh = getSharedPreferences("MySharedPref", MODE_PRIVATE)
        val s1 = sh.getInt("stopvalue",0)
        val s2 = sh.getInt("forwardvalue",0)
        val s3 = sh.getInt("backwardvalue",0)
        val s4 = sh.getInt("leftvalue",0)
        val s5 = sh.getInt("rightvalue",0)
        val s6 = sh.getInt("ultrasoundvalue",0)
        val s7 = sh.getInt("musicvalue",0)
        val s8 = sh.getInt("infraredvalue",0)




        m_address = intent.getStringExtra(MainActivity.EXTRA_ADDRESS).orEmpty()

        var mControlLedStop = findViewById<Button>(R.id.control_led_stop)
        var mControlLedForward = findViewById<Button>(R.id.control_led_forward)
        var mControlLedBackward = findViewById<Button>(R.id.control_led_backward)
        var mControlLedLeft = findViewById<Button>(R.id.control_led_left)
        var mControlLedRight = findViewById<Button>(R.id.control_led_right)
        var mControlLedDisConnect = findViewById<Button>(R.id.control_led_disconnect)
        var mControlLedSettings = findViewById<Button>(R.id.control_settings)
        var mControlLedUltrasound = findViewById<Button>(R.id.control_ultrasound)
        var mControlLedMusic = findViewById<Button>(R.id.control_music)
        var mControlLedInfrared = findViewById<Button>(R.id.control_Infrared)
        var mControlLedSpeedup = findViewById<Button>(R.id.control_Speedup)
        var mControlLedSpeeddown = findViewById<Button>(R.id.control_Speeddown)
        ConnectToDevice(this).execute()

        mControlLedStop.setOnClickListener { sendCommand(s1.toString())}
        mControlLedForward.setOnClickListener { sendCommand(s2.toString())}
        mControlLedBackward.setOnClickListener { sendCommand(s3.toString())}
        mControlLedLeft.setOnClickListener { sendCommand(s4.toString())}
        mControlLedRight.setOnClickListener { sendCommand(s5.toString())}
        mControlLedUltrasound.setOnClickListener { sendCommand(s6.toString())}
        mControlLedMusic.setOnClickListener { sendCommand(s7.toString())}
        mControlLedInfrared.setOnClickListener { sendCommand(s8.toString())}
        mControlLedSpeedup.setOnClickListener { sendCommand("7")}
        mControlLedSpeeddown.setOnClickListener { sendCommand("8")}
        mControlLedDisConnect.setOnClickListener { disconnect() }
        mControlLedSettings.setOnClickListener { settings() }


    }

    private fun sendCommand(input: String) {
        val mmInStream: InputStream = m_bluetoothSocket!!.inputStream
        val mmOutStream: OutputStream = m_bluetoothSocket!!.outputStream
        val mmBuffer: ByteArray = ByteArray(1024) // mmBuffer store for the stream


        if (m_bluetoothSocket != null) {
            try{
                m_bluetoothSocket!!.outputStream.write(input.toByteArray())

            } catch(e: IOException) {
                e.printStackTrace()
            }
        }
    }

    private fun disconnect() {
        if (m_bluetoothSocket != null) {
            try {
                m_bluetoothSocket!!.close()
                m_bluetoothSocket = null
                m_isConnected = false
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
        finish()
    }

    private fun settings() {

        val intent = Intent(this, settingsActivity::class.java)
        //intent.putExtra(EXTRA_ADDRESS, address)
        startActivity(intent)

    }



    private class ConnectToDevice(c: Context) : AsyncTask<Void, Void, String>() {
        private var connectSuccess: Boolean = true
        private val context: Context


        init {
            this.context = c
        }

        override fun onPreExecute() {
            super.onPreExecute()
            m_progress = ProgressDialog.show(context, "Connecting...", "please wait a sec")
        }

        override fun doInBackground(vararg p0: Void?): String? {
            try {
                if (m_bluetoothSocket == null || !m_isConnected) {
                    m_bluetoothAdapter = BluetoothAdapter.getDefaultAdapter()

                    val device: BluetoothDevice = m_bluetoothAdapter.getRemoteDevice(m_address)

                    if (ActivityCompat.checkSelfPermission(
                            this.context,
                            Manifest.permission.BLUETOOTH_CONNECT
                        ) != PackageManager.PERMISSION_GRANTED
                    ) {

                        // TODO: Consider calling
                        //    ActivityCompat#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.
                        //return
                    }
                    m_bluetoothSocket = device.createInsecureRfcommSocketToServiceRecord(m_myUUID)
                    //BluetoothAdapter.getDefaultAdapter().cancelDiscovery()
                    m_bluetoothSocket!!.connect()

                }
            } catch (e: IOException) {
                connectSuccess = false
                e.printStackTrace()
            }
            return null
        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
            if (!connectSuccess) {
                Log.i("data", "couldn't connect")
            } else {
                m_isConnected = true
            }
            m_progress.dismiss()
        }
    }

}

