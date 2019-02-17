package seoft.co.kr.android_poc

import android.content.ContentValues
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import seoft.co.kr.android_poc.data.Person
import seoft.co.kr.android_poc.data.PersonContentProvider
import seoft.co.kr.android_poc.data.PersonContentProvider.Companion.getUriFromId
import seoft.co.kr.android_poc.util.i
import java.util.*

// ref : https://github.com/googlesamples/android-architecture-components/tree/master/PersistenceContentProviderSample/app/src/main/java/com/example/android/contentprovidersample
class MainActivity : AppCompatActivity(){

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

        val kk = contentResolver.insert(PersonContentProvider.URI_PERSSON ,insertData)
        kk.toString().i(TAG)
    }

    fun BBB() {

        val kkk = contentResolver.query(PersonContentProvider.URI_PERSSON ,null,null,null,null)

        while(kkk.moveToNext()){
            val lll = kkk.getString(kkk.getColumnIndex(Person.COLUMN_NAME))
            lll.toString().i(TAG)
        }
    }

    fun CCC() {

        val kkk = contentResolver.query(getUriFromId(1) ,null,null,null,null)
        kkk.moveToNext()
        val lll = kkk.getString(kkk.getColumnIndex(Person.COLUMN_NAME))
        lll.toString().i(TAG)
    }

    fun DDD() {
        val deleteUri = getUriFromId(4)
        val deleteRst = contentResolver.delete(deleteUri ,null,null)

        deleteRst.toString().i(TAG)
    }
}
