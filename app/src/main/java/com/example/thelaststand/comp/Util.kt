package com.example.thelaststand.comp

import java.util.*

object Util {
    // 返回一个start～end的随机数
    fun rand(start: Int,end:Int): Int
    {
        return (start..end).random()
    }
}