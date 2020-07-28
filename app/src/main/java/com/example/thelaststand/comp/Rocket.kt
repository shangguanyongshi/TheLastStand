package com.example.thelaststand.comp

import android.graphics.Canvas
import android.graphics.Matrix
import android.graphics.Paint
import android.util.Log
import com.example.thelaststand.ViewManger
import java.lang.Math.abs
import kotlin.math.*

class Rocket {
    private var LocationX:Int= ViewManger.rocketLocation[0]
    private var LocationY:Int= ViewManger.rocketLocation[1]
    private var drawFrame:Int=1       //用来确定绘制火箭的哪一帧
    private var isAlive=true


    fun returnLocation():IntArray{
        return intArrayOf(LocationX,LocationY)
    }
    fun move(){
        //获取传感器数据
        val x=(ViewManger.orientationValues[0].toFloat()*1.3).toInt()
        val y=(ViewManger.orientationValues[1].toFloat()*1.3).toInt()
        //根据传感器数据确定速度
        val speedX= if (kotlin.math.abs(x) > ViewManger.MAX_SPEED) (if (x > 0) ViewManger.MAX_SPEED else -ViewManger.MAX_SPEED) else x
        val speedY= if (kotlin.math.abs(y) > ViewManger.MAX_SPEED) (if (y > 0) ViewManger.MAX_SPEED else -ViewManger.MAX_SPEED) else y

        //控制x方向速度
        if(LocationX-speedX>=ViewManger.rectRight){
            LocationX=ViewManger.rectRight
        }else if(LocationX-speedX<=ViewManger.rectLeft){
            LocationX=ViewManger.rectLeft
        }else{
            LocationX -=speedX
        }

        //控制y方向速度
       if(LocationY-speedY>=ViewManger.rectBottom){
            LocationY =ViewManger.rectBottom
        }else if(LocationY-speedY<=ViewManger.rectTop){
            LocationY=ViewManger.rectTop
        }else{
            LocationY -=speedY
        }
    }
    fun drawSelf(canvas: Canvas?, paint:Paint?)
    {
        val tempPaint=paint
        val shiftx= ViewManger.player!![0]!!.width.toFloat().div(2)
        val shifty= ViewManger.player!![0]!!.height.toFloat().div(2)

        val realx=LocationX-shiftx
        val realy=LocationY-shifty

        val matrix = Matrix()

        matrix.postRotate(ViewManger.getRadius().toFloat(),shiftx,shifty)
        matrix.postTranslate(realx,realy)
        tempPaint?.strokeWidth=15f
        if(drawFrame%8!=0) {
            canvas?.drawBitmap(ViewManger.player!![0]!!, matrix, tempPaint)
            drawFrame++
        }else{
            canvas?.drawBitmap(ViewManger.player!![1]!!, matrix, tempPaint)
            drawFrame=1
        }
        if(SkillManger.shield_exit==1){ //如果有护盾
            val drawX= (LocationX - ViewManger.skill_shield!!.width/2).toFloat()
            val drawY= (LocationY - ViewManger.skill_shield!!.width/2).toFloat()
            canvas!!.drawBitmap(ViewManger.skill_shield!!,drawX,drawY,paint)
        }

    }

}