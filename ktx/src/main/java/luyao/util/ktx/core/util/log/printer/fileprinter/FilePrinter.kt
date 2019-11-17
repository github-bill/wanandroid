package luyao.util.ktx.core.util.log.printer.fileprinter

import com.safframework.log.printer.file.FileWriter
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.launch
import luyao.util.ktx.core.util.log.LogLevel
import luyao.util.ktx.core.util.log.formatter.Formatter
import luyao.util.ktx.core.util.log.printer.Printer
import luyao.util.ktx.core.util.log.printer.fileprinter.coroutines.ioScope
import luyao.util.ktx.core.util.log.printer.fileprinter.file.FileBuilder
import luyao.util.ktx.core.util.log.printer.fileprinter.file.bean.LogItem
import luyao.util.ktx.core.util.log.printer.fileprinter.file.clean.CleanStrategy
import luyao.util.ktx.core.util.log.printer.fileprinter.file.clean.NeverCleanStrategy
import luyao.util.ktx.core.util.log.printer.fileprinter.file.name.DateFileNameGenerator
import luyao.util.ktx.core.util.log.printer.fileprinter.file.name.FileNameGenerator
import java.io.File


/**
 * Android Q 内部存储设备/Android/data/com.项目包名/
 * L.addPrinter(FileBuilder().folderPath("/storage/emulated/0/Android/data/${getPackageName()}/logs").build())
 *打印到文件的Printer，默认的 formatter 使用 SimpleFormatter
 */
class FilePrinter(fileBuilder: FileBuilder, override val formatter: Formatter) : Printer {
    private val channel = Channel<LogItem>()
    private val folderPath: String
    private val fileNameGenerator: FileNameGenerator
    private val cleanStrategy: CleanStrategy
    private val writer: FileWriter

    init {
        ioScope().launch {
            channel.consumeEach {
                doWrite(it)
            }
        }

        folderPath = fileBuilder.folderPath ?: "/sdcard/logs/"
        fileNameGenerator = fileBuilder.fileNameGenerator ?: DateFileNameGenerator()
        cleanStrategy = fileBuilder.cleanStrategy ?: NeverCleanStrategy()

        writer = FileWriter(folderPath)
    }

    override fun printLog(logLevel: LogLevel, tag: String, msg: String) {
        ioScope().launch {
            channel.send(LogItem(System.currentTimeMillis(), logLevel, tag, msg))
        }
    }

    private fun doWrite(logItem: LogItem) {
        val lastFileName = writer.lastFileName

        if (lastFileName == null || fileNameGenerator.isFileNameChangeable()) {
            val newFileName = fileNameGenerator.generateFileName(
                logItem.level.value,
                logItem.tag,
                System.currentTimeMillis()
            )

            require(newFileName.trim { it <= ' ' }.isNotEmpty()) { "File name should not be empty." }

            if (newFileName != lastFileName) {
                if (writer.isOpened) {
                    writer.close()
                }

                cleanLogFilesIfNecessary()

                if (!writer.open(newFileName)) {
                    return
                }
            }
        }
        writer.appendLog(logItem.toString())
    }

    /**
     * 判断是否需要删除日志文件
     */
    private fun cleanLogFilesIfNecessary() {
        File(folderPath).listFiles()?.map {
            if (cleanStrategy.shouldClean(it)) {
                it.delete()
            }
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as FilePrinter

        if (formatter != other.formatter) return false
        if (channel != other.channel) return false
        if (folderPath != other.folderPath) return false
        if (fileNameGenerator != other.fileNameGenerator) return false
        if (cleanStrategy != other.cleanStrategy) return false
        if (writer != other.writer) return false

        return true
    }

    override fun hashCode(): Int {
        var result = formatter.hashCode()
        result = 31 * result + channel.hashCode()
        result = 31 * result + folderPath.hashCode()
        result = 31 * result + fileNameGenerator.hashCode()
        result = 31 * result + cleanStrategy.hashCode()
        result = 31 * result + writer.hashCode()
        return result
    }
}