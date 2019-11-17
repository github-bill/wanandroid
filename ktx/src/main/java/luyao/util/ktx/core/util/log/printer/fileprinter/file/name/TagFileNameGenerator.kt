package luyao.util.ktx.core.util.log.printer.fileprinter.file.name

import luyao.util.ktx.core.util.log.printer.fileprinter.file.name.FileNameGenerator


class TagFileNameGenerator : FileNameGenerator {
    override fun isFileNameChangeable() = true

    override fun generateFileName(logLevel: Int, tag: String, timestamp: Long): String {
        return tag
    }

}