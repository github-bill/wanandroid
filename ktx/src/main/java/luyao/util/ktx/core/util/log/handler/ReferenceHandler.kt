package luyao.util.ktx.core.util.log.handler

import com.google.gson.Gson
import luyao.util.ktx.core.util.log.L
import luyao.util.ktx.core.util.log.LogLevel
import luyao.util.ktx.core.util.log.LoggerPrinter
import luyao.util.ktx.core.util.log.formatter.Formatter
import luyao.util.ktx.core.util.log.logTag
import luyao.util.ktx.core.util.log.parser.Parser
import luyao.util.ktx.core.util.log.utils.formatJson
import luyao.util.ktx.core.util.log.utils.isPrimitiveType
import luyao.util.ktx.core.util.log.utils.toJavaClass
import org.json.JSONObject
import java.lang.ref.Reference

/**
 * 引用处理
 */
class ReferenceHandler : BaseHandler(), Parser<Reference<*>> {
    override fun handle(obj: Any): Boolean {
        if (obj is Reference<*>) {
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

    override fun parseString(reference: Reference<*>, formatter: Formatter): String {
        val actual = reference.get()
        var msg =
            reference.javaClass.canonicalName + "<" + actual?.toJavaClass() + ">" + LoggerPrinter.BR + formatter.splitter()
        val isPrimitiveType = isPrimitiveType(actual)
        if (isPrimitiveType) {
            msg += "{$actual}"
        } else {
            msg += JSONObject(Gson().toJson(actual))
                .formatJson().replace("\n", "\n${formatter.splitter()}")
        }
        return msg
    }
}