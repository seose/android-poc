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
import seoft.co.kr.android_poc.util.toast
import seoft.co.kr.android_poc.util.x1000L
import java.text.SimpleDateFormat
import java.util.*

/**
 * LOGIC :
 *
 * 1. mainAct -> TimingAct
 * 2. regist BRC & play readying(or refresh view) in Resume
 * 3. wait readying use postDelayed
 * 4. run service with noti
 * 5. bring data from service
 *
 * Last. unregist BRC & unbinding service & (if not playing stop service + finish activity) & stop self in service
 *
 * 0812
 * all time : calculate in onCreate / save to allTimeSt(static) / load in onResume
 *
 * end time :
 * A.
 * - calculate and load with readySec in onCreate
 * B.
 * - calculate only remain time in service's restart method
 * - load in broadcast callback when call BRD.REMAIN_SEC MSG
 * save to endTimeStr
 *
 *
 */

class TimingActivity : AppCompatActivity() {

    val TAG = "TimingActivity#$#"

    companion object {
        val TIMES = "TIMES"
        val READY_SEC = "READY_SEC"

        // TODO : Connect sharedPreference this proerties
        private var endTimeStr = ""
        private var allTimeStr = ""
        private var addingMinute = 0
    }

    lateinit var times: ArrayList<Int>
    var readySec: Int = 0
    var canReady = true // exception of out when postDelay
    lateinit var timeBrd: BroadcastReceiver
    lateinit var svcIntent: Intent
    var timingServiceInterface: TimingServiceInterface? = null
    private val format = SimpleDateFormat("hh:mm a", Locale.US)

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
        initBrd()

        // set end time
        val allTime =  times.reduce { acc, i ->  acc+i }
        if(endTimeStr.isEmpty()) endTimeStr = getEndTimeStringAfterSecond(readySec + allTime)
        if(allTimeStr.isEmpty()) allTimeStr = allTime.x1000L().toTimeStr() // need to [if] for call from notification when remove activity status

    }

    fun initBrd(){
        timeBrd = object : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent) {

                when (intent.action) {
                    CMD_BRD.TIME -> tvTime.text = intent.getStringExtra(CMD_BRD.MSG)
                    CMD_BRD.ROUND -> tvRound.text = intent.getStringExtra(CMD_BRD.MSG)
                    CMD_BRD.END -> {
                        tvTime.text = "종료 브로드케스팅 받음"
                        endTimeStr = ""
                        allTimeStr = ""
                        finish()
                    }
                    CMD_BRD.STOP -> {
                        tvTime.text = times[0].x1000L().toTimeStr()
                        tvRound.text = "0"
                        if(TimingService.timingService != null) TimingService.timingService = null
                        endTimeStr = ""
                        allTimeStr = ""
                        finish()
                    }
                    CMD_BRD.REMAIN_SEC -> {
                        val remainSecond = intent.getLongExtra(CMD_BRD.MSG,0)
                        "CMD_BRD.REMAIN_SEC : $remainSecond".i()
                        endTimeStr = getEndTimeStringAfterSecond(remainSecond.toInt())
                        updateEndingView()
                    }
                    CMD_BRD.UPDATE_REPEAT_BTN -> {
                        val turn = intent.getBooleanExtra(CMD_BRD.MSG,false)
                        "CMD_BRD.REPEAT : $turn".i()
                        updateRepeat(turn)
                    }
                    CMD_BRD.UPDATE_REPEAT_CNT -> {
                        val repeatCnt = intent.getIntExtra(CMD_BRD.MSG,0)
                        "CMD_BRD.UPDATE_REPEAT_CNT : $repeatCnt".i()
                        "CMD_BRD.UPDATE_REPEAT_CNT : $repeatCnt".toast()
                    }
                }
            }
        }
    }

    fun updateRepeat(turn: Boolean) {
        if(turn) btRepeat.text = "repeat on"
        else btRepeat.text = "repeat off"
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

        // TODO : block push button before reading
        btRestart.setOnClickListener {
            TimingService.timingService?.let { timingServiceInterface?.restart() }
        }
        btPause.setOnClickListener {
            TimingService.timingService?.let { timingServiceInterface?.pause() }
        }
        btCancel.setOnClickListener {
            TimingService.timingService?.let { timingServiceInterface?.stop() }
        }
        btAdd.setOnClickListener {
            TimingService.timingService?.let { timingServiceInterface?.addMin() }
            addingMinute++
            updateAddingView()
        }
        bt1.setOnClickListener {

        }
        bt2.setOnClickListener {
            moveTimeBadge(0)
        }
        bt3.setOnClickListener {
            moveTimeBadge(1)
        }
        bt4.setOnClickListener {
            moveTimeBadge(2)
        }
        btRepeat.setOnClickListener {
            timingServiceInterface?.turnRepeat()
        }
    }

    fun moveTimeBadge(pos:Int){
        TimingService.timingService?.let { timingServiceInterface?.move(pos)}
        addingMinute=0
        updateAddingView()
    }

    /**
     * call updateAddingView, updateEndingView from onResume, pushed button
     */
    fun updateAddingView(){
        // TODO if addingMinute == 0 -> invisible , else visible
        tvAdd.text = "+${addingMinute}분"
    }
    fun updateEndingView(){ tvEndTime.text = endTimeStr }

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
            addAction(CMD_BRD.REMAIN_SEC)
            addAction(CMD_BRD.UPDATE_REPEAT_BTN)
            addAction(CMD_BRD.UPDATE_REPEAT_CNT)

        })

        canReady = true

        timingServiceInterface = TimingServiceInterface(this)

//        "onResume TimingService.timingService is null : ${TimingService.timingService == null}".i()

        TimingService.timingService?.let {

            Handler().postDelayed({ timingServiceInterface?.updateActViewNow() }, 50) // wait for registReceiver

        } ?: readying(readySec - 1)


        // init view

        tvAllTime.text = allTimeStr
        updateEndingView()
        updateAddingView()
        timingServiceInterface?.getRepeat()

    }

    override fun onPause() {
        super.onPause()
        canReady = false
        unregisterReceiver(timeBrd)

        timingServiceInterface?.unbindService()
    }

    fun getEndTimeStringAfterSecond(sec:Int) :String {

        val gCalendar = GregorianCalendar()
        format.calendar = gCalendar
        gCalendar.add(Calendar.SECOND,sec)
        return format.format(gCalendar.time)
    }

}
