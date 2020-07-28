package com.example.thelaststand

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.TextView
import kotlin.system.exitProcess

class RankingList : Activity() {

    private lateinit var preferences: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        // 设置全屏
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setContentView(R.layout.activity_ranking_list)

        //本局游戏的结算
        val rank_list_first_kills_text=findViewById<TextView>(R.id.rank_list_first_kills)
        val rank_list_second_kills_text=findViewById<TextView>(R.id.rank_list_second_kills)
        val rank_list_thirsty_kills_text=findViewById<TextView>(R.id.rank_list_thirsty_kills)
        //历史数据部分
        val rank_list_first_times_text=findViewById<TextView>(R.id.rank_list_first_times)
        val rank_list_second_times_text=findViewById<TextView>(R.id.rank_list_second_times)
        val rank_list_thirsty_times_text=findViewById<TextView>(R.id.rank_list_thirsty_times)

        preferences=getSharedPreferences("date_staroge", Context.MODE_PRIVATE)

        //读取数据库排行榜
        val rank_list_1_score=preferences.getInt("rank_list_1_score",0)
        val rank_list_1_duration=preferences.getInt("rank_list_1_duration",0)

        val rank_list_2_score=preferences.getInt("rank_list_2_score",0)
        val rank_list_2_duration=preferences.getInt("rank_list_2_duration",0)

        val rank_list_3_score=preferences.getInt("rank_list_3_score",0)
        val rank_list_3_duration=preferences.getInt("rank_list_3_duration",0)

        //指定分数排行榜
        rank_list_first_kills_text.text=rank_list_1_score.toString()
        rank_list_second_kills_text.text=rank_list_2_score.toString()
        rank_list_thirsty_kills_text.text=rank_list_3_score.toString()

        //指定时间排行榜
        rank_list_first_times_text.text=rank_list_1_duration.toString()
        rank_list_second_times_text.text=rank_list_2_duration.toString()
        rank_list_thirsty_times_text.text=rank_list_3_duration.toString()


    }
    fun gameHome(view: View){
        if(ViewManger.mPlayer.isPlaying) {
            ViewManger.soundPool?.play(ViewManger.soundMap[1]!!, 1f, 1f, 0, 0, 1f)
        }
        val intent= Intent(this,MainActivity::class.java)
        startActivity(intent)
    }
    override fun onBackPressed() {
        if(ViewManger.mPlayer.isPlaying) {
            ViewManger.soundPool?.play(ViewManger.soundMap[1]!!, 1f, 1f, 0, 0, 1f)
        }
        val builder = AlertDialog.Builder(this)
            .setMessage("要退出吗？")
            .setNegativeButton("取消", null)
            .setPositiveButton("确定"){
                    dialog, which ->
                val intent= Intent(this,MainActivity::class.java)
                intent.putExtra("exitGame",true)
                startActivity(intent)
                finish()
            }
            .create().show();
    }
}
