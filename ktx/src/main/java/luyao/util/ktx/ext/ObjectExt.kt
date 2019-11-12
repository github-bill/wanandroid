package luyao.util.ktx.ext

fun <T : Any> T.TAG() = this::class.simpleName


fun <T> Any?.notNull(f: () -> T, t: () -> T): T {
    return if (this != null) f() else t()
}