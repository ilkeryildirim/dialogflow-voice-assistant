package com.fevziomurtekin.chatbot

import android.app.Activity
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.speech.RecognizerIntent
import android.util.Log
import android.view.KeyEvent
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*
import android.view.LayoutInflater
import com.google.api.gax.core.FixedCredentialsProvider
import com.google.auth.oauth2.ServiceAccountCredentials
import com.google.auth.oauth2.GoogleCredentials
import com.google.cloud.dialogflow.v2.*
import java.util.*
import android.widget.*
import android.widget.Toast
import android.content.ActivityNotFoundException
import android.content.Intent
import android.speech.RecognizerResultsIntent


const val USER=0
const val BOT=1
const val SPEECH_INPUT=2

class MainActivity : AppCompatActivity() {

    private val uuid = UUID.randomUUID().toString()

    private var client: SessionsClient? = null
    private var session: SessionName? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val scrollview=findViewById<ScrollView>(R.id.scroll_chat)
        scrollview.post{
            scrollview.fullScroll(ScrollView.FOCUS_DOWN)
        }

        val queryEditText = findViewById<EditText>(R.id.queryEditText);
        queryEditText.setOnKeyListener { view, keyCode, event ->
            if (event.action === KeyEvent.ACTION_DOWN) {
                when (keyCode) {
                    KeyEvent.KEYCODE_DPAD_CENTER, KeyEvent.KEYCODE_ENTER -> {
                        sendMessage(send)
                        true
                    }
                    else -> {
                    }
                }
            }
            false
        }

        send.setOnClickListener(this::sendMessage)

        microphone.setOnClickListener(this::sendMicrophoneMessage)

        initBot()

    }

    private fun initBot() {
        try {
            val stream = resources.openRawResource(R.raw.chatbot)
            val credentials = GoogleCredentials.fromStream(stream)
            val projectId = (credentials as ServiceAccountCredentials).projectId

            val settingsBuilder = SessionsSettings.newBuilder()
            val sessionsSettings =
                settingsBuilder.setCredentialsProvider(FixedCredentialsProvider.create(credentials)).build()
            client = SessionsClient.create(sessionsSettings)
            session = SessionName.of(projectId, uuid)
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }


    private fun sendMessage(view: View) {
        val msg = queryEditText.text.toString()
        if (msg.trim { it <= ' ' }.isEmpty()) {
            Toast.makeText(this@MainActivity, "Please enter your query!", Toast.LENGTH_LONG).show()
        } else {
            appendText(msg, USER)
            queryEditText.setText("")

            // Java V2
            val queryInput =
                QueryInput.newBuilder().setText(TextInput.newBuilder().setText(msg).setLanguageCode("tr")).build()
            RequestTask(this@MainActivity, session!!, client!!, queryInput).execute()
        }
    }

    private fun sendMicrophoneMessage(view:View){
        val intent: Intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
        intent.putExtra(
            RecognizerIntent.EXTRA_LANGUAGE_MODEL,
            RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
        )
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault())
        intent.putExtra(
            RecognizerIntent.EXTRA_PROMPT,
            getString(R.string.speech_prompt)
        )
        try {
            startActivityForResult(intent, SPEECH_INPUT)
        } catch (a: ActivityNotFoundException) {
            Toast.makeText(
                applicationContext,
                getString(R.string.speech_not_supported),
                Toast.LENGTH_SHORT
            ).show()
        }

    }

    private fun appendText(message: String, type: Int) {
        val layout: FrameLayout
        when (type) {
            USER -> layout = appendUserText()
            BOT -> layout = appendBotText()
            else -> layout = appendBotText()
        }
        layout.isFocusableInTouchMode = true
        linear_chat.addView(layout) // move focus to text view to automatically make it scroll up if softfocus
        val tv = layout.findViewById<TextView>(R.id.chatMsg)
        tv.setText(message)
        Util.hideKeyboard(this)
        layout.requestFocus()
        queryEditText.requestFocus() // change focus back to edit text to continue typing
    }


    fun appendUserText(): FrameLayout {
        val inflater = LayoutInflater.from(this@MainActivity)
        return inflater.inflate(R.layout.user_message, null) as FrameLayout
    }

    fun appendBotText(): FrameLayout {
        val inflater = LayoutInflater.from(this@MainActivity)
        return inflater.inflate(R.layout.bot_message, null) as FrameLayout
    }

    fun onResult(response: DetectIntentResponse?) {
        try {
            if (response != null) {
                // process aiResponse here
                val botReply = response.queryResult.fulfillmentMessagesList[0].text.textList[0].toString()
                appendText(botReply, BOT)
            } else {
                appendText(getString(R.string.anlasilmadi), BOT)
            }
        }catch (e:Exception){
            appendText(getString(R.string.anlasilmadi), BOT)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when(requestCode){
            SPEECH_INPUT->{
                if(resultCode== Activity.RESULT_OK
                    && data!=null){
                    val result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
                    appendText(result[0], USER)
                }
            }
        }
    }
}
