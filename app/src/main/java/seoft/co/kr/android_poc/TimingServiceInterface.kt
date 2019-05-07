package seoft.co.kr.android_poc

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.IBinder

class TimingServiceInterface(val ctx: Context) {

    var connection : ServiceConnection?
    var service : TimingService? = null

    init {
        connection = object : ServiceConnection{
            override fun onServiceConnected(name: ComponentName?, iBinder: IBinder?) {
                service = (iBinder as TimingService.TimingServiceBinder).service
            }

            override fun onServiceDisconnected(name: ComponentName?) {
                connection = null
                service = null
            }
        }
        val intent = Intent(ctx,TimingService::class.java)
        ctx.bindService(intent,connection,Context.BIND_AUTO_CREATE)
    }

    fun unbindService(){

        ctx.unbindService(connection)

    }


    fun restart(){
        service?.restart()
    }

    fun pause(){
        service?.pause()
    }

    fun stop(){
        service?.stop()
    }






}