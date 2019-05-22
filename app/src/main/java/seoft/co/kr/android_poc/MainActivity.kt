package seoft.co.kr.android_poc

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(){

    lateinit var gyro:Gyro

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
        gyro = Gyro(this)
    }

    override fun onResume() {
        super.onResume()
        gyro.callWhenOnResume()
    }

    override fun onPause() {
        super.onPause()
        gyro.callWhenOnPause()
    }

    fun BBB() {

    }

    fun CCC() {

    }

    fun DDD() {

    }
}
