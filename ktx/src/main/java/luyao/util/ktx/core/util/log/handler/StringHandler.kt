package luyao.util.ktx.core.util.log.handler

import luyao.util.ktx.core.util.log.L
import luyao.util.ktx.core.util.log.LogLevel
import luyao.util.ktx.core.util.log.formatter.Formatter
import luyao.util.ktx.core.util.log.logTag
import luyao.util.ktx.core.util.log.parser.Parser
import luyao.util.ktx.core.util.log.printer.Printer
import luyao.util.ktx.core.util.log.utils.formatJson
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

/**
 * 字符串处理
 */
class StringHandler : BaseHandler(), Parser<String> {

    companion object {
        private const val MAX_STRING_LENGTH = 4000
    }

    override fun handle(obj: Any): Boolean {
        if (obj is String) {
            val json = obj.trim { it <= ' ' }
            L.printers().map {
                val s = L.getMethodNames(it.formatter)
                val logString = String.format(s, parseString(json, it.formatter))
                printLongLog(LogLevel.INFO, this.logTag(), logString, it)
            }
            return true
        }
        return false
    }

    /**
     * 打印超过4000行的日志
     */
    private fun printLongLog(logLevel: LogLevel, tag: String, logString: String, printer: Printer) {
        if (logString.length > MAX_STRING_LENGTH) {
            var i = 0
            while (i < logString.length) {
                if (i + MAX_STRING_LENGTH < logString.length) {
                    if (i == 0) {
                        printer.printLog(
                            logLevel,
                            tag,
                            logString.substring(i, i + MAX_STRING_LENGTH)
                        )
                    } else {
                        printer.printLog(
                            logLevel,
                            "",
                            logString.substring(i, i + MAX_STRING_LENGTH)
                        )
                    }
                } else
                    printer.printLog(logLevel, "", logString.substring(i, logString.length))
                i += MAX_STRING_LENGTH
            }
        } else
            printer.printLog(logLevel, tag, logString)
    }

    override fun parseString(json: String, formatter: Formatter): String {
        var message = ""
        try {
            if (json.startsWith("{")) {
                val jsonObject = JSONObject(json)
                message = jsonObject.formatJson()
                message = message.replace("\n", "\n${formatter.splitter()}")
            } else if (json.startsWith("[")) {
                val jsonArray = JSONArray(json)
                message = jsonArray.formatJson()
                message = message.replace("\n", "\n${formatter.splitter()}")
            } else { // 普通的字符串
                message = json.replace("\n", "\n${formatter.splitter()}")
            }
        } catch (e: JSONException) {
            L.e("Invalid Json: $json")
            message = ""
        }
        return message
    }
}