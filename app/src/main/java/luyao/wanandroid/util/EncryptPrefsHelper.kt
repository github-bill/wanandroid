package luyao.wanandroid.util

import android.content.Context
import android.content.SharedPreferences
import luyao.util.ktx.base.BaseApplication
import luyao.util.ktx.core.delegate.prefs.boolean
import luyao.util.ktx.core.delegate.prefs.initKey
import luyao.util.ktx.core.delegate.prefs.string
import luyao.wanandroid.model.LoginConst

class LoginPrefsHelper {
    companion object {
        val instance = LoginPrefsHelper()
    }

    private val prefs: SharedPreferences =
        BaseApplication.instance.getSharedPreferences("login_prefs", Context.MODE_PRIVATE)

    init {
        prefs.initKey("12345678910abcde")
    }


    fun initKey() {

    }

    var isLogin by prefs.boolean(LoginConst.IS_LOGIN, isEncrypt = true)

    var userJson by prefs.string(LoginConst.USER_GSON, isEncrypt = true)
}
