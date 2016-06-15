package io.vithor.yamvpframework.gson

import com.google.gson.ExclusionStrategy
import com.google.gson.FieldAttributes

/**
 * Created by Hazer on 1/25/16.
 */
class GsonIgnoreExclusionStrategy : ExclusionStrategy {
    override fun shouldSkipField(f: FieldAttributes): Boolean {
        return f.getAnnotation(GsonIgnore::class.java) != null
    }

    override fun shouldSkipClass(clazz: Class<*>): Boolean {
        return clazz.getAnnotation(GsonIgnore::class.java) != null
    }
}