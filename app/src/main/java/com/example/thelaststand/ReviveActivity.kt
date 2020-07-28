package com.example.thelaststand

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.TextView
import android.widget.Toast

class ReviveActivity : Activity() {
    private lateinit var preferences: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor
    var user_crystal_number=0
    val revive_crystal_num=4000                 //设置复活需要多少水晶
    override fun onCreate(savedInstanceState: Bundle?) {
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        // 设置全屏
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)


        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_revive)
        preferences=getSharedPreferences("date_staroge", Context.MODE_PRIVATE)
        editor=preferences.edit()
        val crystal=findViewById<TextView>(R.id.revive_crystal_sum)             //总水晶数的TextView
        val reviveCrystal=findViewById<TextView>(R.id.revive_crystal_num)          //复活所需水晶数的TextView

        user_crystal_number=preferences.getInt("user_crystal_number",0)
        crystal.text=user_crystal_number.toString()
        reviveCrystal.text=revive_crystal_num.toString()
    }

    fun reviveButton(view: View) {
        if(user_crystal_number-revive_crystal_num>=0){
            setResult(3)
            user_crystal_number-=revive_crystal_num
            editor.putInt("user_crystal_number",user_crystal_number)
            editor.commit()
            finish()
        }else{
            setResult(4)
            Toast.makeText(this,"水晶不足",Toast.LENGTH_LONG).show()
            finish()
        }
    }
    fun endButton(view: View) {
        setResult(4)
        finish()
    }
}
