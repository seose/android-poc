package seoft.co.kr.another_app

import android.annotation.SuppressLint
import android.arch.persistence.room.Room
import android.util.Log
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers

@SuppressLint("CheckResult")
class LocalPersonApiRepo {

    val TAG = "LocalPersonApiRepo#$#"

    private val local = android.arch.persistence.room.Room.databaseBuilder(App.get.applicationContext, AppDatabase::class.java, "person.db")
            .allowMainThreadQueries()
            .build()

    fun findPersons() {

        local.personDao().findPersons()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({

                    Log.i(TAG, "${it.size}")


                    it.map {
                        Log.i(TAG,it.toString())

                    }
                }, { error ->
                    Log.i(TAG,"error")
                })
    }


    fun insertPerson(person:Person) {

        Single.fromCallable { local.personDao().insertPerson(person) }
                .subscribe({
                    Log.i(TAG,"success")
                }, { error ->
                    Log.i(TAG,"error")
                })
    }


}