package com.triobot.chatbot

import android.app.Activity
import android.os.Handler
import com.triobot.chatbot.ChatTypes.ACTIONS
import com.triobot.chatbot.ChatTypes.ACTIONS.*



class ChatActivityPresenter(var view: ChatActivityContract.View, var context: Activity) : ChatActivityContract.Presenter {
    var controlledDevice = arrayListOf<String>()

    fun createAction(action: ACTIONS) {
        when (action) {
            PROBLEM_WITH_DEVICE -> {
                problemWithDevices()
            }
            CANT_LOGIN -> {
            }
            PASSWORD -> {
            }
            NEW_PRODUCT -> {
            }
            ACTION_YES -> TODO()
            ACTION_NO -> TODO()
        }
    }


    override fun problemWithDevices() {
        view.appendTextMessage("Hangi cihaz ile ilgili sorun yaşamaktasınız?", ChatTypes.MESSAGE_OWNER.BOT)
        view.appendDialog(StockDialogs.getDevicesDummy()) {
            //needs api response
            var isControlled = false
            val handler = Handler()
            handler.postDelayed({

                controlledDevice.forEach { device ->
                    if (device == it.text) {
                        isControlled = true
                    }
                }

                if (!isControlled) {
                    view.appendTextMessage(
                        "Cihazınızdan son güncel veri 5 saat önce alınmış, uzaktan müdahale sağlandı. 10 dakika sonra tekrar kontrol edebilir misiniz?",
                        ChatTypes.MESSAGE_OWNER.BOT
                    )
                    controlledDevice.add(it.text)
                    view.appendEndText("")
                    view.appendNeedHelpAgain()

                } else {
                    view.stillGoingSolution()
                }
            }, 2000)


        }
    }



}