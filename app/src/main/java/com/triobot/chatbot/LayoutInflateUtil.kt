package com.triobot.chatbot


import android.content.Context
import android.view.LayoutInflater
import android.widget.FrameLayout
import android.widget.LinearLayout


class LayoutInflateUtil(var context: Context){


    fun userTextLayout(): FrameLayout {
        val inflater = LayoutInflater.from(context)
        return inflater.inflate(R.layout.user_message, null) as FrameLayout
    }


    fun startingLayout(): FrameLayout {
        val inflater = LayoutInflater.from(context)
        return inflater.inflate(R.layout.starting_layout, null) as FrameLayout
    }

    fun endingLayout(): LinearLayout {
        val inflater = LayoutInflater.from(context)
        return inflater.inflate(R.layout.ending_layout, null) as LinearLayout
    }

    fun botTextLayout(): FrameLayout {
        val inflater = LayoutInflater.from(context)
        return inflater.inflate(R.layout.bot_message, null) as FrameLayout
    }



}