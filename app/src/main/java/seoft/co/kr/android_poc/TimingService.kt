package seoft.co.kr.android_poc

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import seoft.co.kr.android_poc.TimingActivity.Companion.TIMES
import seoft.co.kr.android_poc.util.i
import seoft.co.kr.android_poc.util.toTimeStr
import seoft.co.kr.android_poc.util.x1000L


class TimingService : Service() {

    val TAG = "TimingService#$#"

    companion object {
        var timingService: TimingService? = null // check service is alive
    }

    lateinit var times: ArrayList<Int>
    lateinit var cdt: PreciseCountdown

    val timingNotification: TimingNotication by lazy {
        TimingNotication(this, times)
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

    override fun onBind(intent: Intent): IBinder? {
        return binder
    }

    override fun onCreate() {
        super.onCreate()
        timingService = this
        isRunning = false
    }


    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        intent?.run {

            "action : $action".i()

            when (action) {
                CMD_SERVICE.START_WITH_TIMERS -> {
                    times = intent.getIntegerArrayListExtra(TIMES)
                    restart()
                }
                CMD_SERVICE.PAUSE -> {
                    pause()
                }
                CMD_SERVICE.RESTART -> {
                    restart()
                }
                CMD_SERVICE.STOP -> {
                    stop()
                }
                CMD_SERVICE.SOUND_OFF -> {
                    soundOff()
                }
            }


        }
//
//        "onStartCommand".i()
//
//        if (isRunning) return super.onStartCommand(intent, flags, startId)
//
//        if (isPause) {
//            val time = pauseTimer.toTimeStr()
//            broadcastTimeAndRound(time)
//            return super.onStartCommand(intent, flags, startId)
//        }
//
//        if (intent == null) return super.onStartCommand(intent, flags, startId)
//
//        times = intent.getIntegerArrayListExtra(TIMES)
//        restart()
//
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//
//        }
//
        return super.onStartCommand(intent, flags, startId)
    }

    fun soundOff(){

    }

    /**
     * service stop (not self stop) is only TimingActivity pass this method, broadcast
     */
    fun stop() {
        "stop".i(TAG)
        cancelTimerStatus()
        isPause = false
        timingService = null

        sendBroadcast(Intent(CMD_BRD.STOP))
    }

    fun pause() {
        "pause".i(TAG)
        cancelTimerStatus(false)
        isPause = true
    }

    fun restart() {

        if (!isRunning && !isPause) timingNotification.showNotification()

        "restart".i(TAG)
        startTimer()
    }


    /**
     * startTimer is called repeat on PreciseCountdown ( recursive )
     */
    fun startTimer() {

        if (isRunning) return

        val insertTimer: Long = if (isPause) {
            isPause = false
            pauseTimer
        } else times[arrayCnt].x1000L()

        cdt = object : PreciseCountdown(insertTimer, 1000L) {
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
                timingNotification.update(
                        "내타임셋",
                        time,
                        arrayCnt.toString(),
                        NotifiactionButtonType.PAUSE
                )
            }
        }
        isRunning = true
        cdt.start()
    }

    /**
     * call when rejoin activity after out activity
     */
    fun updateActViewNow() {
        broadcastTimeAndRound(pauseTimer.toTimeStr())
    }

    fun broadcastTimeAndRound(timeStr: String) {
        sendBroadcast(Intent(CMD_BRD.TIME).apply { putExtra(CMD_BRD.MSG, timeStr) })
        sendBroadcast(Intent(CMD_BRD.ROUND).apply { putExtra(CMD_BRD.MSG, arrayCnt.toString()) })
    }

    /**
     * param [initArrCnt] is called with false value from only pause()
     */
    fun cancelTimerStatus(initArrCnt: Boolean = true) {
        cdt.cancel()
        isRunning = false
        if (initArrCnt) {
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
        timingService = null
    }

}
