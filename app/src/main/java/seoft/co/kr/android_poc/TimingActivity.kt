package seoft.co.kr.android_poc

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_timing.*
import seoft.co.kr.android_poc.util.i

class TimingActivity : AppCompatActivity() {

    val TAG = "TimingActivity#$#"

    companion object {
        val TIMES = "TIMES"

    }

    lateinit var times : ArrayList<Int>
    lateinit var timeBrd : BroadcastReceiver
    lateinit var svcIntent : Intent
    lateinit var timingServiceInterface : TimingServiceInterface

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_timing)

        times = intent.getIntegerArrayListExtra(TIMES)

        times.toString().i()

        svcIntent = Intent(this,TimingService::class.java)
                .apply {
                    putIntegerArrayListExtra(TIMES,times)
                    }

        startService(svcIntent)

        timingServiceInterface = TimingServiceInterface(this)

        initListener()


    }

    fun initListener(){

        btRestart.setOnClickListener {  v ->
            timingServiceInterface.restart()
        }

        btPause.setOnClickListener {  v ->
            timingServiceInterface.pause()
        }

        btStop.setOnClickListener {  v ->
            timingServiceInterface.stop()
        }

        timeBrd = object : BroadcastReceiver(){
            override fun onReceive(context: Context, intent: Intent) {

                when(intent.action){
                    CMD_BRD.TIME -> tvTime.text = intent.getStringExtra(CMD_BRD.MSG)
                    CMD_BRD.ROUND -> tvRound.text = intent.getStringExtra(CMD_BRD.MSG)
                    CMD_BRD.END -> tvTime.text = "종료 브로드케스팅 받음"
                }
            }
        }

    }

    override fun onResume() {
        super.onResume()
        registerReceiver(timeBrd, IntentFilter().apply {
            addAction(CMD_BRD.ROUND)
            addAction(CMD_BRD.TIME)
            addAction(CMD_BRD.END)
        })
    }

    override fun onPause() {
        super.onPause()
        unregisterReceiver(timeBrd)
        timingServiceInterface.unbindService()
    }





}
