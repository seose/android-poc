package seoft.co.kr.android_poc

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_timing.*
import seoft.co.kr.android_poc.util.i
import seoft.co.kr.android_poc.util.toTimeStr
import seoft.co.kr.android_poc.util.x1000L


class TimingActivity : AppCompatActivity() {

    val TAG = "TimingActivity#$#"

    companion object {
        val TIMES = "TIMES"
        val READY_SEC = "READY_SEC"

    }

    lateinit var times: ArrayList<Int>
    var readySec: Int = 0
    var canReady = true // exception of out when postDelay
    lateinit var timeBrd: BroadcastReceiver
    lateinit var svcIntent: Intent
    var timingServiceInterface: TimingServiceInterface? = null

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        "onNewIntent".i(TAG)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_timing)
        "onCreate".i(TAG)

        times = intent.getIntegerArrayListExtra(TIMES)
        readySec = intent.getIntExtra(READY_SEC, 5)
        initListener()

    }


    fun startServiceFromActivity() {
        svcIntent = Intent(this, TimingService::class.java)
                .apply {
                    putIntegerArrayListExtra(TIMES, times)
                    putExtra(READY_SEC, readySec)
                    action = CMD_SERVICE.START_WITH_TIMERS
                }


        startService(svcIntent)
    }

    fun initListener() {

        btRestart.setOnClickListener {
            TimingService.timingService?.let { timingServiceInterface?.restart() }
        }
        btPause.setOnClickListener {
            TimingService.timingService?.let { timingServiceInterface?.pause() }
        }
        btStop.setOnClickListener {
            TimingService.timingService.let { timingServiceInterface?.stop() }
        }

        timeBrd = object : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent) {

                when (intent.action) {
                    CMD_BRD.TIME -> tvTime.text = intent.getStringExtra(CMD_BRD.MSG)
                    CMD_BRD.ROUND -> tvRound.text = intent.getStringExtra(CMD_BRD.MSG)
                    CMD_BRD.END -> tvTime.text = "종료 브로드케스팅 받음"
                    CMD_BRD.STOP -> {
                        tvTime.text = times[0].x1000L().toTimeStr()
                        tvRound.text = "0"
                        finish()
                    }
                }
            }
        }

    }

    fun readying(cnt: Int) {
        tvReady.text = "$cnt"
        if (!canReady) return
        if (cnt == 0) {
            startServiceFromActivity()
            return
        }
        Handler().postDelayed({
            readying(cnt - 1)
        }, 1000)

    }

    override fun onResume() {
        super.onResume()
        registerReceiver(timeBrd, IntentFilter().apply {
            addAction(CMD_BRD.ROUND)
            addAction(CMD_BRD.TIME)
            addAction(CMD_BRD.END)
            addAction(CMD_BRD.STOP)
        })

        canReady = true

        timingServiceInterface = TimingServiceInterface(this)

        "onResume TimingService.timingService is null : ${TimingService.timingService == null}".i()

        TimingService.timingService?.let {
            Handler().postDelayed({ timingServiceInterface?.updateActViewNow() }, 50) // wait for registReceiver
        } ?: readying(readySec - 1)


    }

    override fun onPause() {
        super.onPause()
        canReady = false
        unregisterReceiver(timeBrd)

        // !isPause AND !isRunning = status of stop
//        if (!timingServiceInterface.service!!.isPause && !timingServiceInterface.service!!.isRunning) {
        if (TimingService.timingService != null
                && !timingServiceInterface?.service!!.isPause
                && !timingServiceInterface?.service!!.isRunning) {
            stopService(svcIntent)
        }

        timingServiceInterface?.unbindService()
    }


}
