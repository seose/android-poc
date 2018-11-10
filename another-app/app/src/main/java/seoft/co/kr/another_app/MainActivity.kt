package seoft.co.kr.another_app

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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun onClickAddName(view: View) {
        // Add a new student record
        val values = ContentValues()
        values.put(StudentsProvider.NAME,
                (findViewById<View>(R.id.editText2) as EditText).text.toString())

        values.put(StudentsProvider.GRADE,
                (findViewById<View>(R.id.editText3) as EditText).text.toString())

        val uri = contentResolver.insert(
                StudentsProvider.CONTENT_URI, values)

        Toast.makeText(baseContext,
                uri!!.toString(), Toast.LENGTH_LONG).show()
    }

    fun onClickRetrieveStudents(view: View) {
        val URL = "content://${StudentsProvider.PROVIDER_NAME}"

        val students = Uri.parse(URL)
        val c = managedQuery(students, null, null, null, "name")

        if (c.moveToFirst()) {
            do {
                Toast.makeText(this,
                        c.getString(c.getColumnIndex(StudentsProvider._ID)) +
                                ", " + c.getString(c.getColumnIndex(StudentsProvider.NAME)) +
                                ", " + c.getString(c.getColumnIndex(StudentsProvider.GRADE)),
                        Toast.LENGTH_SHORT).show()
            } while (c.moveToNext())
        }
    }
}
