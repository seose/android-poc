package seoft.co.kr.android_poc

import android.content.Intent
import android.content.pm.ResolveInfo
import android.net.nsd.NsdManager
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import seoft.co.kr.android_poc.util.i

class DrawerActivity : AppCompatActivity() {

    val TAG = "DrawerActivity#$#"

    lateinit var appList : List<AppDTO>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_drawer)

        appList = getAllApplications(this)

        appList.map { "${it.name} ${it.pkgName} ${it.icon}".i(TAG) }





    }


}
