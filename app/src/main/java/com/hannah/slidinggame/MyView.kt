package com.hannah.slidinggame

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.view.MotionEvent
import android.view.View
import android.widget.Toast

class MyView(c: Context): View(c), TickListener {
    private val p = Paint()
    private var sideMargin = 0f
    private var verticalMargin = 0f
    private var gridLength = 0f
    private var mathDone = false
    private var w = 0f
    private var h = 0f
    private var buttons: ArrayList<Btn> = ArrayList()
    private var tokens: ArrayList<Token> = ArrayList()
    private lateinit var timer: Timer


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
            timer = Timer(Looper.getMainLooper())
            timer.register(this)
        }
        // 繪製出所有的線段
        drawGrid(canvas)
        // 繪製出所有的按鈕
        for (button in buttons) {
            button.drawBtn(canvas)
        }
        // 繪製出所有的 token
        for (token in tokens) {
            token.drawToken(canvas)
        }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_DOWN) {
            var btnToching = false
            // loop through button, check if a button is pressed
            for (btn in buttons) {
                if (btn.contains(event.x, event.y)) {
                    btnToching = true
                    btn.press()
                    // 製作出新 token
                    var token: Token = if (btn.isColumnBtn()) {
                        Token(resources, gridLength.toInt(), btn.getX(), btn.getY(),  ('A'.code-1).toChar(), btn.getChar())
                    }else {
                        Token(resources, gridLength.toInt(), btn.getX(), btn.getY(), btn.getChar(), '0')
                    }
                    tokens.add(token)
                    timer.register(token)

                    // 移動此 token 與所有鄰居
                    val neighbors : ArrayList<Token> = ArrayList()
                    neighbors.add(token)
                    if (btn.isColumnBtn()) {
                        moveVerticalNeighbors(btn, neighbors)

                    }else {
                        moveHorizontalNeighbors(btn, neighbors)
                    }
                    break
                }
            }
            if (!btnToching) {
                Toast.makeText(context, resources.getString(R.string.btn_notification), Toast.LENGTH_SHORT).show()
            }

        }else if (event.action == MotionEvent.ACTION_UP) {
            for (btn in buttons) {
                btn.unPress()
            }
        }
        invalidate()

        return true
    }

    private fun makeButtons() {
        val charArray = arrayOf('1', '2', '3', '4', '5') // button 橫向的標示 row
        val charArray2 = arrayOf('A', 'B', 'C', 'D', 'E') // button 直向的標示 column
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

    private fun moveVerticalNeighbors(btn: Btn, neighborList: ArrayList<Token>) {
        // 直向button char: 1, 2, 3, 4 ,5
        // 垂直移動 column (1, 2, 3, 4 ,5) 固定，row('A', 'B', 'C', 'D', 'E') 改變
        val rowLetters = arrayOf('A', 'B', 'C', 'D', 'E')
        for (i in rowLetters.indices) {
            val dog: Token? = findDog(rowLetters[i], btn.getChar())
            if (dog != null) {
                neighborList.add(dog)
            }else {
                break
            }
        }
        for (neighborToken in neighborList){
            neighborToken.setDestination(0f, gridLength)
            neighborToken.changeVelocity(0f, 5f)
            neighborToken.row = (neighborToken.row + 1)
        }
    }
    private fun moveHorizontalNeighbors(btn: Btn, neighborList: ArrayList<Token>) {
        // 橫向button char: A, B, C, D ,E
        // 水平移動 row('A', 'B', 'C', 'D', 'E') 固定， column (1, 2, 3, 4 ,5) 改變
        val columnLetters = arrayOf('1', '2', '3', '4', '5')
        for (i in columnLetters.indices) {
            val dog: Token? = findDog(btn.getChar(), columnLetters[i])
            if (dog != null) {
                neighborList.add(dog)
            }else {
                break
            }
        }
        for (neighborToken in neighborList){
            neighborToken.setDestination(gridLength, 0f)
            neighborToken.changeVelocity(5f, 0f)
            neighborToken.column = (neighborToken.column + 1)
        }
    }

    private fun findDog(row: Char, column: Char): Token? {
        for (token in tokens) {
            if (token.row == row && token.column == column) {
                return token
            }
        }
        return null
    }


    override fun tick() {
        invalidate()
    }
}