package seoft.co.kr.android_poc

import android.app.Activity
import android.content.Context.SENSOR_SERVICE
import android.content.Context.WINDOW_SERVICE
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.util.Log
import android.view.WindowManager
import android.widget.TextView

// ref : https://stackoverflow.com/questions/9454948/android-pitch-and-roll-issue
class Gyro(act: Activity) : SensorEventListener{


    private val TAG = "Gyro"
    lateinit var mgr: SensorManager
    private var accel: Sensor? = null
    private var compass: Sensor? = null
    private var orient: Sensor? = null
    private var preferred: TextView? = null
    private var orientation: TextView? = null
    private var ready = false
    private val accelValues = FloatArray(3)
    private val compassValues = FloatArray(3)
    private val inR = FloatArray(9)
    private val inclineMatrix = FloatArray(9)
    private val orientationValues = FloatArray(3)
    private val prefValues = FloatArray(3)
    private var mAzimuth: Float = 0.toFloat()
    private var mInclination: Double = 0.toDouble()
    private var counter: Int = 0
    private var mRotation: Int = 0

    init {

        mgr = act.getSystemService(SENSOR_SERVICE) as SensorManager
        accel = mgr.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        compass = mgr.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD)
        orient = mgr.getDefaultSensor(Sensor.TYPE_ORIENTATION)
        val window = act.getSystemService(WINDOW_SERVICE) as WindowManager
        mRotation = window.defaultDisplay.rotation


    }

    fun callWhenOnResume(){

        mgr.registerListener(this, accel, SensorManager.SENSOR_DELAY_GAME)
        mgr.registerListener(this, compass, SensorManager.SENSOR_DELAY_GAME)
        mgr.registerListener(this, orient, SensorManager.SENSOR_DELAY_GAME)
    }

    fun callWhenOnPause(){

        mgr.unregisterListener(this, accel)
        mgr.unregisterListener(this, compass)
        mgr.unregisterListener(this, orient)
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {


    }

    override fun onSensorChanged(event: SensorEvent) {

        when (event.sensor.type) {
            Sensor.TYPE_ACCELEROMETER -> {
                for (i in 0..2) {
                    accelValues[i] = event.values[i]
                }
                if (compassValues[0] != 0f)
                    ready = true
            }
            Sensor.TYPE_MAGNETIC_FIELD -> {
                for (i in 0..2) {
                    compassValues[i] = event.values[i]
                }
                if (accelValues[2] != 0f)
                    ready = true
            }
            Sensor.TYPE_ORIENTATION -> for (i in 0..2) {
                orientationValues[i] = event.values[i]
            }
        }

        if (!ready)
            return
        if (SensorManager.getRotationMatrix(inR, inclineMatrix, accelValues, compassValues)) {
            // got a good rotation matrix
            SensorManager.getOrientation(inR, prefValues)
            mInclination = SensorManager.getInclination(inclineMatrix).toDouble()
            // Display every 10th value
            if (counter++ % 10 == 0) {
                doUpdate()
                counter = 1
            }

        }
    }


    fun doUpdate() {
        if (!ready)
            return
        mAzimuth = Math.toDegrees(prefValues[0].toDouble()).toFloat()
        if (mAzimuth < 0) {
            mAzimuth += 360.0f
        }
        var msg = String.format(
                "Preferred:\nazimuth (Z): %7.3f \npitch (X): %7.3f\nroll (Y): %7.3f",
                mAzimuth, Math.toDegrees(prefValues[1].toDouble()),
                Math.toDegrees(prefValues[2].toDouble()))
//        preferred.setText(msg)
        msg = String.format(
                "Orientation Sensor:\nazimuth (Z): %7.3f\npitch (X): %7.3f\nroll (Y): %7.3f",
                orientationValues[0],
                orientationValues[1],
                orientationValues[2])
//        orientation.setText(msg)
//        preferred.invalidate()
//        orientation.invalidate()

        val rst1 = -180 < orientationValues[1] && orientationValues[1] < -150 || 150 < orientationValues[1] && orientationValues[1] < 180
        val rst2 = -30 < orientationValues[2] && orientationValues[2] < 30
        Log.i("AAAAAAA", rst1.toString() + "")
        Log.i("BBBBBBB", rst2.toString() + "")
        Log.i("CCCCCCC", orientationValues[1].toString() + "," + orientationValues[2])

//        if (rst1 && rst2)
//            act.findViewById<View>(R.id.activity_main).setBackgroundColor(Color.BLUE)
//        else
//            findViewById<View>(R.id.activity_main).setBackgroundColor(Color.BLACK)


    }


}