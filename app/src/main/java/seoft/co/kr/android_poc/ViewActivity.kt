package seoft.co.kr.android_poc

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.RelativeLayout
import kotlinx.android.synthetic.main.activity_view.*
import seoft.co.kr.android_poc.util.i

class ViewActivity : AppCompatActivity() {

    val TAG = "ViewActivity#$#"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view)

        tvClock.textSize = SettingActivity.fontSize.toFloat()

        tvClock.layoutParams = RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,RelativeLayout.LayoutParams.MATCH_PARENT)
                .apply {
                    leftMargin = SettingActivity.saveX
                    topMargin = SettingActivity.saveY }

//        tvClock.layoutParams = layoutParams


    }
}
