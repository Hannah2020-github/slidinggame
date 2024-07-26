package com.hannah.slidinggame.ui

import android.app.Activity
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.media.MediaPlayer
import android.os.Looper
import android.view.MotionEvent
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.AppCompatImageView
import com.hannah.slidinggame.R
import com.hannah.slidinggame.SettingsActivity
import com.hannah.slidinggame.logic.GameBoard
import com.hannah.slidinggame.logic.GameMode
import com.hannah.slidinggame.logic.Player
import java.io.Serializable

class MyView(c: Context): AppCompatImageView(c), TickListener {
    private val p = Paint()
    private val p2 = Paint()
    private var sideMargin = 0f
    private var verticalMargin = 0f
    private var gridLength = 0f
    private var mathDone = false
    private var w = 0f
    private var h = 0f
    private var buttons: ArrayList<Btn> = ArrayList()
    private var tokens: ArrayList<Token> = ArrayList()
    private var timer: Timer = Timer(Looper.getMainLooper())
    private var engine = GameBoard()
    private var player1WinCount = 0
    private var player2WinCount = 0
    private lateinit var mode: Serializable
    private var soundtrack: MediaPlayer? = null

    init {
        p2.textSize = 60f
        p2.color = resources.getColor(R.color.black)
        setImageResource(R.drawable.background)
        scaleType = ScaleType.FIT_XY
        soundtrack = MediaPlayer.create(context, R.raw.example)
        soundtrack!!.isLooping = true
    }

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
            timer.register(this)
        }
        // 繪製出所有的線段
        drawGrid(canvas)

        // 繪製出所有的按鈕
        for (button in buttons) {
            button.drawBtn(canvas)
        }

        // 檢查有無掉出螢幕的 token
        for (token in tokens) {
            if (!token.isVisible(h)) {
                // change velocity
                token.changeVelocity(0f, 0f)
                // timer unregister, tokens arraylist remove token
                timer.unRegister(token)
                tokens.remove(token)
                break
            }
        }

        // 繪製出所有的 token
        for (token in tokens) {
            token.drawToken(canvas)
        }

        // 寫出勝利次數
        canvas.drawText("${resources.getString(R.string.play_one_win_counts)} ${player1WinCount}", 50f, 120f, p2)
        canvas.drawText("${resources.getString(R.string.play_two_win_counts)} ${player2WinCount}", 50f, 200f, p2)

        // token 靜止時，判斷贏家
        if (!Token.isAnyTokenMoving()) {
            val winner = engine.checkWinners()
            if (winner != Player.BLANK) {
                // pause the timer
                timer.pause()
                // alert diolog
                val ab = AlertDialog.Builder(context)
                ab.setTitle(R.string.gameOverTitle)

                when (winner) {
                    Player.TIE -> {
                        ab.setMessage(R.string.tie_game)
                    }
                    Player.X -> {
                        ab.setMessage(R.string.player_one_wins)
                    }
                    else -> {
                        ab.setMessage(R.string.player_two_wins)
                    }
                }
                ab.setCancelable(false)
                ab.setPositiveButton(R.string.yes) { _, _ ->
                    restartGame()
                }
                ab.setNeutralButton(R.string.no) {_, _, ->
                 (context as Activity).finish()
                }
                ab.create().show()

                if (winner == Player.X) {
                    player1WinCount++
                }else if (winner == Player.O) {
                    player2WinCount++
                }

            }
            // AI 下棋的時間點，是在 token 靜止 及 game winner is blank 及 mode is one player 及 current player is AI
            // 讓 AI 後攻(Token.player % 2 != 0)
            else if (mode == GameMode.ONE_PLAYER && Token.player % 2 != 0 ) {
                val choice = engine.aiMove()
                val button = buttons[choice]
                engine.submittMove(button.getChar())

                // 0, 1, 2, 3, 4, 橫向 buttons, '1', '2', '3', '4', '5'
                // 5, 6, 7, 8, 9, 直向 buttons, 'A', 'B', 'C', 'D', 'E'
                val token = if (choice < 5) {
                    Token(resources, gridLength.toInt(), button.getX(), button.getY(), ('A'.code - 1).toChar(), button.getChar())
                }else {
                    Token(resources, gridLength.toInt(), button.getX(), button.getY(), button.getChar(), '0')
                }

                tokens.add(token)
                timer.register(token)
                // 移動此 token 與所有鄰居
                val neighbors : ArrayList<Token> = ArrayList()
                neighbors.add(token)
                if (button.isColumnBtn()) {
                    moveVerticalNeighbors(button, neighbors)
                }else {
                    moveHorizontalNeighbors(button, neighbors)
                }

            }
        }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        // 如果 token 正在移動，則不新增任何 token
        if (Token.isAnyTokenMoving()) {
            for (btn in buttons) {
                btn.unPress()
            }
            return true
        }
        if (event.action == MotionEvent.ACTION_DOWN) {
            var btnToching = false
            // loop through button, check if a button is pressed
            for (btn in buttons) {
                if (btn.contains(event.x, event.y)) {
                    engine.submittMove(btn.getChar())
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

    private fun restartGame() {
        Token.player = 0
        timer.clearAll()
        timer.unPause()
        tokens.clear()
        buttons.clear()
        mathDone = false
        engine = GameBoard()
        invalidate()
    }

    override fun tick() {
        invalidate()
    }

    fun gotBackground() {
        timer.pause()
        if (soundtrack!!.isPlaying) {
            soundtrack!!.pause()
        }
    }

    fun gotForeground() {
        timer.unPause()
        if (SettingsActivity.isSoundOn(context)) {
            soundtrack!!.start()
        }
    }

    fun clearBeforeShunDown() {
        Token.player = 0
        soundtrack!!.release()
        soundtrack = null
    }

    fun setGameMode(m: Serializable) {
        mode = m
    }

}