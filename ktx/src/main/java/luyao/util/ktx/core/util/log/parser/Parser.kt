package luyao.util.ktx.core.util.log.parser

import luyao.util.ktx.core.util.log.formatter.Formatter


/**
 * 将对象解析成特定的字符串
 */
interface Parser<T> {
    /**
     * 将对象解析成字符串
     */
    fun parseString(t: T, formatter: Formatter): String
}