package seoft.co.kr.android_poc

import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.support.v4.app.NotificationCompat
import android.view.View
import android.widget.RemoteViews
import seoft.co.kr.android_poc.util.App
import seoft.co.kr.android_poc.util.i

class TimingNotication(val timingService: TimingService, val times: ArrayList<Int>) {

    val TAG = "TimingNotication"

    val NOTI_ID = 111

    var isForeground = false

    var noti: Notification? = null

    var remoteViews: RemoteViews? = null

    var notificationManager: NotificationManager? = null

    lateinit var notifiactionButtonType: NotifiactionButtonType

    init {
        notificationManager = timingService.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    }

    fun showNotification() {
        "showNotification".i(TAG)

        if (!isForeground) {
            val notificationIntent = Intent(timingService, TimingActivity::class.java).apply {
                putIntegerArrayListExtra(TimingActivity.TIMES, times)
            }
            notificationIntent.action = "TIMING_NOTI_ACTION"
            val pendingIntent = PendingIntent.getActivity(timingService, 0,
                    notificationIntent, 0)

            remoteViews = getRemoteView()

            val notification = NotificationCompat.Builder(timingService, App.CHANNEL_ID)
                    .setContentText("...")
                    .setSmallIcon(R.drawable.ic_tmp)
                    .setContentIntent(pendingIntent)
                    .setOngoing(true)
                    .setOnlyAlertOnce(true)
                    .setCustomContentView(remoteViews)
                    .build()

            isForeground = true
            timingService.startForeground(NOTI_ID, notification)
            noti = notification
        }

    }

    fun removeNotification() {
        "removeNotification".i(TAG)
        if (noti != null) noti = null
        timingService.stopForeground(NOTI_ID)
        isForeground = false
    }

    fun update(timeSetName: String, timer: String, repeat: String, notifiactionButtonType_: NotifiactionButtonType) {
        "update $timeSetName $timer $repeat $notifiactionButtonType_ ".i(TAG)
        notifiactionButtonType = notifiactionButtonType_
        remoteViews!!.run {

            setTextViewText(R.id.notiTvTimeSetName, timeSetName)
            setTextViewText(R.id.notiTvTimer, timer)
            setTextViewText(R.id.notiTvRepeat, repeat)

            setViewVisibility(R.id.notiIvCtrlPlay, View.GONE)
            setViewVisibility(R.id.notiIvCtrlPause, View.GONE)
            setViewVisibility(R.id.notiIvCtrlSoundOff, View.GONE)

            when (notifiactionButtonType_) {
                NotifiactionButtonType.PLAY -> setViewVisibility(R.id.notiIvCtrlPlay, View.VISIBLE)
                NotifiactionButtonType.PAUSE -> setViewVisibility(R.id.notiIvCtrlPause, View.VISIBLE)
                NotifiactionButtonType.SPEAKER -> setViewVisibility(R.id.notiIvCtrlSoundOff, View.VISIBLE)
            }

        }

        notificationManager?.notify(NOTI_ID, noti)
    }

    fun getRemoteView(): RemoteViews {
        val remoteViews = RemoteViews(timingService.packageName, R.layout.noti_timing)

        val soundOffPendingIntent = PendingIntent.getService(timingService, 0, Intent(CMD_SERVICE.SOUND_OFF), 0)
        val playPendingIntent = PendingIntent.getService(timingService, 0, Intent(CMD_SERVICE.RESTART), 0)
        val pausePendingIntent = PendingIntent.getService(timingService, 0, Intent(CMD_SERVICE.PAUSE), 0)

        remoteViews.setOnClickPendingIntent(R.id.notiIvCtrlPlay, playPendingIntent)
        remoteViews.setOnClickPendingIntent(R.id.notiIvCtrlPause, pausePendingIntent)
        remoteViews.setOnClickPendingIntent(R.id.notiIvCtrlSoundOff, soundOffPendingIntent)

        return remoteViews
    }

}

enum class NotifiactionButtonType {
    PLAY,
    PAUSE,
    SPEAKER,

}
