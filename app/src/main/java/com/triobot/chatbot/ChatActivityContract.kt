package com.triobot.chatbot

interface ChatActivityContract {


    interface View {
        fun appendNeedHelpAgain()
        fun stillGoingSolution()
        fun appendTextMessage(message: String, owner: ChatTypes.MESSAGE_OWNER)
        fun appendEndText(text: String)
        fun appendFirstDialog(
            mutableList: MutableList<DialogModel>,
            action: (m: DialogModel) -> Unit
        )

        fun appendDialog(
            mutableList: MutableList<DialogModel>,
            action: (m: DialogModel) -> Unit
        )

    }

    interface Presenter {

        fun problemWithDevices()
    }


}