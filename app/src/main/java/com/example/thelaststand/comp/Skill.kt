package com.example.thelaststand.comp

import android.graphics.Canvas
import android.graphics.Matrix
import android.graphics.Paint
import android.graphics.Rect
import com.example.thelaststand.ViewManger
import kotlin.math.abs
import kotlin.math.pow
import kotlin.math.sqrt
import kotlin.math.tan

class Skill(x:Int,y: Int) {
    private var LocationX:Int=0
    private var LocationX2:Int=0
    private var LocationY:Int=0

    private var drawX:Float=0F
    private var drawX2:Float=0F
    private var drawY:Float=0F

    private var Skill_Type:Int=0
    private var Skill_State:Int=0

    private var speedX:Int=0
    private var speedY:Int=0

    private var boomFrame:Int=0
    private var drawIndex:Int=0

//    private var waveRect: Rect = Rect(0,0,ViewManger.skill_wave!!.width,ViewManger.skill_wave!!.height)
//    private var waveDegree:Float=0f



    init{
        Skill_State=0
        Skill_Type=Util.rand(1,5)
        setLocation(x,y);
        speedX=Util.rand(-1,1)
        speedY=Util.rand(-1,1)
    }


    fun move(){
        if(Skill_State== STATE_ICON) {
            if ((LocationX + speedX > ViewManger.rectRight-ViewManger.skill_icon_radius) || (LocationX + speedX < ViewManger.rectLeft+ViewManger.skill_icon_radius)) {
                speedX = -speedX
            }
            if ((LocationY + speedY > ViewManger.rectBottom-ViewManger.skill_icon_radius) || (LocationY + speedY < ViewManger.rectTop+ViewManger.skill_icon_radius)) {
                speedY = -speedY
            }
            LocationX += speedX
            LocationY += speedY
        }else{
            when(Skill_Type){           //在爆炸状态的技能，护盾，电击，漩涡，冲击波的移动
                TYPE_SHIELD -> {                 //护盾技能的移动
                    LocationX=ViewManger.rocketLocation[0]
                    LocationY=ViewManger.rocketLocation[1]
                }
                TYPE_LIGHTING -> {               //闪电技能的移动
                    if(LocationX < ViewManger.SCREEN_WIDTH || LocationX2 > 0) {
                        LocationX += 20
                        LocationX2 -= 20
                    }else{
                        Skill_State= STATE_DELETE
                    }
                }
                TYPE_WIND -> {
                    if ((LocationX + speedX > ViewManger.rectRight-ViewManger.skill_wind_radius/2) || (LocationX + speedX < ViewManger.rectLeft+ViewManger.skill_wind_radius/2)) {
                        speedX = -speedX
                    }
                    if ((LocationY + speedY > ViewManger.rectBottom-ViewManger.skill_wind_radius/2) || (LocationY + speedY < ViewManger.rectTop+ViewManger.skill_wind_radius/2)) {
                        speedY = -speedY
                    }
                    LocationX += speedX
                    LocationY += speedY
                }
//                TYPE_WAVE -> {
//                    if(boomFrame>10){
//                        LocationX += speedX
//                        LocationY += speedY
//                    }
//                }
            }
        }
    }


    fun checkCollision(x: Int,y: Int,radius:Int):Boolean {
        val dX=x-LocationX
        val dY=y-LocationY
        if (Skill_State == STATE_ICON) {            //对图标技能的区域检测
            if (sqrt((dX*dX+dY*dY).toDouble()) <= ViewManger.skill_icon_radius + radius) {
                    return true
            }
        }
        if(Skill_State == STATE_BOOM){              //对爆炸技能的区域检测
            when(Skill_Type){
                TYPE_BOOM -> {
                    if (sqrt((dX*dX+dY*dY).toDouble()) <= ViewManger.skill_boom_level+radius) {
                        return true
                    }
                }
                TYPE_FROZEN -> {
                    if (sqrt((dX*dX+dY*dY).toDouble()) <= ViewManger.skill_frozen_level+radius) {
                        return true
                    }
                }
                TYPE_SHIELD ->{
                    if (sqrt((dX*dX+dY*dY).toDouble()) <= ViewManger.skill_shield_radius+radius) {
                        return true
                    }
                }
                TYPE_LIGHTING ->{
                    if (abs(LocationX-x)<ViewManger.skill_lighting!![0]!!.width/2) {
                        if(abs(LocationY-y)<ViewManger.skill_lighting!![0]!!.height/2)
                            return true
                    }
                    if (abs(LocationX2-x)<ViewManger.skill_lighting!![0]!!.width/2) {
                        if(abs(LocationY-y)<ViewManger.skill_lighting!![0]!!.height/2)
                            return true
                    }
                }
                TYPE_WIND ->{
                    if (sqrt((dX*dX+dY*dY).toDouble()) <= ViewManger.skill_wind_radius+radius) {
                        return true
                    }
                }
            }

        }
        return false

    }

