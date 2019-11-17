package luyao.util.ktx.core.util.log.printer.fileprinter.file.clean

import java.io.File

/**
 *文件永不清空的策略
 */
class NeverCleanStrategy : CleanStrategy {
    override fun shouldClean(file: File) = false
}