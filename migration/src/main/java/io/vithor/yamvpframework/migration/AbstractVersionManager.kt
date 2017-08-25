package io.vithor.yamvpframework.migration

import android.database.sqlite.SQLiteOpenHelper
import android.support.v4.util.ArrayMap
import android.util.Log
import com.j256.ormlite.support.ConnectionSource

/**
 * Created by Vithorio Polten on 2/23/16.
 */
abstract class AbstractVersionManager protected constructor() {
    companion object {
        private val migrations = ArrayMap<Int, Migration>()
    }

    /**
     * @param migrations Array of migrations to be executed for each app version.
     *
     * @throws MigrationException(message: String, migration: Migration) When migrations have duplicated version, if you need more than one migration per version, use Composition Pattern.
     */
    protected fun addMigrations(vararg migrations: Migration) {
        for (migration in migrations) {

            if (AbstractVersionManager.migrations.containsKey(migration.version)) {
                throw MigrationException("Duplicate migrations for version: " + migration.version, migration)
            }

            AbstractVersionManager.migrations.put(migration.version, migration)
        }
    }

    /**
     * Get migration code for next migration step
     *
     * @param version integer version code of the database
     *
     * @return Migration for version
     *
     * @throws MigrationException When there's a version missing migration step, all version upgrades must have a migration.
     */
    private fun getMigration(version: Int): Migration {
        if (!migrations.containsKey(version)) {
            throw MigrationException("Missing migrations for version: " + version)
        }
        return migrations[version]!!
    }

    /**
     * Run migration from oldVersion to newVersion, executing each version step
     *
     * @param helper SQLiteOpenHelper for active database
     * *
     * @param connectionSource active ConnectionSource
     * *
     * @param oldVersion actual database version
     * *
     * @param newVersion database version if migration succeeds
     * *
     * *
     * @throws MigrationException(message: String, migration: Migration) in case of inconsistency.
     */
    fun migrate(helper: SQLiteOpenHelper, connectionSource: ConnectionSource, oldVersion: Int, newVersion: Int) {
        var ov = oldVersion
        while (ov++ < newVersion) {
            val m = getMigration(ov)
            Log.d(javaClass.simpleName,
                    "Running Migration V${m.version}\nDesc: ${m.description}")
            if (!(m.doUpgrade(helper, connectionSource))) {
                // Unsuccessful, stop migrations
                throw MigrationException("Failed migrating database from version: $ov to version: $newVersion", m)
            }
        }
    }
}
