package io.vithor.yamvpframework.migration

import android.database.sqlite.SQLiteOpenHelper
import com.j256.ormlite.support.ConnectionSource

/**
 * Created by Vithorio Polten on 2/23/16.
 */
abstract class Migration : Comparable<Migration> {
    abstract val description: String

    abstract val version: Int

    abstract fun doUpgrade(db: SQLiteOpenHelper, connectionSource: ConnectionSource): Boolean

    override fun compareTo(other: Migration): Int {
        return version.compareTo(other.version)
    }

    override fun equals(other: Any?): Boolean {
        val migration = other as? Migration
        return migration?.compareTo(this) == 0
    }

    override fun hashCode(): Int{
        var result = description.hashCode()
        result += 31 * result + version
        return result
    }
}
