package luyao.util.ktx.core.util.log.formatter

import luyao.util.ktx.core.util.log.LoggerPrinter


/**
 *
 * @FileName:
 *          luyao.util.ktx.core.util.log.formatter.SimpleFormatter
 * @author: Tony Shen
 * @date: 2019-09-15 16:28
 * @since: V2.0 <描述当前版本功能>
 */
object SimpleFormatter : Formatter {
    override fun top() = LoggerPrinter.BR

    override fun middle() = LoggerPrinter.COMMA + LoggerPrinter.BLANK

    override fun bottom() = LoggerPrinter.BR

    override fun splitter() = ""
}