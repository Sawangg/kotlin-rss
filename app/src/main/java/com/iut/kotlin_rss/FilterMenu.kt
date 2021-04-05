package com.iut.kotlin_rss

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.core.view.forEach
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.category.*
import kotlinx.android.synthetic.main.category.view.*
import kotlinx.android.synthetic.main.filtermenu.view.*

class FilterMenu(val category: String, private val favori : Boolean) : BottomSheetDialogFragment() {

    var dismissHandler : (FilterMenu) -> Unit = {}

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val view = inflater.inflate(R.layout.filtermenu, container, false)

        view.filter_reset_button.setOnClickListener {
            view.category_wrapper_group.selectedButtons.clear()
            view.filter_dof_switch.isChecked = false;
            dismiss()
        }

        view.filter_filter_button.setOnClickListener {
            dismiss()
        }

        view.category_wrapper_group.buttons.forEach {
            if(it.text == category)
                view.category_wrapper_group.selectButton(it.id)
        }

        view.filter_dof_switch.isChecked = favori
        return view
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        dismissHandler(this)
    }

    fun setOnDismiss(function: (FilterMenu) -> Unit) {
        dismissHandler = function
    }
}