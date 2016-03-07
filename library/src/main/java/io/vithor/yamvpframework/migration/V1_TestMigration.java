package io.vithor.yamvpframework.migration;

import android.database.SQLException;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.NonNull;

import com.j256.ormlite.support.ConnectionSource;

/**
 * Created by Vithorio Polten on 2/23/16.
 */
public class V1_TestMigration extends Migration {
    @NonNull
    @Override
    public String getDescription() {
        return "";
    }

    @Override
    public int getVersion() {
        return 1;
    }

    @Override
    public boolean doUpgrade(@NonNull SQLiteOpenHelper db, @NonNull ConnectionSource connectionSource) {
//        try {
//            TableUtils.createTableIfNotExists(connectionSource, MyTestModel.class);

//            ((DatabaseHelper)db).getMyTestModelDao().executeRaw("ALTER TABLE `mytestmodel` ADD COLUMN aloha INTEGER;");
//        } catch (SQLException e) {
//            Log.e(getClass().getName(), "Failed upgrading for migration: V" + getVersion(), e);
//            return false;
//        }
        return false;
    }
}
