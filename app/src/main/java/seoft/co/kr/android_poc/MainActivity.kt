package seoft.co.kr.android_poc

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(),View.OnClickListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


    }

    fun AAA() {

    }

    fun BBB() {

    }

    fun CCC() {

    }

    fun DDD() {

    }




    override fun onClick(v: View?) {
        when(v?.id) {
            btA.id -> AAA()
            btB.id -> BBB()
            btC.id -> CCC()
            btD.id -> DDD()
        }
    }

}
