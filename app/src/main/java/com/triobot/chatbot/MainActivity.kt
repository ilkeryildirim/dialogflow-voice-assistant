package com.triobot.chatbot


import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView
import com.triobot.chatbot.ChatTypes.MESSAGE_OWNER
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.ending_layout.view.*
import kotlinx.android.synthetic.main.starting_layout.view.*


class MainActivity : AppCompatActivity(), ChatActivityContract.View {


    lateinit var chatActivityPresenter: ChatActivityPresenter
    lateinit var layoutUtil: LayoutInflateUtil

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        chatActivityPresenter = ChatActivityPresenter(this, this)
        layoutUtil = LayoutInflateUtil(this)
        initFirstDialog()
    }

    fun scrollDown() {
        scroll_chat.post {
            scroll_chat.fullScroll(ScrollView.FOCUS_DOWN)
        }

    }

    private fun initFirstDialog() {
        appendFirstDialog(StockDialogs.getFirstSelection()) {
            chatActivityPresenter.createAction(it.action)
        }
    }

    override fun appendTextMessage(message: String, owner: MESSAGE_OWNER) {
        val layout: FrameLayout = when (owner) {
            MESSAGE_OWNER.BOT -> layoutUtil.botTextLayout()
            MESSAGE_OWNER.USER -> layoutUtil.userTextLayout()
        }

        layout.animation = AnimationUtils.loadAnimation(this, R.anim.fade_in)
        layout.isFocusableInTouchMode = true
        val tv = layout.findViewById<TextView>(R.id.chatMsg)
        tv.text = message
        linear_chat.addView(layout)
        scrollDown()
    }


    override fun stillGoingSolution() {
        appendTextMessage(
            "Bu cihaz ile ilgili devam eden bir işlem bulunmaktadır. Başka bir cihaz seçmek ister misiniz?",
            MESSAGE_OWNER.BOT
        )
        appendDialog(StockDialogs.getYesNo()) {
            if (it.action == ChatTypes.ACTIONS.ACTION_YES) {
                chatActivityPresenter.problemWithDevices()
            } else {
                appendNeedHelpAgain()
            }

        }
    }

    override fun appendNeedHelpAgain() {
        appendTextMessage(
            "Başka bir konuda yardım almak istermisiniz?",
            MESSAGE_OWNER.BOT
        )
        appendDialog(StockDialogs.getYesNo()) {
            if (it.action == ChatTypes.ACTIONS.ACTION_YES) initFirstDialog() else appendEndText(
                "Tamam, hepsi bu kadar! Görüşmek üzere"
            )

            rvChoosing.visibility=View.GONE
        }
    }

    override fun appendEndText(text: String) {

        val layout: LinearLayout = layoutUtil.endingLayout()
        layout.isFocusableInTouchMode = true
        if (text.isNotEmpty()) layout.tvEnding.text = text
        linear_chat.addView(layout)
    }

    override fun appendFirstDialog(
        mutableList: MutableList<DialogModel>,
        action: (m: DialogModel) -> Unit
    ) {
        val layout: FrameLayout = layoutUtil.startingLayout()
        layout.rvStarting.visibility = View.VISIBLE
        layout.animation = AnimationUtils.loadAnimation(this, R.anim.fade_in)
        layout.rvStarting.layoutManager = LinearLayoutManager(this)
        layout.rvStarting.apply {
            adapter = StartingAdapter(this@MainActivity, mutableList) {
                appendChoosing(it.text)
                layout.animation = AnimationUtils.loadAnimation(this@MainActivity, R.anim.swipe_up)
                rvStarting.visibility = View.GONE
                action(it)


            }
        }
        linear_chat.addView(layout)
    }

    override fun appendDialog(
        mutableList: MutableList<DialogModel>,
        action: (m: DialogModel) -> Unit
    ) {
        rvChoosing.visibility = View.VISIBLE
        rvChoosing.animation = AnimationUtils.loadAnimation(this, R.anim.swipe_up)
        rvChoosing.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        rvChoosing.apply {
            adapter = SelectionAdapter(this@MainActivity, mutableList) {
                appendChoosing(it.text)
                rvChoosing.animation =
                    AnimationUtils.loadAnimation(this@MainActivity, R.anim.fade_out)
                visibility = View.INVISIBLE
                action(it)
            }
        }
    }

    private fun appendChoosing(text: String) {
        appendTextMessage("$text", MESSAGE_OWNER.USER)
        appendTextMessage(
            "'$text' cevabınız için gerekli kontrolleri sağlıyorum",
            MESSAGE_OWNER.BOT
        )
        scrollDown()
    }

}
