package io.vithor.yamvpframework.migration

import android.database.sqlite.SQLiteOpenHelper
import com.j256.ormlite.support.ConnectionSource

/**
 * Created by Vithorio Polten on 2/23/16.
 */
class V1_TestMigration : Migration() {
    override val description: String
        get() = ""

    override val version: Int
        get() = 1

    override fun doUpgrade(db: SQLiteOpenHelper,connectionSource: ConnectionSource): Boolean {
        //        try {
        //            TableUtils.createTableIfNotExists(connectionSource, MyTestModel.class);

        //            ((DatabaseHelper)db).getMyTestModelDao().executeRaw("ALTER TABLE `mytestmodel` ADD COLUMN aloha INTEGER;");
        //        } catch (SQLException e) {
        //            Log.e(getClass().getName(), "Failed upgrading for migration: V" + getVersion(), e);
        //            return false;
        //        }
        return false
    }
}
