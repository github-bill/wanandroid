package luyao.util.ktx.core.util.log.printer.fileprinter.file.clean

import java.io.File

/**
 *根据设置文件可以保存的最大时间，来实现文件的清除策略
 */
class FileLastModifiedCleanStrategy(val maxTimeMillis: Long) : CleanStrategy {

    override fun shouldClean(file: File): Boolean {
        val currentTimeMillis = System.currentTimeMillis()
        val lastModified = file.lastModified()
        return currentTimeMillis - lastModified > maxTimeMillis
    }
}