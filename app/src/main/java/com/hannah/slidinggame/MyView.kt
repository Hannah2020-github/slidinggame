package com.hannah.slidinggame

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.view.View

class MyView(context: Context): View(context) {
    private val p = Paint()
    private var sideMargin = 0f
    private var verticalMargin = 0f
    private var gridLength = 0f
    private var mathDone = false
    private var w = 0f
    private var h = 0f
    private var buttons: ArrayList<Btn> = ArrayList()


    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        if (!mathDone) {
            // 計算 sideMargin, verticalMargin, gridLength
            p.strokeWidth = width * 0.015f // 線條粗細
            sideMargin = width * 0.2f
            verticalMargin = (height - (width - 2 * sideMargin)) / 2
            gridLength = (width - 2 * sideMargin) / 5
            mathDone = true
            w = width.toFloat()
            h = height.toFloat()
            makeButtons()
        }
        // 繪製出所有的線段
        drawGrid(canvas)
        // 繪製出所有的按鈕
        for (button in buttons) {
            button.drawBtn(canvas)
        }
    }


    private fun makeButtons() {
        val charArray = arrayOf('1', '2', '3', '4', '5') // button 橫向的標示
        val charArray2 = arrayOf('A', 'B', 'C', 'D', 'E') // button 直向的標示
        for (i in 0..4) {
            buttons.add(Btn(resources, charArray[i], gridLength.toInt(), sideMargin + i * gridLength, verticalMargin - gridLength))
        }
        for (i in 0..4) {
            buttons.add(Btn(resources, charArray2[i], gridLength.toInt(), sideMargin - gridLength, verticalMargin + i * gridLength))
        }
    }

    private fun drawGrid(c: Canvas) {
//        // c.drawLine(sideMargin, verticalMargin, w - sideMargin, verticalMargin, p)
        // draw 6 horizontal lines
        for (i in 0..5) {
            c.drawLine(sideMargin, verticalMargin + i * gridLength, w - sideMargin, verticalMargin + i * gridLength, p)
        }
        // draw 6 vertical lines
        for(i in 0..5) {
            c.drawLine(sideMargin + i * gridLength, verticalMargin, sideMargin + i * gridLength, h - verticalMargin, p)
        }
    }
}