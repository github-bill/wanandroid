package luyao.util.ktx.core.util.log.handler

import android.content.Intent
import android.os.Bundle
import luyao.util.ktx.core.util.log.L
import luyao.util.ktx.core.util.log.LogLevel
import luyao.util.ktx.core.util.log.LoggerPrinter
import luyao.util.ktx.core.util.log.formatter.Formatter
import luyao.util.ktx.core.util.log.logTag
import luyao.util.ktx.core.util.log.parser.Parser
import luyao.util.ktx.core.util.log.utils.formatJson
import luyao.util.ktx.core.util.log.utils.parseBundle
import luyao.util.ktx.core.util.log.utils.toJavaClass
import org.json.JSONObject

/**
 * 意图处理
 */
class IntentHandler : BaseHandler(), Parser<Intent> {
    override fun handle(obj: Any): Boolean {
        if (obj is Intent) {
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

    override fun parseString(t: Intent, formatter: Formatter): String {
        val msg = t.toJavaClass() + LoggerPrinter.BR + formatter.splitter()
        return msg + JSONObject().apply {
            put("Scheme", t.scheme)
            put("Action", t.action)
            put("DataString", t.dataString)
            put("Type", t.type)
            put("Package", t.`package`)
            put("ComponentInfo", t.component)
            put("Categories", t.categories)
            if (t.extras != null) {
                put("Extras", JSONObject(parseBundleString(t.extras)))
            }
        }
            .formatJson().replace("\n", "\n${formatter.splitter()}")
    }

    private fun parseBundleString(extras: Bundle?) = JSONObject().parseBundle(extras).formatJson()
}