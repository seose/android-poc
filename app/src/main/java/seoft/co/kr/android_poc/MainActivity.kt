package seoft.co.kr.android_poc

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.provider.Settings
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.util.Log
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity(){

    val TAG = "MainActivity#$#"

    val REQ_PERMISSIONS = 101
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        reqPermission()

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

    fun reqPermission(){
        val settingsCanWrite = Settings.System.canWrite(this)
        if (!settingsCanWrite) {
            val intent = Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS).
                    apply { data = Uri.parse("package:seoft.co.kr.android_poc") }
            startActivity(intent)
        } else {
            val alertDialog = AlertDialog.Builder(this@MainActivity).create()
            alertDialog.setMessage("You have system write settings permission now.")
            alertDialog.show()
        }
    }


    fun AAA() {

        val defaultTurnOffTime = Settings.System.getInt(contentResolver, Settings.System.SCREEN_OFF_TIMEOUT, 30000)
        val defaultBrightness = Settings.System.getInt(contentResolver, Settings.System.SCREEN_BRIGHTNESS, 150)

        Log.i(TAG,defaultTurnOffTime.toString())

        Settings.System.putInt(contentResolver,Settings.System.SCREEN_OFF_TIMEOUT, 1)
        Settings.System.putInt(contentResolver,Settings.System.SCREEN_BRIGHTNESS, 0)
        Log.i(TAG,"Settings.System.putInt(contentResolver,Settings.System.SCREEN_OFF_TIMEOUT, 1000)")

        val intent = Intent(applicationContext,BlackScreen::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY)
            addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
        }

        startActivity(intent)

        Handler().postDelayed( {
            Settings.System.putInt(contentResolver,Settings.System.SCREEN_OFF_TIMEOUT, defaultTurnOffTime)
            Settings.System.putInt(contentResolver,Settings.System.SCREEN_BRIGHTNESS, defaultBrightness)
            Log.i(TAG,"Settings.System.putInt(contentResolver,Settings.System.SCREEN_OFF_TIMEOUT, defaultTurnOffTime)")

        },3000)

    }

    fun BBB() {

    }

    fun CCC() {


    }

    fun DDD() {


    }


}
