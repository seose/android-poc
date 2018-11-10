package seoft.co.kr.android_poc

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.view.accessibility.AccessibilityEventCompat
import android.support.v7.app.AppCompatActivity
import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityManager
import kotlinx.android.synthetic.main.activity_main.*
import seoft.co.kr.android_poc.MyAccessibilityService.Companion.STATUS_BAR_OPEN
import android.os.Build
import java.lang.reflect.InvocationTargetException
import java.lang.reflect.Method


class HomeActivity : AppCompatActivity() {

    val TAG = "HomeActivity#$#"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        btA.setOnClickListener { v ->
            AAA()
        }

        btB.setOnClickListener { v ->
            BBB()
        }

    }

    fun AAA() {
        startActivity(Intent(applicationContext,MenuActivity::class.java))
    }


    fun BBB() {
//        val event = AccessibilityEvent.obtain(AccessibilityEvent.TYPE_TOUCH_INTERACTION_END)
//                .apply {
//                    className = "MyAccessibilityService"
//                    text.add("noti")
//                    action = STATUS_BAR_OPEN
//                    packageName = "seoft.co.kr.android_poc"
//                    isEnabled = true
//                }
//
//        val manager = getSystemService(Context.ACCESSIBILITY_SERVICE)
//        val recordCompat = AccessibilityEventCompat.asRecord(event)

        openStatusBar(this)

    }

    fun openStatusBar(ctx: Context) {

        try {
            val sbservice = ctx.getSystemService("statusbar")
            val statusbarManager = Class.forName("android.app.StatusBarManager")
            val showsb: Method
            if (Build.VERSION.SDK_INT >= 17) {
                showsb = statusbarManager.getMethod("expandNotificationsPanel")
            } else {
                showsb = statusbarManager.getMethod("expand")
            }
            showsb.invoke(sbservice)
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    override fun onBackPressed() { }



}