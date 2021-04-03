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
        flux_categories = findViewById<ListView>(R.id.flux_categories)
        flux_categories.adapter = MyCustomAdapter(this)

        /*val flux_category: ToggleButton
        flux_category.setText("category name")
        flux_category.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                flux_category.setChecked(false)
            } else {
                flux_category.setChecked(true)
            }
        }*/

        val buttonAdd : Button = findViewById(R.id.add_flux_add_button)
        buttonAdd.setOnClickListener {
            saveRecord()
        }

    }

    private class MyCustomAdapter(context: Context): BaseAdapter() {
        private val mContext: Context
        init {
            mContext = context
        }
        override fun getCount(): Int {
            return 5
        }
        override fun getItemId(position: Int): Long {
            return position.toLong()
        }
        override fun getItem(position: Int): Any {
            return "Test"
        }
        override fun getView(position: Int, convertView: View?, viewGroup: ViewGroup?): View {
            val textView = TextView(mContext)
            textView.text = "Blabla"
            return textView
        }
    }

    fun saveRecord(){
        val name = flux_name.text.toString()
        val uri = flux_uri.text.toString()
        val databaseHandler: DatabaseHandler = DatabaseHandler(context = this)
        if(name.trim()!="" && uri.trim()!="" ){
            val status = databaseHandler.addFlux(Flux(name,uri,""))
            if(status > -1){
                Toast.makeText(applicationContext,"record save", Toast.LENGTH_LONG).show()
                flux_name.text.clear()
                flux_uri.text.clear()
                val intent = Intent(this@AddFlux, MainActivity::class.java);
                startActivity(intent)
            }
        }else{
            Toast.makeText(applicationContext,"Error", Toast.LENGTH_LONG).show()
        }

    }
}