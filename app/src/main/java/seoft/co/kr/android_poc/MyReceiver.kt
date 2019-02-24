package seoft.co.kr.android_poc

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import seoft.co.kr.android_poc.util.i

class MyReceiver:BroadcastReceiver(){

    val TAG = "MyReceiver#$#"

    override fun onReceive(context: Context, intent: Intent) {

        val packageName = intent.data.schemeSpecificPart
        val action = intent.action

        if (action == Intent.ACTION_PACKAGE_ADDED) {
            "ACTION_PACKAGE_ADDED : $packageName".i(TAG)
        } else if (action == Intent.ACTION_PACKAGE_REMOVED) {
            "ACTION_PACKAGE_REMOVED : $packageName".i(TAG)
        }else if (action == Intent.ACTION_PACKAGE_FULLY_REMOVED) {
            "ACTION_PACKAGE_FULLY_REMOVED : $packageName".i(TAG)
        }

    }

}