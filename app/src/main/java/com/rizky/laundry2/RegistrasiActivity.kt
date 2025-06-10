package com.rizky.laundry2

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.*
import com.rizky.laundry2.modeldata.ModelUser

class RegistrasiActivity : AppCompatActivity() {
    private lateinit var database: DatabaseReference

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registrasi)

        database = FirebaseDatabase.getInstance().reference.child("users")

        val etUsername = findViewById<EditText>(R.id.etUsername)
        val etPassword = findViewById<EditText>(R.id.etPassword)
        val etNoHP = findViewById<EditText>(R.id.etNo_HP)
        val etRole = findViewById<EditText>(R.id.etRole)
        val etCabang = findViewById<EditText>(R.id.etCabang)
        val btnRegister = findViewById<Button>(R.id.btnRegister)

        btnRegister.setOnClickListener {
            val username = etUsername.text.toString().trim()
            val password = etPassword.text.toString().trim()
            val noHP = etNoHP.text.toString().trim()
            val role = etRole.text.toString().trim()
            val cabang = etCabang.text.toString().trim()

            if (username.isEmpty() || password.isEmpty() || noHP.isEmpty() || role.isEmpty() || cabang.isEmpty()) {
                Toast.makeText(this, "Semua field harus diisi", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            database.orderByChild("noHP").equalTo(noHP)
                .addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if (snapshot.exists()) {
                            Toast.makeText(this@RegistrasiActivity, "No HP sudah terdaftar, silakan login", Toast.LENGTH_SHORT).show()
                        } else {
                            val userId = database.push().key ?: ""
                            val newUser = ModelUser(username, password, noHP, role, cabang)
                            database.child(userId).setValue(newUser).addOnCompleteListener {
                                if (it.isSuccessful) {
                                    Toast.makeText(this@RegistrasiActivity, "Registrasi berhasil, silakan login", Toast.LENGTH_SHORT).show()
                                    finish()
                                } else {
                                    Toast.makeText(this@RegistrasiActivity, "Registrasi gagal", Toast.LENGTH_SHORT).show()
                                }
                            }
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        Toast.makeText(this@RegistrasiActivity, "Gagal cek data: ${error.message}", Toast.LENGTH_SHORT).show()
                    }
                })
        }
    }
}