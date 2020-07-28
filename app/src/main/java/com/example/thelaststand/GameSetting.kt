package com.example.thelaststand

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.ImageView

class GameSetting : Activity() {
    private lateinit var musicSwitch:ImageView
    private lateinit var musicService: Intent
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        requestWindowFeature(Window.FEATURE_NO_TITLE)
        // 设置全屏
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)

        setContentView(R.layout.activity_game_setting)

        musicSwitch=findViewById(R.id.setting_volume)
        if(ViewManger.mPlayer.isPlaying){
            musicSwitch.setImageResource(R.drawable.volume_up)
        }else{
            musicSwitch.setImageResource(R.drawable.volume_off)
        }
        musicService=Intent(this,BackMusicService::class.java)
    }
    fun volumeSwitch(view: View){

        if(ViewManger.mPlayer.isPlaying){
            ViewManger.soundPool?.play(ViewManger.soundMap[1]!!,1f,1f,0,0,1f)
            musicSwitch.setImageResource(R.drawable.volume_off)
            stopService(musicService)
        }else{
            musicSwitch.setImageResource(R.drawable.volume_up)
            startService(musicService)
        }
    }
    fun continueGame(view: View){
        if(ViewManger.mPlayer.isPlaying) {
            ViewManger.soundPool?.play(ViewManger.soundMap[1]!!, 1f, 1f, 0, 0, 1f)
        }
        setResult(0,intent)
        finish()
    }
    fun gameHome(view: View){
        if(ViewManger.mPlayer.isPlaying) {
            ViewManger.soundPool?.play(ViewManger.soundMap[1]!!, 1f, 1f, 0, 0, 1f)
        }
        setResult(1,intent)
        finish()
    }
    fun exitGame(view: View){
        if(ViewManger.mPlayer.isPlaying) {
            ViewManger.soundPool?.play(ViewManger.soundMap[1]!!, 1f, 1f, 0, 0, 1f)
        }
        setResult(2,intent)
        finish()
    }
}
