package luyao.util.ktx.core.util

import android.app.Application
import com.squareup.leakcanary.LeakCanary

/**
 * 内存泄露检测工具
 */
object LeakCanaryManager {
    /**
     * 初始化
     */
    fun init(app: Application): Boolean {
        if (LeakCanary.isInAnalyzerProcess(app)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return true
        }
        LeakCanary.install(app)
        return false
    }
}