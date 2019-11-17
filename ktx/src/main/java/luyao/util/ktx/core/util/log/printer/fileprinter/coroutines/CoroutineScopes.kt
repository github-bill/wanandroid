package luyao.util.ktx.core.util.log.printer.fileprinter.coroutines

import kotlinx.coroutines.*

val IO: CoroutineDispatcher = Dispatchers.IO

fun ioScope(errorHandler: CoroutineErrorListener?=null) = SafeCoroutineScope(IO,errorHandler)