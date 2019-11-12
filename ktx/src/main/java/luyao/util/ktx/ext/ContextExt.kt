package luyao.util.ktx.ext

import android.content.ClipData
import android.content.Context
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.graphics.drawable.Drawable
import android.net.ConnectivityManager
import android.os.Build
import android.provider.Settings
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.*
import java.io.File


val Context.versionName: String
    get() = packageManager.getPackageInfo(packageName, 0).versionName

val Context.versionCode: Long
    get() = with(packageManager.getPackageInfo(packageName, 0)) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) longVersionCode else versionCode.toLong()
    }


/**
 * 屏幕宽度，单位：像素
 */
inline val Context.screenWidth
    get() = resources.displayMetrics.widthPixels

/**
 * 屏幕高度，单位：像素
 */
inline val Context.screenHeight
    get() = resources.displayMetrics.heightPixels

/**
 * 判断网络是否可用
 */
inline val Context.isNetworkAvailable: Boolean
    get() {
        val connectivityManager =
            getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetworkInfo = connectivityManager.activeNetworkInfo
        return activeNetworkInfo?.isConnectedOrConnecting ?: false
    }


/**
 * dp转为px
 */
fun Context.dp2px(value: Float): Int = (value * resources.displayMetrics.density).toInt()

/**
 * dp转为px
 */
fun Context.dp2px(dp: Int): Int {
    val scale = resources.displayMetrics.density
    return (dp * scale + 0.5f).toInt()
}

/**
 * px转为dp
 */
fun Context.px2dp(px: Int): Int {
    val scale = resources.displayMetrics.density
    return (px / scale + 0.5f).toInt()
}

/**
 * sp转为px
 */
fun Context.sp2px(value: Int): Int = (value * resources.displayMetrics.scaledDensity).toInt()

/**
 * sp转为px
 */
fun Context.sp2px(value: Float): Int = (value * resources.displayMetrics.scaledDensity).toInt()


/**
 * px转为sp
 */
fun Context.px2sp(px: Int): Float = px.toFloat() / resources.displayMetrics.scaledDensity

/**
 * dimen转为px
 */
fun Context.dimen2px(@DimenRes resource: Int): Int = resources.getDimensionPixelSize(resource)

/**
 * 获取字符串
 */
fun Context.string(@StringRes id: Int): String = getString(id)

/**
 * 获取颜色值
 */
fun Context.color(@ColorRes id: Int): Int = resources.getColor(id)

fun Context.inflateLayout(
    @LayoutRes layoutId: Int, parent: ViewGroup? = null,
    attachToRoot: Boolean = false
): View = LayoutInflater.from(this).inflate(layoutId, parent, attachToRoot)

/**
 * 获取当前app的版本名
 */
fun Context.getAppVersion(): String {
    val appContext = applicationContext
    val manager = appContext.packageManager
    try {
        val info = manager.getPackageInfo(appContext.packageName, 0)
        if (info != null)
            return info.versionName
    } catch (e: PackageManager.NameNotFoundException) {
        e.printStackTrace()
    }
    return ""
}

/**
 * 获取当前app的版本号
 */
fun Context.getAppVersionCode(): Int {
    val appContext = applicationContext
    val manager = appContext.getPackageManager()
    try {
        val info = manager.getPackageInfo(appContext.getPackageName(), 0)
        if (info != null)
            return info.versionCode
    } catch (e: PackageManager.NameNotFoundException) {
        e.printStackTrace()
    }
    return 0
}

/**
 * 获取应用的包名
 */
fun Context.getPackageName(): String = packageName


data class AppInfo(
    val apkPath: String,
    val packageName: String,
    val versionName: String,
    val versionCode: Long,
    val appName: String,
    val icon: Drawable
)

/**
 * 获取指定路径下apk的应用信息
 */
fun Context.getAppInfo(apkPath: String): AppInfo {
    val packageInfo = packageManager.getPackageArchiveInfo(apkPath, PackageManager.GET_META_DATA)
    packageInfo.applicationInfo.sourceDir = apkPath
    packageInfo.applicationInfo.publicSourceDir = apkPath

    val packageName = packageInfo.packageName
    val appName = packageManager.getApplicationLabel(packageInfo.applicationInfo).toString()
    val versionName = packageInfo.versionName
    val versionCode = packageInfo.versionCode
    val icon = packageManager.getApplicationIcon(packageInfo.applicationInfo)
    return AppInfo(apkPath, packageName, versionName, versionCode.toLong(), appName, icon)
}

/**
 * 获取指定目录下apk的应用信息
 */
fun Context.getAppInfos(apkFolderPath: String): List<AppInfo> {
    val appInfoList = ArrayList<AppInfo>()
    for (file in File(apkFolderPath).listFiles())
        appInfoList.add(getAppInfo(file.path))
    return appInfoList
}

/**
 * Whether horizontal layout direction of this view is from Right to Left.
 */
val Context.isRTLLayout: Boolean
    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    get() = resources.configuration.layoutDirection == View.LAYOUT_DIRECTION_RTL

/**
 * Sets the [text] on the clipboard
 */
fun Context.copyToClipboard(text: String, label: String = "KTX") {
    val clipData = ClipData.newPlainText(label, text)
    clipboardManager?.setPrimaryClip(clipData)
}

/**
 * Check if the accessibility Service which name is [serviceName] is enabled
 */
fun Context.checkAccessibilityServiceEnabled(serviceName: String): Boolean {
    val settingValue =
        Settings.Secure.getString(
            applicationContext.contentResolver,
            Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES
        )
    return settingValue.notNull({
        var result = false
        val splitter = TextUtils.SimpleStringSplitter(':')
        while (splitter.hasNext()) {
            if (splitter.next().equals(serviceName, true)) {
                result = true
                break
            }
        }
        result
    }, { false })
}


/**
 * Get app signature by [packageName]
 */
fun Context.getAppSignature(packageName: String = this.packageName): ByteArray? {
    val packageInfo: PackageInfo =
        packageManager.getPackageInfo(packageName, PackageManager.GET_SIGNATURES)
    val signatures = packageInfo.signatures
    return signatures[0].toByteArray()
}


/**
 * Whether the application is installed
 */
fun Context.isPackageInstalled(pkgName: String): Boolean {
    return try {
        packageManager.getPackageInfo(pkgName, 0)
        true
    } catch (e: Exception) {
        false
    }
}