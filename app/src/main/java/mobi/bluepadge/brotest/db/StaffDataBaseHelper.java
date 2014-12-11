package mobi.bluepadge.brotest.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by bluepadge on 14-12-11.
 */
public class StaffDataBaseHelper extends SQLiteOpenHelper {

    public static final String DBNAME = "staffcomputer.db";
    public static final String TABLENAME = "computer";
    public static final String TABLE_COLUMN_NAME = "name";
    public static final String TABLE_COLUMN_ENGILISH= "english";
    public static final String TABLE_COLUMN_CHINESE= "chinese";
    private static final String CREATE_STAFF_WITH_COMP =
            "create table " + TABLENAME + " (" +
                    "_id integer primary key autoincrement, " +
                    TABLE_COLUMN_NAME + " text," +
                    TABLE_COLUMN_ENGILISH + " text," +
                    TABLE_COLUMN_CHINESE + " text)";

    public StaffDataBaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DBNAME, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_STAFF_WITH_COMP);
        Log.d("BroTest", "创建数据库成功");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
