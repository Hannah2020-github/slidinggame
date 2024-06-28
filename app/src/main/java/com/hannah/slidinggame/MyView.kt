package com.hannah.slidinggame

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.view.View

class MyView(context: Context): View(context) {
    private val p = Paint() // 線條粗細
    private var sideMargin = 0f
    private var verticalMargin = 0f
    private var gridLength = 0f
    private var mathDone = false
    private var w = 0f
    private var h = 0f


    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        if (!mathDone) {
            // 計算 sideMargin, verticalMargin, gridLength
            p.strokeWidth = width * 0.015f
            sideMargin = width * 0.2f
            verticalMargin = (height - width + 2 * sideMargin) / 2
            gridLength = (width -2 * sideMargin) / 5
            mathDone = true
            w = width.toFloat()
            h = height.toFloat()
        }
        drawGrid(canvas)
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