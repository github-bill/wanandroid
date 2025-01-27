package luyao.util.ktx.core.util

import luyao.util.ktx.ext.canListFiles
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException
import java.nio.ByteBuffer
import java.text.DecimalFormat


fun exists(file: File?) = file != null && file.exists()

/**
 * 判断是否文件
 * @param file
 *
 * @return
 */
fun isFile(file: File) = exists(file) && file.isFile

/**
 * 判断是否目录
 * @param file
 *
 * @return
 */
fun isDirectory(file: File) = exists(file) && file.isDirectory

/**
 * 判断目录是否存在，不存在则判断是否创建成功
 *
 * @param file 文件
 * @return boolean
 */
fun createOrExistsDir(file: File?) =
    (file != null) && if (file.exists()) file.isDirectory else file.mkdirs()

/**
 * 判断文件是否存在，不存在则判断是否创建成功
 *
 * @param file 文件
 * @return boolean
 */
fun createOrExistsFile(file: File?): Boolean {
    if (file == null || !createOrExistsDir(file.parentFile)) {
        return false
    }
    if (file.exists()) {
        return file.isFile
    }
    try {
        return file.createNewFile()
    } catch (e: IOException) {
        e.printStackTrace()
        return false
    }
}

/**
 * Created by luyao
 * on 2019/7/23 9:29
 */

/**
 * Return the file size, include all sub files
 */
fun getFolderSize(file: File): Long {
    var total = 0L
    for (subFile in file.listFiles()) {
        total += if (subFile.isFile) subFile.length()
        else getFolderSize(subFile)
    }
    return total
}

/**
 * Return the formatted file size, like "4.78 GB"
 * @param unit 1000 or 1024, default to 1000
 */
fun getFormatFileSize(size: Long, unit: Int = 1000): String {
    val formatter = DecimalFormat("####.00")
    return when {
        size < 0 -> "0 B"
        size < unit -> "$size B"
        size < unit * unit -> "${formatter.format(size.toDouble() / unit)} KB"
        size < unit * unit * unit -> "${formatter.format(size.toDouble() / unit / unit)} MB"
        else -> "${formatter.format(size.toDouble() / unit / unit / unit)} GB"
    }
}

/**
 * Return all subFile in the folder
 */
fun getAllSubFile(folder: File): Array<File> {
    var fileList: Array<File> = arrayOf()
    if (!folder.canListFiles) return fileList
    for (subFile in folder.listFiles())
        fileList = if (subFile.isFile) fileList.plus(subFile)
        else fileList.plus(getAllSubFile(subFile))
    return fileList
}

/**
 * copy the [sourceFile] to the [destFile], only for file, not for folder
 * @param overwrite if the destFile is exist, whether to overwrite it
 */
fun copyFile(
    sourceFile: File,
    destFile: File,
    overwrite: Boolean,
    func: ((file: File, i: Int) -> Unit)? = null
) {

    if (!sourceFile.exists()) return

    if (destFile.exists()) {
        val stillExists = if (!overwrite) true else !destFile.delete()

        if (stillExists) {
            return
        }
    }

    if (!destFile.exists()) destFile.createNewFile()

    val inputStream = FileInputStream(sourceFile)
    val outputStream = FileOutputStream(destFile)
    val iChannel = inputStream.channel
    val oChannel = outputStream.channel


    val totalSize = sourceFile.length()
    val buffer = ByteBuffer.allocate(1024)
    var hasRead = 0f
    var progress = -1
    while (true) {
        buffer.clear()
        val read = iChannel.read(buffer)
        if (read == -1)
            break
        buffer.limit(buffer.position())
        buffer.position(0)
        oChannel.write(buffer)
        hasRead += read

        func?.let {
            val newProgress = ((hasRead / totalSize) * 100).toInt()
            if (progress != newProgress) {
                progress = newProgress
                it(sourceFile, progress)
            }
        }
    }

    inputStream.close()
    outputStream.close()
}

/**
 * copy the [sourceFolder] to the [destFolder]
 * @param overwrite if the destFile is exist, whether to overwrite it
 */
fun copyFolder(
    sourceFolder: File,
    destFolder: File,
    overwrite: Boolean,
    func: ((file: File, i: Int) -> Unit)? = null
) {
    if (!sourceFolder.exists()) return

    if (!destFolder.exists()) {
        val result = destFolder.mkdirs()
        if (!result) return
    }

    for (subFile in sourceFolder.listFiles()) {
        if (subFile.isDirectory) {
            copyFolder(
                subFile,
                File("${destFolder.path}${File.separator}${subFile.name}"),
                overwrite,
                func
            )
        } else {
            copyFile(
                subFile,
                File(destFolder, subFile.name),
                overwrite,
                func
            )
        }
    }
}
