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
import android.widget.ImageView
import android.widget.TextView

class UpdateWeapon : Activity() {
    var crystal_number=0
    val skill_update_crystal= intArrayOf(5000,8000,12000,15000,20000)     //每一级升级需要的水晶数目
    val imageIds= intArrayOf(R.drawable.skill_level0,R.drawable.skill_level1,R.drawable.skill_level2,R.drawable.skill_level3,R.drawable.skill_level4,R.drawable.skill_level5)
    var skill_level= intArrayOf(0,0,0,0,0)              //每个技能的等级
    var every_skill_need= Array<TextView?>(5,init = {null})             //升级按钮显示水晶数目的TextView
    var skill_level_image=Array<ImageView?>(5,init = {null})
    lateinit var crystal_display:TextView

    private lateinit var preferences: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        // 设置全屏
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setContentView(R.layout.activity_update_weapon)

        //找到每个技能对应的等级图片
        skill_level_image[0]=findViewById<ImageView>(R.id.boom_skill_level)
        skill_level_image[1]=findViewById<ImageView>(R.id.frozen_skill_level)
        skill_level_image[2]=findViewById<ImageView>(R.id.wind_skill_level)
        skill_level_image[3]=findViewById<ImageView>(R.id.shield_skill_level)
        skill_level_image[4]=findViewById<ImageView>(R.id.lighting_skill_level)
        //找到每个技能对应的升级按钮的值
        every_skill_need[0]=findViewById<TextView>(R.id.skill_boom_need)
        every_skill_need[1]=findViewById<TextView>(R.id.skill_frozen_need)
        every_skill_need[2]=findViewById<TextView>(R.id.skill_wind_need)
        every_skill_need[3]=findViewById<TextView>(R.id.skill_shield_need)
        every_skill_need[4]=findViewById<TextView>(R.id.skill_lighting_need)
        //找到用户水晶数目对应的文本框
        crystal_display=findViewById(R.id.all_crystal_number)


        preferences=getSharedPreferences("date_staroge", Context.MODE_PRIVATE)
        editor=preferences.edit()
        //确定每个技能的等级
        skill_level[0]=preferences.getInt("boom_skill",0)
        skill_level[1]=preferences.getInt("frozen_skill",0)
        skill_level[2]=preferences.getInt("wind_skill",0)
        skill_level[3]=preferences.getInt("shield_skill",0)
        skill_level[4]=preferences.getInt("lighting_skill",0)
        //确定用户拥有的水晶数目
        crystal_number=preferences.getInt("user_crystal_number",5000)
        //设置用户水晶数目的文本框
        crystal_display.text=crystal_number.toString()

