package luyao.wanandroid.model.repository

import com.google.gson.Gson
import luyao.util.ktx.base.BaseRepository
import luyao.util.ktx.bean.Result
import luyao.wanandroid.App
import luyao.wanandroid.model.api.WanRetrofitClient
import luyao.wanandroid.model.bean.User
import luyao.wanandroid.util.LoginPrefsHelper

/**
 * Created by luyao
 * on 2019/4/10 9:42
 */
class LoginRepository : BaseRepository() {
    suspend fun login(userName: String, passWord: String): Result<User> {
        return safeApiCall(
            call = { requestLogin(userName, passWord) },
            errorMessage = "登录失败!"
        )
    }

    // TODO Move into DataSource Layer ?
    private suspend fun requestLogin(userName: String, passWord: String): Result<User> {
        val response = WanRetrofitClient.service.login(userName, passWord)

        return executeResponse(response, {
            val user = response.data
            LoginPrefsHelper.instance.isLogin = true
            LoginPrefsHelper.instance.userJson = Gson().toJson(user)
            App.CURRENT_USER = user
        })
    }

    suspend fun register(userName: String, passWord: String): Result<User> {
        return safeApiCall(call = { requestRegister(userName, passWord) }, errorMessage = "注册失败")
    }

    private suspend fun requestRegister(userName: String, passWord: String): Result<User> {
        val response = WanRetrofitClient.service.register(userName, passWord, passWord)
        return executeResponse(response, { requestLogin(userName, passWord) })
    }
}