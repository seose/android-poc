package seoft.co.kr.android_poc

import android.support.v4.view.PagerAdapter
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup

class ViewPagerAdapter(var recyclerViews:ArrayList<RecyclerView>) : PagerAdapter() {

    val TAG = "ViewPagerAdapter#$#"

    fun add(recyclerViews_:ArrayList<RecyclerView>) {
        recyclerViews.clear()
        recyclerViews_.forEach {
            recyclerViews.add(it)
        }
    }

    override fun isViewFromObject(v: View, o: Any): Boolean {
        return v == o
    }

    override fun getCount(): Int {
        return recyclerViews.size
    }

    override fun getItemPosition( o: Any): Int {
        return POSITION_NONE
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        container.addView(recyclerViews[position])
        return recyclerViews[position]
    }

    override fun destroyItem(container: ViewGroup, position: Int, o: Any) {
        container.removeView(o as View)
    }

}