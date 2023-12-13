package com.example.itog

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore

class registration : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.registration)
        val email = findViewById<EditText>(R.id.email)
        val password = findViewById<EditText>(R.id.password)
        var sp = getSharedPreferences("PC", Context.MODE_PRIVATE)
        val buttonNavigate1 = findViewById<Button>(R.id.button2)
        buttonNavigate1.setOnClickListener {
            if(email.text.isEmpty() || !email.text.contains("@")){
                Toast.makeText(this, "Проверьте поле email!", Toast.LENGTH_LONG).show()
            }
            else if(password.text.isEmpty() || password.text.length < 6){
                Toast.makeText(this, "Пароль должен быть больше 5 символов", Toast.LENGTH_LONG).show()
            }
            else{
                val db = Firebase.firestore
                // Create a new user with a first and last name
                val user = hashMapOf(
                    "email" to email.text.toString(),
                    "password" to password.text.toString()
                )

                db.collection("users")
                    .add(user)
                    .addOnSuccessListener { documentReference ->
                        sp.edit().putString("activ", "онлайн").commit()
                        val intent = Intent(this, glav::class.java)
                        startActivity(intent)
                        finish()
                    }
                    .addOnFailureListener { e ->
                        Toast.makeText(this, "Ошибка", Toast.LENGTH_LONG).show()
                    }
            }
        }
    }
}