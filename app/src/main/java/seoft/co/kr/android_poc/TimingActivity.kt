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
        val TIME_BRD = "TIME_BRD"
        val TIMES = "TIMES"

        val CUR_TIME = "CUR_TIME"
        val END = "END"
    }

    lateinit var times : ArrayList<Int>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_timing)

        times = intent.getIntegerArrayListExtra(TIMES)

        times.toString().i()

        val svcIntent = Intent(this,TimingService::class.java)
                .apply {
                    putIntegerArrayListExtra(TIMES,times)
                    }


//        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
//            startForegroundService(svcIntent)
//        else
            startService(svcIntent)




    }

    override fun onResume() {
        super.onResume()
        registerReceiver(timeBrd, IntentFilter(TIME_BRD))
    }

    override fun onPause() {
        super.onPause()
        unregisterReceiver(timeBrd)
    }

    private val timeBrd = object : BroadcastReceiver(){
        override fun onReceive(context: Context, intent: Intent) {

            if(intent.getStringExtra(CUR_TIME) == END) {
                tvTime.text = "종료 브로드케스팅 받음"
            } else {
                val curTime = intent.getStringExtra(CUR_TIME)
                tvTime.text = curTime
            }

        }
    }





}
