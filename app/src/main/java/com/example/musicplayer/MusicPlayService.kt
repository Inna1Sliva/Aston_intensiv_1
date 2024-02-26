package com.example.musicplayer

import android.Manifest
import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.bluetooth.BluetoothHidDevice
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.os.Bundle
import android.media.AudioManager
import android.media.MediaPlayer
import android.os.Binder
import android.os.IBinder
import android.os.Build
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import java.lang.Exception


@Suppress("UNREACHABLE_CODE")
class MusicPlayService : Service() {
    private lateinit var player: MediaPlayer
    private var currentTrackIndex: Int = -1
    private lateinit var audioManager: AudioManager
    private lateinit var session: MediaSessionCompat

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        СheckIntent(intent)
        return START_NOT_STICKY
    }

    private fun СheckIntent(intent: Intent?) {
    intent?.let {
        when(it.action){
            Action.PLAY.toString() -> {
                playTrack()
            }

            Action.PAUSE.toString() -> {
                TrackPause()
            }
            Action.NEXT.toString() -> {
                val trackName = R.raw.michael_ackson_billie_jean
                creatMediaPlay(trackName)
                nextTrack()
            }
            Action.PREVIOUS.toString() -> {
                Previous()
            }
            Action.TRACK.toString() ->{
                val track = it.getIntExtra(Extra.TRACK_NAME,1)
                creatMediaPlay(track)

            }

        }
     }
    }

    private fun nextTrack() {
        if (player.isPlaying){
            player.stop()
            playTrack()
        }
        player.reset()
    }
    private  fun Previous(){
        if (player.isPlaying){
            player.stop()
            playTrack()
        }
        player.reset()
    }

    private fun creatMediaPlay(track:Int) {
        player =MediaPlayer.create(this,track)
    }

    private fun TrackPause() {
        if (player.isPlaying){
            player.pause()
        }
    }

    private fun playTrack() {
        player.start()

    }


    override fun onCreate() {
        super.onCreate()
        player = MediaPlayer()
        audioManager = getSystemService(Context.AUDIO_SERVICE) as AudioManager
        session = MediaSessionCompat(this, "MusicService")
        callback = object :Callback() {
            override fun onPlay() {
                playTrack()
            }

            override fun onPause() {
                TrackPause()
            }

            override fun onSkipToNext() {
                nextTrack()
            }

            override fun Previous() {
                Previous()
            }
        }
        session.setCallback(callback)
        session.isActive = true

        createNotificationChannel()
        }

    private fun createNotificationChannel(trackName:String){
        val intent = Intent(this, MainActivity::class.java)
        val bundle = Bundle()
        bundle.putString(Extra.TRACK_NAME, trackName)
        intent.putExtras(bundle)
        val pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)


        val playIntent = Intent(this, MusicPlayService::class.java)
        playIntent.action = Action.PLAY.toString()
        val playPendingIntent =
            PendingIntent.getService(this, 0, playIntent, PendingIntent.FLAG_UPDATE_CURRENT)

        val pauseIntent = Intent(this, MusicPlayService::class.java)
        pauseIntent.action = Action.PAUSE.toString()
        val pausePendingIntent =
            PendingIntent.getService(this, 0, pauseIntent, PendingIntent.FLAG_UPDATE_CURRENT)

        val nextIntent = Intent(this, MusicPlayService::class.java)
        nextIntent.action = Action.NEXT.toString()
        val nextPendingIntent =
            PendingIntent.getService(this, 0, nextIntent, PendingIntent.FLAG_UPDATE_CURRENT)

        val previousIntent = Intent(this, MusicPlayService::class.java)
        previousIntent.action = Action.PREVIOUS.toString()
        var previousPendingIntent =
            PendingIntent.getService(this, 0, previousIntent, PendingIntent.FLAG_UPDATE_CURRENT)

        val notification = NotificationCompat.Builder(this, "channel1Id")
            .setContentTitle(getString(R.string.name))
            .setContentText("trackName")
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setLargeIcon(BitmapFactory.decodeResource(resources, R.drawable.michaelimage))
            .setContentIntent(pendingIntent)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .addAction(R.drawable.play_circle,"play", playPendingIntent)
            .addAction(R.drawable.pause_circle,"pause",pausePendingIntent )
            .addAction(R.drawable.skip_next,"next",nextPendingIntent )
            .addAction(R.drawable.skip_next, "previus" , previousPendingIntent)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            .setStyle(androidx.media.app.NotificationCompat.MediaStyle().setMediaSession(session.sessionToken))
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
        val notificationManager =NotificationManagerCompat.from(this)
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {

            return
        }
        notificationManager.notify(1, notification.build())
    }

    enum class Action{
        PLAY,PAUSE,NEXT,PREVIOUS, TRACK
    }


}


