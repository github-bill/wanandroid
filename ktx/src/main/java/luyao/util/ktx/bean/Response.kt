package luyao.util.ktx.bean

/**
 * Created by luyao
 * on 2018/3/13 14:38
 */
data class Response<out T>(val errorCode: Int, val errorMsg: String, val data: T)