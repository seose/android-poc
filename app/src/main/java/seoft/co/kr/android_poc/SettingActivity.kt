package seoft.co.kr.android_poc

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.text.Layout
import android.util.DisplayMetrics
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.widget.RelativeLayout
import android.widget.SeekBar
import kotlinx.android.synthetic.main.activity_setting.*
import seoft.co.kr.android_poc.util.i

class SettingActivity : AppCompatActivity() {

    companion object{
        var saveX = 10
        var saveY = 10
        var fontSize = 10
    }

    val TAG = "SettingActivity#$#"

    var xx = 0
    var yy = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)

        tvClock.textSize = SettingActivity.fontSize.toFloat()

        val layoutParams = RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,RelativeLayout.LayoutParams.MATCH_PARENT)
        layoutParams.leftMargin = SettingActivity.saveX
        layoutParams.topMargin = SettingActivity.saveY
        tvClock.layoutParams = layoutParams

        sbFontSize.progress = SettingActivity.fontSize

        tvClock.setOnTouchListener(ChoiceTouchListener())

        sbFontSize.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(p0: SeekBar?, fSize: Int, p2: Boolean) {
                tvClock.textSize = fSize.toFloat()
                fontSize = fSize
            }
            override fun onStartTrackingTouch(p0: SeekBar?) {}
            override fun onStopTrackingTouch(p0: SeekBar?) {}

        })

    }


    inner class ChoiceTouchListener : View.OnTouchListener{
        override fun onTouch(view: View?, event: MotionEvent?): Boolean {

            event?.let {
                view?.let {

                    val X = event.rawX.toInt()
                    val Y = event.rawY.toInt()
                    when (event.getAction() and MotionEvent.ACTION_MASK) {
                        MotionEvent.ACTION_DOWN -> {
                            val lParams = view.layoutParams as RelativeLayout.LayoutParams
                            xx = X - lParams.leftMargin
                            yy = Y - lParams.topMargin
                        }
                        MotionEvent.ACTION_MOVE -> {
                            val layoutParams = view.layoutParams as RelativeLayout.LayoutParams

                            layoutParams.leftMargin = X - xx
                            layoutParams.topMargin = Y - yy
                            saveX = X - xx
                            saveY = Y - yy

                            view.layoutParams = layoutParams
                        }
                    }
                }
                rlRoot.invalidate()
            }
            return true
        }


    }


}
