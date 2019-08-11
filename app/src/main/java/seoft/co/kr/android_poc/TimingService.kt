package seoft.co.kr.android_poc

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import seoft.co.kr.android_poc.TimingActivity.Companion.TIMES
import seoft.co.kr.android_poc.util.i
import seoft.co.kr.android_poc.util.toTimeStr
import seoft.co.kr.android_poc.util.x1000L

/**
 * LOGIC :
 *
 * 1. start service from TimingAct after readying
 * 2. start timer & show notification when bring CMD_SERVICE.START_WITH_TIMERS cmd from TimingAct
 * 3. get commands when running timer & Adjust command and send command & value to ACT or NOTI
 *
 * Last. cancelTimerStatus() & isPause = false & timingService = null & sendBroadcast(Intent(CMD_BRD.STOP)) from STOP commands
 *
 * 0812
 * move :
 *
 * 1. bring command from activity with move pos
 * 2. refresh arrayCnt to move pos
 * 3. refresh mTimer(current count time set) from move pos, array
 * 4. restart ( with noti, remain sec )
 *
 */

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
    var mTimer = 0L // use when restart from pause status

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

        timingService = this
        "onStartCommand".i()

        intent?.run {

            "action : $action".i()

            when (action) {
                CMD_SERVICE.START_WITH_TIMERS -> {
                    times = intent.getIntegerArrayListExtra(TIMES)
                    restart(StartType.INIT)
                }
                CMD_SERVICE.PAUSE -> {
//                    "timerService ${timingService == null}".i()
                    pause()
                }
                CMD_SERVICE.RESTART -> {
//                    "timerService ${timingService == null}".i()
                    restart(StartType.RESTART)
                }
                CMD_SERVICE.STOP -> {
                    stop()
                }
                CMD_SERVICE.SOUND_OFF -> {
                    soundOff()
                }
            }


        }
        return super.onStartCommand(intent, flags, startId)
    }

    fun soundOff(){

    }

    /**
     * service stop (not self stop) is only TimingActivity pass this method, broadcast
     */
    fun stop() {
        "stop".i(TAG)
        cancelTimerStatus(CancleType.INIT_ARR_CNT)
        isPause = false
        stopSelf()
        timingService = null

        sendBroadcast(Intent(CMD_BRD.STOP))
    }

    fun pause() {
        "pause".i(TAG)
        cancelTimerStatus(CancleType.NONE)
        isPause = true
        timingNotification.update(
                "내타임셋",
                mTimer.toTimeStr(),
                arrayCnt.toString(),
                NotifiactionButtonType.PLAY)
    }

    fun restart(startType: StartType) {

        if (!isRunning && !isPause) timingNotification.showNotification()

        if(startType == StartType.INIT)
            mTimer = times[0].x1000L()

        val sumSecondWithOutMTime = times.drop(arrayCnt).reduce { cur, nxt -> cur + nxt }

        val remainSecond = mTimer/1000 + sumSecondWithOutMTime
        sendBroadcast(Intent(CMD_BRD.REMAIN_SEC).apply { putExtra(CMD_BRD.MSG, remainSecond) })

        "restart".i(TAG)
        startTimer()
        timingNotification.update(
                "내타임셋",
                mTimer.toTimeStr(),
                arrayCnt.toString(),
                NotifiactionButtonType.PAUSE)
    }

    fun addMin(){
        pause()
        mTimer += 60000
        restart(StartType.RESTART)
    }

    fun move(pos:Int){
        arrayCnt = pos
        pause()
        mTimer = times[arrayCnt].x1000L() // set timer picked move
        restart(StartType.RESTART)
    }


    /**
     * startTimer is called repeat on PreciseCountdown ( recursive )
     */
    fun startTimer() {

        if (isRunning) return

        val insertTimer: Long = if (isPause) {
            isPause = false
            mTimer
        } else times[arrayCnt].x1000L()

        cdt = object : PreciseCountdown(insertTimer, 1000L) {

            override fun onFinished() {
                isRunning = false
                arrayCnt++
                if (arrayCnt == times.size) {
                    cancelTimerStatus(CancleType.INIT_ARR_CNT)
                    "sendBroadcast     END".i(TAG)
                    sendBroadcast(Intent(CMD_BRD.END))
                    stopSelf()
                } else {
                    cdt.cancel()
                    startTimer()
                }
            }

            override fun onTick(millisUntilFinished: Long) {
                mTimer = millisUntilFinished
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
        broadcastTimeAndRound(mTimer.toTimeStr())
    }

    fun broadcastTimeAndRound(timeStr: String) {
        sendBroadcast(Intent(CMD_BRD.TIME).apply { putExtra(CMD_BRD.MSG, timeStr) })
        sendBroadcast(Intent(CMD_BRD.ROUND).apply { putExtra(CMD_BRD.MSG, arrayCnt.toString()) })
    }

    /**
     * param [initArrCnt] is called with false value from only pause()
     */
    fun cancelTimerStatus(/*initArrCnt: Boolean = true*/ cancleType : CancleType) {
        cdt.cancel()
        isRunning = false
        if (cancleType == CancleType.INIT_ARR_CNT) {
//            stopForeground(notiId)
            timingNotification.removeNotification()
            arrayCnt = 0
        }
    }

    override fun onDestroy() {
        super.onDestroy()

        cancelTimerStatus(CancleType.INIT_ARR_CNT)
        timingService = null
    }

    enum class CancleType{
        NONE,
        INIT_ARR_CNT
    }

    enum class StartType{
        INIT,
        RESTART
    }
}
