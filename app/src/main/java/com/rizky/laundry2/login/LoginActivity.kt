package com.rizky.laundry2.login

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.*
import com.rizky.laundry2.BerandaActivity
import com.rizky.laundry2.R
import com.rizky.laundry2.RegistrasiActivity
import com.rizky.laundry2.modeldata.ModelUser

class LoginActivity : AppCompatActivity() {
    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        database = FirebaseDatabase.getInstance().reference.child("users")

        val etNoHP = findViewById<EditText>(R.id.etNo_HP)
        val etPass = findViewById<EditText>(R.id.etPASS)
        val btnLogin = findViewById<Button>(R.id.btLOGIN)
        val btnRegister = findViewById<Button>(R.id.btnRegister)

        btnRegister.setOnClickListener {
            startActivity(Intent(this, RegistrasiActivity::class.java))
        }

        btnLogin.setOnClickListener {
            val noHP = etNoHP.text.toString().trim()
            val pass = etPass.text.toString().trim()

            if (noHP.isEmpty() || pass.isEmpty()) {
                Toast.makeText(this, getString(R.string.validasi_login), Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            database.orderByChild("noHP").equalTo(noHP)
                .addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if (snapshot.exists()) {
                            var loginSuccess = false
                            for (userSnapshot in snapshot.children) {
                                val user = userSnapshot.getValue(ModelUser::class.java)
                                if (user != null && user.password == pass) {
                                    loginSuccess = true

                                    val prefs = getSharedPreferences("LOGIN_PREFS", Context.MODE_PRIVATE)
                                    prefs.edit().apply {
                                        putString("noHP", user.noHP)
                                        putString("role", user.role)
                                        putString("nama", user.username)
                                        putString("cabang",user.cabang)
                                        putBoolean("isLoggedIn", true)
                                        apply()
                                    }

                                    val intent = Intent(this@LoginActivity, BerandaActivity::class.java)
                                    startActivity(intent)
                                    finish()
                                    break
                                }
                            }
                            if (!loginSuccess) {
                                Toast.makeText(this@LoginActivity, getString(R.string.password), Toast.LENGTH_SHORT).show()
                            }
                        } else {
                            Toast.makeText(this@LoginActivity, getString(R.string.user_tidak_ditemukan), Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        Toast.makeText(this@LoginActivity, "Gagal membaca data: ${error.message}", Toast.LENGTH_SHORT).show()
                    }
                })
        }
    }
}
