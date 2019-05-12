package seoft.co.kr.android_poc

import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.support.v4.app.NotificationCompat
import android.widget.RemoteViews
import seoft.co.kr.android_poc.util.App
import seoft.co.kr.android_poc.util.i

class TimingNotication(val timingService: TimingService,val times : ArrayList<Int>){

    val TAG = "TimingNotication"

    val NOTI_ID = 111

    var isForeground = false

    var noti : Notification? = null

    var remoteViews :RemoteViews? = null

    var notificationManager : NotificationManager? = null

    init {
        notificationManager = timingService.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    }

    fun showNotification() {
        "showNotification".i(TAG)

        if (!isForeground) {
            val notificationIntent = Intent(timingService, TimingActivity::class.java).apply {
                putIntegerArrayListExtra(TimingActivity.TIMES,times)
            }
            notificationIntent.action = "TIMING_NOTI_ACTION"
            val pendingIntent = PendingIntent.getActivity(timingService, 0,
                    notificationIntent, 0 )

            remoteViews = getRemoteView()

            val notification = NotificationCompat.Builder(timingService, App.CHANNEL_ID)
                    .setContentText("...")
                    .setSmallIcon( R.drawable.ic_tmp)
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

    fun removeNotification(){
        "removeNotification".i(TAG)
        if(noti != null) noti = null
        timingService.stopForeground(NOTI_ID)
        isForeground = false
    }

    fun update(str:String){
        "update $str".i(TAG)
        remoteViews!!.setTextViewText(R.id.notiTime, str)
        notificationManager?.notify(NOTI_ID,noti)
    }

    fun getRemoteView() : RemoteViews{
        val remoteViews = RemoteViews(timingService.packageName, R.layout.noti_timing)

        // TODO : add more commander view ( ex. button , etc )

        return remoteViews
    }






}