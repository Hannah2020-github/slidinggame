package com.hannah.slidinggame

import android.os.Handler
import android.os.Looper
import android.os.Message

class Timer(looper: Looper) : Handler(looper) {
    private val listeners: ArrayList<TickListener> = ArrayList()
    private var paused = false
    private val delay: Long = 10
    init {
        sendMessageDelayed(Message.obtain(), 0)
    }

    override fun handleMessage(msg: Message) {
        if (!paused) {
            notifyListener()
        }
        sendMessageDelayed(Message.obtain(), delay)
    }

    fun register(t: TickListener) {
        listeners.add(t)
    }

    fun unRegister(t: TickListener) {
        listeners.remove(t)
    }

    fun clearAll() {
        listeners.clear()
    }

    fun setPause() {
        paused = true
    }

    fun unPasused() {
        paused = false
    }

    fun notifyListener() {
        for (listener in listeners) {
            listener.tick()
        }
    }


}