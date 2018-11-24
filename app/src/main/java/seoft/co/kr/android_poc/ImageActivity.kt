package seoft.co.kr.android_poc

import android.graphics.BitmapFactory
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import kotlinx.android.synthetic.main.activity_image.*
import java.io.File
import java.io.FileInputStream

class ImageActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image)


        window.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        val path = intent.getStringExtra(MainActivity.SP_IMG_PATH)

        loadBitmap(path)


    }

    fun loadBitmap(path:String) {
        val f = File(path, "bg.png")
        val b = BitmapFactory.decodeStream(FileInputStream(f))
        ivBg.setImageBitmap(b)
    }


}
