package luyao.util.ktx.core.util.log.handler

import luyao.util.ktx.core.util.log.L
import luyao.util.ktx.core.util.log.LogLevel
import luyao.util.ktx.core.util.log.LoggerPrinter
import luyao.util.ktx.core.util.log.formatter.Formatter
import luyao.util.ktx.core.util.log.logTag
import luyao.util.ktx.core.util.log.parser.Parser
import luyao.util.ktx.core.util.log.utils.formatJson
import luyao.util.ktx.core.util.log.utils.isPrimitiveType
import luyao.util.ktx.core.util.log.utils.parseToJSONArray
import org.json.JSONArray

/**
 * 列表处理
 */
class CollectionHandler : BaseHandler(), Parser<Collection<*>> {
    override fun handle(obj: Any): Boolean {
        if (obj is Collection<*>) {
            val value = obj.firstOrNull()
            val isPrimitiveType = isPrimitiveType(value)
            if (isPrimitiveType) {
                val simpleName = obj.javaClass
                var msg = "%s size = %d" + LoggerPrinter.BR
                L.printers().map {
                    msg = String.format(msg, simpleName, obj.size) + it.formatter.splitter()
                    val s = L.getMethodNames(it.formatter)
                    it.printLog(
                        LogLevel.INFO,
                        this.logTag(),
                        String.format(s, msg + obj.toString())
                    )
                }
                return true
            }
            L.printers().map {
                val s = L.getMethodNames(it.formatter)
                it.printLog(
                    LogLevel.INFO,
                    this.logTag(),
                    String.format(s, parseString(obj, it.formatter))
                )
            }
            return true
        }
        return false
    }

    override fun parseString(t: Collection<*>, formatter: Formatter): String {
        val jsonArray = JSONArray()
        val simpleName = t.javaClass
        var msg = "%s size = %d" + LoggerPrinter.BR
        msg = String.format(msg, simpleName, t.size) + formatter.splitter()
        return msg + t.parseToJSONArray(jsonArray)
            .formatJson().replace("\n", "\n${formatter.splitter()}")
    }

}