package com.hannah.slidinggame

import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.RectF


class Btn(res: Resources, label: Char, size: Int, x: Float, y: Float) {
    private val unpressedImage: Bitmap = Bitmap.createScaledBitmap(
        BitmapFactory.decodeResource(
            res,
            R.drawable.unpressed_button,
            null
        ), size, size, true
    ) // 縮放圖片與按鈕大小相同
    private val pressedImage: Bitmap = Bitmap.createScaledBitmap(
        BitmapFactory.decodeResource(
            res,
            R.drawable.pressed_button,
            null
        ), size, size, true
    )

    private var char: Char = label // 辨別 button 的標誌
    private var pressed: Boolean = false
    private var bounds: RectF = RectF(x, y, x + size, y + size) // button 位置

    fun drawBtn(c: Canvas) {
        if (pressed) {
            c.drawBitmap(pressedImage, bounds.left, bounds.top, null)

        } else {
            c.drawBitmap(unpressedImage, bounds.left, bounds.top, null)
        }
    }

    /*
        1. RectF check if the btn is pressed,
        2. pressed methods,
        3. onTouchEvent( in MyView class)
     */
    // 1. RectF check if the btn is pressed,
    fun contains(x: Float, y: Float): Boolean {
        return bounds.contains(x, y)
    }

    //  2. pressed methods,
    fun press() {
        pressed = true
    }

    fun unPress() {
        pressed = false
    }

    fun getX(): Float {
        return bounds.left
    }

    fun getY(): Float {
        return bounds.top
    }

    fun getChar(): Char {
        return char
    }

    fun isColumnBtn(): Boolean{
//        return char == '1' || char == '2' || char == '3' || char == '4' || char == '5'
        return char == 'A' || char == 'B' || char == 'C' || char == 'D' || char == 'E'
    }

}