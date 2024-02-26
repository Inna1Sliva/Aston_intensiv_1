package com.example.musicplayer

import android.Manifest
import android.app.Activity
import android.app.Notification
import android.app.NotificationManager
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
    private lateinit var notification: Notification


    private lateinit var binding: ActivityMainBinding
    private var data :String? = null
    private var treakId:Int? = null
    private  var nameTreak: String? = null
    private val channel1Id: String = "channel_1"
    private var musicList:ArrayList<ListMusic> = ArrayList()

    private val permissionRequestCode = 1
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        getListMusic()
        var notificationManager =
            this.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notification = NotificationCompat.Builder(this, channel1Id)
            .setContentTitle(getString(R.string.name))
            .setContentText("trackName")
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setLargeIcon(BitmapFactory.decodeResource(resources, R.drawable.michaelimage))
            // .setContentIntent(pendingIntent)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            //.addAction(R.drawable.play_circle,getString(R.string.music_service_notification_channel_description))
            //.addAction(R.drawable.pause_circle,getString(R.string.music_service_notification_channel_description))
            //.addAction(R.drawable.skip_next,getString(R.string.music_service_notification_channel_description))
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            .build()

        startPermissions()
        player = MediaPlayer.create(this, R.raw.michael_jackson_bad)
        binding.apply {
            buttonPlay.setOnClickListener {
                notificationManager.notify(1, notification)
                buttonPlay.visibility = View.GONE
                buttonPause.visibility = View.VISIBLE
                player.start()
                Intent(this@MainActivity, MusicPlayService::class.java).also {
                    it.action = MusicPlayService.Action.PLAY.toString()
                    it.putExtra(Extra.TRACK_NAME, data)
                    //it.putExtra(Extra.TRACK_INDEX, treakId)
                    //it.putExtra(Extra.TRACK_LIST, musicList)
                    startService(it)
                }

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
            val readExternalStoragePermission = ContextCompat.checkSelfPermission(this, android.Manifest.permission.POST_NOTIFICATIONS)
            return readExternalStoragePermission == PackageManager.PERMISSION_GRANTED
    }

    private fun getListMusic():ArrayList<ListMusic> {
        musicList.add(ListMusic(0,getString(R.string.name), getString(R.string.name_track_1), getString(R.string.time_track_1),R.string.resurses1))
        musicList.add(ListMusic(1, getString(R.string.name), getString(R.string.name_track_2), getString(R.string.time_track_2),R.string.resurses2))
        musicList.add(ListMusic(2,getString(R.string.name), getString(R.string.name_track_3), getString(R.string.time_track_3),R.string.resurses3))
        return musicList
    }
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == Extra.RQ_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //readMusicFromDevice()
            } else {
            }
        }
    }

}