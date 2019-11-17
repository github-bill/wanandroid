package luyao.util.ktx.core.util.log.printer

import luyao.util.ktx.core.util.log.LogLevel
import luyao.util.ktx.core.util.log.formatter.Formatter

/**
 *
 *打印日志，支持打印日志输出到 Console、File 等等
 */
interface Printer {
    /**
     * 用于格式化日志
     */
    val formatter: Formatter

    /**
     * 打印日志
     */
    fun printLog(logLevel: LogLevel, tag: String, msg: String)
}