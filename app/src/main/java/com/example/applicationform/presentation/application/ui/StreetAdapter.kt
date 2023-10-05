package com.example.applicationform.presentation.application.ui

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Filter
import android.widget.Filterable
import android.widget.TextView
import com.example.applicationform.R
import com.example.applicationform.domain.models.Street

class StreetAdapter(
    context: Context,
    id: Int,
    private val list: List<Street>,
    //private val actionListener: StreetClickListener
    private val onItemClickListener: (street: Street) -> Unit
) :
    ArrayAdapter<Street>(context, id, list), Filterable /*View.OnClickListener*/ {

    private var mList: List<Street> = list

    /*override fun onClick(v: View?) {
        val street = v?.tag as Street
        actionListener.onStreetClick(street)
    }*/


    override fun getCount(): Int {
        return mList.size
    }

    override fun getItem(position: Int): Street? {
        return mList[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val item = mList[position]
        val view: TextView = convertView as TextView? ?: LayoutInflater.from(context)
            .inflate(R.layout.text_item, parent, false) as TextView
        //view.rootView.setOnClickListener(this)
        view.text = item.streetName
        view.tag = item
        view.rootView.setOnClickListener { onItemClickListener(item) }
        return view
    }


    override fun getFilter(): Filter {
        return NewFilter()
    }

    inner class NewFilter : Filter() {

        override fun performFiltering(constraint: CharSequence?): FilterResults {

            val resultsValue = list.filter {
                it.streetName.contains(constraint ?: "", true)
            }
            val filterResults = FilterResults()
            filterResults.values = resultsValue
            filterResults.count = resultsValue.size
            return filterResults
        }

        override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
            mList = results?.values as List<Street>
            notifyDataSetChanged()
        }
    }

    interface StreetClickListener {
        fun onStreetClick(street: Street)
    }

}