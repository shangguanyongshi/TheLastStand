package com.example.thelaststand

import android.content.Context
import android.graphics.*
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.media.SoundPool
import android.provider.MediaStore
import com.example.thelaststand.comp.EnemyManger
import com.example.thelaststand.comp.SkillManger
import kotlin.math.PI
import kotlin.math.atan2

object ViewManger {
    //屏幕的宽高
    var SCREEN_WIDTH: Int = 0
    var SCREEN_HEIGHT: Int = 0

    //技能图标相关变量
    var skill_icon_radius:Int=0


    var map: Bitmap?=null  // 地图图片
    var player:Array<Bitmap?>?=arrayOfNulls(2)    //火箭图片

    var kills:String="Kills:"

    var skill_level= IntArray(5)

    var skill_temp:Bitmap?=null               //缓存刚读入的图片资源

    var skill_boom:Array<Bitmap?>?=arrayOfNulls(5)   //爆炸技能帧
    var skill_boom_icon:Bitmap?=null     //爆炸技能图标
    var enemy_die_boom:Bitmap?=null        //爆炸敌人死亡帧
    var skill_boom_level:Int=0           //爆炸技能等级

    var skill_frozen:Array<Bitmap?>?=arrayOfNulls(5)   //冰冻技能帧
    var skill_frozen_icon:Bitmap?=null     //冰冻技能图标
    var enemy_frozen:Bitmap?=null          //敌人被冰冻的冰
    var enemy_die_frozen:Bitmap?=null       //冰冻敌人死亡帧
    var skill_frozen_level:Int=0           //冰冻技能等级

    var skill_lighting:Array<Bitmap?>?=arrayOfNulls(3)        //电击技能资源
    var skill_lighting_icon:Bitmap?=null                          //电击技能图标
    var enemy_lighting:Array<Bitmap?>?=arrayOfNulls(3)      //电击敌人效果的数组
    var skill_linghting_level:Int=0                              //冰冻技能等级


    var skill_shield:Bitmap?=null               //护盾技能资源
    var skill_shield_icon:Bitmap?=null          //护盾技能图标
    var skill_shield_level:Int=100                //护盾技能持续时间
    var skill_shield_radius:Int=0               //护盾技能大小

    var skill_wind:Array<Bitmap?>?=arrayOfNulls(4)                //旋风技能
    var skill_wind_icon:Bitmap?=null          //旋风技能图标
    var skill_wind_level:Int=30                //旋风技能持续时间
    var skill_wind_radius:Int=0               //旋风技能半径

    var skill_wave:Bitmap?=null                 //冲击波技能
    var skill_wave_icon:Bitmap?=null          //护盾技能图标
    var skill_wave_level:Int=0               //护盾技能大小



    var rectLeft:Int=0
    var rectRight:Int=0
    var rectBottom:Int=0
    var rectTop:Int=0
    var rect:Rect?=null                                //边界矩形

    var orientationValues = IntArray(2)             //传感器传回的角度
    var MAX_SPEED=60                                    //火箭的最大速度

    val ENEMY_SPEED=2                     //红点速度
    var enemyRadius:Int=0                 //红点半径
    var enemy_frozen_radius:Int=0


    var rocketLocation= IntArray(2)   //火箭位置
    var rocketRadius:Int=0
    var duration: Int=0    //游戏的运行时间

    var killEnemyNumber:Long=0

    //声音相关变量
    var mPlayer:MediaPlayer=MediaPlayer.create(MainActivity.mainActivity, R.raw.back_music)


    var soundPool:SoundPool?=null
    var soundMap:HashMap<Int,Int?> = HashMap()


    fun setRect(){
        rectLeft= SCREEN_HEIGHT/20
        rectRight= SCREEN_WIDTH- SCREEN_HEIGHT/20
        rectTop= SCREEN_HEIGHT/10
        rectBottom= SCREEN_HEIGHT- SCREEN_HEIGHT/20
        rect=Rect(rectLeft, rectTop, rectRight, rectBottom)
    }

