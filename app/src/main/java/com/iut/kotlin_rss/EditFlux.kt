package com.iut.kotlin_rss

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import com.iut.kotlin_rss.handler.DatabaseHandler

class EditFlux : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_editflux)
        val id = intent.getStringExtra("FluxId")
        val name = intent.getStringExtra("FluxName")
        val url = intent.getStringExtra("FluxUrl")
        val category = intent.getStringExtra("FluxCategory")
        val fluxName = findViewById<EditText>(R.id.flux_name)
        val fluxUrl = findViewById<EditText>(R.id.flux_uri)
        fluxName.setText(name)
        fluxUrl.setText(url)
        val buttonDelete : Button = findViewById(R.id.delete_flux_delete_button)
        buttonDelete.setOnClickListener {
            val db = DatabaseHandler(this)

            if (id != null) {
                db.deleteFlux(id)
            }

            val intent = Intent(this@EditFlux, EditActivity::class.java);
            startActivity(intent);
            finish()
        }

    }



}