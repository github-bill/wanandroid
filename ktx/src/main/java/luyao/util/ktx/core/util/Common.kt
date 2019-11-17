package luyao.util.ktx.core.util

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Looper
import java.io.File

/**
 * 判断是否是主线程
 */
fun isOnMainThread() = Looper.myLooper() == Looper.getMainLooper()

/**
 * 获取AndroidManifest.xml中<meta-data>元素的值
 * @param context
 *
 * @param name
 *
 * @return
 */
fun <T> getMetaData(context: Context, name: String): T? {
    try {
        val ai = context.packageManager.getApplicationInfo(
            context.packageName,
            PackageManager.GET_META_DATA
        )

        return ai.metaData?.get(name) as T
    } catch (e: Exception) {
        print("Couldn't find meta-data: " + name)
    }

    return null
}

/**
 * 安装apk
 * @param fileName apk文件的绝对路径
 *
 * @param context
 */
fun installApk(fileName: String, context: Context) {
    val intent = Intent(Intent.ACTION_VIEW)
    intent.setDataAndType(Uri.fromFile(File(fileName)), "application/vnd.android.package-archive")
    context.startActivity(intent)
}