package luyao.util.ktx.core.util.log.handler

import com.google.gson.Gson
import luyao.util.ktx.core.util.log.L
import luyao.util.ktx.core.util.log.LogLevel
import luyao.util.ktx.core.util.log.LoggerPrinter
import luyao.util.ktx.core.util.log.logTag
import luyao.util.ktx.core.util.log.utils.formatJson
import luyao.util.ktx.core.util.log.utils.toJavaClass
import org.json.JSONObject

/**
 * 对象处理
 */
class ObjectHandler : BaseHandler() {
    override fun handle(obj: Any): Boolean {
        L.printers().map {
            val formatter = it.formatter
            val msg = obj.toJavaClass() + LoggerPrinter.BR + formatter.splitter()
            val message = Gson().toJson(obj).run {
                JSONObject(this)
            }
                .formatJson()
                .let {
                    it.replace("\n", "\n${formatter.splitter()}")
                }
            val s = L.getMethodNames(formatter)
            it.printLog(LogLevel.INFO, this.logTag(), String.format(s, msg + message))
        }
        return true
    }
}