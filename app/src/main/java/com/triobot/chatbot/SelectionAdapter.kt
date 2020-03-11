package com.triobot.chatbot

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.opinion_item.view.*

class SelectionAdapter(
    var context: Context,
    var items: MutableList<DialogModel>,
    private val itemClick: (DialogModel) -> Unit
) :
    RecyclerView.Adapter<SelectionAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.opinion_item, parent, false)
        return ViewHolder(view, itemClick)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindForecast(items[position])
    }

    override fun getItemCount() = items.size
    inner class ViewHolder(view: View, private val itemClick: (DialogModel) -> Unit) :
        RecyclerView.ViewHolder(view) {
        fun bindForecast(menuItem: DialogModel) {
            with(menuItem) {
                itemView.tvOpinion.text = menuItem.text

                itemView.setOnClickListener { itemClick(this) }
            }
        }
    }
}