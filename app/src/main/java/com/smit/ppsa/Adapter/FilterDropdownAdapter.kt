package com.smit.ppsa.Adapter

import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Filter
import android.widget.Filterable
import android.widget.TextView
import com.smit.ppsa.R
import com.smit.ppsa.Response.PatientFilterDataModel

class FilterDropdownAdapter(
    var activity: Activity?, var list: ArrayList<PatientFilterDataModel>?,
   /* private var dropDownListClickListener: DropDownListClickListener*/
) : BaseAdapter(), Filterable {

    override fun getCount(): Int {
        return list!!.size
    }

    override fun getItem(position: Int): PatientFilterDataModel {
        return list!![position]
    }

    override fun getItemId(position: Int): Long {
        return 0
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View? {
        var convertView: View? = convertView
        if (convertView == null) {
            val inflater =
                activity!!.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            convertView = inflater.inflate(R.layout.dropdown_row_layout, parent, false)
        }
        val textView: TextView = convertView!!.findViewById(R.id.textView)
        textView.text = getItem(position).value
        return convertView
    }

    fun updateList(list1: ArrayList<PatientFilterDataModel>?) {
        list = list1
        notifyDataSetChanged()
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun convertResultToString(resultValue: Any?): CharSequence {
                return (resultValue as PatientFilterDataModel).value
            }

            override fun performFiltering(charSequence: CharSequence): FilterResults {
                return FilterResults()
            }

            override fun publishResults(charSequence: CharSequence, filterResults: FilterResults) {
            }
        }
    }
}