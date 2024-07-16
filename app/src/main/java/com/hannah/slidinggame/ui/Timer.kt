package com.hannah.slidinggame.ui

import android.os.Handler
import android.os.Looper
import android.os.Message

class Timer(looper: Looper) : Handler(looper) {
    private var listeners: ArrayList<TickListener> = ArrayList()
    private var paused = false

    init {
        sendMessageDelayed(Message.obtain(), 0)
    }
    override fun handleMessage(msg: Message) {
        if (!paused) {
            notifyListener()
        }
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

    fun clearAll() {
        listeners.clear()
    }

    fun pause() {
        paused = true
    }

    fun unPause() {
        paused = false
    }
}