package luyao.util.ktx.core.util.log.printer.fileprinter.file.name

import java.text.SimpleDateFormat
import java.util.*

/**
 *基于 yyyy-MM-dd 的日期格式生成文件
 */
class DateFileNameGenerator : FileNameGenerator {
    val mLocalDateFormat: ThreadLocal<SimpleDateFormat> = object : ThreadLocal<SimpleDateFormat>() {
        override fun initialValue() = SimpleDateFormat("yyyy-MM-dd", Locale.CHINA)
    }

    override fun isFileNameChangeable() = true

    override fun generateFileName(logLevel: Int, tag:String, timestamp: Long): String {

        var sdf = mLocalDateFormat.get()

        if (sdf==null) {
            sdf = SimpleDateFormat("yyyy-MM-dd", Locale.CHINA)
        }

        return sdf.let {

            it.timeZone = TimeZone.getDefault()
            it.format(Date(timestamp))
        }
    }
}