    fun loadResource(){

        skill_icon_radius= SCREEN_WIDTH/40     //设置技能图标的半径

        enemyRadius= SCREEN_WIDTH/130         //设置红点半径

        skill_shield_radius = skill_icon_radius*2  //护盾技能的半径

        //判断每个技能的等级
        for(i in skill_level.indices){
            when(i){
                0 ->{                       //爆炸技能
                    when(skill_level[i]){
                        0 ->{
                            skill_boom_level= SCREEN_WIDTH/7
                        }
                        1 ->{
                            skill_boom_level= SCREEN_WIDTH/6
                        }
                        2 ->{
                            skill_boom_level= SCREEN_WIDTH/5
                        }
                        3 ->{
                            skill_boom_level= SCREEN_WIDTH/4
                        }
                        4 ->{
                            skill_boom_level= SCREEN_WIDTH/3
                        }
                        5 ->{
                            skill_boom_level= SCREEN_WIDTH/2
                        }
                    }
                }
                1 ->{                                   //冰冻技能
                    when(skill_level[i]){
                        0 ->{
                            skill_frozen_level= SCREEN_WIDTH/7
                        }
                        1 ->{
                            skill_frozen_level= SCREEN_WIDTH/6
                        }
                        2 ->{
                            skill_frozen_level= SCREEN_WIDTH/5
                        }
                        3 ->{
                            skill_frozen_level= SCREEN_WIDTH/4
                        }
                        4 ->{
                            skill_frozen_level= SCREEN_WIDTH/3
                        }
                        5 ->{
                            skill_frozen_level= SCREEN_WIDTH/2
                        }
                    }
                }
                2 ->{                   //旋风技能
                    when(skill_level[i]){
                        0 ->{
                            skill_wind_level= 30
                        }
                        1 ->{
                            skill_wind_level= 40
                        }
                        2 ->{
                            skill_wind_level= 50
                        }
                        3 ->{
                            skill_wind_level= 60
                        }
                        4 ->{
                            skill_wind_level= 70
                        }
                        5 ->{
                            skill_wind_level= 80
                        }
                    }
                }
                3 ->{                              //护盾技能
                    when(skill_level[i]){
                        0 ->{
                            skill_shield_level= 30
                        }
                        1 ->{
                            skill_shield_level= 40
                        }
                        2 ->{
                            skill_shield_level= 50
                        }
                        3 ->{
                            skill_shield_level= 60
                        }
                        4 ->{
                            skill_shield_level= 70
                        }
                        5 ->{
                            skill_shield_level= 80
                        }
                    }
                }
                4 ->{                           //电击技能
                    when(skill_level[i]){
                        0 ->{
                            skill_linghting_level= SCREEN_WIDTH/5
                        }
                        1 ->{
                            skill_linghting_level= SCREEN_WIDTH/4
                        }
                        2 ->{
                            skill_linghting_level= SCREEN_WIDTH*7/2
                        }
                        3 ->{
                            skill_linghting_level= SCREEN_WIDTH/3
                        }
                        4 ->{
                            skill_linghting_level= SCREEN_WIDTH/2
                        }
                        5 ->{
                            skill_linghting_level= SCREEN_WIDTH*3/2
                        }
                    }
                }

            }
        }
                //爆炸技能的半径
        skill_frozen_level= SCREEN_WIDTH/8        //冰冻技能的半径
        skill_linghting_level = SCREEN_WIDTH/5    //电击技能的等级
        skill_wind_radius = SCREEN_WIDTH/12         //旋风技能的半径


        skill_wave_level = SCREEN_WIDTH/8           //冲击波技能的等级


        rocketLocation[0]= SCREEN_WIDTH/2
        rocketLocation[1] = SCREEN_HEIGHT/2

        //加载背景图片
        map = BitmapFactory.decodeResource(MainActivity.res, R.drawable.background)
        if (map != null && !map!!.isRecycled)
        {
            map = Bitmap.createScaledBitmap(map!!, SCREEN_WIDTH , SCREEN_HEIGHT,false )
        }

        //加载火箭图片
        skill_temp = BitmapFactory.decodeResource(MainActivity.res, R.drawable.rocket1)
        val newWidth= SCREEN_WIDTH/20               //设置新图标的宽度
        val newHeight= skill_temp!!.height*newWidth/ skill_temp!!.width         //同比例改变高度
        rocketRadius=newHeight/2
        player!![0] = Bitmap.createScaledBitmap(skill_temp!!, newWidth , newHeight,false )
        skill_temp = BitmapFactory.decodeResource(MainActivity.res, R.drawable.rocket2)
        player!![1] = Bitmap.createScaledBitmap(skill_temp!!, newWidth , newHeight,false )


        //加载技能图标

        //加载爆炸技能图标
        skill_boom_icon = BitmapFactory.decodeResource(MainActivity.res, R.drawable.boom_skill_icon)   //加载原始文件
        if(skill_boom_icon!=null&&!skill_boom_icon!!.isRecycled)
        {           //重新设置图标大小
            skill_boom_icon= Bitmap.createScaledBitmap(skill_boom_icon!!, skill_icon_radius*2, skill_icon_radius*2,false)
        }
        //加载冰冻技能图标
        skill_frozen_icon = BitmapFactory.decodeResource(MainActivity.res, R.drawable.frozen_skill_icon)   //加载原始文件
        if(skill_frozen_icon!=null&&!skill_frozen_icon!!.isRecycled)
        {           //重新设置图标大小
            skill_frozen_icon= Bitmap.createScaledBitmap(skill_frozen_icon!!, skill_icon_radius*2, skill_icon_radius*2,false)
        }
        //加载护盾技能图标
        skill_shield_icon = BitmapFactory.decodeResource(MainActivity.res, R.drawable.skill_shield_icon)   //加载原始文件
        if(skill_shield_icon!=null&&!skill_shield_icon!!.isRecycled)
        {           //重新设置图标大小
            skill_shield_icon= Bitmap.createScaledBitmap(skill_shield_icon!!, skill_icon_radius*2, skill_icon_radius*2,false)
        }
        //加载电击技能图标
        skill_lighting_icon = BitmapFactory.decodeResource(MainActivity.res, R.drawable.lighting_skill_icon)   //加载原始文件
        if(skill_lighting_icon!=null&&!skill_lighting_icon!!.isRecycled)
        {           //重新设置图标大小
            skill_lighting_icon= Bitmap.createScaledBitmap(skill_lighting_icon!!, skill_icon_radius*2, skill_icon_radius*2,false)
        }
        //加载旋风技能图标
        skill_wind_icon = BitmapFactory.decodeResource(MainActivity.res, R.drawable.skill_wind_icon)   //加载原始文件
        if(skill_wind_icon!=null&&!skill_wind_icon!!.isRecycled)
        {           //重新设置图标大小
            skill_wind_icon= Bitmap.createScaledBitmap(skill_wind_icon!!, skill_icon_radius*2, skill_icon_radius*2,false)
        }
        //加载冲击波技能图标
        skill_wave_icon = BitmapFactory.decodeResource(MainActivity.res, R.drawable.skill_wave_icon)   //加载原始文件
        if(skill_wave_icon!=null&&!skill_wave_icon!!.isRecycled)
        {           //重新设置图标大小
            skill_wave_icon= Bitmap.createScaledBitmap(skill_wave_icon!!, skill_icon_radius*2, skill_icon_radius*2,false)
        }




        //加载技能资源

        //加载爆炸技能
        skill_temp= BitmapFactory.decodeResource(MainActivity.res,R.drawable.boom_skill)
        if(skill_temp!=null&&!skill_temp!!.isRecycled)
        {
            skill_boom!![0]= Bitmap.createScaledBitmap(skill_temp!!, skill_icon_radius*2, skill_icon_radius*2,false)
            skill_boom!![1]= Bitmap.createScaledBitmap(skill_temp!!, skill_boom_level, skill_boom_level,false)
            skill_boom!![2]= Bitmap.createScaledBitmap(skill_temp!!,
                skill_boom_level*2, skill_boom_level*2,false)
            skill_boom!![3]= Bitmap.createScaledBitmap(skill_temp!!, skill_boom_level, skill_boom_level,false)
            skill_boom!![4]= Bitmap.createScaledBitmap(skill_temp!!, skill_icon_radius*2, skill_icon_radius*2,false)
        }
        //加载死亡动画
        enemy_die_boom=Bitmap.createScaledBitmap(skill_temp!!, enemyRadius*4, enemyRadius*4,false)


        //加载冰冻技能
        skill_temp= BitmapFactory.decodeResource(MainActivity.res,R.drawable.frozen_skill)
        if(skill_temp!=null&&!skill_temp!!.isRecycled)
        {
            skill_frozen!![0]= Bitmap.createScaledBitmap(skill_temp!!, skill_icon_radius*2, skill_icon_radius*2,false)
            skill_frozen!![1]= Bitmap.createScaledBitmap(skill_temp!!, skill_frozen_level, skill_frozen_level,false)
            skill_frozen!![2]= Bitmap.createScaledBitmap(skill_temp!!,
                skill_frozen_level*2, skill_frozen_level*2,false)
            skill_frozen!![3]= Bitmap.createScaledBitmap(skill_temp!!, skill_frozen_level, skill_frozen_level,false)
            skill_frozen!![4]= Bitmap.createScaledBitmap(skill_temp!!, skill_icon_radius*2, skill_icon_radius*2,false)
        }
        //加载冰冻图标
        skill_temp= BitmapFactory.decodeResource(MainActivity.res,R.drawable.frozen_enemy)
        enemy_frozen = Bitmap.createScaledBitmap(skill_temp!!, enemyRadius*4, enemyRadius*4,false)
        enemy_frozen_radius= enemy_frozen!!.width/2
        //加载冰冻破裂图标
        skill_temp= BitmapFactory.decodeResource(MainActivity.res,R.drawable.frozen_break)
        enemy_die_frozen = Bitmap.createScaledBitmap(skill_temp!!, enemyRadius*6, enemyRadius*6,false)

        //加载护盾技能
        skill_temp = BitmapFactory.decodeResource(MainActivity.res,R.drawable.shield_skill)
        skill_shield = Bitmap.createScaledBitmap(skill_temp!!, skill_shield_radius*2, skill_shield_radius*2,false)


        //加载电击技能
        skill_temp = BitmapFactory.decodeResource(MainActivity.res,R.drawable.ligthing1)
        skill_lighting!![0] = Bitmap.createScaledBitmap(skill_temp!!, skill_linghting_level/5, skill_linghting_level,false)
        skill_temp = BitmapFactory.decodeResource(MainActivity.res,R.drawable.ligthing2)
        skill_lighting!![1] = Bitmap.createScaledBitmap(skill_temp!!, skill_linghting_level/5, skill_linghting_level,false)
        skill_temp= BitmapFactory.decodeResource(MainActivity.res,R.drawable.ligthing3)
        skill_lighting!![2] = Bitmap.createScaledBitmap(skill_temp!!, skill_linghting_level/5, skill_linghting_level,false)
        //加载电击敌人资源
        skill_temp = BitmapFactory.decodeResource(MainActivity.res,R.drawable.lighting_enemy1)
        enemy_lighting!![0] = Bitmap.createScaledBitmap(skill_temp!!, enemyRadius*6, enemyRadius*6,false)
        skill_temp = BitmapFactory.decodeResource(MainActivity.res,R.drawable.lighting_enemy2)
        enemy_lighting!![1] = Bitmap.createScaledBitmap(skill_temp!!, enemyRadius*6, enemyRadius*6,false)
        skill_temp = BitmapFactory.decodeResource(MainActivity.res,R.drawable.lighting_enemy3)
        enemy_lighting!![2] = Bitmap.createScaledBitmap(skill_temp!!, enemyRadius*6, enemyRadius*6,false)

        //加载旋风技能
        skill_temp = BitmapFactory.decodeResource(MainActivity.res,R.drawable.skill_wind1)
        skill_wind!![0] = Bitmap.createScaledBitmap(skill_temp!!, skill_wind_radius*2, skill_wind_radius*2,false)
        skill_temp = BitmapFactory.decodeResource(MainActivity.res,R.drawable.skill_wind2)
        skill_wind!![1] = Bitmap.createScaledBitmap(skill_temp!!, skill_wind_radius*2, skill_wind_radius*2,false)
        skill_temp = BitmapFactory.decodeResource(MainActivity.res,R.drawable.skill_wind3)
        skill_wind!![2] = Bitmap.createScaledBitmap(skill_temp!!, skill_wind_radius*2, skill_wind_radius*2,false)
        skill_temp = BitmapFactory.decodeResource(MainActivity.res,R.drawable.skill_wind4)
        skill_wind!![3] = Bitmap.createScaledBitmap(skill_temp!!, skill_wind_radius*2, skill_wind_radius*2,false)

        //加载冲击波技能
        skill_temp = BitmapFactory.decodeResource(MainActivity.res,R.drawable.skill_wave)
        skill_wave = Bitmap.createScaledBitmap(skill_temp!!, skill_wave_level, skill_wave_level,false)


        //加载sound pool资源
        val attr=AudioAttributes.Builder()
            .setUsage(AudioAttributes.USAGE_GAME)
            .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
            .build()

        soundPool=SoundPool.Builder()
            .setAudioAttributes(attr)
            .setMaxStreams(10).build()

        soundMap[1]= soundPool?.load(MainActivity.mainActivity,R.raw.button_sound,1)    //加载按钮声音
        soundMap[2]= soundPool?.load(MainActivity.mainActivity,R.raw.frozen_sound,1)    //加载冰冻声音
        soundMap[3]= soundPool?.load(MainActivity.mainActivity,R.raw.lighting_sound,1)  //加载电击声音
        soundMap[4]= soundPool?.load(MainActivity.mainActivity,R.raw.wind_sound,1)      //加载旋风声音

    }

    fun getRadius():Double{
        var temp= atan2(orientationValues[1].toDouble(), orientationValues[0].toDouble())
        temp=temp*180/ PI
        return temp
    }
    fun drawScore(canvas: Canvas,paint: Paint){
        paint.textSize = 40f
        paint.style = Paint.Style.FILL
        paint.strokeWidth=2f
        paint.typeface= Typeface.MONOSPACE
        paint.textAlign=Paint.Align.LEFT
        kills= "Kills:"
        canvas.drawText(kills, rectLeft.toFloat(), (rectTop-5).toFloat(), paint)
        paint.color=Color.WHITE
        kills = killEnemyNumber.toString()
        canvas.drawText(kills, (rectLeft+150).toFloat(), (rectTop-5).toFloat(), paint)
        paint.textAlign=Paint.Align.CENTER
        canvas.drawText(duration.toString()+"S", (rectRight- rectLeft).toFloat()/2f,(rectTop-5).toFloat(),paint)
    }



    fun exitGame(){
        EnemyManger.exitEnemy()
        SkillManger.exitSkill()
        killEnemyNumber=0
        duration=0
        rocketLocation= intArrayOf(ViewManger.SCREEN_WIDTH/2,ViewManger.SCREEN_HEIGHT/2)

    }

}