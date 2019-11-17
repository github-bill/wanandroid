package luyao.util.ktx.core.util.log.printer.fileprinter.file.clean

import java.io.File

/**
 *文件清空策略
 */
interface CleanStrategy {
    /**
     * Whether we should clean a specified log file.
     *
     * @param file the log file
     * @return true is we should clean the log file
     */
    fun shouldClean(file: File): Boolean
}