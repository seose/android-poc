package seoft.co.kr.android_poc.util

import android.app.Application

class App: Application() {

    companion object {
        lateinit var get: App
            private set
    }

    override fun onCreate() {
        super.onCreate()
        get = this
    }

}