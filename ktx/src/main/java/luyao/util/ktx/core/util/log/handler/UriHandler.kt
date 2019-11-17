package luyao.util.ktx.core.util.log.handler

import android.net.Uri
import luyao.util.ktx.core.util.log.L
import luyao.util.ktx.core.util.log.LogLevel
import luyao.util.ktx.core.util.log.LoggerPrinter
import luyao.util.ktx.core.util.log.formatter.Formatter
import luyao.util.ktx.core.util.log.logTag
import luyao.util.ktx.core.util.log.parser.Parser
import luyao.util.ktx.core.util.log.utils.formatJson
import luyao.util.ktx.core.util.log.utils.toJavaClass
import org.json.JSONObject

/**
 * Uri处理
 */
class UriHandler : BaseHandler(), Parser<Uri> {
    override fun handle(obj: Any): Boolean {
        if (obj is Uri) {
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

    override fun parseString(uri: Uri, formatter: Formatter): String {
        val msg = uri.toJavaClass() + LoggerPrinter.BR + formatter.splitter()
        return msg + JSONObject().apply {
            put("Scheme", uri.scheme)
            put("Host", uri.host)
            put("Port", uri.port)
            put("Path", uri.path)
            put("Query", uri.query)
            put("Fragment", uri.fragment)
        }
            .formatJson()
            .let {
                it.replace("\n", "\n${formatter.splitter()}")
            }
    }
}