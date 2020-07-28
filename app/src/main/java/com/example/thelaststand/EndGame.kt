package com.example.thelaststand

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.TextView

class EndGame : Activity() {

    private lateinit var preferences: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        // 设置全屏
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setContentView(R.layout.activity_end_game)

        //本局游戏的结算
        val kill_number_text=findViewById<TextView>(R.id.kill_number)
        val game_time_text=findViewById<TextView>(R.id.game_time)
        val get_crystal_text=findViewById<TextView>(R.id.get_crystal)
        //历史数据部分
        val most_kill_text=findViewById<TextView>(R.id.most_kill)
        val most_time_text=findViewById<TextView>(R.id.most_time)
        val all_kill_text=findViewById<TextView>(R.id.all_kill)

        val all_crystal_number_text=findViewById<TextView>(R.id.all_crystal_number)

        preferences=getSharedPreferences("date_staroge", Context.MODE_PRIVATE)
        editor=preferences.edit()

        val kill_number=ViewManger.killEnemyNumber          //获取杀敌数
        val duration=ViewManger.duration                    //获取持续时间
        ViewManger.exitGame()                               //清除游戏数据

        //读取数据库排行榜
        var most_kill=preferences.getInt("rank_list_1_score",0)
        var most_time=preferences.getInt("rank_list_1_duration",0)

        var rank_list_2_score=preferences.getInt("rank_list_2_score",0)
        var rank_list_3_score=preferences.getInt("rank_list_3_score",0)

        var rank_list_2_duration=preferences.getInt("rank_list_2_duration",0)
        var rank_list_3_duration=preferences.getInt("rank_list_3_duration",0)

        var all_kill=preferences.getInt("all_kill_number",0)
        var all_crystal_number=preferences.getInt("user_crystal_number",0)

        val get_crystal=kill_number/10

        kill_number_text.text=kill_number.toString()        //显示杀敌数
        all_kill += kill_number.toInt()     //累加累计杀敌
        editor.putInt("all_kill_number",all_kill)       //重新写入累计杀敌个数

        game_time_text.text=(duration.toString()+'s')         //显示本局持续时间

        get_crystal_text.text=get_crystal.toString()    //显示获取水晶个数

        if(kill_number>most_kill){              //更新分数排行榜
            most_kill=kill_number.toInt()
            editor.putInt("rank_list_1_score",most_kill)
        }else if(kill_number>rank_list_2_score){
            rank_list_2_score=kill_number.toInt()
            editor.putInt("rank_list_2_score",rank_list_2_score)
        }else if(kill_number>rank_list_3_score){
            rank_list_3_score=kill_number.toInt()
            editor.putInt("rank_list_3_score",rank_list_3_score)
        }
        most_kill_text.text=most_kill.toString()        //设置最多杀敌数

        if(duration>most_time){             //更新时间排行榜
            most_time=duration
            editor.putInt("rank_list_1_duration",most_time)
        }else if(duration>rank_list_2_duration){
            rank_list_2_duration=duration.toInt()
            editor.putInt("rank_list_2_duration",rank_list_2_duration)
        }else if(duration>rank_list_3_duration){
            rank_list_3_duration=duration.toInt()
            editor.putInt("rank_list_3_duration",rank_list_3_duration)
        }
        most_time_text.text=(most_time.toString()+'s')    //设置最多持续时间


        all_kill_text.text=all_kill.toString()    //设置累计杀敌数

        all_crystal_number += get_crystal.toInt()
        all_crystal_number_text.text=all_crystal_number.toString()   //设置现有水晶个数
        editor.putInt("user_crystal_number",all_crystal_number)


        editor.apply()          //提交修改


    }

    fun updateWeapon(view: View) {
        if(ViewManger.mPlayer.isPlaying) {
            ViewManger.soundPool?.play(ViewManger.soundMap[1]!!, 1f, 1f, 0, 0, 1f)
        }
        val intent= Intent(this,UpdateWeapon::class.java)
        startActivity(intent)
        finish()
    }
    fun continueGame(view: View) {
        if(ViewManger.mPlayer.isPlaying) {
            ViewManger.soundPool?.play(ViewManger.soundMap[1]!!, 1f, 1f, 0, 0, 1f)
        }
        val intent= Intent(this,DoGame::class.java)
        startActivity(intent)
        finish()
    }
    fun gameHome(view: View) {
        if(ViewManger.mPlayer.isPlaying) {
            ViewManger.soundPool?.play(ViewManger.soundMap[1]!!, 1f, 1f, 0, 0, 1f)
        }
        val intent= Intent(this,MainActivity::class.java)
        startActivity(intent)
        finish()
    }
    fun exitGame(view: View) {
        if(ViewManger.mPlayer.isPlaying) {
            ViewManger.soundPool?.play(ViewManger.soundMap[1]!!, 1f, 1f, 0, 0, 1f)
        }
        val intent= Intent(this,MainActivity::class.java)
        intent.putExtra("exitGame",true)
        startActivity(intent)
        finish()
    }
}
