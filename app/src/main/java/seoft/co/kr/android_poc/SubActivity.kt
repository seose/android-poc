package seoft.co.kr.android_poc

import android.app.Activity
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_sub.*
import android.content.Intent
import android.net.Uri
import android.util.Log


// if drawer
class SubActivity : AppCompatActivity() {

    val TAG = "SubActivity#$#"
    val UNINSTALL_REQUEST_CODE = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sub)

        bt.setOnClickListener { _ ->

            val app_pkg_name = "seoft.co.kr.test"
            val intent = Intent(Intent.ACTION_UNINSTALL_PACKAGE)
            intent.data = Uri.parse("package:$app_pkg_name")
            intent.putExtra(Intent.EXTRA_RETURN_RESULT, true)
            startActivityForResult(intent, UNINSTALL_REQUEST_CODE)
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == UNINSTALL_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                Log.i(TAG, "onActivityResult: user accepted the (un)install")
            } else if (resultCode == Activity.RESULT_CANCELED) {
                Log.i(TAG, "onActivityResult: user canceled the (un)install")
            } else if (resultCode == Activity.RESULT_FIRST_USER) {
                Log.i(TAG, "onActivityResult: failed to (un)install")
            }
        }
    }
}
