package com.iut.kotlin_rss

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ToggleButton
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.category.view.*
import kotlinx.android.synthetic.main.filtermenu.view.*

class FilterMenu(val reset: () -> Unit, val function: (FilterMenu) -> Unit) : BottomSheetDialogFragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val view = inflater.inflate(R.layout.filtermenu, container, false)
        val filterFilterButton : Button = view.filter_filter_button
        val resetButton : Button = view.filter_reset_button
        resetButton.setOnClickListener {
            reset()
            dismiss()
        }
        filterFilterButton.setOnClickListener {
            function(this)
        }
        return view
    }
}