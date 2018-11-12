package seoft.co.kr.android_poc

import android.app.Service
import android.content.Context
import android.content.Intent
import android.graphics.PixelFormat
import android.os.Build
import android.os.IBinder
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.ImageButton
import android.widget.TextView


class MyService : Service() {

    lateinit var wm: WindowManager
    lateinit var mView: View

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()
        val inflate = getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater?
        wm = getSystemService(WINDOW_SERVICE) as WindowManager

//        val params = WindowManager.LayoutParams(
//                /*ViewGroup.LayoutParams.MATCH_PARENT*/300,
//                ViewGroup.LayoutParams.WRAP_CONTENT,
//                WindowManager.LayoutParams.TYPE_SYSTEM_ALERT,
//                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
//                        or WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
//                        or WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH,
//                PixelFormat.TRANSLUCENT)

        val params : WindowManager.LayoutParams

        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {

            params = WindowManager.LayoutParams(
//                    WindowManager.LayoutParams.MATCH_PARENT,
                    300,
                    WindowManager.LayoutParams.WRAP_CONTENT,
                    WindowManager.LayoutParams.TYPE_SYSTEM_ALERT,
                    WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL,
                    PixelFormat.TRANSLUCENT)

        } else {
            params = WindowManager.LayoutParams(
//                    WindowManager.LayoutParams.MATCH_PARENT,
                    300,
                    WindowManager.LayoutParams.WRAP_CONTENT,
                    WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
                    WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL,
                    PixelFormat.TRANSLUCENT)
        }

        params.gravity = Gravity.LEFT or Gravity.TOP
        mView = inflate!!.inflate(R.layout.view_in_service, null)

        val textView = mView.findViewById(R.id.textView) as TextView
        val bt = mView.findViewById(R.id.bt) as ImageButton

        bt.setOnClickListener { v ->
            bt.setImageResource(R.mipmap.ic_launcher_round)
            textView.text = "on click!!"
        }

        wm.addView(mView, params)
    }

    override fun onDestroy() {
        super.onDestroy()
        wm.removeView(mView)
    }


}