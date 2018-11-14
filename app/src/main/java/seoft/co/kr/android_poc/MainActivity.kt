package seoft.co.kr.android_poc

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*

// ref https://github.com/delaroy/DragNDrop/blob/master/app/src/main/java/com/delaroystudios/dragndrop/MainActivity.java

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
        startActivity(Intent(this,SettingActivity::class.java))
    }

    fun BBB() {
        startActivity(Intent(this,ViewActivity::class.java))

    }

    fun CCC() {

    }

    fun DDD() {

    }
}
