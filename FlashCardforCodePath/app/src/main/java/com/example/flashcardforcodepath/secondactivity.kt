package com.example.flashcardforcodepath

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import com.google.android.material.snackbar.Snackbar

class secondactivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_secondactivity)
        val cancelie = findViewById<ImageView>(R.id.cancel_button)
        val qsedit=findViewById<EditText>(R.id.questionedittext)
        val ans1=findViewById<EditText>(R.id.answeredittext)
        val ans2=findViewById<EditText>(R.id.answer2edittext)
        val ans3=findViewById<EditText>(R.id.answer3edittext)
        val save=findViewById<ImageView>(R.id.save_button)
        if (intent.getStringExtra("key1")!=null) {
            val val1=intent.getStringExtra("key2")
            val val2=intent.getStringExtra("key3")
            val val3=intent.getStringExtra("key4")
            val val4=intent.getStringExtra("key1")
            qsedit.setText(val4)
            ans1.setText(val1)
            ans2.setText(val2)
            ans3.setText(val3)
        }
        save.setOnClickListener {
            if (qsedit.text.toString().equals("") || ans1.text.toString().equals("") || ans2.text.toString().equals("") || ans3.text.toString().equals("")) {
                Toast.makeText(getApplicationContext(), "Please fill in all the fields to create a flashcard.", Toast.LENGTH_SHORT).show()
            } else {
                val qsString = qsedit.text.toString()
                val ans1s = ans1.text.toString()
                val ans2s = ans2.text.toString()
                val ans3s = ans3.text.toString()
                val data = Intent()
                data.putExtra("qskey", qsString)
                data.putExtra("ans1key", ans1s)
                data.putExtra("ans2key", ans2s)
                data.putExtra("ans3key", ans3s)
                setResult(RESULT_OK, data)
                finish()
            }
        }
        cancelie.setOnClickListener {
            //startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }
}