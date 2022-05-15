package com.automotivecodelab.websockettest.ui.main

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.automotivecodelab.websockettest.R

class Adapter() : RecyclerView.Adapter<Adapter.MessageViewHolder>() {

    private var data: List<Message> = emptyList()

    @SuppressLint("NotifyDataSetChanged")
    fun setData(messages: List<Message>) {
        data = messages
        notifyDataSetChanged()
    }

    class MessageViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val textView: TextView = view.findViewById(R.id.text)
    }

    override fun getItemViewType(position: Int): Int {
        return data[position].layoutId
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(viewType, parent, false)
        return MessageViewHolder(view)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
        holder.textView.text = data[position].message
    }
}