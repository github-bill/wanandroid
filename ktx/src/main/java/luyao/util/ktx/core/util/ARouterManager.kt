package luyao.util.ktx.core.util

import android.app.Application
import com.alibaba.android.arouter.launcher.ARouter
import luyao.util.ktx.BuildConfig

/**
 * 路由管理
 */
object ARouterManager {
    //初始化路由
    fun init(app: Application) {
        if (BuildConfig.DEBUG) {
            ARouter.openLog()
            ARouter.openDebug()
        }
        ARouter.init(app)
    }
}