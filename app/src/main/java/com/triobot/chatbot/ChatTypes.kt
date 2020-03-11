package com.triobot.chatbot


object ChatTypes {
    enum class MESSAGE_OWNER{
        BOT,
        USER,
    }
    enum class ITEM_TYPE{
        TEXT_MESSAGE,
        SELECTION,
    }
    enum class ACTIONS{
        NO_ACTION,
        ACTION_YES,
        ACTION_NO,
        PROBLEM_WITH_DEVICE,
        CANT_LOGIN,
        PASSWORD,
        NEW_PRODUCT
    }

}
