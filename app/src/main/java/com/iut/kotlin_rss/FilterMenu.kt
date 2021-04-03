package com.iut.kotlin_rss

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.filtermenu.view.*

class FilterMenu(val function: (FilterMenu) -> Unit) : BottomSheetDialogFragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val view = inflater.inflate(R.layout.filtermenu, container, false)
        val filterFilterButton : Button = view.filter_filter_button
        filterFilterButton.setOnClickListener {
            function(this)
        }
        return view
    }
}