package seoft.co.kr.another_app

import android.content.ContentProvider
import android.content.ContentValues
import android.content.Context
import android.content.UriMatcher
import android.database.Cursor
import android.net.Uri
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.content.ContentUris
import android.database.SQLException
import android.database.sqlite.SQLiteQueryBuilder
import android.text.TextUtils

class StudentsProvider :ContentProvider(){

    companion object {
        val NAME = "name"
        val GRADE = "grade"
        val _ID = "_id"
        val PROVIDER_NAME = "seoft.co.kr.another_app.StudentsProvider"
        val URL = "content://$PROVIDER_NAME/students"
        val CONTENT_URI = Uri.parse(URL)

    }



    private val STUDENTS_PROJECTION_MAP: HashMap<String, String>? = null

    val STUDENTS = 1
    val STUDENT_ID = 2

    val uriMatcher = UriMatcher(UriMatcher.NO_MATCH)
            .apply {
                addURI(PROVIDER_NAME,"students",STUDENTS)
                addURI(PROVIDER_NAME,"students/#",STUDENT_ID)
            }

    var db:SQLiteDatabase? = null
    val DATABASE_NAME = "College"
    val STUDENTS_TABLE_NAME = "students"
    val DATABASE_VERSION = 1
    val CREATE_DB_TABLE = " CREATE TABLE " + STUDENTS_TABLE_NAME +
    " (_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
    " name TEXT NOT NULL, " +
    " grade TEXT NOT NULL);"


    inner class DatabaseHelper(context: Context?, name: String?, factory: SQLiteDatabase.CursorFactory?, version: Int) : SQLiteOpenHelper(context, name, factory, version) {

        constructor(context_: Context?) :this(context_,DATABASE_NAME,null,DATABASE_VERSION)

        override fun onCreate(db: SQLiteDatabase?) {
            db?.execSQL(CREATE_DB_TABLE)
        }

        override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
            db?.execSQL("DROP TABLE IF EXISTS " +  STUDENTS_TABLE_NAME)
            onCreate(db)
        }

    }

    override fun onCreate(): Boolean {
        val dbHelper = DatabaseHelper(context)
        db = dbHelper.writableDatabase

        return db != null
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {

        val rowID = db?.insert(STUDENTS_TABLE_NAME, "", values)

        /**
         * If record is added successfully
         */

        rowID?.let {
            if (rowID > 0) {
                val _uri = ContentUris.withAppendedId(CONTENT_URI, rowID)
                context!!.contentResolver.notifyChange(_uri, null)
                return _uri
            }
        }

        throw SQLException("Failed to add a record into " + uri)
    }

    override fun query(uri: Uri, projection: Array<String>?, selection: String?, selectionArgs: Array<String>?, sortOrder_: String?): Cursor? {

        val qb = SQLiteQueryBuilder()
        qb.tables = STUDENTS_TABLE_NAME

        when (uriMatcher.match(uri)) {
            STUDENTS -> qb.setProjectionMap(STUDENTS_PROJECTION_MAP)
            STUDENT_ID -> qb.appendWhere("$_ID=${uri.pathSegments.get(1)}")
        }

        var sortOrder = ""


        if(sortOrder_.isNullOrBlank()) {
            sortOrder = NAME
        }

        val c = qb.query(db,projection,selection,selectionArgs,null,null, sortOrder)
                .apply { setNotificationUri(context.contentResolver,uri) }
        return c
    }

    override fun update(uri: Uri, values: ContentValues?, selection: String?, selectionArgs: Array<String>?): Int {
        var cnt = 0

        when(uriMatcher.match(uri)) {
            STUDENTS -> {
                db?.let {
                    cnt = it.update(STUDENTS_TABLE_NAME,values,selection,selectionArgs)
                }
            }
            STUDENT_ID -> {
                db?.let {
                    val q = "$_ID = ${uri.pathSegments.get(1)}${if(!TextUtils.isEmpty(selection)) " AND ($selection)" else ""}"
                    cnt = it.update(STUDENTS_TABLE_NAME,values,q ,selectionArgs)
                }
            }
        }

        context.contentResolver.notifyChange(uri,null)
        return cnt
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<String>?): Int {
        var cnt = 0

        when(uriMatcher.match(uri)) {
            STUDENTS -> {
                db?.let {
                    cnt = it.delete(STUDENTS_TABLE_NAME,selection,selectionArgs)
                }
            }
            STUDENT_ID -> {
                db?.let {
                    val q = "$_ID = ${uri.pathSegments.get(1)}${if(!TextUtils.isEmpty(selection)) " AND ($selection)" else ""}"
                    cnt = it.delete(STUDENTS_TABLE_NAME,q ,selectionArgs)
                }
            }
    }

        context.contentResolver.notifyChange(uri,null)
        return cnt
    }

    override fun getType(uri: Uri): String? {
        return when(uriMatcher.match(uri)) {
            STUDENTS -> "vnd.android.cursor.dir/vnd.example.students"
            STUDENT_ID -> "vnd.android.cursor.item/vnd.example.students"
            else -> throw IllegalArgumentException("Unsupported URI: " + uri);
        }
    }

}