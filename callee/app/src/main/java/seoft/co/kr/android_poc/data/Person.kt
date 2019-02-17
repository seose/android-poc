package seoft.co.kr.android_poc.data

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import android.content.ContentValues
import android.provider.BaseColumns

@Entity(tableName = Person.TABLE_NAME)
data class Person(
        @PrimaryKey(autoGenerate = true)
        @ColumnInfo(name = COLUMN_ID)
        var id : Long? = null,
        @ColumnInfo(name = COLUMN_NAME)
        var name:String


) {
    companion object {
        const val TABLE_NAME = "PERSON"
        const val COLUMN_ID = BaseColumns._ID
        const val COLUMN_NAME = "name"

        fun fromContentValues(values: ContentValues) : Person {
            return Person( if(values.containsKey(COLUMN_ID)) values.getAsLong(COLUMN_ID) else null,
                    if(values.containsKey(COLUMN_NAME)) values.getAsString(COLUMN_NAME) else "" )
        }
    }


}

