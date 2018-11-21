package seoft.co.kr.android_poc

import android.content.ComponentName
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_drawer.*
import seoft.co.kr.android_poc.util.i
import seoft.co.kr.android_poc.util.toast


class DrawerActivity : AppCompatActivity() {

    val TAG = "DrawerActivity#$#"

    var recyclerViews:ArrayList<RecyclerView> = arrayListOf()

    lateinit var viewPagerAdapter: ViewPagerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_drawer)

        initViews()
        initDatas()

    }

    fun initViews(){
        viewPagerAdapter = ViewPagerAdapter(recyclerViews)
        vpDrawer.adapter = viewPagerAdapter
    }

    fun initDatas(){
        val pageSize = if(SC.apps.size % ITEM_GRID_NUM == 0)
            SC.apps.size / ITEM_GRID_NUM
        else
            SC.apps.size / ITEM_GRID_NUM + 1

        for ( i in 0 until pageSize) {
            val rv = RecyclerView(this)
            val lm = GridLayoutManager(this,NUMBER_OF_COLUMNS)
            val appAdapter = AppAdapter(SC.apps, i) {

                it.toString().toast()

                // launch app
                val compname = ComponentName(it.pkgName, it.optName)
                val actintent = Intent(Intent.ACTION_MAIN)
                        .apply {
                            addCategory(Intent.CATEGORY_LAUNCHER)
                            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED
                            component = compname
                        }
                applicationContext.startActivity(actintent)

            }

            rv.layoutManager = lm
            rv.adapter = appAdapter
            recyclerViews.add(rv)
            viewPagerAdapter.notifyDataSetChanged()
        }



    }

    companion object {
        var ITEM_GRID_NUM = 12
        var NUMBER_OF_COLUMNS = 4
    }



//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_drawer)
//
//        appList = getAllApplications(this)
//
//        appList.map { "${it.name} ${it.pkgName} ${it.icon}".i(TAG) }
//
//        val lm = GridLayoutManager(this,5)
//        appAdapter = AppAdapter(appList){
//
//            val compname = ComponentName(it.pkgName, it.optName)
//            val actintent = Intent(Intent.ACTION_MAIN)
//                    .apply {
//                        addCategory(Intent.CATEGORY_LAUNCHER)
//                        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED
//                        component = compname
//
//                    }
//            applicationContext.startActivity(actintent)
//        }
//
//        rvApps.layoutManager = lm
//        rvApps.adapter = appAdapter
//
//
//
//
//    }


}
