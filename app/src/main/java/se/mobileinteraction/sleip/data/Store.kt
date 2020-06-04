package se.mobileinteraction.sleip.data

import android.content.SharedPreferences
import androidx.core.content.edit
import kotlinx.serialization.ImplicitReflectionSerializer
import kotlinx.serialization.SerializationStrategy
import kotlinx.serialization.json.Json
import kotlinx.serialization.serializer
import kotlin.reflect.KClass


class Store(
    private val sharedPref: SharedPreferences,
    private val json: Json
) {

    @OptIn(ImplicitReflectionSerializer::class)
    fun <T : Any> store(key: String, obj: T, clazz: KClass<T>) {
        val serializer: SerializationStrategy<T> = try {
            clazz.serializer()
        } catch (throwable: Throwable) {
            throw IllegalArgumentException("Trying to access none serializable class")
        }
        sharedPref.edit {
            putString(key, json.stringify(serializer, obj))
        }
    }

    @OptIn(ImplicitReflectionSerializer::class)
    fun <T : Any> readCache(key: String, clazz: KClass<T>): T? {

        return sharedPref.getString(key, null)?.let { serializer ->
            try {
                json.parse(clazz.serializer(), serializer)
            } catch (throwable: Throwable) {
                throw IllegalArgumentException("This class not serializable!")
            }
        }
    }

    fun remove(key: String) {
        sharedPref.edit {
            remove(key)
        }
    }

    fun clear() {
        sharedPref.edit { clear() }
    }
}
