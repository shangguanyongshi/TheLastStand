package com.example.thelaststand.comp

import android.graphics.Canvas
import android.graphics.Paint
import android.util.Log
import android.view.SurfaceHolder
import com.example.thelaststand.ViewManger
import java.util.ArrayList

object SkillManger {
    private val icon_Skill_List: MutableList<Skill?> = ArrayList()
    private val boom_Skill_List: MutableList<Skill?> = ArrayList()
    private val clear_Skill_List: MutableList<Skill?> = ArrayList()
    var shield_exit:Int=0

    private var tempx=0
    private var tempy=0

    private fun generateOneSkill(){                     //产生某个技能
        tempx=Util.rand(ViewManger.rectLeft+ViewManger.skill_icon_radius,ViewManger.rectRight-ViewManger.skill_icon_radius)
        tempy=Util.rand(ViewManger.rectTop+ViewManger.skill_icon_radius,ViewManger.rectBottom-ViewManger.skill_icon_radius)
        val enemy=Skill(tempx,tempy)
        icon_Skill_List.add(enemy)
    }
    fun generateSkill(){                    //产生几个技能
        while(icon_Skill_List.size < 3){
            generateOneSkill()
        }
    }
    fun generateShieldSkill(){
        val skill_shield=Skill(ViewManger.rocketLocation[0],ViewManger.rocketLocation[1])
        skill_shield.shieldSkill()   //设置技能类型为护盾
        boom_Skill_List.add(skill_shield)
    }
    fun moveAllSkill(){                             //移动技能图标
        for(i in icon_Skill_List.indices){
            icon_Skill_List[i]?.move()
        }
        for(i in boom_Skill_List.indices){
            boom_Skill_List[i]?.move()
        }
    }
    fun drawAllSkill(canvas: Canvas,paint: Paint){
        for(i in icon_Skill_List.indices){     //画技能图标
            icon_Skill_List[i]?.drawSelf(canvas,paint)
        }
        for(i in boom_Skill_List.indices){    //画触发的技能,同时检测技能时间是否完成
            if(boom_Skill_List[i]?.state()==-2) {
                clear_Skill_List.add(boom_Skill_List[i])
            }else{
                boom_Skill_List[i]?.drawSelf(canvas,paint)
            }
        }
        boom_Skill_List.removeAll(clear_Skill_List)         //先删除该删除的技能
        clear_Skill_List.clear()                            //讲缓存删除技能的列表清空
    }


    fun checkTrigger(x:Int,y:Int,radius:Int){           //检测是否触发技能
        for(i in icon_Skill_List.indices){
            if(icon_Skill_List[i]?.checkCollision(x,y,radius)!!){
                icon_Skill_List[i]?.changeState()
                boom_Skill_List.add(icon_Skill_List[i])
            }
        }
        icon_Skill_List.removeAll(boom_Skill_List)
    }

