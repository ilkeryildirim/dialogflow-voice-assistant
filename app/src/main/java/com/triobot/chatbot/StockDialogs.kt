package com.triobot.chatbot

import com.triobot.chatbot.ChatTypes.ACTIONS.*

object StockDialogs {

    fun getDevicesDummy():MutableList<DialogModel>{
        val devices: MutableList<DialogModel> = mutableListOf()
        devices.add(DialogModel("34 TR 1881"))
        devices.add(DialogModel("34 TR 1923"))
        devices.add(DialogModel("34 AA 2020"))
        return devices
    }

    fun getFirstSelection():MutableList<DialogModel>{
        val firstSelection: MutableList<DialogModel> = mutableListOf()
        firstSelection.add(DialogModel("Cihazım İle ilgili iletişim problemi yaşamaktayım", PROBLEM_WITH_DEVICE))
        firstSelection.add(DialogModel("Diğer Problem 1", CANT_LOGIN))
        firstSelection.add(DialogModel("Diğer Problem 2", PASSWORD))
        firstSelection.add(DialogModel("Diğer Problem 3", PASSWORD))
        return firstSelection
    }

    fun getYesNo(): MutableList<DialogModel>{
        val yesNo: MutableList<DialogModel> = mutableListOf()
        yesNo.add(DialogModel("Evet", ACTION_YES))
        yesNo.add(DialogModel("Hayır", ACTION_NO))

        return yesNo

    }
}