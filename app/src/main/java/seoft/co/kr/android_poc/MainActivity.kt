package seoft.co.kr.android_poc

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Point
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.preference.PreferenceManager
import android.provider.MediaStore
import android.support.v7.app.AlertDialog
import android.util.DisplayMetrics
import android.view.View
import android.view.WindowManager
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import kotlinx.android.synthetic.main.activity_main.*
import seoft.co.kr.android_poc.util.i
import seoft.co.kr.android_poc.util.showDialog
import seoft.co.kr.android_poc.util.toast
import java.io.File
import java.io.FileOutputStream
import java.util.concurrent.TimeUnit

/*

ref
https://github.com/ArthurHub/Android-Image-Cropper





 */

class MainActivity : AppCompatActivity(){

    companion object {
        val SP_IMG_PATH = "SP_IMG_PATH"
    }

    val TAG = "MainActivity#$#"

    var deviceWidth = 0
    var deviceHeight = 0

    val REQ_PERMISSIONS = 101


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

        btE.setOnClickListener { v ->
            EEE()
        }

    }

    fun AAA() {
        // full screen contain statusbar, bottombar
        // ref : https://stackoverflow.com/questions/43511326/android-making-activity-full-screen-with-status-bar-on-top-of-it
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
    }

    fun BBB() {
        reqPermissions()
   }

    fun CCC() {


        // First, set device's width , height
        // ref : https://stackoverflow.com/questions/30033585/android-screen-size-full-screen
        val size = Point()
        windowManager.defaultDisplay.getRealSize( size )
        deviceWidth = size.x
        deviceHeight = size.y

        deviceWidth.toString().i(TAG)
        deviceHeight.toString().i(TAG)

        // Second, crop image using width, height
        CropImage.startPickImageActivity(this)
    }

    fun DDD() {
        val path = loadPath()

        val intent = Intent(this,ImageActivity::class.java)
        intent.putExtra(SP_IMG_PATH,path)
        startActivity(intent)

   }

    fun EEE(){

    }

    // BBB START

    private fun reqPermissions(){

        // need to add permissions in manifests
        requestPermissions(arrayOf(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA),
                REQ_PERMISSIONS)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if(requestCode == REQ_PERMISSIONS && grantResults.all {it == PackageManager.PERMISSION_GRANTED}) {

            "요청 승인 완료".toast()

        } else {
            AlertDialog.Builder(this).showDialog(
                    title = "ABCD 에서 다음 권한이 필요합니다",
                    message = "- 파일 입출력 권한 (사진전송)\n- 카메라 권한 (촬영후 전송)\n\n권한요청을 다시 하시겠습니까?",
                    postiveBtText = "확인",
                    negativeBtText = "취소",
                    cbPostive = {
                        reqPermissions()
                    },
                    cbNegative = {
                        "권한요청이 거부되어 종료합니다".toast()
                        finish()
                    }
            )
        }
    }

    // BBB END

    // CCC START

    // 적당한 set과 save,load 방법 ref :
    // http://sharp57dev.tistory.com/22
    // https://stackoverflow.com/questions/48046106/difference-between-setimagebitmap-and-setimageuri
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == CropImage.PICK_IMAGE_CHOOSER_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            val imageUri = CropImage.getPickImageResultUri(this, data)
            CropImage.activity(imageUri)
                    .setAspectRatio(deviceWidth, deviceHeight)
                    .start(this)
        } else if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            val result = CropImage.getActivityResult(data)
            ivBackground.setImageURI(result.uri)

            val bitImg = MediaStore.Images.Media.getBitmap(this.contentResolver,result.uri)

            val path = saveBitmap(bitImg)

            savePath(path)

        }
    }


    // load function in ImageActivity
    fun saveBitmap(bitmap: Bitmap) : String{
        val cw = ContextWrapper(applicationContext)
        val dir = cw.getDir("imageDir", Context.MODE_PRIVATE)
        val myPath = File(dir, "bg.png")

        val fos = FileOutputStream(myPath)
        bitmap.compress(Bitmap.CompressFormat.PNG,100,fos)

        return dir.absolutePath
    }


    fun savePath(path:String){
        val sp = PreferenceManager.getDefaultSharedPreferences(this)
        val editor = sp.edit()
        editor.putString("SP_IMG_PATH",path)
        editor.apply()
    }

    // CCC END

    // DDD START

    fun loadPath():String{
        val sp = PreferenceManager.getDefaultSharedPreferences(this)
        return sp.getString("SP_IMG_PATH","")
    }

    // DDD END



}
