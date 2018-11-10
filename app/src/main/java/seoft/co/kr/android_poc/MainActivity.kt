package seoft.co.kr.android_poc

import android.content.ContentValues
import android.net.Uri
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.EditText
import android.widget.Toast

// ref :
// https://www.tutorialspoint.com/android/android_content_providers.htm
// https://stackoverflow.com/questions/14368867/permission-denial-opening-provider

class MainActivity : AppCompatActivity() {

    val TAG = "MainActivity#$#"

    val NAME = "name"
    val GRADE = "grade"
    val _ID = "_id"
    val PROVIDER_NAME = "seoft.co.kr.another_app.StudentsProvider"
    val URL = "content://$PROVIDER_NAME/students"
    val CONTENT_URI = Uri.parse(URL)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun onClickAddName(view: View) {
        // Add a new student record
        val values = ContentValues()
        values.put(NAME,
                (findViewById<View>(R.id.editText2) as EditText).text.toString())

        values.put(GRADE,
                (findViewById<View>(R.id.editText3) as EditText).text.toString())

        val uri = contentResolver.insert(
                CONTENT_URI, values)

        Toast.makeText(baseContext,
                uri!!.toString(), Toast.LENGTH_LONG).show()
    }

    fun onClickRetrieveStudents(view: View) {
        val URL = "content://${PROVIDER_NAME}"

        val students = Uri.parse(URL)
        val c = managedQuery(students, null, null, null, "name")

        if (c.moveToFirst()) {
            do {
                Toast.makeText(this,
                        c.getString(c.getColumnIndex(_ID)) +
                                ", " + c.getString(c.getColumnIndex(NAME)) +
                                ", " + c.getString(c.getColumnIndex(GRADE)),
                        Toast.LENGTH_SHORT).show()
            } while (c.moveToNext())
        }
    }
}
