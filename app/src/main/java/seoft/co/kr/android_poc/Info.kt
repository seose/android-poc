package seoft.co.kr.android_poc

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import seoft.co.kr.android_poc.util.i
import seoft.co.kr.android_poc.util.toast

class Info : AppCompatActivity() {

    val TAG = "Info#$#"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_info)

        if(Intent.ACTION_VIEW.equals(intent.action)){
            val uri = intent.data
            val id = uri.getQueryParameter("id")
            id.toString().i(TAG)
            Toast.makeText(this,"Success to get $id",Toast.LENGTH_LONG).show()

        }



    }


}
