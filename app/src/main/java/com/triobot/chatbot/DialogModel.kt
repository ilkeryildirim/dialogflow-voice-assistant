package com.triobot.chatbot

data class DialogModel(var text: String,var action: ChatTypes.ACTIONS=ChatTypes.ACTIONS.NO_ACTION)