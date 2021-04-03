package com.iut.kotlin_rss

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import com.iut.kotlin_rss.classes.Flux
import com.iut.kotlin_rss.handler.DatabaseHandler

class AddFlux : AppCompatActivity() {

    lateinit var flux_name : EditText
    lateinit var flux_uri : EditText
    lateinit var flux_categories : ListView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_addflux)
        flux_name = findViewById(R.id.flux_name)
        flux_uri = findViewById(R.id.flux_uri)

        val buttonAdd : Button = findViewById(R.id.add_flux_add_button)
        buttonAdd.setOnClickListener {
            saveRecord()
        }
    }
  
    private fun saveRecord() {
        val name = flux_name.text.toString()
        val url = flux_uri.text.toString()
        val databaseHandler = DatabaseHandler(context = this)
        if(name.trim()!="" && url.trim()!="" ){
            val status = databaseHandler.addFlux(Flux(name,url,""))
            if(status > -1){
                flux_name.text.clear()
                flux_uri.text.clear()
                val intent = Intent(this@AddFlux, MainActivity::class.java);
                startActivity(intent)
                finish()
            }
        } else {
            Toast.makeText(applicationContext,"Error", Toast.LENGTH_LONG).show()
        }
    }
}