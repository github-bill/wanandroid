package luyao.util.ktx.core.util.log.printer.fileprinter.file

import luyao.util.ktx.core.util.log.formatter.Formatter
import luyao.util.ktx.core.util.log.formatter.SimpleFormatter
import luyao.util.ktx.core.util.log.printer.fileprinter.FilePrinter
import luyao.util.ktx.core.util.log.printer.fileprinter.file.clean.CleanStrategy
import luyao.util.ktx.core.util.log.printer.fileprinter.file.name.FileNameGenerator


/**
 *生成文件的 Builder
 */
class FileBuilder {

    var folderPath: String? = null

    var fileNameGenerator: FileNameGenerator? = null // The file name generator for log file.

    var formatter: Formatter? = null

    var cleanStrategy: CleanStrategy? = null

    fun folderPath(folderPath: String): FileBuilder {
        this.folderPath = folderPath
        return this
    }

    fun fileNameGenerator(fileNameGenerator: FileNameGenerator): FileBuilder {
        this.fileNameGenerator = fileNameGenerator
        return this
    }

    fun formatter(formatter: Formatter): FileBuilder {
        this.formatter = formatter
        return this
    }

    fun cleanStrategy(cleanStrategy: CleanStrategy): FileBuilder {
        this.cleanStrategy = cleanStrategy
        return this
    }

    fun build() = FilePrinter(this, this.formatter ?: SimpleFormatter)
}