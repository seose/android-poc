package seoft.co.kr.android_poc

import android.R
import android.app.Notification
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.Build
import android.os.IBinder
import android.support.v4.app.NotificationCompat
import seoft.co.kr.android_poc.TimingActivity.Companion.TIMES
import seoft.co.kr.android_poc.util.App
import seoft.co.kr.android_poc.util.i
import java.util.concurrent.TimeUnit


class TimingService : Service() {

    val TAG = "TimingService#$#"

    val notiId = 111
    lateinit var times : ArrayList<Int>
    lateinit var cdt : PreciseCountdown
    var arrayCnt = 0
    var isRunning = false

    val binder = TimingServiceBinder()
    inner class TimingServiceBinder : Binder() {
        internal val service: TimingService
            get() = this@TimingService
    }
    override fun onBind(intent: Intent): IBinder? { return binder }

    override fun onCreate() {
        super.onCreate()
        isRunning = false
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        if(isRunning) return super.onStartCommand(intent, flags, startId)
        if(intent == null) return super.onStartCommand(intent, flags, startId)

        times = intent.getIntegerArrayListExtra(TIMES)
        startTimer(/*arrayCnt*/)


        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){

        }

        val noti = getNotification()

        startForeground(notiId , noti)

        return super.onStartCommand(intent, flags, startId)
    }

    fun stop(){
        "stop".i(TAG)
        cancelTimerStatus()
    }

    fun pause(){
        "pause".i(TAG)

    }

    fun restart(){
        "restart".i(TAG)
        startTimer()
    }



    /**
     * startTimer is called repeat on PreciseCountdown ( recursive )
     */
    fun startTimer(/*idx:Int*/){

        if(isRunning) return

        cdt = object : PreciseCountdown(x1000L(times[arrayCnt]),1000L) {
            override fun onFinished() {

                isRunning= false
                arrayCnt++
                if (arrayCnt == times.size) {
                    cancelTimerStatus()
                    "sendBroadcast     END".i(TAG)
                    sendBroadcast(Intent(CMD_BRD.END))
                    stopSelf()

                } else {
                    cdt.cancel()
                    startTimer(/*arrayCnt*/)
                }
            }

            override fun onTick(millisUntilFinished: Long) {
                val time = longToString(millisUntilFinished)
                "sendBroadcast     millisUntilFinished $millisUntilFinished     time $time".i(TAG)

                sendBroadcast(Intent(CMD_BRD.TIME).apply { putExtra(CMD_BRD.MSG, time) })
                sendBroadcast(Intent(CMD_BRD.ROUND).apply { putExtra(CMD_BRD.MSG, arrayCnt.toString()) })
            }
        }
        isRunning = true
        cdt.start()
    }


    fun longToString(time:Long) :String {
        return String.format("%02d:%02d:%02d",
                TimeUnit.MILLISECONDS.toHours(time),
                TimeUnit.MILLISECONDS.toMinutes(time) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(time)),
                TimeUnit.MILLISECONDS.toSeconds(time) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(time)))
    }

    /**
     * for easy convenient convert, milliseconds is long, need to multiply 1000
     */
    fun x1000L(i :Int):Long{
        return i*1000L
    }

    fun cancelTimerStatus(){
        cdt.cancel()
        isRunning = false
        arrayCnt = 0
    }

    /**
     * temp noticification
     *
     * cur status : pendingIntent flags 0 AND TimingActivity's launchMode is singleTask
     */
    fun getNotification() : Notification{
        val notificationIntent = Intent(this, TimingActivity::class.java).apply {
            putIntegerArrayListExtra(TimingActivity.TIMES,times)
        }
        notificationIntent.action = "Action2"
        val pendingIntent = PendingIntent.getActivity(this, 0,
                notificationIntent, 0 )

        val notification = NotificationCompat.Builder(this, App.CHANNEL_ID)
                .setContentTitle("...")
                .setTicker("... ")
                .setContentText("...")
                .setSmallIcon( R.drawable.ic_delete)
                .setContentIntent(pendingIntent)
                .setOngoing(true).build()

        return notification
    }

    override fun onDestroy() {
        super.onDestroy()

        cancelTimerStatus()
    }

}
