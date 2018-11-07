package seoft.co.kr.another_app

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

@Entity(tableName = "PERSON")
data class Person (
        @PrimaryKey(autoGenerate = true)
        var id:Int = 0,

        var name:String,
        var age:Int
)