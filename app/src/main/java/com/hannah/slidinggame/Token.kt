package com.hannah.slidinggame

import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.PointF
import android.graphics.RectF

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

class Token(res: Resources, size: Int, x: Float, y: Float, row: Char, cloumn: Char): TickListener {
    companion object {
        var player = 0 // 0 代表玩家一號，1 代表玩家二號
    }
    private val bounds: RectF = RectF(x, y, x + size, y + size) // token 位置
    private var dog: Bitmap
    private val velocity = PointF(0f, 0f)

    init {
        if (player % 2 == 0) {
            dog = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(res, R.drawable.doggy3), size, size, true)
        }else {
            dog = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(res, R.drawable.doggy4), size, size, true)
        }
        player++ // 玩家輪替轉換
//        println("Token row = $row")
//        println("Token cloumn = $cloumn")
    }

    fun drawToken(c: Canvas) {
        c.drawBitmap(dog, bounds.left, bounds.top, null)
    }

    fun move() {
        bounds.left += velocity.x
        bounds.top += velocity.y
    }

    fun changeVelocity(x: Float, y: Float) {
        velocity.x = x
        velocity.y = y
    }

    override fun tick() {
        move()
    }
}