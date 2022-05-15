package com.automotivecodelab.websockettest.ui.main

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.automotivecodelab.websockettest.BuildConfig
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.websocket.*
import io.ktor.websocket.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {
    private val _messages = MutableStateFlow(emptyList<Message>())
    val messages = _messages.asStateFlow()

    private var session: DefaultClientWebSocketSession? = null

    fun sendMessage(message: String) {
        if (message.isEmpty()) return
        viewModelScope.launch {
            _messages.value = _messages.value.plus(Message.OutgoingMessage(message))
            session?.send(message)
        }
    }

    fun connect() {
        _messages.value = _messages.value.plus(Message.ServiceMessage("connected"))
        viewModelScope.launch {
            val client = HttpClient(CIO) {
                install(WebSockets)
            }
            //"wss://socketsbay.com/wss/v2/2/demo/"
            session = client.webSocketSession(urlString = BuildConfig.SERVER_URL)
            session?.incoming?.consumeAsFlow()
                ?.catch { t ->
                    _messages.value = _messages.value.plus(
                        Message.ServiceMessage(t.message.toString()))
                }
                ?.collect {
                    val incomingMessage = try {
                        Message.IncomingMessage((it as Frame.Text).readText())
                    } catch (t: Throwable) {
                        Message.ServiceMessage(t.message ?: "err")
                    }
                    Log.d("happy", incomingMessage.message)
                    _messages.value = _messages.value.plus(incomingMessage)
                }
        }
    }

    fun disconnect() {
        _messages.value = _messages.value.plus(Message.ServiceMessage("disconnected"))
        viewModelScope.launch {
            session?.close()
            session = null
        }
    }
}