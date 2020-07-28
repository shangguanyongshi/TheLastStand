package com.example.thelaststand

import android.app.Service
import android.content.Intent
import android.media.MediaPlayer
import android.os.IBinder

class BackMusicService : Service() {

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()
    }
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if(!ViewManger.mPlayer.isPlaying) {
            ViewManger.mPlayer.isLooping=true
            ViewManger.mPlayer.start()
        }
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onDestroy() {
        if(ViewManger.mPlayer.isPlaying) {
            ViewManger.mPlayer.pause()
        }
        super.onDestroy()
    }
}
