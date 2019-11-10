package luyao.util.ktx.ext

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.fragment.app.Fragment
import java.io.Serializable

/**
 * Created by luyao
 * on 2019/7/9 14:17
 */


inline fun <reified T : Activity> Activity.startKtxActivity(
        flags: Int? = null,
        extra: Bundle? = null,
        value: Pair<String, Any>? = null,
        values: Collection<Pair<String, Any>?>? = null
) {

    val list = ArrayList<Pair<String, Any>?>()
    value?.let { list.add(it) }
    values?.let { list.addAll(it) }
    startActivity(getIntent<T>(flags, extra, list))
}

inline fun <reified T : Activity> Fragment.startKtxActivity(
        flags: Int? = null,
        extra: Bundle? = null,
        value: Pair<String, Any>? = null,
        values: Collection<Pair<String, Any>?>? = null
) =
        activity?.let {
            val list = ArrayList<Pair<String, Any>?>()
            value?.let { v -> list.add(v) }
            values?.let { v -> list.addAll(v) }
            startActivity(it.getIntent<T>(flags, extra, list))
        }

inline fun <reified T : Activity> Context.startKtxActivity(
        flags: Int? = null,
        extra: Bundle? = null,
        value: Pair<String, Any>? = null,
        values: Collection<Pair<String, Any>?>? = null
) {
    val list = ArrayList<Pair<String, Any>?>()
    value?.let { v -> list.add(v) }
    values?.let { v -> list.addAll(v) }
    startActivity(getIntent<T>(flags, extra, list))
}

inline fun <reified T : Activity> Activity.startKtxActivityForResult(
        requestCode: Int,
        flags: Int? = null,
        extra: Bundle? = null,
        value: Pair<String, Any>? = null,
        values: Collection<Pair<String, Any>?>? = null
) {
    val list = ArrayList<Pair<String, Any>?>()
    value?.let { list.add(it) }
    values?.let { list.addAll(it) }
    startActivityForResult(getIntent<T>(flags, extra, list), requestCode)
}

inline fun <reified T : Activity> Fragment.startKtxActivityForResult(
        requestCode: Int,
        flags: Int? = null,
        extra: Bundle? = null,
        value: Pair<String, Any>? = null,
        values: Collection<Pair<String, Any>?>? = null
) =
        activity?.let {
            val list = ArrayList<Pair<String, Any>?>()
            value?.let { list.add(it) }
            values?.let { list.addAll(it) }
            startActivityForResult(activity?.getIntent<T>(flags, extra, list), requestCode)
        }

inline fun <reified T : Context> Context.getIntent(
        flags: Int? = null,
        extra: Bundle? = null,
        pairs: List<Pair<String, Any>?>? = null
): Intent =
        Intent(this, T::class.java).apply {
            flags?.let { setFlags(flags) }
            extra?.let { putExtras(extra) }
            pairs?.let {
                for (pair in pairs)
                    pair?.let {
                        val name = pair.first
                        when (val value = pair.second) {
                            is Int -> putExtra(name, value)
                            is Byte -> putExtra(name, value)
                            is Char -> putExtra(name, value)
                            is Short -> putExtra(name, value)
                            is Boolean -> putExtra(name, value)
                            is Long -> putExtra(name, value)
                            is Float -> putExtra(name, value)
                            is Double -> putExtra(name, value)
                            is String -> putExtra(name, value)
                            is CharSequence -> putExtra(name, value)
                            is Parcelable -> putExtra(name, value)
                            is Array<*> -> putExtra(name, value)
                            is ArrayList<*> -> putExtra(name, value)
                            is Serializable -> putExtra(name, value)
                            is BooleanArray -> putExtra(name, value)
                            is ByteArray -> putExtra(name, value)
                            is ShortArray -> putExtra(name, value)
                            is CharArray -> putExtra(name, value)
                            is IntArray -> putExtra(name, value)
                            is LongArray -> putExtra(name, value)
                            is FloatArray -> putExtra(name, value)
                            is DoubleArray -> putExtra(name, value)
                            is Bundle -> putExtra(name, value)
                            is Intent -> putExtra(name, value)
                            else -> {
                            }
                        }
                    }
            }
        }

fun Activity.hideKeyboard() {
    inputMethodManager?.hideSoftInputFromWindow((currentFocus ?: View(this)).windowToken, 0)
    window!!.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)
    currentFocus?.clearFocus()
}

fun Activity.showKeyboard(et: EditText) {
    et.requestFocus()
    inputMethodManager?.showSoftInput(et, InputMethodManager.SHOW_IMPLICIT)
}

fun Activity.hideKeyboard(view: View) {
    inputMethodManager?.hideSoftInputFromWindow(view.windowToken, 0)
}

/**
 * private val vinCode: String by extraString(DATA_VIN_CODE)
 */
fun Activity.extraString(key: String, default: String = ""): Lazy<String> = lazy {
    intent?.extras?.getString(key) ?: default
}

/**
 * startActivityExt<TestActivity>(
 *  "1" to 2,
 *  "2" to "3"
 *   )
 */
inline fun <reified T : Activity> Activity.startActivityExt(vararg extras: Pair<String, Any?>) {
    startActivity(Intent(this, T::class.java).apply {
        putParams(*extras)
    })
}

fun Intent.putParams(
        vararg extras: Pair<String, Any?>
): Intent {
    if (extras.isEmpty()) return this
    extras.forEach { (key, value) ->
        value ?: let {
            it.putExtra(key, it.toString())
            return@forEach
        }
        when (value) {
            is Bundle -> this.putExtra(key, value)
            is Boolean -> this.putExtra(key, value)
            is BooleanArray -> this.putExtra(key, value)
            is Byte -> this.putExtra(key, value)
            is ByteArray -> this.putExtra(key, value)
            is Char -> this.putExtra(key, value)
            is CharArray -> this.putExtra(key, value)
            is String -> this.putExtra(key, value)
            is CharSequence -> this.putExtra(key, value)
            is Double -> this.putExtra(key, value)
            is DoubleArray -> this.putExtra(key, value)
            is Float -> this.putExtra(key, value)
            is FloatArray -> this.putExtra(key, value)
            is Int -> this.putExtra(key, value)
            is IntArray -> this.putExtra(key, value)
            is Long -> this.putExtra(key, value)
            is LongArray -> this.putExtra(key, value)
            is Short -> this.putExtra(key, value)
            is ShortArray -> this.putExtra(key, value)
            is Parcelable -> this.putExtra(key, value)
            is Serializable -> this.putExtra(key, value)
            else -> {
                throw IllegalArgumentException("Not support $value type ${value.javaClass}..")
            }
        }
    }
    return this
}

