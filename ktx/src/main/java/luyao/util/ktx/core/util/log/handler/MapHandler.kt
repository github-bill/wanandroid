package luyao.util.ktx.core.util.log.handler

import luyao.util.ktx.core.util.log.L
import luyao.util.ktx.core.util.log.LogLevel
import luyao.util.ktx.core.util.log.LoggerPrinter
import luyao.util.ktx.core.util.log.formatter.Formatter
import luyao.util.ktx.core.util.log.logTag
import luyao.util.ktx.core.util.log.parser.Parser
import luyao.util.ktx.core.util.log.utils.formatJson
import luyao.util.ktx.core.util.log.utils.parseMap
import luyao.util.ktx.core.util.log.utils.toJavaClass
import org.json.JSONObject

/**
 * 集合处理
 */
class MapHandler : BaseHandler(), Parser<Map<*, *>> {
    override fun handle(obj: Any): Boolean {
        if (obj is Map<*, *>) {
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

    override fun parseString(t: Map<*, *>, formatter: Formatter): String {
        val msg = t.toJavaClass() + LoggerPrinter.BR + formatter.splitter()
        return msg + JSONObject().parseMap(t)
            .formatJson().replace("\n", "\n${formatter.splitter()}")
    }

}