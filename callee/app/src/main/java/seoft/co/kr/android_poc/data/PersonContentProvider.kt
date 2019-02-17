package seoft.co.kr.android_poc.data

import android.content.ContentProvider
import android.content.ContentProviderOperation
import android.content.ContentProviderResult
import android.content.ContentUris
import android.content.ContentValues
import android.content.UriMatcher
import android.database.Cursor
import android.net.Uri
import java.util.*

class PersonContentProvider : ContentProvider() {

    val TAG  = "PersonContentProvider#$#"

    companion object {
        val AUTHORITY = "seoft.co.kr.android_poc.data.PersonContentProvider"

        val URI_PERSSON = Uri.parse("content://$AUTHORITY/${Person.TABLE_NAME}")

        fun getUriFromId(id:Long):Uri{
            return Uri.parse("content://$AUTHORITY/${Person.TABLE_NAME}/$id")
        }

    }


    /** The match code for some items in the Person table.  */
    private val CODE_PERSON_DIR = 1

    /** The match code for an item in the Person table.  */
    private val CODE_PERSON_ITEM = 2

    /** The URI matcher.  */
    private val MATCHER = UriMatcher(UriMatcher.NO_MATCH)
            .apply {
                addURI(AUTHORITY, Person.TABLE_NAME, CODE_PERSON_DIR)
                addURI(AUTHORITY, Person.TABLE_NAME + "/*", CODE_PERSON_ITEM)
            }

    override fun onCreate(): Boolean { return true }

    override fun query(uri: Uri, projection: Array<String>?, selection: String?, selectionArgs: Array<String>?, sortOrder: String?): Cursor? {
        val code = MATCHER.match(uri)

        val personDao = AppDatabase.INSTANCE.personDao()
        var cursor:Cursor?

        when(code){
            CODE_PERSON_DIR -> {
                cursor = personDao.selectAll()
            }
            CODE_PERSON_ITEM -> {
                cursor = personDao.selectById(ContentUris.parseId(uri))
            }
            else -> cursor = null
        }
        cursor!!.setNotificationUri(context.contentResolver, uri)
        return cursor
    }

    override fun getType(uri: Uri): String? {
        return when(MATCHER.match(uri)){
            CODE_PERSON_DIR -> "vnd.android.cursor.dir/$AUTHORITY.${Person.TABLE_NAME}"
            CODE_PERSON_ITEM -> "vnd.android.cursor.item/$AUTHORITY .${Person.TABLE_NAME}"
            else -> throw IllegalArgumentException("Unknown URI: $uri")
        }
    }

    override fun insert(uri: Uri, values: ContentValues): Uri? {
        when(MATCHER.match(uri)){
            CODE_PERSON_DIR -> {
                val id = AppDatabase.INSTANCE.personDao()
                        .insert(Person.fromContentValues(values))
                context.contentResolver.notifyChange(uri,null)
                return ContentUris.withAppendedId(uri,id)
            }
            CODE_PERSON_ITEM -> throw IllegalArgumentException("Invalid URI, cannot insert with ID: $uri")
            else -> throw IllegalArgumentException("Unknown URI: $uri")
        }

    }

    override fun update(uri: Uri, values: ContentValues?, selection: String?, selectionArgs: Array<String>?): Int {
        when(MATCHER.match(uri)){
            CODE_PERSON_DIR -> throw IllegalArgumentException("Invalid URI, cannot insert with ID: $uri")
            CODE_PERSON_ITEM -> {
                val person = Person.fromContentValues(values!!)
                person.id = ContentUris.parseId(uri)
                val cnt = AppDatabase.INSTANCE.personDao().update(person)
                context.contentResolver.notifyChange(uri,null)
                return cnt
            }
            else -> throw IllegalArgumentException("Unknown URI: $uri")
        }
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<String>?): Int {
        when(MATCHER.match(uri)){
            CODE_PERSON_DIR -> throw IllegalArgumentException("Invalid URI, cannot insert with ID: $uri")
            CODE_PERSON_ITEM -> {
                val cnt = AppDatabase.INSTANCE.personDao().deleteById(ContentUris.parseId(uri))
                context.contentResolver.notifyChange(uri,null)
                return cnt
            }
            else -> throw IllegalArgumentException("Unknown URI: $uri")
        }
      }

    override fun applyBatch(operations: ArrayList<ContentProviderOperation>): Array<ContentProviderResult> {

        AppDatabase.INSTANCE.beginTransaction()

        try {
            val results = super.applyBatch(operations)
            AppDatabase.INSTANCE.setTransactionSuccessful()
            return results
        } finally {
            AppDatabase.INSTANCE.endTransaction()
        }
    }

    override fun bulkInsert(uri: Uri, valuesArray: Array<ContentValues>): Int {
        when(MATCHER.match(uri)){
            CODE_PERSON_DIR -> {

                val persons = arrayListOf<Person>()

                valuesArray.forEach {
                    persons.add(Person.fromContentValues(it))
                }

                return AppDatabase.INSTANCE.personDao().insertAll(persons.toTypedArray()).size
            }
            CODE_PERSON_ITEM -> throw IllegalArgumentException("Invalid URI, cannot insert with ID: $uri")
            else -> throw IllegalArgumentException("Unknown URI: $uri")
        }    }

}