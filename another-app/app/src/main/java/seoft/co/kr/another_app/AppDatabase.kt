package seoft.co.kr.another_app

import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase

@Database(entities = arrayOf(Person::class), version = 1)
abstract class AppDatabase : RoomDatabase(){
    abstract fun personDao():PersonDao
}
