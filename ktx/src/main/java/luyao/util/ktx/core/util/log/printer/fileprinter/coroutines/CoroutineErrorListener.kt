package luyao.util.ktx.core.util.log.printer.fileprinter.coroutines


interface CoroutineErrorListener {
    fun onError(throwable: Throwable)
}