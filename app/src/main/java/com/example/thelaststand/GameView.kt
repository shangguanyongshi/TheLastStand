package com.example.thelaststand

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.os.Handler
import android.os.Message
import android.view.SurfaceHolder
import android.view.SurfaceView
import com.example.thelaststand.comp.EnemyManger
import com.example.thelaststand.comp.Rocket
import com.example.thelaststand.comp.SkillManger

class GameView(context: Context, handler: Handler): SurfaceView(context),SurfaceHolder.Callback {
    private var canvas:Canvas?=null
    private var paint: Paint?=null
    private var updateViewThread:UpdateViewThread?=null
    var gameViewFlag:Boolean = true
    private var rocket:Rocket

    private var startFrame:Long=0
    private var endFrame:Long=0
    private var frameDuration:Long=0
    private var sleepTime:Long=50
    private var minSleepTime:Long=30
    private var millis:Long=0

    val handlerx=handler

    private var fisetTime=System.currentTimeMillis()
    private var secondTime=System.currentTimeMillis()


    init {
        paint= Paint()
        paint?.isAntiAlias=true
        holder.addCallback(this)
        keepScreenOn=true   //保持屏幕常亮
        rocket= Rocket()
    }


    fun resume(){
        updateViewThread=UpdateViewThread(holder)
        updateViewThread?.start()
    }

    override fun surfaceChanged(holder: SurfaceHolder?, format: Int, width: Int, height: Int) {

    }

    override fun surfaceCreated(holder: SurfaceHolder?) {
        resume()
    }

    override fun surfaceDestroyed(holder: SurfaceHolder?) {

        if(updateViewThread!=null)
            updateViewThread=null
    }
    internal inner class UpdateViewThread(holder: SurfaceHolder?):Thread(){
        private var surfaceHolder: SurfaceHolder? = null
        init{
            surfaceHolder=holder
        }
        override fun run() {
            while(gameViewFlag){
                synchronized(holder){
                    try {
                        startFrame=System.currentTimeMillis()
                        //画画
                        canvas = surfaceHolder?.lockCanvas()
                        if(canvas!=null){

                            canvas!!.drawColor(Color.BLACK)
                            canvas?.drawBitmap(ViewManger.map!!,0f,0f,paint)
                            val tempPaint=paint  //保存现有画笔
                            paint?.color=resources.getColor(R.color.baseColor)
                            paint?.style=Paint.Style.STROKE
                            paint?.strokeWidth=4f
                            canvas?.drawRect(ViewManger.rect!!,paint!!)
                            paint=tempPaint   //恢复成原画笔

                            secondTime=System.currentTimeMillis()
                            if((secondTime-fisetTime)>=1000){
                                ViewManger.duration+=1
                                fisetTime=secondTime
                            }
                            EnemyManger.generateEnemy()
                            SkillManger.generateSkill()
                            moveItem()
                            drawGame(canvas!!,paint!!)
                            checkCollision()
                        }
                        endFrame=System.currentTimeMillis()
                        frameDuration=endFrame-startFrame
                        millis=sleepTime-frameDuration
                        if(millis<minSleepTime){
                            millis=minSleepTime
                        }
                        sleep(millis)
                    }finally {
                        if(canvas!=null) {
                            surfaceHolder?.unlockCanvasAndPost(canvas)
                        }
                    }
                }

            }
        }
        private fun moveItem(){
            EnemyManger.moveAllEnemy()
            SkillManger.moveAllSkill()
            rocket.move()
            ViewManger.rocketLocation=rocket.returnLocation()
        }
        private fun drawGame(canvas: Canvas,paint: Paint){
            ViewManger.drawScore(canvas,paint)
            EnemyManger.drawAllEnemy(canvas,paint)
            SkillManger.drawAllSkill(canvas,paint)
            rocket.drawSelf(canvas,paint)
        }
        private fun checkCollision(){
            SkillManger.checkTrigger(ViewManger.rocketLocation[0],ViewManger.rocketLocation[1],ViewManger.rocketRadius)
            SkillManger.killEnemy()
            if(EnemyManger.checkRocket()){
                gameViewFlag=false
                val msg=Message()
                msg.what=0x123
                handlerx.sendMessage(msg)
            }

        }

    }





}