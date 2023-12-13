package com.example.itog
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.content.Intent
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import com.yandex.mapkit.MapKitFactory

class MainActivity : AppCompatActivity() {
    private var first = true
    override fun onCreate(savedInstanceState: Bundle?) {
        if (first)
        {MapKitFactory.setApiKey("f313bf50-e7e9-49ee-b229-341a1295de08")
        first = false}
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val email = findViewById<EditText>(R.id.email)
        val password = findViewById<EditText>(R.id.password)
        var db = Firebase.firestore
        var sp = getSharedPreferences("PC", Context.MODE_PRIVATE)
        val buttonNavigate1 = findViewById<Button>(R.id.button1)
        if(sp.getString("activ", "онлайн") == "онлайн"){
            startActivity(Intent(this, glav::class.java))
        }


        buttonNavigate1.setOnClickListener {
            db.collection("users")
                .get()
                .addOnSuccessListener { result ->
                    for (document in result){
                    if(document.getString("email")==email.text.toString()){
                        if(document.getString("password")==password.text.toString()){
                            sp.edit().putString("activ", "онлайн").commit()
                            val intent = Intent(this, glav::class.java)
                            startActivity(intent)
                            }
                        else if(document.getString("password")!==password.text.toString()){
                            Toast.makeText(this, "Неправильный пароль", Toast.LENGTH_LONG).show()
                            password.setText("")
                            }
                        }
                    }
                }
        }

        val buttonNavigate2 = findViewById<Button>(R.id.button2)
        buttonNavigate2.setOnClickListener {
            val intent = Intent(this, registration::class.java)
            startActivity(intent)
        }

    }
}