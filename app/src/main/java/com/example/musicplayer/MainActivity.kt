package com.example.musicplayer

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.NotificationManager.IMPORTANCE_DEFAULT
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.media.MediaPlayer
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.example.musicplayer.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var player: MediaPlayer
    private lateinit var binding: ActivityMainBinding

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
       startPermissions()

        val music = R.raw.michael_jackson_bad
        Intent(this@MainActivity, MusicPlayService::class.java).also {
            it.action = MusicPlayService.Action.TRACK.toString()
            it.putExtra(Extra.TRACK_NAME, music)
            startService(it)
        }
        player = MediaPlayer()
        binding.buttonPlay.setOnClickListener {

            binding.buttonPlay.visibility = View.GONE
            binding.buttonPause.visibility = View.VISIBLE
                Intent(this@MainActivity, MusicPlayService::class.java).also {
                    it.action = MusicPlayService.Action.PLAY.toString()
                    //it.putExtra(Extra.TRACK_INDEX, treakId)
                    //it.putExtra(Extra.TRACK_LIST, musicList)
                    startService(it)
                }

            }
        binding.buttonPause.setOnClickListener {
            binding.buttonPlay.visibility = View.VISIBLE
            binding.buttonPause.visibility = View.GONE
            Intent(this@MainActivity, MusicPlayService::class.java).also {
                it.action = MusicPlayService.Action.PAUSE.toString()
                startService(it)

            }
            if (player.isPlaying){
                player.pause()
            }
        }
        binding.buttonNext.setOnClickListener {
            binding.buttonPlay.visibility = View.GONE
            binding.buttonPause.visibility = View.VISIBLE
            val music = R.raw.michael_ackson_billie_jean
            Intent(this@MainActivity, MusicPlayService::class.java).also {
                it.action = MusicPlayService.Action.NEXT.toString()
                it.putExtra(Extra.TRACK_NAME, music)
                startService(it)
            }

        }
        binding.buttonBack.setOnClickListener {
            binding.buttonPlay.visibility = View.GONE
            binding.buttonPause.visibility = View.VISIBLE
            val music = R.raw.michael_jackson_bad
            Intent(this@MainActivity, MusicPlayService::class.java).also {
                it.action = MusicPlayService.Action.NEXT.toString()
                it.putExtra(Extra.TRACK_NAME, music)
                startService(it)
            }

        }


    }



    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private fun startPermissions() {
        if (checkPermissions()) {

        } else {
            requestPermissions()
        }
    }


    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    fun requestPermissions(){
        ActivityCompat.requestPermissions(
            this,
            arrayOf(android.Manifest.permission.POST_NOTIFICATIONS),
            Extra.RQ_CODE
        )
    }

    private fun checkPermissions(): Boolean {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) {
            return true
        }
            val readExternalStoragePermission = ContextCompat.checkSelfPermission(this,android.Manifest.permission.POST_NOTIFICATIONS)
            return readExternalStoragePermission == PackageManager.PERMISSION_GRANTED
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == Extra.RQ_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            } else {
            }
        }
    }

}