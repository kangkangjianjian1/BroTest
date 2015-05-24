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
    public static final String ADDEDTABLENAME = "added";
    public static final String CHECKED = "1";
    public static final String UNCHECKED = "0";
    public static final String CHECKEDATENULL = "";
    /**
     * 资产名称
     */
    public static final String TABLE_COLUMN_SOURCENAME= "sourceName";
    /**
     * 规则型号
     */
    public static final String TABLE_COLUMN_SPECIFICATION = "specifications";
    /**
     * 入账日期
     */
    public static final String TABLE_COLUMN_DATE = "date";
    /**
     * 主机序列号
     */
    public static final String TABLE_COLUMN_COMSERIESNUM = "comseriesnumber";
    /**
     * 显示器序列号
     */
    public static final String TABLE_COLUMN_MONSERIESNUM = "monseriesnumber";
    /**
     * 存放地点
     */
    public static final String TABLE_COLUMN_STOREPOSITION = "storeposition";
    /**
     * 使用人部门
     */
    public static final String TABLE_COLUMN_DEPARTMENTOFUSER = "usersdepartment";
    /**
     * 现在使用人
     */
    public static final String TABLE_COLUMN_USER = "user";

    /**
     * 检查结果
     */
    public static final String TABLE_COLUMN_RESULT = "result";
    /**
     * 备注
     */
    public static final String TABLE_COLUMN_ADDEDNOTE = "note";
    /**
     * 检查
     * 0代表false ,1代表true
     */
    public static final String TABLE_COLUMN_CHECKED = "checked";

    public static final String TABLE_COLUMN_CHECKDATE = "checkDate";

    private static final String CREATE_STAFF_WITH_COMP =
            "create table " + TABLENAME + " (" +
                    "_id integer primary key autoincrement, " +
                    TABLE_COLUMN_SOURCENAME + " text," +
                    TABLE_COLUMN_SPECIFICATION + " text," +
                    TABLE_COLUMN_DATE + " text," +
                    TABLE_COLUMN_COMSERIESNUM + " text," +
                    TABLE_COLUMN_MONSERIESNUM + " text," +
                    TABLE_COLUMN_STOREPOSITION + " text," +
                    TABLE_COLUMN_DEPARTMENTOFUSER + " text," +
                    TABLE_COLUMN_USER + " text," +
                    TABLE_COLUMN_ADDEDNOTE + " text," +
                    TABLE_COLUMN_RESULT + " text," +
                    TABLE_COLUMN_CHECKDATE + " text," +
                    TABLE_COLUMN_CHECKED + " text)";

    private static final String CREATE_ADDED_TABLE =
            "create table " + ADDEDTABLENAME + " (" +
                    "_id integer primary key autoincrement, " +
                    TABLE_COLUMN_SOURCENAME + " text," +
                    TABLE_COLUMN_SPECIFICATION + " text," +
                    TABLE_COLUMN_DATE + " text," +
                    TABLE_COLUMN_COMSERIESNUM + " text," +
                    TABLE_COLUMN_MONSERIESNUM + " text," +
                    TABLE_COLUMN_STOREPOSITION + " text," +
                    TABLE_COLUMN_DEPARTMENTOFUSER + " text," +
                    TABLE_COLUMN_USER + " text," +
                    TABLE_COLUMN_ADDEDNOTE + " text," +
                    TABLE_COLUMN_RESULT + " text," +
                    TABLE_COLUMN_CHECKDATE + " text," +
                    TABLE_COLUMN_CHECKED + " text)";

    public StaffDataBaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_STAFF_WITH_COMP);
        Log.d("BroTest", "创建数据库成功"+CREATE_STAFF_WITH_COMP);
        db.execSQL(CREATE_ADDED_TABLE);
        Log.d("BroTest", "创建新添加的数据库成功"+CREATE_ADDED_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
