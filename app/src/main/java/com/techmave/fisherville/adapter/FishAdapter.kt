package com.techmave.fisherville.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.appcompat.widget.AppCompatTextView
import com.techmave.fisherville.R
import com.techmave.fisherville.model.Market

class FishAdapter(context: Context, private val resource: Int, private val items: List<Market>): ArrayAdapter<Market>(context, resource, items) {

    override fun getItem(position: Int): Market {

        return items[position]
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {

        var view = convertView

        if (view == null) {

            view = LayoutInflater.from(parent.context).inflate(resource, parent, false)
        }

        val tView = view?.findViewById(R.id.spinner_text) as AppCompatTextView
        tView.text = getItem(position).name

        return view
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {

        var view = convertView

        if (view == null) {

            view = LayoutInflater.from(parent.context).inflate(resource, parent, false)
        }

        val tView = view?.findViewById(R.id.spinner_text) as AppCompatTextView
        tView.text = getItem(position).name

        return view
    }
}