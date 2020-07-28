package com.example.thelaststand.comp

import android.graphics.Canvas
import android.graphics.Paint
import com.example.thelaststand.ViewManger
import java.util.ArrayList
import kotlin.math.sqrt

object EnemyManger {
    var loadEnemyList: MutableList<Enemy?> = ArrayList()             //产生中的敌人
    var enemyList: MutableList<Enemy?> = ArrayList()                //正常敌人的列表
    var dieEnemyList: MutableList<Enemy?> = ArrayList()             //已经死亡的敌人
    var clearEnemyList:MutableList<Enemy?> = ArrayList()            //死亡动画已经放完的敌人
    private var tempx:Float=0f              //暂存产生的x,y
    private var tempy:Float=0f
    private var lastTime=-1     //记录游戏的时间，控制是否产生敌人


    private fun generateOneEnemy(){
        tempx=Util.rand(ViewManger.rectLeft,ViewManger.rectRight).toFloat()
        tempy=Util.rand(ViewManger.rectTop,ViewManger.rectBottom).toFloat()
        val enemy=Enemy(tempx,tempy)
        loadEnemyList.add(enemy)
    }


    fun generateEnemy(){                //控制什么时候产生敌人

        when(ViewManger.duration){
//            3 -> {
//                for (i in 1..3) {
//                    generateOneEnemy()
//                }
//            }
//            4 ->{
//                for (i in 1..4) {
//                    generateOneEnemy()
//                }
//            }
//            5 -> {
//                for (i in 1..5) {
//                    generateOneEnemy()
//                }
//            }
//            15 -> {         //在指定时间出现指定类型的敌人
//                for (i in 1..sqrt(15.toDouble()).toInt()) {
//                    generateOneEnemy()
//                }
//            }
//            20 -> {         //在指定时间出现指定类型的敌人
//                for (i in 1..sqrt(5.toDouble()).toInt()) {
//                    generateOneEnemy()
//                }
//            }
//            25 -> {         //在指定时间出现指定类型的敌人
//                for (i in 1..sqrt(10.toDouble()).toInt()) {
//                    generateOneEnemy()
//                }
//            }
//            30 -> {         //在指定时间出现指定类型的敌人
//                for (i in 1..sqrt(15.toDouble()).toInt()) {
//                    generateOneEnemy()
//                }
//            }
//            40 -> {         //在指定时间出现指定类型的敌人
//                for (i in 1..sqrt(15.toDouble()).toInt()) {
//                    generateOneEnemy()
//                }
//            }
//            50 -> {         //在指定时间出现指定类型的敌人
//                for (i in 1..sqrt(15.toDouble()).toInt()) {
//                    generateOneEnemy()
//                }
//            }
            else ->{
                if(enemyNumber()<50) {
                    if (lastTime != ViewManger.duration) {
                        lastTime = ViewManger.duration
                        if (lastTime < 2) {
                        } else {
                            for (i in 1..20) {
                                generateOneEnemy()
                            }
                        }
                        if (lastTime > 60) {
                            for (i in 1..10) {//Util.rand(1,sqrt(lastTime.toDouble()).toInt())
                                generateOneEnemy()
                            }
                        }

                    }
                }

            }
        }
    }

    private fun enemyNumber():Int{
        return enemyList.size+ loadEnemyList.size
    }
    fun moveAllEnemy(){
        for(i in enemyList.indices){
            enemyList[i]!!.move()
        }
    }

    fun drawAllEnemy(canvas: Canvas,paint: Paint){
        for(i in loadEnemyList.indices){          //绘制加载状态的敌人
            if(loadEnemyList[i]!!.loadOver) {
                enemyList.add(loadEnemyList[i])
            }else{
                loadEnemyList[i]!!.drawSelf(canvas, paint)
            }
        }
        loadEnemyList.removeAll(enemyList)          //把加载完成的敌人加入enemyList

        for(i in enemyList.indices){
           enemyList[i]!!.drawSelf(canvas, paint)
        }
        for(i in dieEnemyList.indices){
            if(dieEnemyList[i]!!.dieFrameNumber<0){
                clearEnemyList.add(dieEnemyList[i])
            }else {
                dieEnemyList[i]!!.drawSelf(canvas, paint)
            }
        }
        dieEnemyList.removeAll(clearEnemyList)
        clearEnemyList.clear()
    }

    fun checkRocket():Boolean{
        for(i in enemyList.indices){
            val dX=ViewManger.rocketLocation[0] - enemyList[i]!!.getX()
            val dY=ViewManger.rocketLocation[1] - enemyList[i]!!.getY()
                if (sqrt((dX*dX+dY*dY).toDouble()) <= ViewManger.rocketRadius + ViewManger.enemyRadius) {    //如果火箭和某个红点碰撞
                    if(enemyList[i]!!.isFrozen()){      //如果敌人处于冰冻状态
                        enemyList[i]!!.isAlive=false
                        enemyList[i]!!.setDieType(2)
                        dieEnemyList.add(enemyList[i])
                    }else{                              //如果敌人没有处于冰冻状态，火箭死亡，游戏结束
                        return true
                    }
                }
        }
        enemyList.removeAll(dieEnemyList)
        return false
    }


    fun exitEnemy(){        //设置游戏重新开始时清空这些列表
        loadEnemyList.clear()
        enemyList.clear()
        dieEnemyList.clear()
        clearEnemyList.clear()
        lastTime=-1
    }
}