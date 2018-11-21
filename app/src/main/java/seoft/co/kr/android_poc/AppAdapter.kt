package seoft.co.kr.android_poc

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView

class AppAdapter(apps_ : List<AppDTO>,page:Int ,var cb: (appDto:AppDTO)->Unit) : RecyclerView.Adapter<AppAdapter.ViewHolder>(){

    var apps = ArrayList<AppDTO>()

    init {
        var stt = page * DrawerActivity.ITEM_GRID_NUM
        var end = stt + DrawerActivity.ITEM_GRID_NUM
        while((stt < apps_.size) && (stt < end) )
            apps.add(apps_[stt++])
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AppAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_app,parent,false)
        return ViewHolder(view, cb)
    }

    override fun getItemCount() = apps.size

    override fun onBindViewHolder(holder: AppAdapter.ViewHolder, pos: Int) {
        holder.ivIcon.setImageDrawable(apps[pos].icon)
        holder.tvName.text = apps[pos].name
        holder.appDTO = apps[pos]
    }

    inner class ViewHolder(v: View,var cb: (appDto:AppDTO)->Unit) : RecyclerView.ViewHolder(v) {

        var ivIcon : ImageView
        var tvName : TextView
        lateinit var appDTO : AppDTO

        init {
            ivIcon = v.findViewById(R.id.ivIcon)
            tvName = v.findViewById(R.id.tvName)

            ivIcon.setOnClickListener { v ->
                cb.invoke(appDTO)
            }
        }

    }

}
