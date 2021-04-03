package com.iut.kotlin_rss.adapter

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.iut.kotlin_rss.R
import java.util.ArrayList

class ArticleAdapter(private val context: Activity, private val title: ArrayList<String>, private val content: ArrayList<String>,
                     private val url: ArrayList<String>,  private val date: ArrayList<String>) : ArrayAdapter<String>(context, R.layout.article, content) {

    override fun getView(position: Int, view: View?, parent: ViewGroup): View {

        val inflater = context.layoutInflater
        val rowView = inflater.inflate(R.layout.article, null, true)

        val idText = rowView.findViewById(R.id.article_title) as TextView
        val nameText = rowView.findViewById(R.id.article_content) as TextView
        val dateText = rowView.findViewById(R.id.article_date) as TextView

        rowView.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url[position]))
            context.startActivity(intent)
        }

        idText.text = title[position]
        nameText.text = content[position]
        dateText.text = date[position]

        return rowView
    }
}