    fun killEnemy(){                        //检测技能杀死哪些敌人
        for(i in boom_Skill_List.indices){
            when(boom_Skill_List[i]?.type()) {
                1 -> {                                   //爆炸技能的检测方式
                    for (j in EnemyManger.enemyList.indices) {
                        val x = EnemyManger.enemyList[j]?.getX()
                        val y = EnemyManger.enemyList[j]?.getY()
                        if (boom_Skill_List[i]?.checkCollision(x!!, y!!, ViewManger.enemyRadius)!!) {
                            EnemyManger.enemyList[j]?.isAlive = false
                            EnemyManger.enemyList[j]?.setDieType(1)
                            ViewManger.killEnemyNumber++
                            EnemyManger.dieEnemyList.add(EnemyManger.enemyList[j])
                        }
                    }
                    for (j in EnemyManger.loadEnemyList.indices) {          //对加载中的红点的检测
                        val x = EnemyManger.loadEnemyList[j]?.getX()
                        val y = EnemyManger.loadEnemyList[j]?.getY()
                        if (boom_Skill_List[i]?.checkCollision(x!!, y!!, ViewManger.enemyRadius)!!) {
                            EnemyManger.loadEnemyList[j]?.isAlive = false
                            EnemyManger.loadEnemyList[j]?.setDieType(1)
                            EnemyManger.loadEnemyList[j]?.loadOver=true
                            ViewManger.killEnemyNumber++
                            EnemyManger.dieEnemyList.add(EnemyManger.loadEnemyList[j])
                        }
                    }
                }
                2 -> {                                      //冰冻技能的检测方式
                    for (j in EnemyManger.enemyList.indices) {
                        val x = EnemyManger.enemyList[j]?.getX()
                        val y = EnemyManger.enemyList[j]?.getY()
                        if (boom_Skill_List[i]?.checkCollision(x!!, y!!, ViewManger.enemyRadius)!!) {
                            EnemyManger.enemyList[j]?.setFrozen()
                        }
                    }
                    for (j in EnemyManger.loadEnemyList.indices) {              //对加载中的红点的检测
                        val x = EnemyManger.loadEnemyList[j]?.getX()
                        val y = EnemyManger.loadEnemyList[j]?.getY()
                        if (boom_Skill_List[i]?.checkCollision(x!!, y!!, ViewManger.enemyRadius)!!) {
                            EnemyManger.loadEnemyList[j]?.loadOver=true
                            EnemyManger.loadEnemyList[j]?.setFrozen()
                        }
                    }
                }
                3 -> {                                      //护盾技能的检测方式
                    for (j in EnemyManger.enemyList.indices) {
                        val x = EnemyManger.enemyList[j]?.getX()
                        val y = EnemyManger.enemyList[j]?.getY()
                        if (boom_Skill_List[i]?.checkCollision(x!!, y!!, ViewManger.enemyRadius)!!) {
                            EnemyManger.enemyList[j]?.isAlive = false
                            EnemyManger.enemyList[j]?.setDieType(3)
                            ViewManger.killEnemyNumber++
                            EnemyManger.dieEnemyList.add(EnemyManger.enemyList[j])
                        }
                    }
                    for (j in EnemyManger.loadEnemyList.indices) {                        //对加载中的红点的检测
                        val x = EnemyManger.loadEnemyList[j]?.getX()
                        val y = EnemyManger.loadEnemyList[j]?.getY()
                        if (boom_Skill_List[i]?.checkCollision(x!!, y!!, ViewManger.enemyRadius)!!) {
                            EnemyManger.loadEnemyList[j]?.isAlive = false
                            EnemyManger.loadEnemyList[j]?.loadOver=true
                            EnemyManger.loadEnemyList[j]?.setDieType(3)
                            ViewManger.killEnemyNumber++
                            EnemyManger.dieEnemyList.add(EnemyManger.loadEnemyList[j])
                        }
                    }
                }
                4 -> {                                   //闪电技能的检测方式
                    for (j in EnemyManger.enemyList.indices) {
                        val x = EnemyManger.enemyList[j]?.getX()
                        val y = EnemyManger.enemyList[j]?.getY()
                        if (boom_Skill_List[i]?.checkCollision(x!!, y!!, ViewManger.enemyRadius)!!) {
                            EnemyManger.enemyList[j]?.isAlive = false
                            EnemyManger.enemyList[j]?.setDieType(4)
                            ViewManger.killEnemyNumber++
                            EnemyManger.enemyList[j]?.dieFrameNumber=20
                            EnemyManger.dieEnemyList.add(EnemyManger.enemyList[j])
                        }
                    }
                    for (j in EnemyManger.loadEnemyList.indices) {
                        val x = EnemyManger.loadEnemyList[j]?.getX()
                        val y = EnemyManger.loadEnemyList[j]?.getY()
                        if (boom_Skill_List[i]?.checkCollision(x!!, y!!, ViewManger.enemyRadius)!!) {
                            EnemyManger.loadEnemyList[j]?.isAlive = false
                            EnemyManger.loadEnemyList[j]?.setDieType(4)
                            EnemyManger.loadEnemyList[j]?.loadOver=true
                            ViewManger.killEnemyNumber++
                            EnemyManger.loadEnemyList[j]?.dieFrameNumber=20
                            EnemyManger.dieEnemyList.add(EnemyManger.loadEnemyList[j])
                        }
                    }
                }
                5 -> {                                   //旋风技能的检测方式
                    for (j in EnemyManger.enemyList.indices) {
                        val x = EnemyManger.enemyList[j]?.getX()
                        val y = EnemyManger.enemyList[j]?.getY()
                        if (boom_Skill_List[i]?.checkCollision(x!!, y!!, ViewManger.enemyRadius)!!) {
                            EnemyManger.enemyList[j]?.isAlive = false
                            EnemyManger.enemyList[j]?.setDieType(5)
                            ViewManger.killEnemyNumber++
                            EnemyManger.dieEnemyList.add(EnemyManger.enemyList[j])
                        }
                    }
                    for (j in EnemyManger.loadEnemyList.indices) {
                        val x = EnemyManger.loadEnemyList[j]?.getX()
                        val y = EnemyManger.loadEnemyList[j]?.getY()
                        if (boom_Skill_List[i]?.checkCollision(x!!, y!!, ViewManger.enemyRadius)!!) {
                            EnemyManger.loadEnemyList[j]?.isAlive = false
                            EnemyManger.loadEnemyList[j]?.setDieType(5)
                            EnemyManger.loadEnemyList[j]?.loadOver=true
                            ViewManger.killEnemyNumber++
                            EnemyManger.dieEnemyList.add(EnemyManger.loadEnemyList[j])
                        }
                    }
                }
                6 ->{                       //冲击波技能的检测方式

                }
            }
            EnemyManger.enemyList.removeAll(EnemyManger.dieEnemyList)
            EnemyManger.loadEnemyList.removeAll(EnemyManger.dieEnemyList)

        }
    }
    fun exitSkill(){
        icon_Skill_List.clear()
        boom_Skill_List.clear()
        clear_Skill_List.clear()
        shield_exit=0
    }
}
