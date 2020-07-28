package com.example.thelaststand

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.util.Log
import android.view.*
import android.widget.LinearLayout
import com.example.thelaststand.comp.SkillManger
import java.lang.ref.WeakReference
import kotlin.system.exitProcess

class DoGame : Activity(),SensorEventListener {
    private var mainView:GameView?=null

    private var values = FloatArray(3)
    private var accelerometerValues = FloatArray(3)
    private var magneticFieldValues = FloatArray(3)
    private var S = FloatArray(9)           //获取传感器数据的辅助变量

    private var accelerometer : Sensor? = null// 加速度传感器
    private var magnetic : Sensor? = null// 地磁场传感器

    private lateinit var handler: Handler

    var sensorManger:SensorManager?=null
    class MyHandler(private var doGame:WeakReference<DoGame>):Handler(){
        override fun handleMessage(msg: Message?) {
            if(msg?.what==0x123){
                val intent= Intent(doGame.get(),ReviveActivity::class.java)
                doGame.get()?.startActivityForResult(intent,0)   //用请求码打开设置页面
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        // 设置全屏
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)

        handler=MyHandler(WeakReference(this))
        mainView = GameView(this,handler)          //创建surface实例
        setContentView(R.layout.activity_do_game)
        val layout=findViewById<LinearLayout>(R.id.game_view)
        layout.addView(mainView)                    //添加surface实例



        sensorManger=this.getSystemService(Context.SENSOR_SERVICE) as SensorManager
        accelerometer = sensorManger!!.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)	// 初始化地磁场传感器
        magnetic = sensorManger!!.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD)

    }

    override fun onResume() {
        sensorManger?.registerListener(this, accelerometer,SensorManager.SENSOR_DELAY_GAME)
        sensorManger?.registerListener(this, magnetic,SensorManager.SENSOR_DELAY_GAME)
        super.onResume()
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {

    }

    override fun onSensorChanged(event: SensorEvent) {
        when(event.sensor.type){
            Sensor.TYPE_ACCELEROMETER ->
                accelerometerValues=event.values
            Sensor.TYPE_MAGNETIC_FIELD ->
                magneticFieldValues=event.values
        }
        SensorManager.getRotationMatrix(S, null, accelerometerValues,magneticFieldValues)
        SensorManager.getOrientation(S, values)

        ViewManger.orientationValues[0] = Math.toDegrees(values[1].toDouble()).toInt()
        ViewManger.orientationValues[1] = Math.toDegrees(values[2].toDouble()).toInt()

    }

    override fun onPause() {
        sensorManger?.unregisterListener(this)
        super.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        ViewManger.exitGame()
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if(requestCode==0&&resultCode==0){      //如果返回 继续游戏
            mainView?.gameViewFlag=true
            mainView?.resume()
        }else if(requestCode==0&&resultCode==1){    //如果返回  返回主页
            ViewManger.exitGame()
            mainView=null
//            sensorManger=null
//            accelerometer=null
//            magnetic=null
            val intent= Intent(this,MainActivity::class.java)
            startActivity(intent)
            //finish()
            exitProcess(0)
        }else if(requestCode==0&&resultCode==2){        //如果返回  退出游戏
            val intent= Intent(this,MainActivity::class.java)
            intent.putExtra("exitGame",true)
            startActivity(intent)

        }else if(requestCode==0&&resultCode==3){            //如果返回为游戏复活
            mainView?.gameViewFlag=true
            SkillManger.generateShieldSkill()           //产生一个护盾技能
            mainView?.resume()
        }else if(requestCode==0&&resultCode==4){            //如果返回为游戏结束
            mainView=null
            val intent= Intent(this,EndGame::class.java)
            startActivity(intent)
            finish()
        }
    }

    fun gameSetting(view: View){            //点击设置按钮触发设置页面
        if(ViewManger.mPlayer.isPlaying) {
            ViewManger.soundPool?.play(ViewManger.soundMap[1]!!, 1f, 1f, 0, 0, 1f)
        }
        mainView?.gameViewFlag=false
        val intent= Intent(this,GameSetting::class.java)
        startActivityForResult(intent,0)   //用请求码打开设置页面
    }
    override fun onBackPressed() {              //点击返回技能时和点击设置按钮处理方式相同
        if(ViewManger.mPlayer.isPlaying) {
            ViewManger.soundPool?.play(ViewManger.soundMap[1]!!, 1f, 1f, 0, 0, 1f)
        }
        mainView?.gameViewFlag=false
        val intent= Intent(this,GameSetting::class.java)
        startActivityForResult(intent,0)   //用请求码打开设置页面
    }
}
