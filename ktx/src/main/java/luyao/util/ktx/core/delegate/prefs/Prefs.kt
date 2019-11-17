package luyao.util.ktx.core.delegate.prefs

import android.content.SharedPreferences
import android.content.SharedPreferences.Editor
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

/**
 * 普通用法
 * class PrefsHelper(prefs: SharedPreferences) {
 * var name by prefs.string("name")
 * var password by prefs.string("password")
 * var age by prefs.int("age")
 * var isForeigner by prefs.boolean("isForeigner")
 * }
 *
 * 加密用法
 * class EncryptPrefsHelper(prefs: SharedPreferences) {
 * init {
 * prefs.initKey("12345678910abcde") // 初始化密钥，且密钥是16位的
 * }
 * var name by prefs.string("name",isEncrypt=true)
 * var password by prefs.string("password",isEncrypt=true)
 * var age by prefs.int("age",isEncrypt=true)
 * var isForeigner by prefs.boolean("isForeigner",isEncrypt=true)
 * }
 */
private inline fun <T> SharedPreferences.delegate(
    key: String? = null,
    defaultValue: T,
    crossinline getter: SharedPreferences.(String, T) -> T?,
    crossinline setter: Editor.(String, T) -> Editor
): ReadWriteProperty<Any, T> =
    object : ReadWriteProperty<Any, T> {
        override fun getValue(thisRef: Any, property: KProperty<*>): T =
            getter(key ?: property.name, defaultValue)!!

        override fun setValue(thisRef: Any, property: KProperty<*>, value: T) =
            edit().setter(key ?: property.name, value).apply()
    }

fun SharedPreferences.int(
    key: String? = null,
    defValue: Int = 0,
    isEncrypt: Boolean = false
): ReadWriteProperty<Any, Int> {
    return if (isEncrypt) {
        delegate(key, defValue, SharedPreferences::getEncryptInt, Editor::putEncryptInt)
    } else {
        delegate(key, defValue, SharedPreferences::getInt, Editor::putInt)
    }
}

fun SharedPreferences.long(
    key: String? = null,
    defValue: Long = 0L,
    isEncrypt: Boolean = false
): ReadWriteProperty<Any, Long> {
    return if (isEncrypt) {
        delegate(key, defValue, SharedPreferences::getEncryptLong, Editor::putEncryptLong)
    } else {
        delegate(key, defValue, SharedPreferences::getLong, Editor::putLong)
    }
}

fun SharedPreferences.float(
    key: String? = null,
    defValue: Float = 0f,
    isEncrypt: Boolean = false
): ReadWriteProperty<Any, Float> {
    return if (isEncrypt) {
        delegate(key, defValue, SharedPreferences::getEncryptFloat, Editor::putEncryptFloat)
    } else {
        delegate(key, defValue, SharedPreferences::getFloat, Editor::putFloat)
    }
}

fun SharedPreferences.boolean(
    key: String? = null,
    defValue: Boolean = false,
    isEncrypt: Boolean = false
): ReadWriteProperty<Any, Boolean> {
    return if (isEncrypt) {
        delegate(key, defValue, SharedPreferences::getEncryptBoolean, Editor::putEncryptBoolean)
    } else {
        delegate(key, defValue, SharedPreferences::getBoolean, Editor::putBoolean)
    }
}


fun SharedPreferences.stringSet(
    key: String? = null,
    defValue: Set<String> = emptySet(),
    isEncrypt: Boolean = false
): ReadWriteProperty<Any, Set<String>> {
    return if (isEncrypt) {
        delegate(key, defValue, SharedPreferences::getEncryptStringSet, Editor::putEncryptStringSet)
    } else {
        delegate(key, defValue, SharedPreferences::getStringSet, Editor::putStringSet)
    }
}

fun SharedPreferences.string(
    key: String? = null,
    defValue: String = "",
    isEncrypt: Boolean = false
): ReadWriteProperty<Any, String> {
    return if (isEncrypt) {
        delegate(key, defValue, SharedPreferences::getEncryptString, Editor::putEncryptString)
    } else {
        delegate(key, defValue, SharedPreferences::getString, Editor::putString)
    }
}


/**
 * 初始化秘钥,必须要16位密钥匙
 */
fun SharedPreferences.initKey(key: String) = EncryptUtil.getInstance().key(key)


fun SharedPreferences.getEncryptInt(key: String, defValue: Int): Int {
    val encryptValue = this.getString(encryptPreference(key), null)
        ?: return defValue
    return decryptPreference(encryptValue).toInt()
}

fun Editor.putEncryptInt(key: String, value: Int): Editor {
    this.putString(encryptPreference(key), encryptPreference(value.toString()))
    return this
}


fun SharedPreferences.getEncryptLong(key: String, defValue: Long): Long {
    val encryptValue = this.getString(encryptPreference(key), null)
        ?: return defValue
    return decryptPreference(encryptValue).toLong()
}

fun Editor.putEncryptLong(key: String, value: Long): Editor {
    this.putString(encryptPreference(key), encryptPreference(value.toString()))
    return this
}


fun SharedPreferences.getEncryptFloat(key: String, defValue: Float): Float {
    val encryptValue = this.getString(encryptPreference(key), null)
        ?: return defValue
    return decryptPreference(encryptValue).toFloat()
}

fun Editor.putEncryptFloat(key: String, value: Float): Editor {
    this.putString(encryptPreference(key), encryptPreference(value.toString()))
    return this
}


fun SharedPreferences.getEncryptBoolean(key: String, defValue: Boolean): Boolean {
    val encryptValue = this.getString(encryptPreference(key), null)
        ?: return defValue
    return decryptPreference(encryptValue).toBoolean()
}

fun Editor.putEncryptBoolean(key: String, value: Boolean): Editor {
    this.putString(encryptPreference(key), encryptPreference(java.lang.Boolean.toString(value)))
    return this
}


fun SharedPreferences.getEncryptString(key: String, defValue: String?): String {
    val encryptValue = this.getString(encryptPreference(key), null)
    return if (encryptValue == null) defValue ?: "" else decryptPreference(encryptValue)
}

fun Editor.putEncryptString(key: String, value: String): Editor {
    this.putString(encryptPreference(key), encryptPreference(value))
    return this
}


fun SharedPreferences.getEncryptStringSet(key: String, defValues: Set<String>): Set<String> {
    val encryptSet = this.getStringSet(encryptPreference(key), null)
        ?: return defValues
    val decryptSet = HashSet<String>()
    for (encryptValue in encryptSet) {
        decryptSet.add(decryptPreference(encryptValue))
    }
    return decryptSet
}

fun Editor.putEncryptStringSet(key: String, values: Set<String>): Editor {
    val encryptSet = HashSet<String>()
    for (value in values) {
        encryptSet.add(encryptPreference(value))
    }
    this.putStringSet(encryptPreference(key), encryptSet)
    return this
}

/**
 * encrypt function
 * @return cipherText base64
 */
private fun encryptPreference(plainText: String): String {
    return EncryptUtil.getInstance().encrypt(plainText)
}

/**
 * decrypt function
 * @return plainText
 */
private fun decryptPreference(cipherText: String): String {
    return EncryptUtil.getInstance().decrypt(cipherText)
}