    fun shieldSkill(){          //产生护盾技能，为复活时添加护盾技能
        Skill_Type=TYPE_SHIELD
        Skill_State= STATE_BOOM
    }
    fun changeState(){          //在改变状态时播放音效
        Skill_State= STATE_BOOM
        when(Skill_Type){
            TYPE_BOOM -> {
                if(ViewManger.mPlayer.isPlaying) {
                    ViewManger.soundPool?.play(ViewManger.soundMap[2]!!, 1f, 1f, 0, 0, 1f)  //播放冰冻声音
                }
            }
            TYPE_FROZEN ->{
                if(ViewManger.mPlayer.isPlaying) {
                    ViewManger.soundPool?.play(ViewManger.soundMap[2]!!, 1f, 1f, 0, 0, 1f)//播放冰冻声音
                }
            }
            TYPE_LIGHTING ->{
                if(ViewManger.mPlayer.isPlaying) {
                     ViewManger.soundPool?.play(ViewManger.soundMap[3]!!,1f,1f,0,0,1f)//播放冰冻声音
                }

            }
            TYPE_WIND -> {
                speedX = Util.rand(10, 15) * (if (Util.rand(1, 2) % 2 == 0) -1 else 1)
                speedY = Util.rand(10, 15) * (if (Util.rand(1, 2) % 2 == 0) -1 else 1)
                if(ViewManger.mPlayer.isPlaying) {
                    ViewManger.soundPool?.play(ViewManger.soundMap[4]!!, 1f, 1f, 0, 0, 1f)//播放旋风声音
                }
            }
            //护盾没有设置声音
        }

    }

