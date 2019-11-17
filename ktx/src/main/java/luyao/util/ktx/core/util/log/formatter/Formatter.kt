package luyao.util.ktx.core.util.log.formatter

/**
 * 格式接口
 */
interface Formatter {
    /**
     * 顶部
     */
    fun top(): String

    /**
     * 中部
     */
    fun middle(): String

    /**
     * 底部
     */
    fun bottom(): String

    /**
     * 分割
     */
    fun splitter(): String
}