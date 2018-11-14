package seoft.co.kr.android_poc

import android.content.Intent
import android.content.pm.ResolveInfo
import android.net.nsd.NsdManager
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import kotlinx.android.synthetic.main.activity_drawer.*
import seoft.co.kr.android_poc.util.i
import android.R.attr.name
import android.content.ComponentName



class DrawerActivity : AppCompatActivity() {

    val TAG = "DrawerActivity#$#"

    lateinit var appList : List<AppDTO>
    lateinit var appAdapter: AppAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_drawer)

        appList = getAllApplications(this)

        appList.map { "${it.name} ${it.pkgName} ${it.icon}".i(TAG) }

        val lm = GridLayoutManager(this,5)
        appAdapter = AppAdapter(appList){
            
            val compname = ComponentName(it.pkgName, it.optName)
            val actintent = Intent(Intent.ACTION_MAIN)
                    .apply {
                        addCategory(Intent.CATEGORY_LAUNCHER)
                        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED
                        component = compname

                    }
            applicationContext.startActivity(actintent)
        }

        rvApps.layoutManager = lm
        rvApps.adapter = appAdapter




    }


}
