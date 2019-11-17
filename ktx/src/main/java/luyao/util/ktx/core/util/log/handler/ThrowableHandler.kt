package luyao.util.ktx.core.util.log.handler

import luyao.util.ktx.core.util.log.L
import luyao.util.ktx.core.util.log.LogLevel
import luyao.util.ktx.core.util.log.formatter.Formatter
import luyao.util.ktx.core.util.log.logTag
import luyao.util.ktx.core.util.log.parser.Parser
import java.io.PrintWriter
import java.io.StringWriter

/**
 * 异常处理
 */
class ThrowableHandler : BaseHandler(), Parser<Throwable> {
    override fun handle(obj: Any): Boolean {
        if (obj is Throwable) {
            L.printers().map {
                val s = L.getMethodNames(it.formatter)
                it.printLog(
                    LogLevel.ERROR,
                    this.logTag(),
                    String.format(s, parseString(obj, it.formatter))
                )
            }
            return true
        }
        return false
    }

    override fun parseString(throwable: Throwable, formatter: Formatter): String {
        val sw = StringWriter(256)
        val pw = PrintWriter(sw, false)
        throwable.printStackTrace(pw)
        pw.flush()
        var message = sw.toString()
        message = message.replace("\n", "\n${formatter.splitter()}")
        return message
    }
}