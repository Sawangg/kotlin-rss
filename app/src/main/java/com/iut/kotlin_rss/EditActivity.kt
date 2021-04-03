package com.iut.kotlin_rss

import android.content.Intent
import android.os.Bundle
import android.widget.ListView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.iut.kotlin_rss.adapter.ListAdapter
import com.iut.kotlin_rss.classes.Flux
import com.iut.kotlin_rss.handler.DatabaseHandler
import nl.bryanderidder.themedtogglebuttongroup.ThemedToggleButtonGroup
import java.util.ArrayList

class EditActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_flux_list)
        displayFlux()
        val retour : TextView = findViewById(R.id.edit_flux_back)
        retour.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun displayFlux() {
        val databaseHandler = DatabaseHandler(this)
        val fluxs: List<Flux> = databaseHandler.viewFlux()
        val lvEditFlux = findViewById<ListView>(R.id.lv_edit_flux)
        val listName = ArrayList<String>()
        val listUrl = ArrayList<String>()
        fluxs.forEach {
            if(it.name != null && it.url != null){
                listName.add(it.name!!)
                listUrl.add(it.url!!)
            }
        }
       val adapter = ListAdapter(this,listName, listUrl);
        lvEditFlux.adapter = adapter
        lvEditFlux.setOnItemClickListener { _, _, position, _ ->
            val flux = fluxs[position]
            val intent = Intent(this, EditFlux::class.java)
            intent.putExtra("FluxId", flux.id.toString())
            intent.putExtra("FluxName", flux.name)
            intent.putExtra("FluxUrl", flux.url)
            intent.putExtra("FluxCategory", flux.category)
            startActivity(intent)
            finish()
        }
    }
    override fun onBackPressed() {
        val intent = Intent(this@EditActivity, MainActivity::class.java);
        startActivity(intent)
        finish()
    }
}