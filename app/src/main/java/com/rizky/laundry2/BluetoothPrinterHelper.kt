package com.rizky.laundry2

import android.Manifest
import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.widget.Toast
import androidx.core.content.ContextCompat
import java.io.IOException
import java.io.OutputStream
import java.util.*

class BluetoothPrinterHelper(private val context: Context) {

    private var bluetoothAdapter: BluetoothAdapter? = null
    private var socket: BluetoothSocket? = null
    private var outputStream: OutputStream? = null

    // UUID untuk SPP (Serial Port Profile)
    private val uuid = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB")

    init {
        // Inisialisasi BluetoothAdapter dengan pengecekan izin
        if (checkBluetoothPermission()) {
            bluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
        }
    }

    private fun checkBluetoothPermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.BLUETOOTH_CONNECT
        ) == PackageManager.PERMISSION_GRANTED
    }

    fun connect(deviceAddress: String): Boolean {
        if (!hasBluetoothPermission()) {
            Toast.makeText(context, "Bluetooth permission required", Toast.LENGTH_SHORT).show()
            return false
        }

        return try {
            val device = bluetoothAdapter?.getRemoteDevice(deviceAddress)
            device?.let {
                socket = it.createRfcommSocketToServiceRecord(uuid)
                socket?.connect()
                outputStream = socket?.outputStream
                true
            } ?: false
        } catch (e: SecurityException) {
            Toast.makeText(context, "Bluetooth operation not permitted", Toast.LENGTH_SHORT).show()
            false
        } catch (e: IOException) {
            e.printStackTrace()
            false
        }
    }

    fun printText(text: String) {
        try {
            outputStream?.write(text.toByteArray(charset("GBK")))
            outputStream?.write(byteArrayOf(0x0A)) // New line
            outputStream?.write(byteArrayOf(0x1D, 0x56, 0x41, 0x10)) // Cut paper
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(context, "Gagal mencetak", Toast.LENGTH_SHORT).show()
        }
    }

    fun disconnect() {
        try {
            outputStream?.close()
            socket?.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    fun isBluetoothEnabled(): Boolean {
        if (!checkBluetoothPermission()) return false
        return bluetoothAdapter?.isEnabled == true
    }

    private fun hasBluetoothPermission(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.BLUETOOTH_CONNECT
            ) == PackageManager.PERMISSION_GRANTED
        } else {
            true // Untuk versi Android di bawah 12
        }
    }

    @SuppressLint("MissingPermission")
    fun getPairedDevices(): Set<BluetoothDevice> {
        return if (hasBluetoothPermission()) {
            bluetoothAdapter?.bondedDevices ?: emptySet()
        } else {
            Toast.makeText(context, "Bluetooth permission required", Toast.LENGTH_SHORT).show()
            emptySet()
        }
    }

}