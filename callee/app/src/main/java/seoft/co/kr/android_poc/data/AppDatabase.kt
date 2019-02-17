package seoft.co.kr.android_poc.data

import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import seoft.co.kr.android_poc.util.App

@Database(entities = arrayOf(Person::class) , version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun personDao() : PersonDao

    companion object {
        val INSTANCE: AppDatabase by lazy {
            Room.databaseBuilder(App.get.applicationContext,AppDatabase::class.java,"myDb.db")
                    .allowMainThreadQueries()
                    .build()
        }
    }



}