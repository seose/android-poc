package seoft.co.kr.android_poc

import android.content.Context
import android.graphics.drawable.Drawable
import android.content.Intent



data class AppDTO(
        val pkgName:String,
        val name:String,
        val optName:String,
        val icon:Drawable
)

fun getAllApplications(context:Context): MutableList<AppDTO> {

    val packageManager = context.packageManager
    val intent = Intent(Intent.ACTION_MAIN, null)
            .apply { addCategory(Intent.CATEGORY_LAUNCHER) }

    val packages = packageManager.queryIntentActivities(intent,0)
            .map { it.activityInfo }

    return packages
            .map {
                AppDTO(
                        it.packageName,
                        it.loadLabel(packageManager).toString(),
                        it.name,
                        it.loadIcon(packageManager)
                )
            }.toMutableList()
}