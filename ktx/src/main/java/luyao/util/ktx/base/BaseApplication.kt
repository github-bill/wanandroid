package luyao.util.ktx.base

import android.app.Application
import com.facebook.stetho.Stetho
import luyao.util.ktx.core.util.ARouterManager
import luyao.util.ktx.core.util.LeakCanaryManager
import kotlin.properties.Delegates

/**
 * Application基类
 */
abstract class BaseApplication : Application() {
    companion object {
        var instance: Application by Delegates.notNull()
    }

    override fun onCreate() {
        super.onCreate()
        instance = this

        //初始化内存泄漏检测工具
        if (LeakCanaryManager.init(this)) return

        //初始化路由
        ARouterManager.init(this)

        //网络拦截，用于调试接口
        Stetho.initializeWithDefaults(this)
    }
}