package seoft.co.kr

import android.content.ContentValues
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

class MainActivity : AppCompatActivity() {


    val TAG = "MainActivity#$#"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btA.setOnClickListener { v ->
            AAA()
        }

        btB.setOnClickListener { v ->
            BBB()
        }

        btC.setOnClickListener { v ->
            CCC()
        }

        btD.setOnClickListener { v ->
            DDD()
        }
    }

    fun AAA() {
        val insertData = ContentValues()
        insertData.put(Person.COLUMN_NAME,"AA${Random().nextInt(11)}")

        val kk = contentResolver.insert(Person.URI_PERSSON ,insertData)
        kk.toString().i(TAG)
    }

    fun BBB() {

        val kkk = contentResolver.query(Person.URI_PERSSON ,null,null,null,null)

        while(kkk.moveToNext()){
            val lll = kkk.getString(kkk.getColumnIndex(Person.COLUMN_NAME))
            lll.toString().i(TAG)
        }
    }

    fun CCC() {

        val kkk = contentResolver.query(Person.getUriFromId(6) ,null,null,null,null)
        kkk.moveToNext()
        val lll = kkk.getString(kkk.getColumnIndex(Person.COLUMN_NAME))
        lll.toString().i(TAG)
    }

    fun DDD() {
        val deleteUri = Person.getUriFromId(7)
        val deleteRst = contentResolver.delete(deleteUri ,null,null)

        deleteRst.toString().i(TAG)
    }
}
