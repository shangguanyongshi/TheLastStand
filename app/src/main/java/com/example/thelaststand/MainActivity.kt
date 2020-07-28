package com.example.thelaststand

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.res.Configuration
import android.content.res.Resources
import android.graphics.drawable.BitmapDrawable
import android.media.Image
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.ImageView
import android.widget.LinearLayout
import kotlin.system.exitProcess

class MainActivity : Activity() {
    private lateinit var back:ImageView
    private lateinit var musicSwitch:ImageView
    private lateinit var musicService: Intent
    private lateinit var preferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mainActivity = this
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        // 设置全屏
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        val metric = DisplayMetrics()
        // 获取屏幕高度、宽度
        windowManager.defaultDisplay.getMetrics(metric)
        windowHeight = metric.heightPixels  // 屏幕高度
        windowWidth = metric.widthPixels  // 屏幕宽度
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)
        res = resources

        setContentView(R.layout.activity_main)

        ViewManger.SCREEN_WIDTH= windowWidth
        ViewManger.SCREEN_HEIGHT= windowHeight

        preferences=getSharedPreferences("date_staroge", Context.MODE_PRIVATE)

        ViewManger.skill_level[0]=preferences.getInt("boom_skill",0)
        ViewManger.skill_level[1]=preferences.getInt("frozen_skill",0)
        ViewManger.skill_level[2]=preferences.getInt("wind_skill",0)
        ViewManger.skill_level[3]=preferences.getInt("shield_skill",0)
        ViewManger.skill_level[4]=preferences.getInt("lighting_skill",0)


        ViewManger.loadResource()               //加载游戏资源

        back=findViewById<ImageView>(R.id.first_main_background)
        back.setImageBitmap(ViewManger.map)
        mainActivity=this

        musicSwitch=findViewById<ImageView>(R.id.main_volume_icon)

        ViewManger.setRect()

        musicService=Intent(this,BackMusicService::class.java)
        if(!ViewManger.mPlayer.isPlaying){
            startService(musicService)
        }


    }
    fun beginGame(source: View){
        if(ViewManger.mPlayer.isPlaying) {
            ViewManger.soundPool?.play(ViewManger.soundMap[1]!!, 1f, 1f, 0, 0, 1f)
        }
        val intent= Intent(this,DoGame::class.java)
        startActivity(intent)
    }
    fun rankingList(source: View){
        if(ViewManger.mPlayer.isPlaying) {
            ViewManger.soundPool?.play(ViewManger.soundMap[1]!!, 1f, 1f, 0, 0, 1f)
        }
        val intent= Intent(this,RankingList::class.java)
        startActivity(intent)
    }
    fun updateWeapon(source: View){
        if(ViewManger.mPlayer.isPlaying) {
            ViewManger.soundPool?.play(ViewManger.soundMap[1]!!, 1f, 1f, 0, 0, 1f)
        }
        val intent= Intent(this,UpdateWeapon::class.java)
        startActivity(intent)
    }

    @SuppressLint("WrongConstant")
    override fun onConfigurationChanged(newConfig: Configuration?) {
        super.onConfigurationChanged(newConfig)
        if(newConfig?.orientation==Configuration.ORIENTATION_LANDSCAPE){
            return
        }else{
            requestedOrientation=Configuration.ORIENTATION_LANDSCAPE
        }
    }


    override fun onDestroy() {
        if(ViewManger.mPlayer.isPlaying){
            stopService(musicService)
        }
        super.onDestroy()

    }
    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        if (intent != null) {
            if(intent.getBooleanExtra("exitGame",false)){
                finish()
                exitProcess(0)
            }
        }
    }
    fun musicSwitch(view: View) {

        if(ViewManger.mPlayer.isPlaying){
            ViewManger.soundPool?.play(ViewManger.soundMap[1]!!,1f,1f,0,0,1f)
            musicSwitch.setImageResource(R.drawable.volume_off)
            stopService(musicService)
        }else{
            musicSwitch.setImageResource(R.drawable.volume_up)
            startService(musicService)
        }

    }
//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        if(resultCode==0){
//            finish()
//        }
//    }
    override fun onBackPressed() {
    if(ViewManger.mPlayer.isPlaying) {
        ViewManger.soundPool?.play(ViewManger.soundMap[1]!!, 1f, 1f, 0, 0, 1f)
    }
        val builder = AlertDialog.Builder(this)
            .setMessage("要退出吗？")
            .setNegativeButton("取消", null)
            .setPositiveButton("确定"){
                    _, _ ->
                ViewManger.exitGame()
                exitProcess(0)
            }
            .create().show();
    }
    companion object
    {
        // 定义资源管理的核心类
        var res: Resources? = null
        var mainActivity: MainActivity? = null
        // 定义成员变量记录游戏窗口的宽度、高度
        var windowWidth: Int = 0
        var windowHeight: Int = 0
    }


}
