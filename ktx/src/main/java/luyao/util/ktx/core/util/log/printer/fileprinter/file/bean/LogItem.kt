package luyao.util.ktx.core.util.log.printer.fileprinter.file.bean

import luyao.util.ktx.core.util.log.LogLevel

/**
 * 每次写入文件的内容，记录了当前时间、LogLevel、tag、msg
 */
class LogItem(var timeMillis: Long, var level: LogLevel, var tag: String, var msg: String)