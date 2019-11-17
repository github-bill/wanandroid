package luyao.wanandroid.model.repository

import com.google.gson.Gson
import luyao.util.ktx.base.BaseRepository
import luyao.util.ktx.bean.Result
import luyao.wanandroid.App
import luyao.wanandroid.model.api.WanRetrofitClient
import luyao.wanandroid.model.bean.User
import luyao.wanandroid.util.LoginPrefsHelper
import java.io.IOException

/**
 * Created by luyao
 * on 2019/4/10 9:42
 */
class LoginRepository : BaseRepository() {
    suspend fun login(userName: String, passWord: String): Result<User> {
//        return apiCall { WanRetrofitClient.service.login(userName, passWord) }
        return safeApiCall(
            call = { requestLogin(userName, passWord) },
            errorMessage = "登录失败!"
        )
    }

    // TODO Move into DataSource Layer ?
    private suspend fun requestLogin(userName: String, passWord: String): Result<User> {
        val response = WanRetrofitClient.service.login(userName, passWord)
        return if (response.errorCode != -1) {
            val user = response.data
            LoginPrefsHelper.instance.isLogin = true
            LoginPrefsHelper.instance.userJson = Gson().toJson(user)
            App.CURRENT_USER = user
            Result.Success(user)
        } else {
            Result.Error(IOException(response.errorMsg))
        }
    }

    suspend fun register(userName: String, passWord: String): Result<User> {
//        return apiCall { WanRetrofitClient.service.register(userName, passWord, passWord) }
        return safeApiCall(call = { requestRegister(userName, passWord) }, errorMessage = "注册失败")
    }

    private suspend fun requestRegister(userName: String, passWord: String): Result<User> {
        val response = WanRetrofitClient.service.register(userName, passWord, passWord)
        return if (response.errorCode != -1) {
            requestLogin(userName, passWord)
        } else {
            Result.Error(IOException(response.errorMsg))
        }
    }
}