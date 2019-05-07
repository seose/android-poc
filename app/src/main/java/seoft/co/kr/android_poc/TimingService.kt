package seoft.co.kr.android_poc

import android.app.Service
import android.content.Intent
import android.os.IBinder
import seoft.co.kr.android_poc.TimingActivity.Companion.CUR_TIME
import seoft.co.kr.android_poc.TimingActivity.Companion.END
import seoft.co.kr.android_poc.TimingActivity.Companion.TIMES
import seoft.co.kr.android_poc.TimingActivity.Companion.TIME_BRD
import seoft.co.kr.android_poc.util.i
import java.util.concurrent.TimeUnit

class TimingService : Service() {

    val TAG = "TimingService#$#"

    lateinit var times : ArrayList<Int>

//    lateinit var cdt : CountDownTimer
    lateinit var cdt : PreciseCountdown

    var arrayCnt = 0

    var isRunning = false

    override fun onBind(intent: Intent): IBinder? {

        return null
    }

    override fun onCreate() {
        super.onCreate()
        isRunning = false
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {

        if(isRunning) {
            cancelTimerStatus()
        }

        times = intent.getIntegerArrayListExtra(TIMES)
        startTimer(0)
        return super.onStartCommand(intent, flags, startId)
    }

    /**
     * startTimer is called repeat on PreciseCountdown ( recursive )
     */
    fun startTimer(idx:Int){
        cdt = object : PreciseCountdown(x1000L(times[idx]),1000L) {
            override fun onFinished() {
                arrayCnt++
                if (arrayCnt == times.size) {
                    cancelTimerStatus()
                    "sendBroadcast     END".i(TAG)
                    sendBroadcast(Intent(TIME_BRD).apply { putExtra(CUR_TIME, END) })
                } else {
                    cdt.cancel()
                    startTimer(arrayCnt)
                }
            }

            override fun onTick(millisUntilFinished: Long) {
                val time = longToString(millisUntilFinished)
                "sendBroadcast     millisUntilFinished $millisUntilFinished     time $time".i(TAG)
                sendBroadcast(Intent(TIME_BRD).apply { putExtra(CUR_TIME,time) })
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

}
