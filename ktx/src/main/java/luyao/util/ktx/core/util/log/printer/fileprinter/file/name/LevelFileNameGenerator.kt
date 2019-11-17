package luyao.util.ktx.core.util.log.printer.fileprinter.file.name


/**
 *基于日志的 Level 生成对应的文件，相应的日志等级存放相应的文件
 */
class LevelFileNameGenerator : FileNameGenerator {
    override fun isFileNameChangeable() = true

    override fun generateFileName(logLevel: Int, tag: String, timestamp: Long): String {
        return when (logLevel) {
            0 -> "error"
            1 -> "warn"
            2 -> "info"
            3 -> "debug"
            else -> "info"
        }
    }
}