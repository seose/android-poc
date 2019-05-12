package seoft.co.kr.android_poc

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

		btA.setOnClickListener { v ->
            AAA()
        }

        btB.setOnClickListener { v ->
            BBB()
        }

        btC.setOnClickListener { v ->
            CCC()
        }

        btD.setOnClickListener { v ->
            DDD()
        }
    }

    fun AAA() {

        val times = arrayListOf(3,4,5)

        startActivity(Intent(applicationContext,TimingActivity::class.java).apply{
            putIntegerArrayListExtra(TimingActivity.TIMES,times)
        })


    }

    fun BBB() {

    }

    fun CCC() {

    }

    fun DDD() {

    }
}
