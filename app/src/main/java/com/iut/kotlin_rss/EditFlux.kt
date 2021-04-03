package com.iut.kotlin_rss

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.core.view.forEach
import com.iut.kotlin_rss.classes.Flux
import com.iut.kotlin_rss.handler.DatabaseHandler
import nl.bryanderidder.themedtogglebuttongroup.ThemedToggleButtonGroup

class EditFlux : AppCompatActivity() {

    lateinit var fluxName : EditText
    lateinit var fluxUrl : EditText
    lateinit var fluxFav : ToggleButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_editflux)
        val id = intent.getStringExtra("FluxId")
        val name = intent.getStringExtra("FluxName")
        val url = intent.getStringExtra("FluxUrl")
        val category = intent.getStringExtra("FluxCategory")
        val fav = intent.getStringExtra("FluxFav")

        val themedButtonGroup = findViewById<ThemedToggleButtonGroup>(R.id.category_wrapper_group)
        themedButtonGroup.buttons.forEach {
            if(it.text == category){
                themedButtonGroup.selectButton(it.id)
            }
        }
        fluxName = findViewById(R.id.flux_name)
        fluxUrl = findViewById(R.id.flux_uri)
        fluxFav = findViewById(R.id.edit_favorite)
        fluxName.setText(name)
        fluxUrl.setText(url)

        if(fav.toBoolean()){
            fluxFav.toggle()
        }



        val buttonDelete : Button = findViewById(R.id.delete_flux_delete_button)
        buttonDelete.setOnClickListener {
            deleteFlux(id!!)
        }

        val buttonUpdate : Button = findViewById(R.id.edit_flux_edit_button);
        buttonUpdate.setOnClickListener {
            editFlux(id!!)
        }

        val retour : TextView = findViewById(R.id.flux_edit_back_button)
        retour.setOnClickListener {
            val intent = Intent(this, EditActivity::class.java)
            startActivity(intent)
            finish()
        }

    }

    private fun deleteFlux(id : String) {
        val db = DatabaseHandler(this)
        db.deleteFlux(id)
        val intent = Intent(this@EditFlux, EditActivity::class.java);
        startActivity(intent);
        finish()
    }

    private fun editFlux(id : String) {
        val themedButtonGroup = findViewById<ThemedToggleButtonGroup>(R.id.category_wrapper_group)
        val name = fluxName.text.toString()
        val url = fluxUrl.text.toString()
        val category = themedButtonGroup.selectedButtons[0].text
        val databaseHandler = DatabaseHandler(this)
        if(name.trim()!="" && url.trim()!="" ){
            val flux = Flux(name,url,category);
            flux.id = id.toInt()
            flux.fav = fluxFav.isChecked
            val status = databaseHandler.updateFlux(flux)
            if(status > -1){
                fluxName.text.clear()
                fluxUrl.text.clear()
                val intent = Intent(this@EditFlux, EditActivity::class.java);
                startActivity(intent)
                finish()
            }
        } else {
            Toast.makeText(applicationContext,"Error", Toast.LENGTH_LONG).show()
        }
    }

    override fun onBackPressed() {
        val intent = Intent(this@EditFlux, EditActivity::class.java);
        startActivity(intent)
        finish()
    }
}