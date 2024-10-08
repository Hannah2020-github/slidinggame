package com.hannah.slidinggame.ui

import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.PointF
import android.graphics.RectF
import com.hannah.slidinggame.R
import com.hannah.slidinggame.logic.Player

/*
    邏輯流程：
    1. token 繪製於 grid 裡，跟 btn 性質很類似，
    因此，constructor 屬性也會很相似
    2. 利用兩個 char，就可得知 token 的位置：row、column
    3. RectF 物件，保存 token 座標
    4. token image
    5. 玩家輪流替換：player
    6. 繪製 token

    7. 移動 token(move)，token 順著 x 或 y 軸滑動，且在表格內
 */

class Token(res: Resources, size: Int, x: Float, y: Float, var row: Char, var column: Char, player: Player, var myView: MyView):
    TickListener {

    private val bounds: RectF = RectF(x, y, x + size, y + size) // token 位置
    private var dog: Bitmap
    private val velocity = PointF(0f, 0f) // move token
    private val destination = PointF(x, y) // 指定 token 移至特定位置
    private var falling: Boolean = false

    init {
        dog = if (player == Player.X) {
            Bitmap.createScaledBitmap(BitmapFactory.decodeResource(res, R.drawable.doggy3), size, size, true)
        }else {
            Bitmap.createScaledBitmap(BitmapFactory.decodeResource(res, R.drawable.doggy4), size, size, true)
        }

//        println("Token row = $row")
//        println("Token cloumn = $cloumn")
    }

    fun drawToken(c: Canvas) {
        c.drawBitmap(dog, bounds.left, bounds.top, null)

    }

    fun isVisible(h: Float): Boolean {
        return bounds.top <= h
    }
    private fun move() {
        if (falling) {
            velocity.y *= 1.25f

            // token 橫向移動，且與格子的距離很接近(<=5)
        }else if (velocity.x != 0f && destination.x - bounds.left <= 5) {
            // 暫停移動  token
            changeVelocity(0f, 0f)
            myView.removeMoves()

            // 往下掉的 token
            if (column > '5') {
                changeVelocity(0f, 5f)
                myView.addMovers()
                falling = true
            }
        }else if (velocity.y != 0f && destination.y -bounds.top <= 5) {
            changeVelocity(0f, 0f)
            myView.removeMoves()

            // 往下掉的 token
            if (row > 'E') {
                changeVelocity(0f, 5f)
                myView.addMovers()
                falling = true
            }
        }
        bounds.left += velocity.x
        bounds.top += velocity.y
    }

    fun changeVelocity(x: Float, y: Float) {
        velocity.x = x
        velocity.y = y
    }

    fun setDestination(x: Float, y: Float) {
        destination.offset(x, y)
//        destination.x += x
//        destination.y += y
    }

    override fun tick() {
        move()
    }
}