package com.hannah.slidinggame.ui

import android.os.Handler
import android.os.Looper
import android.os.Message

class Timer(looper: Looper) : Handler(looper) {
    private var listeners: ArrayList<TickListener> = ArrayList()

    init {
        sendMessageDelayed(Message.obtain(), 0)
    }
    override fun handleMessage(msg: Message) {
        notifyListener()
        sendMessageDelayed(Message.obtain(), 10)
    }

    fun register(t: TickListener) {
        listeners.add(t)

    }

    fun unRegister(t: TickListener) {
        listeners.remove(t)
    }

    fun notifyListener() {
        for (listener in listeners) {
            listener.tick()
        }
    }
}