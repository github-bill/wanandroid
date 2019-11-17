package luyao.wanandroid.util

import android.app.Activity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.coroutineScope
import luyao.util.ktx.bean.Response
import luyao.util.ktx.ext.toast
import luyao.wanandroid.R
import luyao.wanandroid.model.bean.SystemChild
import retrofit2.HttpException

/**
 * Created by Lu
 * on 2018/3/15 21:53
 */

const val TOOL_URL = "http://www.wanandroid.com/tools"
const val GITHUB_PAGE = "https://github.com/lulululbj/wanandroid"
const val ISSUE_URL = "https://github.com/lulululbj/wanandroid/issues"

suspend fun executeResponse(
    response: Response<Any>, successBlock: suspend CoroutineScope.() -> Unit,
    errorBlock: suspend CoroutineScope.() -> Unit
) {
    coroutineScope {
        if (response.errorCode == -1) errorBlock()
        else successBlock()
    }
}

fun Activity.onNetError(e: Throwable, func: (e: Throwable) -> Unit) {
    if (e is HttpException) {
        toast(R.string.net_error)
        func(e)
    }
}

fun Response<Any>.isSuccess(): Boolean = this.errorCode == 0

fun transFormSystemChild(children: List<SystemChild>): String {
    return children.joinToString("     ", transform = { child -> child.name })
}