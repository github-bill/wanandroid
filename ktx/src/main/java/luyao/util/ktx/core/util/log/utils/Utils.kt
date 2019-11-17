package luyao.util.ktx.core.util.log.utils

import android.os.Bundle
import com.google.gson.Gson
import luyao.util.ktx.core.util.log.L
import luyao.util.ktx.core.util.log.LoggerPrinter
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

/**
 * 判断 Any 是否为基本类型
 */
fun isPrimitiveType(value: Any?) = when (value) {
    is Boolean -> true
    is String -> true
    is Int -> true
    is Float -> true
    is Double -> true
    else -> false
}

fun Any.toJavaClass() = this.javaClass.toString()

fun JSONObject.formatJson(): String = this.toString(LoggerPrinter.JSON_INDENT)

fun JSONArray.formatJson(): String = this.toString(LoggerPrinter.JSON_INDENT)

/**
 * 解析 bundle ，并存储到 JSONObject
 */
fun JSONObject.parseBundle(bundle: Bundle?): JSONObject {
    bundle?.keySet()?.map {
        val isPrimitiveType = isPrimitiveType(bundle.get(it))
        try {
            if (isPrimitiveType) {
                this.put(it, bundle.get(it))
            } else {
                this.put(it, JSONObject(Gson().toJson(bundle.get(it))))
            }
        } catch (e: JSONException) {
            L.e("Invalid Json")
        }
    }
    return this
}

/**
 * 解析 map ，并存储到 JSONObject
 */
fun JSONObject.parseMap(map: Map<*, *>): JSONObject {
    val keys = map.keys
    val values = map.values
    val value = values.firstOrNull()
    val isPrimitiveType = isPrimitiveType(value)
    keys.map {
        try {
            if (isPrimitiveType) {
                this.put(it.toString(), map[it])
            } else {
                this.put(it.toString(), JSONObject(Gson().toJson(map[it])))
            }
        } catch (e: JSONException) {
            L.e("Invalid Json")
        }
    }
    return this
}

/**
 * 解析 collection ，并存储到 JSONArray
 */
fun Collection<*>.parseToJSONArray(jsonArray: JSONArray): JSONArray {
    this.map {
        try {
            val objStr = Gson().toJson(it)
            val jsonObject = JSONObject(objStr)
            jsonArray.put(jsonObject)
        } catch (e: JSONException) {
            L.e("Invalid Json")
        }
    }
    return jsonArray
}
