package io.vithor.yamvpframework.migration

import android.database.sqlite.SQLiteOpenHelper
import android.support.annotation.NonNull
import android.support.v4.util.ArrayMap
import android.util.Log

import com.j256.ormlite.support.ConnectionSource

import io.vithor.sentry.raven.Sentry

/**
 * Created by Vithorio Polten on 2/23/16.
 */
abstract class AbstractVersionManager protected constructor() {

    /**

     * @param migrations Array of migrations to be executed for each app version.
     * *
     * @throws IllegalStateException When migrations have duplicated version, if you need more than one migration per version, use Composition Pattern.
     */
    protected fun addMigrations(vararg migrations: Migration) {
        for (migration in migrations) {

            if (AbstractVersionManager.migrations.containsKey(migration.version)) {
                throw IllegalStateException("Duplicate migrations for version: " + migration.version)
            }

            AbstractVersionManager.migrations.put(migration.version, migration)
        }
    }

    /**
     * Get migration code for next migration step

     * @param version integer version code of the database
     * *
     * @return Migration for version
     * *
     * @throws IllegalStateException When there's a version missing migration step, all version upgrades must have a migration.
     */
    @NonNull private fun getMigration(version: Int): Migration? {
        if (!migrations.containsKey(version)) {
            val ex = IllegalStateException("Missing migrations for version: " + version)

            Sentry.INSTANCE.captureException(ex)

            throw ex
        }
        return migrations.get(version)
    }

    /**
     * Run migration from oldVersion to newVersion, executing each version step

     * @param helper SQLiteOpenHelper for active database
     * *
     * @param connectionSource active ConnectionSource
     * *
     * @param oldVersion actual database version
     * *
     * @param newVersion database version if migration succeeds
     * *
     * *
     * @throws IllegalStateException in case of inconsistency.
     */
    fun migrate(helper: SQLiteOpenHelper, connectionSource: ConnectionSource, oldVersion: Int, newVersion: Int) {
        var oldVersion = oldVersion
        while (oldVersion++ < newVersion) {
            val m = getMigration(oldVersion)
            Log.d(javaClass.simpleName,
                    "Running Migration V" + m?.version + "\nDesc: " +
                            m?.description)
            if (!(m?.doUpgrade(helper, connectionSource) ?: false)) {
                // Unsuccessful, stop migrations

                val ex = IllegalStateException("Failed migrating database from version: $oldVersion to version: $newVersion")

                Sentry.INSTANCE.captureException(ex)
                throw ex
            }
        }
    }

    companion object {
        private val migrations = ArrayMap<Int, Migration>()
    }
}
