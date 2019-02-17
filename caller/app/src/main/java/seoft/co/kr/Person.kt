package seoft.co.kr

import android.content.ContentValues
import android.net.Uri
import android.provider.BaseColumns

data class Person(
    var id : Long? = null,
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

        val AUTHORITY = "seoft.co.kr.android_poc.data.PersonContentProvider"

        val URI_PERSSON = Uri.parse("content://$AUTHORITY/${Person.TABLE_NAME}")

        fun getUriFromId(id:Long): Uri {
            return Uri.parse("content://$AUTHORITY/${Person.TABLE_NAME}/$id")
        }

    }

}