    fun drawSelf(canvas: Canvas,paint: Paint){              //画技能
        when(Skill_Type){

            TYPE_BOOM ->{               //画爆炸技能
                if(Skill_State== STATE_ICON){
                    drawX=(LocationX-ViewManger.skill_icon_radius).toFloat()
                    drawY=(LocationY-ViewManger.skill_icon_radius).toFloat()
                    canvas.drawBitmap(ViewManger.skill_boom_icon!!,drawX,drawY,paint)
                }else{
                    boomFrame++
                    when(boomFrame){  //前几帧图片由小变大
                        3 -> drawIndex++
                        5 -> drawIndex++
                        40 -> drawIndex++
                        41 -> {
                            drawIndex++
                            Skill_State= STATE_DELETE
                        }
                    }
                    drawX= (LocationX - ViewManger.skill_boom!![drawIndex]!!.width/2).toFloat()
                    drawY= (LocationY - ViewManger.skill_boom!![drawIndex]!!.width/2).toFloat()
                    canvas.drawBitmap(ViewManger.skill_boom!![drawIndex]!!,drawX,drawY,paint)
                }

            }

            TYPE_FROZEN ->{             //画冰冻技能
                if(Skill_State== STATE_ICON){
                    drawX=(LocationX-ViewManger.skill_icon_radius).toFloat()
                    drawY=(LocationY-ViewManger.skill_icon_radius).toFloat()
                    canvas.drawBitmap(ViewManger.skill_frozen_icon!!,drawX,drawY,paint)
                }else{
                    boomFrame++
                    when(boomFrame){
                        3 -> drawIndex++
                        5 -> drawIndex++
                        40 -> drawIndex++
                        41 -> {
                            drawIndex++
                            Skill_State= STATE_DELETE
                        }
                    }
                    drawX= (LocationX - ViewManger.skill_frozen!![drawIndex]!!.width/2).toFloat()
                    drawY= (LocationY - ViewManger.skill_frozen!![drawIndex]!!.width/2).toFloat()
                    canvas.drawBitmap(ViewManger.skill_frozen!![drawIndex]!!,drawX,drawY,paint)
                }
            }

            TYPE_SHIELD ->{            //画护盾技能
                if(Skill_State== STATE_ICON){
                    drawX=(LocationX-ViewManger.skill_icon_radius).toFloat()
                    drawY=(LocationY-ViewManger.skill_icon_radius).toFloat()
                    canvas.drawBitmap(ViewManger.skill_shield_icon!!,drawX,drawY,paint)
                }else{
                    boomFrame++                 //用该变量来记录进行了多少帧，通过和下面的值比较，确定消失的时间
                    if(boomFrame>=ViewManger.skill_shield_level){
                        SkillManger.shield_exit=0
                        Skill_State = STATE_DELETE
                    }else{
                        SkillManger.shield_exit=1
                    }

                }
            }

            TYPE_LIGHTING ->{               //画电击技能
                if(Skill_State== STATE_ICON){
                    drawX=(LocationX-ViewManger.skill_icon_radius).toFloat()
                    drawY=(LocationY-ViewManger.skill_icon_radius).toFloat()
                    canvas.drawBitmap(ViewManger.skill_lighting_icon!!,drawX,drawY,paint)
                }else{
                    boomFrame++
                    drawIndex=boomFrame%3  //确定画第几帧
                    //x和x2确定两个光柱的x坐标，y确定y坐标
                    drawX= (LocationX - ViewManger.skill_lighting!![drawIndex]!!.width/2).toFloat()
                    drawX2= (LocationX2 - ViewManger.skill_lighting!![drawIndex]!!.width/2).toFloat()
                    drawY= (LocationY - ViewManger.skill_lighting!![drawIndex]!!.height/2).toFloat()
                    //分别画两个光柱
                    canvas.drawBitmap(ViewManger.skill_lighting!![drawIndex]!!,drawX,drawY,paint)
                    canvas.drawBitmap(ViewManger.skill_lighting!![drawIndex]!!,drawX2,drawY,paint)
                }
            }
            TYPE_WIND ->{            //画旋风技能
                if(Skill_State== STATE_ICON){
                    drawX=(LocationX-ViewManger.skill_icon_radius).toFloat()
                    drawY=(LocationY-ViewManger.skill_icon_radius).toFloat()
                    canvas.drawBitmap(ViewManger.skill_wind_icon!!,drawX,drawY,paint)
                }else{
                    boomFrame++                 //用该变量来记录进行了多少帧，通过和下面的值比较，确定消失的时间
                    if(boomFrame>=ViewManger.skill_wind_level){
                        Skill_State = STATE_DELETE
                    }else{
                        drawIndex=boomFrame%4
                        drawX= (LocationX - ViewManger.skill_wind!![drawIndex]!!.width/2).toFloat()
                        drawY= (LocationY - ViewManger.skill_wind!![drawIndex]!!.width/2).toFloat()
                        canvas.drawBitmap(ViewManger.skill_wind!![drawIndex]!!,drawX,drawY,paint)
                    }

                }
            }
//            TYPE_WAVE ->{
//                if(Skill_State== STATE_ICON){
//                    drawX=(LocationX-ViewManger.skill_icon_radius).toFloat()
//                    drawY=(LocationY-ViewManger.skill_icon_radius).toFloat()
//                    canvas.drawBitmap(ViewManger.skill_wave_icon!!,drawX,drawY,paint)
//                }else{
//                    boomFrame++
//                    if(boomFrame==10){       //在触发冲击波技能后，前几帧准备发射
//                        waveDegree=ViewManger.getRadius().toFloat()     //准备发射时候火箭的旋转角度
//                        //准备发射时候，根据火箭的朝向确定冲击波x和y的速度
//                        if(waveDegree>=0){
//                            if(waveDegree<=90){
//                                speedX=-15
//                                speedY=-15*tan(waveDegree).toInt()
//                            }else{
//                                speedX=15
//                                speedY=-15*tan(180-waveDegree).toInt()
//                            }
//
//                        }else{
//                            if(waveDegree>=-90){
//                                speedX=-15
//                                speedY=-15*tan(waveDegree).toInt()   //角度为负，tan值为负
//                            }else{
//                                speedX=15
//                                speedY=15*tan(waveDegree+180).toInt()
//                            }
//                        }
//                        //准备发射时候确定发射位置
//                        LocationX=ViewManger.rocketLocation[0]
//                        LocationY=ViewManger.rocketLocation[1]
//                    }
//                    if(boomFrame>10){
//                        val matrix = Matrix()
//                        matrix.postRotate(waveDegree,ViewManger.skill_wave!!.width/2.toFloat(),ViewManger.skill_wave!!.height/2.toFloat())
//                        matrix.postTranslate(LocationX-ViewManger.skill_wave!!.width/2.toFloat(),LocationY-ViewManger.skill_wave!!.height/2.toFloat())
//                        canvas.drawBitmap(ViewManger.skill_wave!!, matrix, paint)
//                        waveRect.offset(LocationX,LocationY)
//                        canvas.drawRect(waveRect,paint)
//
//
//                        //超出边界时删除
//                        if(LocationX<0||LocationX>ViewManger.SCREEN_WIDTH){
//                            Skill_State= STATE_DELETE
//                        }
//                        if(LocationY<0||LocationY>ViewManger.SCREEN_HEIGHT){
//                            Skill_State= STATE_DELETE
//                        }
//                    }
//                }
//
//            }



        }
    }


    fun state():Int{
        return Skill_State
    }
    fun type():Int{
        return Skill_Type
    }
    private fun setLocation(x:Int, y:Int){
        LocationX=x
        LocationX2=x
        LocationY=y
    }


    companion object{
        const val STATE_ICON=0
        const val STATE_BOOM=-1
        const val STATE_DELETE=-2

        const val TYPE_BOOM=1
        const val TYPE_FROZEN=2
        const val TYPE_SHIELD=3
        const val TYPE_LIGHTING=4
        const val TYPE_WIND=5
        const val TYPE_WAVE=6


    }
}