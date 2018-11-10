package seoft.co.kr.android_poc

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*
import android.app.admin.DevicePolicyManager
import android.content.Intent
import android.content.ComponentName
import android.content.Context
import android.content.Context.DEVICE_POLICY_SERVICE
import android.widget.Toast
import android.app.admin.DeviceAdminReceiver




// ref
// http://toonraon.tistory.com/3
//

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btA.setOnClickListener { v -> AA() }
        btB.setOnClickListener { v -> BB() }
    }

    lateinit var devicePolicyManager : DevicePolicyManager

    fun AA(){
        devicePolicyManager = applicationContext.getSystemService(Context.DEVICE_POLICY_SERVICE) as DevicePolicyManager

        val componentName = ComponentName(applicationContext, ShutdownConfigAdminReceiver::class.java)

        if (!devicePolicyManager.isAdminActive(componentName)) {
            val intent = Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN)
                    .apply { putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, componentName) }

            startActivityForResult(intent, 0)
        }
    }

    fun BB(){
        devicePolicyManager.lockNow()
    }



}
