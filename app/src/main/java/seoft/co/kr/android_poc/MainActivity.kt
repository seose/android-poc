package seoft.co.kr.android_poc

import android.content.Intent
import android.content.IntentFilter
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*
import seoft.co.kr.android_poc.util.toast

/**
 * using broadcast
 * ACTION_PACKAGE_ADDED,INSTALL,REMOVED  not work version o
 * but only work ACTION_PACKAGE_FULLY_REMOVED
 */
class MainActivity : AppCompatActivity(){
    private val mPackageReceiver = MyReceiver()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        regist()
    }

    fun regist() {
        val intentFilter = IntentFilter()
        intentFilter.addAction(Intent.ACTION_PACKAGE_ADDED)
        intentFilter.addAction(Intent.ACTION_PACKAGE_INSTALL)
        intentFilter.addAction(Intent.ACTION_PACKAGE_REMOVED)
        intentFilter.addAction(Intent.ACTION_PACKAGE_FULLY_REMOVED)
        intentFilter.addDataScheme("package")
        registerReceiver(mPackageReceiver, intentFilter)
    }

    override fun onDestroy() {
        super.onDestroy()
        if(mPackageReceiver != null) unregisterReceiver(mPackageReceiver)
    }
}
