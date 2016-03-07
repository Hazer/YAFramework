package io.vithor.yamvpframework.migration;

import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.NonNull;
import android.support.v4.util.ArrayMap;
import android.util.Log;

import com.j256.ormlite.support.ConnectionSource;

import io.vithor.sentry.raven.Sentry;

/**
 * Created by Vithorio Polten on 2/23/16.
 */
public abstract class AbstractVersionManager {
    private static ArrayMap<Integer, Migration> migrations = new ArrayMap<>();

    protected AbstractVersionManager() {
    }

    /**
     *
     * @param migrations Array of migrations to be executed for each app version.
     * @throws IllegalStateException When migrations have duplicated version, if you need more than one migration per version, use Composition Pattern.
     */
    protected final void addMigrations(Migration... migrations) {
        for (Migration migration : migrations) {

            if (AbstractVersionManager.migrations.containsKey(migration.getVersion())) {
                throw new IllegalStateException("Duplicate migrations for version: " + migration.getVersion());
            }

            AbstractVersionManager.migrations.put(migration.getVersion(), migration);
        }
    }

    /**
     * Get migration code for next migration step
     *
     * @param version integer version code of the database
     * @return Migration for version
     * @throws IllegalStateException When there's a version missing migration step, all version upgrades must have a migration.
     */
    private final @NonNull Migration getMigration(int version) {
        if (!migrations.containsKey(version)) {
            IllegalStateException ex = new IllegalStateException("Missing migrations for version: " + version);

            Sentry.INSTANCE.captureException(ex);

            throw ex;
        }
        return migrations.get(version);
    }

    /**
     * Run migration from oldVersion to newVersion, executing each version step
     *
     * @param helper SQLiteOpenHelper for active database
     * @param connectionSource active ConnectionSource
     * @param oldVersion actual database version
     * @param newVersion database version if migration succeeds
     *
     * @throws IllegalStateException in case of inconsistency.
     */
    public final void migrate(SQLiteOpenHelper helper, ConnectionSource connectionSource, int oldVersion, int newVersion) {
        while (oldVersion++ < newVersion) {
            Migration m = getMigration(oldVersion);
            Log.d(getClass().getSimpleName(),
                    "Running Migration V" + m.getVersion() + "\nDesc: " +
                    m.getDescription()
            );
            if (!m.doUpgrade(helper, connectionSource)) {
                // Unsuccessful, stop migrations

                IllegalStateException ex = new IllegalStateException("Failed migrating database from version: " +
                        oldVersion + " to version: " + newVersion);

                Sentry.INSTANCE.captureException(ex);
                throw ex;
            }
        }
    }
}
