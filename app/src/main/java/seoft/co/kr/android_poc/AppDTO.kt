package seoft.co.kr.android_poc

import android.content.Context
import android.graphics.drawable.Drawable

data class AppDTO(
        val pkgName:String,
        val name:String,
        val icon:Drawable
)

fun getAllApplications(context:Context): MutableList<AppDTO> {

    val packageManager = context.packageManager
    val packages = packageManager.getInstalledPackages(0)

    val appList = mutableListOf<AppDTO>()

    return packages
    .filter {
        it.versionName != null
    }.map {
                AppDTO(
                        it.packageName,
                        it.applicationInfo.loadLabel(packageManager).toString(),
                        it.applicationInfo.loadIcon(packageManager)
                )
    }.toMutableList()

}