package seoft.co.kr.another_app

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btA.setOnClickListener { view ->
            testA()
        }

        btB.setOnClickListener { v ->
            testB()
        }

    }


    fun testA() {

        val k = Random().nextInt()%100

        Repo.localPersonApiRepo.insertPerson(Person(name="ABCD",age=k ))
        Toast.makeText(this,"insert $k success",Toast.LENGTH_LONG).show()

    }

    fun testB() {

        Repo.localPersonApiRepo.findPersons()


    }


}
