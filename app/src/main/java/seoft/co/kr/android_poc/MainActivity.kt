package seoft.co.kr.android_poc

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*


/**
 * ref
 *
 * https://developer.android.com/guide/topics/manifest/activity-element?hl=ko
 * https://github.com/OpenLauncherTeam/openlauncher
 *
 *
 *
 *
 *
 *
 */
class MainActivity : AppCompatActivity() {

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

    }

    fun BBB() {

    }

    fun CCC() {

    }

    fun DDD() {

    }




}
