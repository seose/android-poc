package seoft.co.kr.android_poc

import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*
import android.provider.Settings.canDrawOverlays
import android.os.Build
import android.provider.Settings
import android.content.Intent
import android.net.Uri


// ref : http://milkissboy.tistory.com/46
// and fixed bug : http://gogorchg.tistory.com/entry/Android-Deprecated-TYPESYSTEMALERT

class MainActivity : AppCompatActivity() {

    val ACTION_MANAGE_OVERLAY_PERMISSION_REQUEST_CODE = 1

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
        checkPermission()
    }

    fun checkPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {   // 마시멜로우 이상일 경우
            if (!Settings.canDrawOverlays(this)) {              // 체크
                val intent = Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                        Uri.parse("package:$packageName"))
                startActivityForResult(intent, ACTION_MANAGE_OVERLAY_PERMISSION_REQUEST_CODE)
            } else {
                startService(Intent(this@MainActivity, MyService::class.java))
            }
        } else {
            startService(Intent(this@MainActivity, MyService::class.java))
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (!Settings.canDrawOverlays(this)) {
            finish()
        } else {
            startService(Intent(this@MainActivity, MyService::class.java))
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    fun BBB() {
        stopService(Intent(this@MainActivity, MyService::class.java))
    }



    fun CCC() {

    }

    fun DDD() {

    }


}
