package com.colvecs.gopabtcompose

import android.Manifest
import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.colvecs.gopabtcompose.ui.theme.GopabtcomposeTheme


class MainActivity : ComponentActivity() {
    private lateinit var stopvalue: EditText
    private lateinit var forwardvalue: EditText
    private var m_bluetoothAdapter: BluetoothAdapter? = null
    private lateinit var m_pairedDevices: Set<BluetoothDevice>
    val REQUEST_ENABLE_BLUETOOTH = 1

    companion object {
        val EXTRA_ADDRESS: String = "Device_address"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            GopabtcomposeTheme {

                setContentView(R.layout.select_device_layout)


                var mDeviceRefresh = findViewById<Button>(R.id.select_device_refresh)
                m_bluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
                if (m_bluetoothAdapter == null) {
                    showToast("this device doesn't support bluetooth")
                    //return
                }
                if (!m_bluetoothAdapter!!.isEnabled) {
                    val enableBluetoothIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
                    if (ActivityCompat.checkSelfPermission(
                            this,
                            Manifest.permission.BLUETOOTH_CONNECT
                        ) != PackageManager.PERMISSION_GRANTED
                    ) {
                        //if (ContextCompat.checkSelfPermission(this@MainActivity, Manifest.permission.BLUETOOTH_CONNECT) == PackageManager.PERMISSION_DENIED)
                        //{
                        //  if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S)
                        //{
                        //  ActivityCompat.requestPermissions(this@MainActivity, arrayOf(Manifest.permission.BLUETOOTH_CONNECT), 2)
                        // return
                        //}
                        //}
                        // TODO: Consider calling
                        //    ActivityCompat#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.
                      //  return
                    }
                    startActivityForResult(enableBluetoothIntent, REQUEST_ENABLE_BLUETOOTH)
                }

                mDeviceRefresh.setOnClickListener { pairedDeviceList() }

                // Storing data into SharedPreferences
                val sharedPreferences = getSharedPreferences("MySharedPref", MODE_PRIVATE)

                // Creating an Editor object to edit(write to the file)
                val myEdit = sharedPreferences.edit()

                // Storing the key and its value as the data fetched from edittext
                myEdit.putInt("stop", 0);
                myEdit.putInt("forward", 1);

                // Once the changes have been made, we need to commit to apply those changes made,
                // otherwise, it will throw an error
                myEdit.commit();

            }
        }
    }
    private fun pairedDeviceList() {
        var mDeviceList = findViewById<ListView>(R.id.select_device_list)
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.BLUETOOTH_CONNECT
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            if (ContextCompat.checkSelfPermission(this@MainActivity, Manifest.permission.BLUETOOTH_CONNECT) == PackageManager.PERMISSION_DENIED)
            {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S)
                {
                    ActivityCompat.requestPermissions(this@MainActivity, arrayOf(Manifest.permission.BLUETOOTH_CONNECT), 2)
                    return
                }
            }

            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return
        }
        m_pairedDevices = m_bluetoothAdapter!!.bondedDevices
        val list : ArrayList<BluetoothDevice> = ArrayList()

        if (!m_pairedDevices.isEmpty()) {
            for (device: BluetoothDevice in m_pairedDevices) {
                list.add(device)
                Log.i("device", ""+device.name)
            }
        } else {
            showToast("no paired bluetooth devices found")
        }
        //Show the Refresh List
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, list)
        mDeviceList.adapter = adapter
        mDeviceList.onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ ->
            val device: BluetoothDevice = list[position]
            val address: String = device.address

            val intent = Intent(this, ControlActivity::class.java)
            intent.putExtra(EXTRA_ADDRESS, address)
            startActivity(intent)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_ENABLE_BLUETOOTH) {
            if (resultCode == Activity.RESULT_OK) {
                if (m_bluetoothAdapter!!.isEnabled) {
                    showToast("Bluetooth has been enabled")
                } else {
                    showToast("Bluetooth has been disabled")
                }
            } else if (resultCode == Activity.RESULT_CANCELED) {
                showToast("Bluetooth enabling has been canceled")
            }
        }
    }

    fun Context.showToast(message: String, duration: Int = Toast.LENGTH_SHORT){
        Toast.makeText(this, message , duration).show()
    }
}



