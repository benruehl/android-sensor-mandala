package de.beuth.test.adapters

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import de.beuth.test.filters.SensorFilter

/**
 * Created by Benjamin Rühl on 30.12.2017.
 */
class NamedItemsArrayAdapter(context: Context?, resource: Int, objects: List<NamedAdaptee>?)
    : ArrayAdapter<NamedItemsArrayAdapter.NamedAdaptee>(context, resource, objects) {

    interface NamedAdaptee {
        fun getDisplayName(context: Context): String
    }

    private var items: List<NamedAdaptee> = objects ?: ArrayList()

    override fun getCount(): Int {
        return items.count()
    }

    override fun getItem(position: Int): NamedAdaptee {
        return items[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val label = TextView(context)
        label.text = getItem(position).getDisplayName(context)
        label.setPadding(50, 25, 50, 25)
        return label
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup?): View {
        return getView(position, convertView, parent)
    }
}