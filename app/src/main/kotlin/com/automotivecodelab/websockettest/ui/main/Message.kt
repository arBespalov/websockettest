package com.automotivecodelab.websockettest.ui.main

import com.automotivecodelab.websockettest.R

sealed class Message(
    val message: String,
    val layoutId: Int
) {
    class IncomingMessage(message: String) : Message(message, R.layout.incoming_message_layout)
    class OutgoingMessage(message: String) : Message(message, R.layout.outgoing_message_layout)
    class ServiceMessage(message: String) : Message(message, R.layout.service_message_layout)
}
