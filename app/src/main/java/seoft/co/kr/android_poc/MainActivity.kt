package seoft.co.kr.android_poc

import android.graphics.Bitmap
import android.graphics.Color
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import me.priyesh.chroma.ChromaDialog
import me.priyesh.chroma.ColorMode
import me.priyesh.chroma.ColorSelectListener
import seoft.co.kr.android_poc.util.i


// ref : https://github.com/ItsPriyesh/chroma
class MainActivity : AppCompatActivity(){

    val COLOR_DECODER = "\"#%06X\""

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

        //        ChromaUtil.getFormattedColorString(color,false).toString().i("222")
        ChromaDialog.Builder()
                .initialColor(Color.GREEN)
                .colorMode(ColorMode.RGB) // There's also ARGB and HSV
                .onColorSelected(object : ColorSelectListener{
                    override fun onColorSelected(color: Int) {
                        val stringHexCode = String.format(COLOR_DECODER, 0xFFFFFF and color)

                        stringHexCode.i("stringHexCode")

                        val bit = Bitmap.createBitmap(1,1,Bitmap.Config.ARGB_8888)
                        bit.eraseColor(color)

                        iv.setImageBitmap(bit)
                    }

                })
                .create()
                .show(getSupportFragmentManager(), "ChromaDialog");
    }


    fun BBB() {


    }

    fun CCC() {

    }

    fun DDD() {

    }
}
