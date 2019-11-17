package luyao.util.ktx.core.util.log.formatter

import luyao.util.ktx.core.util.log.LoggerPrinter

/**
 * 边框格式
 */
object BorderFormatter : Formatter {
    override fun top() =
        LoggerPrinter.BR + LoggerPrinter.TOP_BORDER + LoggerPrinter.BR + LoggerPrinter.HORIZONTAL_DOUBLE_LINE

    override fun middle() = LoggerPrinter.BR + LoggerPrinter.MIDDLE_BORDER + LoggerPrinter.BR

    override fun bottom() = LoggerPrinter.BR + LoggerPrinter.BOTTOM_BORDER + LoggerPrinter.BR

    override fun splitter() = LoggerPrinter.HORIZONTAL_DOUBLE_LINE
}