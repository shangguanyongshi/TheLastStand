package com.example.thelaststand.comp

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import com.example.thelaststand.ViewManger

class Enemy(x: Float,y: Float) {
    private var LocationX:Float= 0f
    private var LocationY:Float= 0f
    private var dieType=0             //确定死亡动画的类型
    private var isfrozen=false       //是否被冰冻
    private var frozenTime=20         //冰冻时间
    var dieFrameNumber=10              //死亡动画存在的时间
    var loadFrame=20                 //红点的加载时间
    var isAlive=true
    var loadOver=false
    init {
        setLocation(x,y)
    }

    private fun setLocation(x:Float, y:Float){
        LocationX=x
        LocationY=y
    }

    fun getX():Int{
        return LocationX.toInt()
    }
    fun getY():Int{
        return LocationY.toInt()
    }

    fun setFrozen(){
        isfrozen=true
    }
    fun isFrozen():Boolean{
        return isfrozen
    }

    fun setDieType(type:Int){           //设置死亡动画的类型
        dieType=type
    }
    fun drawSelf(canvas: Canvas,paint: Paint){//画自己
        if(!loadOver){
            loadFrame--
            paint.color = Color.WHITE
            paint.style = Paint.Style.FILL
            if(loadFrame>14){
                canvas.drawCircle(LocationX, LocationY, ViewManger.enemyRadius/2.toFloat(), paint)//画加载状态的白点
            }else if(loadFrame in 1..14) {
                canvas.drawCircle(LocationX, LocationY, ViewManger.enemyRadius.toFloat()/1.3f, paint)//画加载状态的白点
            }else{
                loadOver = true
            }

        }else {         //如果加载状态已经完成
            if (isAlive) {
                paint.color = Color.RED
                paint.style = Paint.Style.FILL
                canvas.drawCircle(LocationX, LocationY, ViewManger.enemyRadius.toFloat(), paint)//画内圈红色
                paint.color = Color.WHITE
                paint.style = Paint.Style.STROKE
                paint.strokeWidth = 3f
                canvas.drawCircle(LocationX, LocationY, ViewManger.enemyRadius.toFloat(), paint)//画外圈白色
                if (isfrozen) {
                    val drawX = LocationX - ViewManger.enemy_frozen_radius
                    val drawY = LocationY - ViewManger.enemy_frozen_radius
                    canvas.drawBitmap(ViewManger.enemy_frozen!!, drawX, drawY, paint)
                    frozenTime--
                    if (frozenTime == 0) {
                        isfrozen = false
                        frozenTime = 20
                    }
                }
            } else {
                when (dieType) {
                    1, 3 -> {          //死亡类型为1或3时是爆炸或护盾技能
                        if (dieFrameNumber >= 0) {//如果死亡动画帧还没放完，播放死亡动画帧
                            val drawX = LocationX - ViewManger.enemyRadius * 2      //根据死亡帧的大小计算偏移量
                            val drawY = LocationY - ViewManger.enemyRadius * 2
                            canvas.drawBitmap(ViewManger.enemy_die_boom!!, drawX, drawY, paint)
                            dieFrameNumber--
                        }
                    }
                    2 -> {           //冰冻死亡
                        if (dieFrameNumber >= 0) {//如果死亡动画帧还没放完，播放死亡动画帧
                            val drawX =
                                LocationX - ViewManger.enemyRadius * 3          //根据死亡帧的大小计算偏移量
                            val drawY = LocationY - ViewManger.enemyRadius * 3
                            canvas.drawBitmap(ViewManger.enemy_die_frozen!!, drawX, drawY, paint)
                            dieFrameNumber--
                        }
                    }
                    4 -> {          //电击死亡
                        if (dieFrameNumber >= 0) {//如果死亡动画帧还没放完，播放死亡动画帧
                            val drawX =
                                LocationX - ViewManger.enemyRadius * 3          //根据死亡帧的大小计算偏移量
                            val drawY = LocationY - ViewManger.enemyRadius * 3
                            canvas.drawBitmap(
                                ViewManger.enemy_lighting!![dieFrameNumber % 3]!!,
                                drawX,
                                drawY,
                                paint
                            )
                            dieFrameNumber--
                        }

                    }
                    5 -> {          //旋风死亡
                        dieFrameNumber = -1
                    }

                }
            }
        }
    }





    fun move(){   //向火箭方向移动
        if(isfrozen||!isAlive||!loadOver) {         //冰冻、死亡、加载状态都不移动
            return
        }
        if(ViewManger.rocketLocation[0].toFloat()==LocationX){//在一条线了什么也不做

        }else if(ViewManger.rocketLocation[0]<LocationX){
            if((LocationX-ViewManger.ENEMY_SPEED)<ViewManger.rectLeft) {
                LocationX = ViewManger.rectLeft.toFloat()
            }else{
                LocationX -= ViewManger.ENEMY_SPEED
            }
        } else{
            if((LocationX+ViewManger.ENEMY_SPEED)>ViewManger.rectRight) {
                LocationX = ViewManger.rectRight.toFloat()
            }else{
                LocationX += ViewManger.ENEMY_SPEED
            }
        }
        if(ViewManger.rocketLocation[1].toFloat()==LocationY){   //在一条线了什么也不做

        }else if(ViewManger.rocketLocation[1]<LocationY){
            if((LocationY-ViewManger.ENEMY_SPEED)<ViewManger.rectTop) {
                LocationY = ViewManger.rectTop.toFloat()
            }else{
                LocationY -= ViewManger.ENEMY_SPEED
            }
        }else{
            if((LocationY+ViewManger.ENEMY_SPEED)>ViewManger.rectBottom) {
                LocationY = ViewManger.rectBottom.toFloat()
            }else{
                LocationY += ViewManger.ENEMY_SPEED
            }

        }
    }

}