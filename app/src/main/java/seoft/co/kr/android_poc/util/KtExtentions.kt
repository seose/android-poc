package seoft.co.kr.android_poc.util

import android.support.v7.app.AlertDialog
import android.util.Log
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import java.util.concurrent.TimeUnit

fun String.i(tag:String = "#$#") {
    Log.i(tag,this)
}

fun String.e(tag:String = "#$#") {
    Log.e(tag,this)
}

fun String.toast( duration: Int = Toast.LENGTH_LONG): Toast {
    return Toast.makeText(App.get, this, duration).apply { show()  }
}


fun Any.i(tag:String = "#$#") {
    Log.i(tag,this.toString())
}

// dialog
fun AlertDialog.Builder.showDialog(title:String? = null, message:String? = null,
                                   postiveBtText:String? = null, negativeBtText:String? = null,
                                   cbPostive : (()->Unit)? = null, cbNegative:(()->Unit)? = null) {
    title?.let {  setTitle(title) }
    message?.let { setMessage(message) }
    postiveBtText?.let { setPositiveButton(postiveBtText){
        _,_ -> cbPostive?.invoke()
    }}
    negativeBtText?.let { setNegativeButton(negativeBtText){
        _,_ -> cbNegative?.invoke()
    }}
    show()
}

fun AlertDialog.Builder.showDialogWithInput(title:String? = null, message:String? = null,
                                            postiveBtText:String? = null, negativeBtText:String? = null,
                                            cbPostive : ((String)->Unit)? = null, cbNegative:(()->Unit)? = null,
                                            inputType: Int) {
    title?.let {  setTitle(title) }
    message?.let { setMessage(message) }

    val etInput = EditText(context).apply { this.inputType = inputType }
    val ll = LinearLayout(context).apply { orientation = LinearLayout.VERTICAL }
    val lp = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT).apply { setMargins(55,0,55,0) }

    ll.addView(etInput,lp)

    this.setView(ll)

    postiveBtText?.let { setPositiveButton(postiveBtText){
        _,_ -> cbPostive?.invoke(etInput.text.toString())
    }}
    negativeBtText?.let { setNegativeButton(negativeBtText){
        _,_ -> cbNegative?.invoke()
    }}
    show()
}

fun Long.toTimeStr():String{
    return String.format("%02d:%02d:%02d",
            TimeUnit.MILLISECONDS.toHours(this),
            TimeUnit.MILLISECONDS.toMinutes(this) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(this)),
            TimeUnit.MILLISECONDS.toSeconds(this) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(this)))
}

/**
 * for easy convenient convert, milliseconds is long, need to multiply 1000
 */
fun Int.x1000L():Long {
    return this*1000L
}
