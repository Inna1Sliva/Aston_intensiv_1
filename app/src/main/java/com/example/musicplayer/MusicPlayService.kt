package com.example.musicplayer

import android.Manifest
import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
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
    private var trackList: List<String> = emptyList()
    private var isForegroundService: Boolean = false
    private var serviceName: String = ""
    private val notificationId: Int = 1
    private val channel1Id: String = "channel_1"
    private val channel1Name: String = "Music Channel"

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
                Log.e("servise", "play")
                val trackName = it.getStringExtra(Extra.TRACK_NAME)
                Log.e("servise", "${trackName}")

                val trackIndex = it.getIntExtra(Extra.TRACK_INDEX, -1)
                // val tracks = it.getStringArrayListExtra(Extra.TRACK_LIST)
                //if (trackIndex >= 0 && trackIndex < tracks!!.size) {
                //  trackList = tracks
                //  currentTrackIndex = trackIndex
                playTrack(trackIndex)

            }

            Action.PAUSE.toString() -> {}
            Action.NEXT.toString() -> {}
            Action.PREVIOUS.toString() -> {}

        }
     }
    }

    private fun playTrack(name:Int) {
       player.reset()
        try {
            player =MediaPlayer.create(this,name)
            player.start()
        }catch (e:Exception){
            e.printStackTrace()
        }
    }




 ///   private fun createNotification(trackName: String): Notification {
      //  val intent = Intent(this, MainActivity::class.java)
      //  val bundle = Bundle()
      //  bundle.putString(Extra.TRACK_NAME, trackName)
      //  intent.putExtras(bundle)
      //  val pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
      //  TODO("Создаем интент для команды воспроизведения")

       // val playIntent = Intent(this, MusicPlayService::class.java)
        //playIntent.action = Action.PLAY_TRACK
       // val playPendingIntent =
         //   PendingIntent.getService(this, 0, playIntent, PendingIntent.FLAG_UPDATE_CURRENT)

       // TODO("Создаем интент для команды паузы")
      //  val pauseIntent = Intent(this, MusicPlayService::class.java)
       // pauseIntent.action = Action.PAUSE_TRACK
        //val pausePendingIntent =
           // PendingIntent.getService(this, 0, pauseIntent, PendingIntent.FLAG_UPDATE_CURRENT)
//
        // Создаем интент для команды переключения на следующий трек
        //val nextIntent = Intent(this, MusicPlayService::class.java)
       // nextIntent.action = Action.NEXT_TRACK
       // val nextPendingIntent =
          //  PendingIntent.getService(this, 0, nextIntent, PendingIntent.FLAG_UPDATE_CURRENT)

        // Создаем интент для команды переключения на предыдущий трек
       // val previousIntent = Intent(this, MusicPlayService::class.java)
        //previousIntent.action = Action.PREVIOUS_TRACK
       // val previousPendingIntent =
          //  PendingIntent.getService(this, 0, previousIntent, PendingIntent.FLAG_UPDATE_CURRENT)


       // return notification
 //   }

    override fun onCreate() {
        super.onCreate()
        player = MediaPlayer()


    }

    fun setCurrentTrackIndex(index:Int){
        currentTrackIndex = index
    }
    fun setTrackList(tracks: List<String>) {
        trackList = tracks
    }
    fun getCurrentTrackIndex(): Int {
        return currentTrackIndex
    }
    enum class Action{
        PLAY,PAUSE,NEXT,PREVIOUS
    }


}


