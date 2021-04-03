package com.iut.kotlin_rss.adapter

import android.app.Activity
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.iut.kotlin_rss.R
import java.util.ArrayList

class ArticleAdapter(private val context: Activity, private val title: ArrayList<String>, private val content: ArrayList<String>) : ArrayAdapter<String>(context, R.layout.flux, content) {

    override fun getView(position: Int, view: View?, parent: ViewGroup): View {

        val inflater = context.layoutInflater
        val rowView = inflater.inflate(R.layout.flux, null, true)

        val idText = rowView.findViewById(R.id.flux_title) as TextView
        val nameText = rowView.findViewById(R.id.flux_content) as TextView

        idText.text = title[position]
        nameText.text = content[position]
        return rowView
    }
}