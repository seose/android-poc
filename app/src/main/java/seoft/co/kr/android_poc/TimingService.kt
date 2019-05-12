package seoft.co.kr.android_poc

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.Build
import android.os.IBinder
import seoft.co.kr.android_poc.TimingActivity.Companion.TIMES
import seoft.co.kr.android_poc.util.i
import seoft.co.kr.android_poc.util.toTimeStr
import seoft.co.kr.android_poc.util.x1000L


class TimingService : Service() {

    val TAG = "TimingService#$#"

    lateinit var times : ArrayList<Int>
    lateinit var cdt : PreciseCountdown

    val timingNotification :TimingNotication by lazy {
        TimingNotication(this,times)
    }

    var arrayCnt = 0
    var isRunning = false
    var isPause = false
    var pauseTimer = 0L

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
        if(isPause) {
            val time = pauseTimer.toTimeStr()
            broadcastTimeAndRound(time)
            return super.onStartCommand(intent, flags, startId)
        }
        if(intent == null) return super.onStartCommand(intent, flags, startId)

        times = intent.getIntegerArrayListExtra(TIMES)
        restart()


        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){

        }

        return super.onStartCommand(intent, flags, startId)
    }

    /**
     * service stop (not self stop) is only TimingActivity pass this method, broadcast
     */
    fun stop(){
        "stop".i(TAG)
        cancelTimerStatus()
        sendBroadcast(Intent(CMD_BRD.STOP))
        isPause = false
    }

    fun pause(){
        "pause".i(TAG)
        cancelTimerStatus(false)
        isPause = true
    }

    fun restart(){

        if(!isRunning && !isPause) timingNotification.showNotification()

        "restart".i(TAG)
        startTimer()
    }



    /**
     * startTimer is called repeat on PreciseCountdown ( recursive )
     */
    fun startTimer(){

        if(isRunning) return

        val insertTimer : Long = if(isPause) {
            isPause = false
            pauseTimer
        } else times[arrayCnt].x1000L()

        cdt = object : PreciseCountdown(insertTimer,1000L) {
            override fun onFinished() {
                isRunning = false
                arrayCnt++
                if (arrayCnt == times.size) {
                    cancelTimerStatus()
                    "sendBroadcast     END".i(TAG)
                    sendBroadcast(Intent(CMD_BRD.END))
                    stopSelf()
                } else {
                    cdt.cancel()
                    startTimer()
                }
            }

            override fun onTick(millisUntilFinished: Long) {
                pauseTimer = millisUntilFinished
                val time = millisUntilFinished.toTimeStr()
                "sendBroadcast     millisUntilFinished $millisUntilFinished     time $time".i(TAG)

                broadcastTimeAndRound(time)
                timingNotification.update(time)
            }
        }
        isRunning = true
        cdt.start()
    }

    fun broadcastTimeAndRound(timeStr:String){
        sendBroadcast(Intent(CMD_BRD.TIME).apply { putExtra(CMD_BRD.MSG, timeStr) })
        sendBroadcast(Intent(CMD_BRD.ROUND).apply { putExtra(CMD_BRD.MSG, arrayCnt.toString()) })
    }

    /**
     * param [initArrCnt] is called with false value from only pause()
     */
    fun cancelTimerStatus(initArrCnt : Boolean = true){
        cdt.cancel()
        isRunning = false
        if(initArrCnt) {
//            stopForeground(notiId)
            timingNotification.removeNotification()
            arrayCnt = 0
        }
    }

    /**
     * temp noticification
     *
     * cur status : pendingIntent flags 0 AND TimingActivity's launchMode is singleTask
     */
//    fun getNotification() : Notification{
//        val notificationIntent = Intent(this, TimingActivity::class.java).apply {
//            putIntegerArrayListExtra(TimingActivity.TIMES,times)
//        }
//        notificationIntent.action = "Action2"
//        val pendingIntent = PendingIntent.getActivity(this, 0,
//                notificationIntent, 0 )
//
//        val notification = NotificationCompat.Builder(this, App.CHANNEL_ID)
//                .setContentTitle("...")
//                .setTicker("... ")
//                .setContentText("...")
//                .setSmallIcon( R.drawable.ic_delete)
//                .setContentIntent(pendingIntent)
//                .setOngoing(true).build()
//
//        return notification
//    }

    override fun onDestroy() {
        super.onDestroy()

        cancelTimerStatus()
    }

}
