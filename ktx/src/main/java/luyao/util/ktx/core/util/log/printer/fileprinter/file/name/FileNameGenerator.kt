package luyao.util.ktx.core.util.log.printer.fileprinter.file.name

/**
 *简单的文件命名/生成策略
 */
interface FileNameGenerator {

    fun isFileNameChangeable(): Boolean

    fun generateFileName(logLevel: Int, tag:String, timestamp: Long): String
}