        for(i in skill_level.indices){
            when(skill_level[i]){
                0 ->{
                    skill_level_image[i]?.setImageResource(imageIds[0])
                    every_skill_need[i]?.text = skill_update_crystal[0].toString()
                }
                1 -> {
                    skill_level_image[i]?.setImageResource(imageIds[1])
                    every_skill_need[i]?.text = skill_update_crystal[1].toString()
                }
                2 -> {
                    skill_level_image[i]?.setImageResource(imageIds[2])
                    every_skill_need[i]?.text = skill_update_crystal[2].toString()
                }
                3 -> {
                    skill_level_image[i]?.setImageResource(imageIds[3])
                    every_skill_need[i]?.text = skill_update_crystal[3].toString()
                }
                4 -> {
                    skill_level_image[i]?.setImageResource(imageIds[4])
                    every_skill_need[i]?.text = skill_update_crystal[4].toString()
                }
                5 -> {
                    skill_level_image[i]?.setImageResource(imageIds[5])
                    every_skill_need[i]?.text = "----"
                }
            }
        }



    }
    fun gameHome(view: View){
        if(ViewManger.mPlayer.isPlaying) {
            ViewManger.soundPool?.play(ViewManger.soundMap[1]!!, 1f, 1f, 0, 0, 1f)
        }
        val intent= Intent(this,MainActivity::class.java)
        startActivity(intent)
    }

    fun skillBoomUpdate(view: View) {
        if(ViewManger.mPlayer.isPlaying) {
            ViewManger.soundPool?.play(ViewManger.soundMap[1]!!, 1f, 1f, 0, 0, 1f)
        }
        when(skill_level[0]){
            0 ->{
                if(crystal_number>=skill_update_crystal[0]){
                    crystal_number-=skill_update_crystal[0]
                    editor.putInt("user_crystal_number",crystal_number)
                    skill_level[0]=1
                    editor.putInt("boom_skill",1)
                    skill_level_image[0]?.setImageResource(imageIds[1])
                    every_skill_need[0]?.text = skill_update_crystal[1].toString()
                    crystal_display.text=crystal_number.toString()
                    ViewManger.skill_level[0]=1
                }else{
                    val builder = AlertDialog.Builder(this)
                        .setTitle("水晶不足")
                    .setMessage("去玩游戏获得更多水晶吧！")
                    .setPositiveButton("确定"){
                        dialog, which ->
                    }
                    .create().show();
                }
            }
            1 ->{
                if(crystal_number>=skill_update_crystal[1]){
                    crystal_number-=skill_update_crystal[1]
                    editor.putInt("user_crystal_number",crystal_number)
                    skill_level[0]=2
                    editor.putInt("boom_skill",2)
                    skill_level_image[0]?.setImageResource(imageIds[2])
                    every_skill_need[0]?.text = skill_update_crystal[2].toString()
                    crystal_display.text=crystal_number.toString()
                    ViewManger.skill_level[0]=2
                }else{
                    val builder = AlertDialog.Builder(this)
                        .setTitle("水晶不足")
                        .setMessage("去玩游戏获得更多水晶吧！")
                        .setPositiveButton("确定"){
                                dialog, which ->
                        }
                        .create().show();
                }
            }
            2 ->{
                if(crystal_number>=skill_update_crystal[2]){
                    crystal_number-=skill_update_crystal[2]
                    editor.putInt("user_crystal_number",crystal_number)
                    skill_level[0]=3
                    editor.putInt("boom_skill",3)
                    skill_level_image[0]?.setImageResource(imageIds[3])
                    every_skill_need[0]?.text = skill_update_crystal[3].toString()
                    crystal_display.text=crystal_number.toString()
                    ViewManger.skill_level[0]=3
                }else{
                    val builder = AlertDialog.Builder(this)
                        .setTitle("水晶不足")
                        .setMessage("去玩游戏获得更多水晶吧！")
                        .setPositiveButton("确定"){
                                dialog, which ->
                        }
                        .create().show();
                }
            }
            3 ->{
                if(crystal_number>=skill_update_crystal[3]){
                    crystal_number-=skill_update_crystal[3]
                    editor.putInt("user_crystal_number",crystal_number)
                    skill_level[0]=4
                    editor.putInt("boom_skill",4)
                    skill_level_image[0]?.setImageResource(imageIds[4])
                    every_skill_need[0]?.text = skill_update_crystal[4].toString()
                    crystal_display.text=crystal_number.toString()
                    ViewManger.skill_level[0]=4
                }else{
                    val builder = AlertDialog.Builder(this)
                        .setTitle("水晶不足")
                        .setMessage("去玩游戏获得更多水晶吧！")
                        .setPositiveButton("确定"){
                                dialog, which ->
                        }
                        .create().show();
                }
            }
            4 ->{
                if(crystal_number>=skill_update_crystal[4]){
                    crystal_number-=skill_update_crystal[4]
                    editor.putInt("user_crystal_number",crystal_number)
                    skill_level[0]=5
                    editor.putInt("boom_skill",5)
                    skill_level_image[0]?.setImageResource(imageIds[5])
                    every_skill_need[0]?.text = "----"
                    crystal_display.text=crystal_number.toString()
                    ViewManger.skill_level[0]=5
                }else{
                    val builder = AlertDialog.Builder(this)
                        .setTitle("水晶不足")
                        .setMessage("去玩游戏获得更多水晶吧！")
                        .setPositiveButton("确定"){
                                dialog, which ->
                        }
                        .create().show();
                }
            }
            5 ->{

            }
        }
        editor.commit()
        ViewManger.loadResource()
    }
    fun skillFrozenUpdate(view: View) {
        if(ViewManger.mPlayer.isPlaying) {
            ViewManger.soundPool?.play(ViewManger.soundMap[1]!!, 1f, 1f, 0, 0, 1f)
        }
        when(skill_level[1]){
            0 ->{
                if(crystal_number>=skill_update_crystal[0]){
                    crystal_number-=skill_update_crystal[0]
                    editor.putInt("user_crystal_number",crystal_number)
                    skill_level[1]=1
                    editor.putInt("frozen_skill",1)
                    skill_level_image[1]?.setImageResource(imageIds[1])
                    every_skill_need[1]?.text = skill_update_crystal[1].toString()
                    crystal_display.text=crystal_number.toString()
                    ViewManger.skill_level[1]=1
                }else{
                    val builder = AlertDialog.Builder(this)
                        .setTitle("水晶不足")
                        .setMessage("去玩游戏获得更多水晶吧！")
                        .setPositiveButton("确定"){
                                dialog, which ->
                        }
                        .create().show();
                }
            }
            1 ->{
                if(crystal_number>=skill_update_crystal[1]){
                    crystal_number-=skill_update_crystal[1]
                    editor.putInt("user_crystal_number",crystal_number)
                    skill_level[1]=2
                    editor.putInt("frozen_skill",2)
                    skill_level_image[1]?.setImageResource(imageIds[2])
                    every_skill_need[1]?.text = skill_update_crystal[2].toString()
                    crystal_display.text=crystal_number.toString()
                    ViewManger.skill_level[1]=2
                }else{
                    val builder = AlertDialog.Builder(this)
                        .setTitle("水晶不足")
                        .setMessage("去玩游戏获得更多水晶吧！")
                        .setPositiveButton("确定"){
                                dialog, which ->
                        }
                        .create().show();
                }
            }
            2 ->{
                if(crystal_number>=skill_update_crystal[2]){
                    crystal_number-=skill_update_crystal[2]
                    editor.putInt("user_crystal_number",crystal_number)
                    skill_level[1]=3
                    editor.putInt("frozen_skill",3)
                    skill_level_image[1]?.setImageResource(imageIds[3])
                    every_skill_need[1]?.text = skill_update_crystal[3].toString()
                    crystal_display.text=crystal_number.toString()
                    ViewManger.skill_level[1]=3
                }else{
                    val builder = AlertDialog.Builder(this)
                        .setTitle("水晶不足")
                        .setMessage("去玩游戏获得更多水晶吧！")
                        .setPositiveButton("确定"){
                                dialog, which ->
                        }
                        .create().show();
                }
            }
            3 ->{
                if(crystal_number>=skill_update_crystal[3]){
                    crystal_number-=skill_update_crystal[3]
                    editor.putInt("user_crystal_number",crystal_number)
                    skill_level[1]=4
                    editor.putInt("frozen_skill",4)
                    skill_level_image[1]?.setImageResource(imageIds[4])
                    every_skill_need[1]?.text = skill_update_crystal[4].toString()
                    crystal_display.text=crystal_number.toString()
                    ViewManger.skill_level[1]=4
                }else{
                    val builder = AlertDialog.Builder(this)
                        .setTitle("水晶不足")
                        .setMessage("去玩游戏获得更多水晶吧！")
                        .setPositiveButton("确定"){
                                dialog, which ->
                        }
                        .create().show();
                }
            }
            4 ->{
                if(crystal_number>=skill_update_crystal[4]){
                    crystal_number-=skill_update_crystal[4]
                    editor.putInt("user_crystal_number",crystal_number)
                    skill_level[1]=5
                    editor.putInt("frozen_skill",5)
                    skill_level_image[1]?.setImageResource(imageIds[5])
                    every_skill_need[1]?.text = "----"
                    crystal_display.text=crystal_number.toString()
                    ViewManger.skill_level[1]=5
                }else{
                    val builder = AlertDialog.Builder(this)
                        .setTitle("水晶不足")
                        .setMessage("去玩游戏获得更多水晶吧！")
                        .setPositiveButton("确定"){
                                dialog, which ->
                        }
                        .create().show();
                }
            }
            5 ->{

            }
        }
        editor.commit()
        ViewManger.loadResource()
    }
    fun skillWindUpdate(view: View) {
        if(ViewManger.mPlayer.isPlaying) {
            ViewManger.soundPool?.play(ViewManger.soundMap[1]!!, 1f, 1f, 0, 0, 1f)
        }
        when(skill_level[2]){
            0 ->{
                if(crystal_number>=skill_update_crystal[0]){
                    crystal_number-=skill_update_crystal[0]
                    editor.putInt("user_crystal_number",crystal_number)
                    skill_level[2]=1
                    editor.putInt("wind_skill",1)
                    skill_level_image[2]?.setImageResource(imageIds[1])
                    every_skill_need[2]?.text = skill_update_crystal[1].toString()
                    crystal_display.text=crystal_number.toString()
                    ViewManger.skill_level[2]=1
                }else{
                    val builder = AlertDialog.Builder(this)
                        .setTitle("水晶不足")
                        .setMessage("去玩游戏获得更多水晶吧！")
                        .setPositiveButton("确定"){
                                dialog, which ->
                        }
                        .create().show();
                }
            }
            1 ->{
                if(crystal_number>=skill_update_crystal[1]){
                    crystal_number-=skill_update_crystal[1]
                    editor.putInt("user_crystal_number",crystal_number)
                    skill_level[2]=2
                    editor.putInt("wind_skill",2)
                    skill_level_image[2]?.setImageResource(imageIds[2])
                    every_skill_need[2]?.text = skill_update_crystal[2].toString()
                    crystal_display.text=crystal_number.toString()
                    ViewManger.skill_level[2]=2
                }else{
                    val builder = AlertDialog.Builder(this)
                        .setTitle("水晶不足")
                        .setMessage("去玩游戏获得更多水晶吧！")
                        .setPositiveButton("确定"){
                                dialog, which ->
                        }
                        .create().show();
                }
            }
            2 ->{
                if(crystal_number>=skill_update_crystal[2]){
                    crystal_number-=skill_update_crystal[2]
                    editor.putInt("user_crystal_number",crystal_number)
                    skill_level[2]=3
                    editor.putInt("wind_skill",3)
                    skill_level_image[2]?.setImageResource(imageIds[3])
                    every_skill_need[2]?.text = skill_update_crystal[3].toString()
                    crystal_display.text=crystal_number.toString()
                    ViewManger.skill_level[2]=3
                }else{
                    val builder = AlertDialog.Builder(this)
                        .setTitle("水晶不足")
                        .setMessage("去玩游戏获得更多水晶吧！")
                        .setPositiveButton("确定"){
                                dialog, which ->
                        }
                        .create().show();
                }
            }
            3 ->{
                if(crystal_number>=skill_update_crystal[3]){
                    crystal_number-=skill_update_crystal[3]
                    editor.putInt("user_crystal_number",crystal_number)
                    skill_level[2]=4
                    editor.putInt("wind_skill",4)
                    skill_level_image[2]?.setImageResource(imageIds[4])
                    every_skill_need[2]?.text = skill_update_crystal[4].toString()
                    crystal_display.text=crystal_number.toString()
                    ViewManger.skill_level[2]=4
                }else{
                    val builder = AlertDialog.Builder(this)
                        .setTitle("水晶不足")
                        .setMessage("去玩游戏获得更多水晶吧！")
                        .setPositiveButton("确定"){
                                dialog, which ->
                        }
                        .create().show();
                }
            }
            4 ->{
                if(crystal_number>=skill_update_crystal[4]){
                    crystal_number-=skill_update_crystal[4]
                    editor.putInt("user_crystal_number",crystal_number)
                    skill_level[2]=5
                    editor.putInt("wind_skill",5)
                    skill_level_image[2]?.setImageResource(imageIds[5])
                    every_skill_need[2]?.text = "----"
                    crystal_display.text=crystal_number.toString()
                    ViewManger.skill_level[2]=5
                }else{
                    val builder = AlertDialog.Builder(this)
                        .setTitle("水晶不足")
                        .setMessage("去玩游戏获得更多水晶吧！")
                        .setPositiveButton("确定"){
                                dialog, which ->
                        }
                        .create().show();
                }
            }
            5 ->{

            }
        }
        editor.commit()
        ViewManger.loadResource()
    }
    fun skillShieldUpdate(view: View) {
        if(ViewManger.mPlayer.isPlaying) {
            ViewManger.soundPool?.play(ViewManger.soundMap[1]!!, 1f, 1f, 0, 0, 1f)
        }
        when(skill_level[3]){
            0 ->{
                if(crystal_number>=skill_update_crystal[0]){
                    crystal_number-=skill_update_crystal[0]
                    editor.putInt("user_crystal_number",crystal_number)
                    skill_level[3]=1
                    editor.putInt("shield_skill",1)
                    skill_level_image[3]?.setImageResource(imageIds[1])
                    every_skill_need[3]?.text = skill_update_crystal[1].toString()
                    crystal_display.text=crystal_number.toString()
                    ViewManger.skill_level[3]=1
                }else{
                    val builder = AlertDialog.Builder(this)
                        .setTitle("水晶不足")
                        .setMessage("去玩游戏获得更多水晶吧！")
                        .setPositiveButton("确定"){
                                dialog, which ->
                        }
                        .create().show();
                }
            }
            1 ->{
                if(crystal_number>=skill_update_crystal[1]){
                    crystal_number-=skill_update_crystal[1]
                    editor.putInt("user_crystal_number",crystal_number)
                    skill_level[3]=2
                    editor.putInt("shield_skill",2)
                    skill_level_image[3]?.setImageResource(imageIds[2])
                    every_skill_need[3]?.text = skill_update_crystal[2].toString()
                    crystal_display.text=crystal_number.toString()
                    ViewManger.skill_level[3]=2
                }else{
                    val builder = AlertDialog.Builder(this)
                        .setTitle("水晶不足")
                        .setMessage("去玩游戏获得更多水晶吧！")
                        .setPositiveButton("确定"){
                                dialog, which ->
                        }
                        .create().show();
                }
            }
            2 ->{
                if(crystal_number>=skill_update_crystal[2]){
                    crystal_number-=skill_update_crystal[2]
                    editor.putInt("user_crystal_number",crystal_number)
                    skill_level[3]=3
                    editor.putInt("shield_skill",3)
                    skill_level_image[3]?.setImageResource(imageIds[3])
                    every_skill_need[3]?.text = skill_update_crystal[3].toString()
                    crystal_display.text=crystal_number.toString()
                    ViewManger.skill_level[3]=3
                }else{
                    val builder = AlertDialog.Builder(this)
                        .setTitle("水晶不足")
                        .setMessage("去玩游戏获得更多水晶吧！")
                        .setPositiveButton("确定"){
                                dialog, which ->
                        }
                        .create().show();
                }
            }
            3 ->{
                if(crystal_number>=skill_update_crystal[3]){
                    crystal_number-=skill_update_crystal[3]
                    editor.putInt("user_crystal_number",crystal_number)
                    skill_level[3]=4
                    editor.putInt("shield_skill",4)
                    skill_level_image[3]?.setImageResource(imageIds[4])
                    every_skill_need[3]?.text = skill_update_crystal[4].toString()
                    crystal_display.text=crystal_number.toString()
                    ViewManger.skill_level[3]=4
                }else{
                    val builder = AlertDialog.Builder(this)
                        .setTitle("水晶不足")
                        .setMessage("去玩游戏获得更多水晶吧！")
                        .setPositiveButton("确定"){
                                dialog, which ->
                        }
                        .create().show();
                }
            }
            4 ->{
                if(crystal_number>=skill_update_crystal[4]){
                    crystal_number-=skill_update_crystal[4]
                    editor.putInt("user_crystal_number",crystal_number)
                    skill_level[3]=5
                    editor.putInt("shield_skill",5)
                    skill_level_image[3]?.setImageResource(imageIds[5])
                    every_skill_need[3]?.text = "----"
                    crystal_display.text=crystal_number.toString()
                    ViewManger.skill_level[3]=5
                }else{
                    val builder = AlertDialog.Builder(this)
                        .setTitle("水晶不足")
                        .setMessage("去玩游戏获得更多水晶吧！")
                        .setPositiveButton("确定"){
                                dialog, which ->
                        }
                        .create().show();
                }
            }
            5 ->{

            }
        }
        editor.commit()
        ViewManger.loadResource()
    }
    fun skillLightingUpdate(view: View) {
        if(ViewManger.mPlayer.isPlaying) {
            ViewManger.soundPool?.play(ViewManger.soundMap[1]!!, 1f, 1f, 0, 0, 1f)
        }
        when(skill_level[4]){
            0 ->{
                if(crystal_number>=skill_update_crystal[0]){
                    crystal_number-=skill_update_crystal[0]
                    editor.putInt("user_crystal_number",crystal_number)
                    skill_level[4]=1
                    editor.putInt("lighting_skill",1)
                    skill_level_image[4]?.setImageResource(imageIds[1])
                    every_skill_need[4]?.text = skill_update_crystal[1].toString()
                    crystal_display.text=crystal_number.toString()
                    ViewManger.skill_level[4]=1
                }else{
                    val builder = AlertDialog.Builder(this)
                        .setTitle("水晶不足")
                        .setMessage("去玩游戏获得更多水晶吧！")
                        .setPositiveButton("确定"){
                                dialog, which ->
                        }
                        .create().show();
                }
            }
            1 ->{
                if(crystal_number>=skill_update_crystal[1]){
                    crystal_number-=skill_update_crystal[1]
                    editor.putInt("user_crystal_number",crystal_number)
                    skill_level[4]=2
                    editor.putInt("lighting_skill",2)
                    skill_level_image[4]?.setImageResource(imageIds[2])
                    every_skill_need[4]?.text = skill_update_crystal[2].toString()
                    crystal_display.text=crystal_number.toString()
                    ViewManger.skill_level[4]=2
                }else{
                    val builder = AlertDialog.Builder(this)
                        .setTitle("水晶不足")
                        .setMessage("去玩游戏获得更多水晶吧！")
                        .setPositiveButton("确定"){
                                dialog, which ->
                        }
                        .create().show();
                }
            }
            2 ->{
                if(crystal_number>=skill_update_crystal[2]){
                    crystal_number-=skill_update_crystal[2]
                    editor.putInt("user_crystal_number",crystal_number)
                    skill_level[4]=3
                    editor.putInt("lighting_skill",3)
                    skill_level_image[4]?.setImageResource(imageIds[3])
                    every_skill_need[4]?.text = skill_update_crystal[3].toString()
                    crystal_display.text=crystal_number.toString()
                    ViewManger.skill_level[4]=3
                }else{
                    val builder = AlertDialog.Builder(this)
                        .setTitle("水晶不足")
                        .setMessage("去玩游戏获得更多水晶吧！")
                        .setPositiveButton("确定"){
                                dialog, which ->
                        }
                        .create().show();
                }
            }
            3 ->{
                if(crystal_number>=skill_update_crystal[3]){
                    crystal_number-=skill_update_crystal[3]
                    editor.putInt("user_crystal_number",crystal_number)
                    skill_level[4]=4
                    editor.putInt("lighting_skill",4)
                    skill_level_image[4]?.setImageResource(imageIds[4])
                    every_skill_need[4]?.text = skill_update_crystal[4].toString()
                    crystal_display.text=crystal_number.toString()
                    ViewManger.skill_level[4]=4
                }else{
                    val builder = AlertDialog.Builder(this)
                        .setTitle("水晶不足")
                        .setMessage("去玩游戏获得更多水晶吧！")
                        .setPositiveButton("确定"){
                                dialog, which ->
                        }
                        .create().show();
                }
            }
            4 ->{
                if(crystal_number>=skill_update_crystal[4]){
                    crystal_number-=skill_update_crystal[4]
                    editor.putInt("user_crystal_number",crystal_number)
                    skill_level[4]=5
                    editor.putInt("lighting_skill",5)
                    skill_level_image[4]?.setImageResource(imageIds[5])
                    every_skill_need[4]?.text = "----"
                    crystal_display.text=crystal_number.toString()
                    ViewManger.skill_level[4]=5
                }else{
                    val builder = AlertDialog.Builder(this)
                        .setTitle("水晶不足")
                        .setMessage("去玩游戏获得更多水晶吧！")
                        .setPositiveButton("确定"){
                                dialog, which ->
                        }
                        .create().show();
                }
            }
            5 ->{

            }
        }
        editor.commit()
        ViewManger.loadResource()
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
